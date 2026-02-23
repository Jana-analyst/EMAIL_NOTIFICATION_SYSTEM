package com.example.CapStoneProject.controller;

import com.example.CapStoneProject.dto.request.CreateTemplateRequest;
import com.example.CapStoneProject.dto.request.UpdateTemplateRequest;
import com.example.CapStoneProject.dto.response.TemplateDetailsResponse;
import com.example.CapStoneProject.dto.response.TemplateListItemResponse;
import com.example.CapStoneProject.service.TemplateService;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/templates")
@CrossOrigin(origins = "*")
public class TemplateController {

    private final TemplateService templateService;

    public TemplateController(TemplateService templateService) {
        this.templateService = templateService;
    }

    @PostMapping
    public TemplateListItemResponse createTemplate(@RequestBody
                                                       CreateTemplateRequest request) {
        return templateService.createTemplate(request);
    }

    @GetMapping
    public List<TemplateListItemResponse> listTemplates() {
        return templateService.listTemplates();
    }

    @GetMapping("/{templateId}")
    public TemplateDetailsResponse getTemplateDetails(
            @PathVariable UUID templateId) {
        return templateService.getTemplateDetails(templateId);
    }

    @PutMapping("/{templateId}")
    public TemplateListItemResponse updateTemplate(
            @PathVariable UUID templateId,
            @RequestBody UpdateTemplateRequest request) {
        return templateService.updateTemplate(templateId, request);
    }

    @DeleteMapping("/{templateId}")
    public void deleteTemplate(@PathVariable UUID templateId) {
        templateService.deleteTemplate(templateId);
    }
}