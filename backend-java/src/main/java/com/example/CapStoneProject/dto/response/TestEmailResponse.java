package com.example.CapStoneProject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class TestEmailResponse {

    private UUID emailId;
    private String subject;
    private String systemStatus;
    private OffsetDateTime createdAt;
}