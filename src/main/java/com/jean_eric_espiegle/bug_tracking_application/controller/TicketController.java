package com.jean_eric_espiegle.bug_tracking_application.controller;

import com.jean_eric_espiegle.bug_tracking_application.dto.TicketRequest;
import com.jean_eric_espiegle.bug_tracking_application.dto.TicketResponse;
import com.jean_eric_espiegle.bug_tracking_application.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public ResponseEntity<TicketResponse> createTicket(@RequestBody TicketRequest request) {
        TicketResponse response = ticketService.createTicket(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<TicketResponse> getTicket(@PathVariable Long ticketId) {
        TicketResponse response = ticketService.getTicketById(ticketId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{ticketId}")
    public ResponseEntity<TicketResponse> updateTicket(
            @PathVariable Long ticketId,
            @RequestBody TicketRequest request) {
        TicketResponse response = ticketService.updateTicket(ticketId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{ticketId}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long ticketId) {
        ticketService.deleteTicket(ticketId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{ticketId}/assign-version/{versionId}")
    public ResponseEntity<TicketResponse> assignTicketToVersion(
            @PathVariable Long ticketId,
            @PathVariable Long versionId) {
        TicketResponse response = ticketService.assignVersion(ticketId, versionId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/organization/{orgId}")
    public ResponseEntity<List<TicketResponse>> getTicketsByOrganization(@PathVariable Long orgId) {
        List<TicketResponse> tickets = ticketService.getTicketsByOrganization(orgId);
        return ResponseEntity.ok(tickets);
    }
}
