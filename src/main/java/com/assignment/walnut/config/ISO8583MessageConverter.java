package com.assignment.walnut.config;

import com.assignment.walnut.dto.ApprovalRequest;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@Component
public class ISO8583MessageConverter implements HttpMessageConverter<Object> {

    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return MediaType.TEXT_PLAIN.includes(mediaType);
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return false;
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return Collections.singletonList(MediaType.TEXT_PLAIN);
    }

    @Override
    public Object read(Class<?> clazz, HttpInputMessage inputMessage) throws IOException {
        byte[] body = inputMessage.getBody().readAllBytes();
        String payload = new String(body, StandardCharsets.UTF_8).trim();

        String token = payload.substring(0, 64).trim();
        String currency = payload.substring(65, 68).trim();
        String approvalId = payload.substring(69,89).trim();
        Double amount = Double.parseDouble(payload.substring(90).trim());

        ApprovalRequest request = new ApprovalRequest();
        request.setToken(token);
        request.setAmount(amount);
        request.setCurrency(currency);
        request.setApprovalRequestId(approvalId);
        return request;
    }

    @Override
    public void write(Object o, MediaType mediaType, HttpOutputMessage outputMessage) {
        throw new UnsupportedOperationException("Writing is not supported.");
    }
}
