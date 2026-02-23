package com.example.CapStoneProject.ServiceIntegrationTest;
import org.springframework.test.context.ActiveProfiles;
import com.example.CapStoneProject.dto.request.CreateTemplateRequest;
import com.example.CapStoneProject.dto.request.UpdateTemplateRequest;
import com.example.CapStoneProject.dto.response.TemplateDetailsResponse;
import com.example.CapStoneProject.dto.response.TemplateListItemResponse;
import com.example.CapStoneProject.models.EmailTemplate;
import com.example.CapStoneProject.repository.TemplateRepository;
import com.example.CapStoneProject.service.TemplateService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TemplateServiceIntegrationTest {

    @Autowired
    TemplateService templateService;

    @Autowired
    TemplateRepository templateRepository;

    @Test
    void createTemplate_shouldPersistInDatabase() {

        CreateTemplateRequest request =
                new CreateTemplateRequest(
                        "Welcome",
                        "Hello {{name}}",
                        "Body {{code}}"
                );

        TemplateListItemResponse response =
                templateService.createTemplate(request);

        assertNotNull(response.getTemplateId());
    }

    @Test
    void getTemplateDetails_shouldExtractPlaceholders() {

        EmailTemplate template = new EmailTemplate(
                "Test",
                "Hello {{user}}",
                "Body {{token}}"
        );

        templateRepository.save(template);

        TemplateDetailsResponse details =
                templateService.getTemplateDetails(template.getId());

        assertEquals(2, details.getPlaceholders().size());
    }

    @Test
    void updateTemplate_shouldUpdateFields() {

        EmailTemplate template = new EmailTemplate(
                "Test2",
                "Old Subject",
                "Old Body"
        );

        templateRepository.save(template);

        UpdateTemplateRequest request =
                new UpdateTemplateRequest("New Subject", "New Body");

        TemplateListItemResponse response =
                templateService.updateTemplate(template.getId(), request);

        assertNotNull(response);
    }

    @Test
    void getTemplateDetails_shouldThrowWhenMissing() {

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> templateService.getTemplateDetails(UUID.randomUUID()));

        assertEquals("Template not found", ex.getMessage());
    }
}