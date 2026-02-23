package com.example.CapStoneProject.ControllerUnitTest;

import com.example.CapStoneProject.controller.TemplateController;
import com.example.CapStoneProject.dto.request.CreateTemplateRequest;
import com.example.CapStoneProject.dto.request.UpdateTemplateRequest;
import com.example.CapStoneProject.dto.response.TemplateDetailsResponse;
import com.example.CapStoneProject.dto.response.TemplateListItemResponse;
import com.example.CapStoneProject.service.TemplateService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TemplateControllerTest {

    private TemplateService templateService;
    private TemplateController templateController;

    @BeforeEach
    void setup() {
        templateService = mock(TemplateService.class);
        templateController = new TemplateController(templateService);
    }

    // ============================
    // createTemplate
    // ============================
    @Test
    void createTemplate_shouldDelegateToService() {

        UUID templateId = UUID.randomUUID();

        CreateTemplateRequest request =
                new CreateTemplateRequest(
                        "Welcome",
                        "Subject",
                        "Body"
                );

        TemplateListItemResponse response =
                new TemplateListItemResponse(
                        templateId,
                        "Welcome",
                        "Subject",
                        null
                );

        when(templateService.createTemplate(request)).thenReturn(response);

        TemplateListItemResponse result =
                templateController.createTemplate(request);

        assertNotNull(result);
        assertEquals(templateId, result.getTemplateId());

        verify(templateService).createTemplate(request);
    }

    // ============================
    // listTemplates
    // ============================
    @Test
    void listTemplates_shouldReturnFromService() {

        TemplateListItemResponse item =
                new TemplateListItemResponse(
                        UUID.randomUUID(),
                        "Template",
                        "Subject",
                        null
                );

        when(templateService.listTemplates()).thenReturn(List.of(item));

        List<TemplateListItemResponse> result =
                templateController.listTemplates();

        assertEquals(1, result.size());

        verify(templateService).listTemplates();
    }

    // ============================
    // getTemplateDetails
    // ============================
    @Test
    void getTemplateDetails_shouldReturnFromService() {

        UUID templateId = UUID.randomUUID();

        TemplateDetailsResponse response =
                new TemplateDetailsResponse(
                        templateId,
                        "Welcome",
                        "Subject",
                        "Body",
                        List.of("name"),
                        null,
                        null
                );

        when(templateService.getTemplateDetails(templateId))
                .thenReturn(response);

        TemplateDetailsResponse result =
                templateController.getTemplateDetails(templateId);

        assertNotNull(result);
        assertEquals(templateId, result.getTemplateId());

        verify(templateService).getTemplateDetails(templateId);
    }

    // ============================
    // updateTemplate
    // ============================
    @Test
    void updateTemplate_shouldDelegateToService() {

        UUID templateId = UUID.randomUUID();

        UpdateTemplateRequest request =
                new UpdateTemplateRequest("New Subject", "New Body");

        TemplateListItemResponse response =
                new TemplateListItemResponse(
                        templateId,
                        "Welcome",
                        "New Subject",
                        null
                );

        when(templateService.updateTemplate(templateId, request))
                .thenReturn(response);

        TemplateListItemResponse result =
                templateController.updateTemplate(templateId, request);

        assertNotNull(result);
        assertEquals(templateId, result.getTemplateId());

        verify(templateService).updateTemplate(templateId, request);
    }

    // ============================
    // deleteTemplate
    // ============================
    @Test
    void deleteTemplate_shouldCallService() {

        UUID templateId = UUID.randomUUID();

        doNothing().when(templateService).deleteTemplate(templateId);

        assertDoesNotThrow(() ->
                templateController.deleteTemplate(templateId));

        verify(templateService).deleteTemplate(templateId);
    }
}