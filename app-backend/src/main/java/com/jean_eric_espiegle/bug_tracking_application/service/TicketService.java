package com.jean_eric_espiegle.bug_tracking_application.service;

import com.jean_eric_espiegle.bug_tracking_application.audit.LoggableAction;
import com.jean_eric_espiegle.bug_tracking_application.dto.TicketRequest;
import com.jean_eric_espiegle.bug_tracking_application.dto.TicketResponse;
import com.jean_eric_espiegle.bug_tracking_application.model.*;
import com.jean_eric_espiegle.bug_tracking_application.repository.MembershipRepository;
import com.jean_eric_espiegle.bug_tracking_application.repository.TicketRepository;
import com.jean_eric_espiegle.bug_tracking_application.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final VersionService versionService;
    private final OrganizationService organizationService;
    private final MembershipRepository membershipRepository;

    public TicketService(TicketRepository ticketRepository, UserRepository userRepository,
            VersionService versionService, OrganizationService organizationService,
            MembershipRepository membershipRepository) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.versionService = versionService;
        this.organizationService = organizationService;
        this.membershipRepository = membershipRepository;
    }

    @LoggableAction(action = "Created Ticket", entity = "Ticket")
    public TicketResponse createTicket(TicketRequest request, Long organizationId) {
        User currentUser = getCurrentUser();
        Organization organization = organizationService.getOrganizationById(organizationId);
        ensureSameOrganization(currentUser, organization);

        User reporter = userRepository.findByUsername(request.reporterUsername())
                .orElse(currentUser);

        ensureSameOrganization(reporter, organization);

        Ticket ticket = new Ticket();
        ticket.setTitle(request.title());
        ticket.setDescription(request.description());
        ticket.setReporter(reporter);
        ticket.setOrganization(organization);

        if (request.status() != null) {
            ticket.setStatus(request.status());
        }

        if (request.assigneeId() != null) {
            User assigneeUser = userRepository.findById(request.assigneeId())
                    .orElseThrow(() -> new IllegalStateException("Authenticated user not found"));
            ensureSameOrganization(assigneeUser, organization);
            ticket.setAssignee(assigneeUser);
        }

        if (request.versionId() != null) {
            Version version = versionService.getVersionById(request.versionId());
            if (!version.getOrganization().equals(organization)) {
                throw new IllegalArgumentException("Version does not belong to the same organization as the ticket.");
            }
            ticket.setVersion(version);
        }
        ticketRepository.save(ticket);

        return mapToResponse(ticket);
    }

    @LoggableAction(action = "Updated Ticket", entity = "Ticket")
    public TicketResponse updateTicket(Long ticketId, TicketRequest request) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found"));

        ensureSameOrganization(getCurrentUser(), ticket.getOrganization());

        if (request.title() != null) {
            ticket.setTitle(request.title());
        }

        if (request.description() != null) {
            ticket.setDescription(request.description());
        }

        if (request.status() != null) {
            ticket.setStatus(request.status());
        }

        if (request.assigneeId() != null) {
            User assigneeUser = userRepository.findById(request.assigneeId())
                    .orElseThrow(() -> new IllegalStateException("Authenticated user not found"));
            ensureSameOrganization(assigneeUser, ticket.getOrganization());
            ticket.setAssignee(assigneeUser);
        }

        if (request.versionId() != null) {
            Version version = versionService.getVersionById(request.versionId());
            if (!version.getOrganization().equals(ticket.getOrganization())) {
                throw new IllegalArgumentException("Version does not belong to the same organization as the ticket.");
            }
            ticket.setVersion(version);
        }

        ticketRepository.save(ticket);
        return mapToResponse(ticket);
    }

    @LoggableAction(action = "Deleted Ticket", entity = "Ticket")
    public void deleteTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found"));

        ensureSameOrganization(getCurrentUser(), ticket.getOrganization());

        ticketRepository.delete(ticket);
    }

    @LoggableAction(action = "Assigned Ticket Version", entity = "Ticket")
    public TicketResponse assignVersion(Long ticketId, Long versionId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found"));

        ensureSameOrganization(getCurrentUser(), ticket.getOrganization());

        Version version = versionService.getVersionById(versionId);
        if (!version.getOrganization().equals(ticket.getOrganization())) {
            throw new IllegalArgumentException("Version does not belong to the same organization as the ticket.");
        }
        ticket.setVersion(version);
        ticketRepository.save(ticket);
        return mapToResponse(ticket);
    }

    public TicketResponse getTicketById(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found"));
        ensureSameOrganization(getCurrentUser(), ticket.getOrganization());
        return mapToResponse(ticket);
    }

    public List<TicketResponse> getTicketsByOrganization(Long orgId) {
        Organization organization = organizationService.getOrganizationById(orgId);
        ensureSameOrganization(getCurrentUser(), organization);
        List<Ticket> tickets = ticketRepository.findByOrganizationId(orgId);
        return tickets.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public List<TicketResponse> getTicketsByVersion(Long versionId) {
        Version version = versionService.getVersionById(versionId);
        ensureSameOrganization(getCurrentUser(), version.getOrganization());
        List<Ticket> tickets = ticketRepository.findByVersionId(versionId);
        return tickets.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    private User getCurrentUser() {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found"));
    }

    private void ensureSameOrganization(User user, Organization organization) {
        if (!membershipRepository.existsByUserAndOrganization(user, organization)) {
            throw new IllegalArgumentException("Operation not allowed: user " + user.getUsername()
                    + " does not belong to organization " + organization.getName());
        }
    }

    private TicketResponse mapToResponse(Ticket ticket) {
        String assigneeUsername = ticket.getAssignee() != null ? ticket.getAssignee().getUsername() : null;
        String reporterUsername = ticket.getReporter() != null ? ticket.getReporter().getUsername() : null;
        Long versionId = ticket.getVersion() != null ? ticket.getVersion().getId() : null;

        return new TicketResponse(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getDescription(),
                reporterUsername,
                assigneeUsername,
                ticket.getStatus(),
                ticket.getOrganization().getId(),
                ticket.getOrganization().getName(),
                versionId);
    }
}
