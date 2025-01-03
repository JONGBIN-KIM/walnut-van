package com.assignment.walnut.dto;

import com.assignment.walnut.enums.ApprovalStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ApprovalResponse {
    private ApprovalStatus status;
    private String approvalId;
    private String reason;
    private Double amount;
    private String currency;
    private LocalDateTime timestamp;
    private String approvalNumber;
}
