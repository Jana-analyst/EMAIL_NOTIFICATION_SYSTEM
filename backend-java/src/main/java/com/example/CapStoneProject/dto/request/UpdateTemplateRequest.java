package com.example.CapStoneProject.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateTemplateRequest {

    private String subject;
    private String body;
}