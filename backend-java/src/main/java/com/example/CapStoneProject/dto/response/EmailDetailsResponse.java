package com.example.CapStoneProject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class EmailDetailsResponse {

    private UUID emailId;
    private String recipient;
    private String subject;
    private String body;
    private String systemStatus;
    private String providerStatus;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}