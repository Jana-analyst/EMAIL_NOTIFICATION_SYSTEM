package com.example.CapStoneProject.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class CreateEmailRequest {

    @NotNull
    @Email
    private String recipient;

    @NotNull
    private UUID templateId;

    private Map<String, String> variables = Collections.emptyMap();

}