package com.assignment.walnut.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenValidationRequest {
    private String token;
}
