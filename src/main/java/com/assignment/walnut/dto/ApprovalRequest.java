package com.assignment.walnut.dto;

import lombok.Data;

@Data
public class ApprovalRequest {
    private String token;
    private Double amount;
    private String currency;
    private String approvalRequestId;
}
