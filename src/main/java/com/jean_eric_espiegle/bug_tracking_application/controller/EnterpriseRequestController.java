package com.jean_eric_espiegle.bug_tracking_application.controller;

import com.jean_eric_espiegle.bug_tracking_application.dto.EnterpriseRequestDTO;
import com.jean_eric_espiegle.bug_tracking_application.model.*;
import com.jean_eric_espiegle.bug_tracking_application.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/enterprise")
public class EnterpriseRequestController {

    private final EnterpriseRequestRepository requestRepository;
    private final UserRepository userRepository;

    public EnterpriseRequestController(
            EnterpriseRequestRepository requestRepository,
            UserRepository userRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/request")
    public ResponseEntity<?> submitRequest(
            @RequestBody EnterpriseRequestDTO dto,
            @RequestParam Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        EnterpriseRequest request = new EnterpriseRequest();
        request.setUser(user);
        request.setCompanyName(dto.companyName());
        request.setContactEmail(dto.contactEmail());
        request.setStatus(RequestStatus.PENDING);

        requestRepository.save(request);

        return ResponseEntity.ok().build();
    }
}
