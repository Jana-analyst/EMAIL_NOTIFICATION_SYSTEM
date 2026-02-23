package com.example.CapStoneProject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class TemplateListItemResponse {

    private UUID templateId;
    private String name;
    private String subject;
    private OffsetDateTime updatedAt;
}