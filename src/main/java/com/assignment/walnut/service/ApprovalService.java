package com.assignment.walnut.service;

import com.assignment.walnut.dto.ApprovalRequest;
import com.assignment.walnut.dto.ApprovalResponse;
import com.assignment.walnut.entity.Approval;
import com.assignment.walnut.enums.ApprovalStatus;
import com.assignment.walnut.repository.ApprovalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
public class ApprovalService {

    private final RestTemplate restTemplate;
    private final ApprovalRepository approvalRepository;

    private static final Logger logger = LoggerFactory.getLogger(ApprovalService.class);

    @Value("${token-system.url}")
    private String tokenValidationUrl;

    public ApprovalService(RestTemplate restTemplate, ApprovalRepository approvalRepository) {
        this.restTemplate = restTemplate;
        this.approvalRepository = approvalRepository;
    }

    public String processApproval(ApprovalRequest request) {
        logger.info("[토큰 유효성 확인] token : "+request.getToken());
        boolean isTokenValid = validateToken(request.getToken());
        logger.info("[토큰 유효성 결과] valid : "+isTokenValid);

        ApprovalResponse response;

        if (!isTokenValid) {
            response = ApprovalResponse.builder()
                    .approvalNumber("REJECTED-0000")
                    .status(ApprovalStatus.REJECTED)
                    .reason("Invalid Token")
                    .timestamp(LocalDateTime.now())
                    .build();
            logger.info("[유효하지않은 토큰] : "+response);
        } else {
            String approvalNumber = generateApprovalNumber(request.getApprovalRequestId());
            saveApproval(request, approvalNumber);
            logger.info("[승인정보 저장] : "+approvalNumber);

            response = ApprovalResponse.builder()
                    .status(ApprovalStatus.APPROVED)
                    .amount(request.getAmount())
                    .currency(request.getCurrency())
                    .timestamp(LocalDateTime.now())
                    .approvalNumber(approvalNumber != null ? approvalNumber : "APPROVED-0000")
                    .build();
            logger.info("[승인응답내용] : "+response);
        }
        return toIso8583Payload(response);
    }

    private String toIso8583Payload(ApprovalResponse response) {
        return String.format(
                "%-8s%-20s%-100s%-20s%-20s%-20s",
                response.getStatus().name(), // 상태
                response.getApprovalId() != null ? response.getApprovalId() : "UNKNOWN", // 승인 ID
                response.getReason() != null ? response.getReason() : "No reason provided", // 사유
                response.getTimestamp() != null ? response.getTimestamp().toString() : LocalDateTime.now().toString(), // 타임스탬프
                response.getApprovalNumber() != null ? response.getApprovalNumber() : "UNKNOWN", // 승인 번호
                response.getCurrency() != null ? response.getCurrency() : "N/A" // 통화
        );
    }


    private String generateApprovalNumber(String approvalRequestId) {
        return "BLWN" + approvalRequestId + (int)(Math.random() * 10000);
    }

    private Approval saveApproval(ApprovalRequest request, String approvalNumber) {
        Approval approval = Approval.builder()
                .token(request.getToken())
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .approvalNumber(approvalNumber)
                .createdAt(LocalDateTime.now())
                .build();
        return approvalRepository.save(approval);
    }

    private boolean validateToken(String token) {
        String url = tokenValidationUrl + "?token=" + token;
        return restTemplate.getForObject(url, Boolean.class);
    }
}
