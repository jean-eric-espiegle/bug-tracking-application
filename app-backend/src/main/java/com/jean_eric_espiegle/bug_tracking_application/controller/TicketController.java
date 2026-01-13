package com.jean_eric_espiegle.bug_tracking_application.controller;

import com.jean_eric_espiegle.bug_tracking_application.dto.TicketRequest;
import com.jean_eric_espiegle.bug_tracking_application.dto.TicketResponse;
import com.jean_eric_espiegle.bug_tracking_application.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/organizations/{orgId}/tickets")
    public ResponseEntity<TicketResponse> createTicket(@RequestBody TicketRequest request, @PathVariable Long orgId) {
        TicketResponse response = ticketService.createTicket(request, orgId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tickets/{ticketId}")
    public ResponseEntity<TicketResponse> getTicket(@PathVariable Long ticketId) {
        TicketResponse response = ticketService.getTicketById(ticketId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/tickets/{ticketId}")
    public ResponseEntity<TicketResponse> updateTicket(
            @PathVariable Long ticketId,
            @RequestBody TicketRequest request) {
        TicketResponse response = ticketService.updateTicket(ticketId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/tickets/{ticketId}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long ticketId) {
        ticketService.deleteTicket(ticketId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/tickets/{ticketId}/assign-version/{versionId}")
    public ResponseEntity<TicketResponse> assignTicketToVersion(
            @PathVariable Long ticketId,
            @PathVariable Long versionId) {
        TicketResponse response = ticketService.assignVersion(ticketId, versionId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/organizations/{orgId}/tickets")
    public ResponseEntity<List<TicketResponse>> getTicketsByOrganization(@PathVariable Long orgId) {
        List<TicketResponse> tickets = ticketService.getTicketsByOrganization(orgId);
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/versions/{versionId}/tickets")
    public ResponseEntity<List<TicketResponse>> getTicketsByVersion(@PathVariable Long versionId) {
        List<TicketResponse> tickets = ticketService.getTicketsByVersion(versionId);
        return ResponseEntity.ok(tickets);
    }
}
