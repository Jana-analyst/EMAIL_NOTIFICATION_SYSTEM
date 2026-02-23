package com.example.CapStoneProject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class EmailListItemResponse {

    private UUID emailId;
    private String recipient;
    private String subject;
    private String systemStatus;
    private String providerStatus;
    private OffsetDateTime createdAt;
}
