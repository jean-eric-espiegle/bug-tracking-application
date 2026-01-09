package com.jean_eric_espiegle.bug_tracking_application.service;

import com.jean_eric_espiegle.bug_tracking_application.dto.TicketRequest;
import com.jean_eric_espiegle.bug_tracking_application.dto.TicketResponse;
import com.jean_eric_espiegle.bug_tracking_application.model.*;
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

    public TicketService(TicketRepository ticketRepository, UserRepository userRepository,
            VersionService versionService) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.versionService = versionService;
    }

    // Create ticket with organization check
    public TicketResponse createTicket(TicketRequest request) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found"));

        User reporter = userRepository.findByUsername(request.reporterUsername())
                .orElse(currentUser); // fallback to current user if reporter not found

        // Ensure reporter and current user are in the same organization
        if (reporter.getOrganization() == null || !reporter.getOrganization().equals(currentUser.getOrganization())) {
            throw new IllegalArgumentException("Reporter must belong to the same organization as the current user");
        }

        Ticket ticket = new Ticket();
        ticket.setTitle(request.title());
        ticket.setDescription(request.description());
        ticket.setReporter(reporter);
        ticket.setOrganization(currentUser.getOrganization());

        if (request.assigneeId() != null) {
            User assigneeUser = userRepository.findById(request.assigneeId())
                    .orElseThrow(() -> new IllegalStateException("Authenticated user not found"));
            ticket.setAssignee(assigneeUser);
        }

        if (request.versionId() != null) {
            Version version = versionService.getVersionById(request.versionId());
            ticket.setVersion(version);
        }
        ticketRepository.save(ticket);

        return mapToResponse(ticket);
    }

    public TicketResponse updateTicket(Long ticketId, TicketRequest request) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found"));

        ensureSameOrganization(ticket);

        if (request.title() != null) {
            ticket.setTitle(request.title());
        }

        if (request.description() != null) {
            ticket.setDescription(request.description());
        }

        if (request.assigneeId() != null) {
            User assigneeUser = userRepository.findById(request.assigneeId())
                    .orElseThrow(() -> new IllegalStateException("Authenticated user not found"));
            ticket.setAssignee(assigneeUser);
        }

        if (request.versionId() != null) {
            Version version = versionService.getVersionById(request.versionId());
            ticket.setVersion(version);
        }

        ticketRepository.save(ticket);
        return mapToResponse(ticket);
    }

    public void deleteTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found"));

        ensureSameOrganization(ticket);

        ticketRepository.delete(ticket);
    }

    public TicketResponse assignVersion(Long ticketId, Long versionId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found"));

        ensureSameOrganization(ticket);

        Version version = versionService.getVersionById(versionId);
        ticket.setVersion(version);
        ticketRepository.save(ticket);
        return mapToResponse(ticket);
    }

    public TicketResponse getTicketById(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found"));
        return mapToResponse(ticket);
    }

    public List<TicketResponse> getTicketsByOrganization(Long orgId) {
        List<Ticket> tickets = ticketRepository.findByOrganizationId(orgId);
        return tickets.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    // Organization check helper
    private void ensureSameOrganization(Ticket ticket) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found"));

        if (ticket.getOrganization() == null || !ticket.getOrganization().equals(currentUser.getOrganization())) {
            throw new IllegalArgumentException("Operation not allowed: ticket belongs to another organization");
        }
    }

    // Mapper
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
                ticket.getOrganization().getId(),
                ticket.getOrganization().getName(),
                versionId);
    }
}
