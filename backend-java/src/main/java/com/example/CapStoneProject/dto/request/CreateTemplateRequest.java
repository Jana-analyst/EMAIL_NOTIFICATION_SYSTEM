package com.example.CapStoneProject.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateTemplateRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String subject;

    @NotBlank
    private String body;
}