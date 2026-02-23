package com.example.CapStoneProject.ServiceIntegrationTest;

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

    @Autowired TemplateService templateService;
    @Autowired TemplateRepository templateRepository;

    // ✅ CREATE + DB VALIDATION
    @Test
    void createTemplate_shouldPersistInDatabase() {

        String uniqueName = "Welcome-" + UUID.randomUUID();

        CreateTemplateRequest request = new CreateTemplateRequest(
                uniqueName,
                "Hello {{name}}",
                "Body {{code}}"
        );

        TemplateListItemResponse response =
                templateService.createTemplate(request);

        assertNotNull(response.getTemplateId());

        EmailTemplate saved =
                templateRepository.findById(response.getTemplateId())
                        .orElseThrow();

        assertEquals(uniqueName, saved.getName());
    }

    // ✅ PLACEHOLDER EXTRACTION BRANCH
    @Test
    void getTemplateDetails_shouldExtractPlaceholders() {

        EmailTemplate template = new EmailTemplate(
                "Placeholders-" + UUID.randomUUID(),
                "Hello {{user}}",
                "Body {{token}}"
        );

        templateRepository.save(template);

        TemplateDetailsResponse details =
                templateService.getTemplateDetails(template.getId());

        assertEquals(2, details.getPlaceholders().size());
    }

    // ✅ FULL UPDATE BRANCH
    @Test
    void updateTemplate_shouldUpdateFields() {

        EmailTemplate template = new EmailTemplate(
                "Update-" + UUID.randomUUID(),
                "Old Subject",
                "Old Body"
        );

        templateRepository.save(template);

        UpdateTemplateRequest request =
                new UpdateTemplateRequest("New Subject", "New Body");

        TemplateListItemResponse response =
                templateService.updateTemplate(template.getId(), request);

        EmailTemplate updated =
                templateRepository.findById(template.getId())
                        .orElseThrow();

        assertEquals("New Subject", updated.getSubject());
        assertEquals("New Body", updated.getBody());
    }

    // ✅ PARTIAL UPDATE BRANCH (IMPORTANT FOR SONAR)
    @Test
    void updateTemplate_shouldUpdateSubjectOnly() {

        EmailTemplate template = new EmailTemplate(
                "Partial-" + UUID.randomUUID(),
                "Old Subject",
                "Old Body"
        );

        templateRepository.save(template);

        UpdateTemplateRequest request =
                new UpdateTemplateRequest("Only Subject", null);

        templateService.updateTemplate(template.getId(), request);

        EmailTemplate updated =
                templateRepository.findById(template.getId())
                        .orElseThrow();

        assertEquals("Only Subject", updated.getSubject());
        assertEquals("Old Body", updated.getBody());
    }

    // ✅ DELETE BRANCH (YOU WERE MISSING THIS)
    @Test
    void deleteTemplate_shouldRemoveFromDatabase() {

        EmailTemplate template = new EmailTemplate(
                "Delete-" + UUID.randomUUID(),
                "Subject",
                "Body"
        );

        templateRepository.save(template);

        templateService.deleteTemplate(template.getId());

        assertFalse(templateRepository.existsById(template.getId()));
    }

    // ✅ EXCEPTION BRANCH
    @Test
    void getTemplateDetails_shouldThrowWhenMissing() {

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> templateService.getTemplateDetails(UUID.randomUUID()));

        assertEquals("Template not found", ex.getMessage());
    }
}