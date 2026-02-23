package com.example.CapStoneProject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.List;

@AllArgsConstructor
@Getter
public class TemplateDetailsResponse {

    private UUID templateId;
    private String name;
    private String subject;
    private String body;
    private List<String> placeholders;

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}