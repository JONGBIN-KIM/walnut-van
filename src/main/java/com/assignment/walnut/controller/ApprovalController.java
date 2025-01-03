package com.assignment.walnut.controller;

import com.assignment.walnut.dto.ApprovalRequest;
import com.assignment.walnut.dto.ApprovalResponse;
import com.assignment.walnut.service.ApprovalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/approval")
public class ApprovalController {

    private final ApprovalService approvalService;

    public ApprovalController(ApprovalService approvalService) {
        this.approvalService = approvalService;
    }

    @PostMapping(consumes = "text/plain", produces = "application/json")
    public ResponseEntity<String> approve(@RequestBody ApprovalRequest request) {
        return ResponseEntity.ok(approvalService.processApproval(request));
    }
}
