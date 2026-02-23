package com.example.CapStoneProject.ServiceUnitTest;

import com.example.CapStoneProject.dto.request.CreateTemplateRequest;
import com.example.CapStoneProject.dto.request.UpdateTemplateRequest;
import com.example.CapStoneProject.dto.response.TemplateDetailsResponse;
import com.example.CapStoneProject.dto.response.TemplateListItemResponse;
import com.example.CapStoneProject.models.EmailTemplate;
import com.example.CapStoneProject.repository.TemplateRepository;
import com.example.CapStoneProject.service.TemplateService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TemplateServiceTest {

    @Mock TemplateRepository templateRepository;
    @InjectMocks TemplateService templateService;

    private UUID templateId;

    @BeforeEach
    void setup() {
        templateId = UUID.randomUUID();
    }

    // ✅ createTemplate SUCCESS
    @Test
    void createTemplate_shouldSaveAndReturnResponse() {

        CreateTemplateRequest request =
                new CreateTemplateRequest("Welcome", "Hello", "Body");

        TemplateListItemResponse response =
                templateService.createTemplate(request);

        assertNotNull(response);

        verify(templateRepository).save(any(EmailTemplate.class));
    }

    // ✅ listTemplates SUCCESS
    @Test
    void listTemplates_shouldReturnMappedResponses() {

        EmailTemplate template = mock(EmailTemplate.class);

        when(template.getId()).thenReturn(templateId);
        when(template.getName()).thenReturn("Test");
        when(template.getSubject()).thenReturn("Subject");
        when(template.getUpdatedAt()).thenReturn(null);

        when(templateRepository.findAll()).thenReturn(List.of(template));

        List<TemplateListItemResponse> responses =
                templateService.listTemplates();

        assertEquals(1, responses.size());
    }

    // ✅ getTemplateDetails SUCCESS
    @Test
    void getTemplateDetails_shouldReturnDetails() {

        EmailTemplate template = mock(EmailTemplate.class);

        when(template.getId()).thenReturn(templateId);
        when(template.getName()).thenReturn("Welcome");
        when(template.getSubject()).thenReturn("Hello {{name}}");
        when(template.getBody()).thenReturn("Body {{code}}");
        when(template.getCreatedAt()).thenReturn(null);
        when(template.getUpdatedAt()).thenReturn(null);

        when(templateRepository.findById(templateId))
                .thenReturn(Optional.of(template));

        TemplateDetailsResponse response =
                templateService.getTemplateDetails(templateId);

        assertNotNull(response);
        assertEquals(2, response.getPlaceholders().size());
    }

    // ✅ getTemplateDetails MISSING
    @Test
    void getTemplateDetails_shouldThrowWhenMissing() {

        when(templateRepository.findById(templateId))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> templateService.getTemplateDetails(templateId));

        assertEquals("Template not found", ex.getMessage());
    }

    // ✅ updateTemplate SUBJECT ONLY
    @Test
    void updateTemplate_shouldUpdateSubjectOnly() {

        UpdateTemplateRequest request = mock(UpdateTemplateRequest.class);
        when(request.getSubject()).thenReturn("New Subject");
        when(request.getBody()).thenReturn(null);

        EmailTemplate template = mock(EmailTemplate.class);

        when(templateRepository.findById(templateId))
                .thenReturn(Optional.of(template));

        TemplateListItemResponse response =
                templateService.updateTemplate(templateId, request);

        assertNotNull(response);

        verify(template).updateSubject("New Subject");
        verify(templateRepository).save(template);
    }

    // ✅ updateTemplate BODY ONLY (Coverage Gain)
    @Test
    void updateTemplate_shouldUpdateBodyOnly() {

        UpdateTemplateRequest request = mock(UpdateTemplateRequest.class);
        when(request.getSubject()).thenReturn(null);
        when(request.getBody()).thenReturn("New Body");

        EmailTemplate template = mock(EmailTemplate.class);

        when(templateRepository.findById(templateId))
                .thenReturn(Optional.of(template));

        TemplateListItemResponse response =
                templateService.updateTemplate(templateId, request);

        assertNotNull(response);

        verify(template).updateBody("New Body");
        verify(templateRepository).save(template);
    }

    // ✅ deleteTemplate SUCCESS
    @Test
    void deleteTemplate_shouldDeleteWhenExists() {

        when(templateRepository.existsById(templateId)).thenReturn(true);

        templateService.deleteTemplate(templateId);

        verify(templateRepository).deleteById(templateId);
    }

    // ✅ deleteTemplate MISSING
    @Test
    void deleteTemplate_shouldThrowWhenMissing() {

        when(templateRepository.existsById(templateId)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> templateService.deleteTemplate(templateId));

        assertEquals("Template not found", ex.getMessage());
    }
}