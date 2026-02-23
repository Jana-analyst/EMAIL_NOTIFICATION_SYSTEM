package com.example.CapStoneProject.service;

import com.example.CapStoneProject.dto.request.CreateTemplateRequest;
import com.example.CapStoneProject.dto.request.UpdateTemplateRequest;
import com.example.CapStoneProject.dto.response.TemplateDetailsResponse;
import com.example.CapStoneProject.dto.response.TemplateListItemResponse;
import com.example.CapStoneProject.models.EmailTemplate;
import com.example.CapStoneProject.repository.TemplateRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TemplateService {

    private final TemplateRepository templateRepository;

    public TemplateService(TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    @Transactional
    public TemplateListItemResponse createTemplate(CreateTemplateRequest request) {

        EmailTemplate template = new EmailTemplate(
                request.getName(),
                request.getSubject(),
                request.getBody()
        );

        templateRepository.save(template);

        return mapToListItem(template);
    }

    @Transactional(readOnly = true)
    public List<TemplateListItemResponse> listTemplates() {
        return templateRepository.findAll()
                .stream()
                .map(this::mapToListItem)
                .toList();
    }

    @Transactional(readOnly = true)
    public TemplateDetailsResponse getTemplateDetails(UUID templateId) {

        EmailTemplate template = templateRepository.findById(templateId)
                .orElseThrow(() -> new RuntimeException("Template not found"));

        return mapToDetails(template);
    }

    @Transactional
    public TemplateListItemResponse updateTemplate(UUID templateId, UpdateTemplateRequest request) {

        EmailTemplate template = templateRepository.findById(templateId)
                .orElseThrow(() -> new RuntimeException("Template not found"));

        if (request.getSubject() != null) {
            template.updateSubject(request.getSubject());
        }

        if (request.getBody() != null) {
            template.updateBody(request.getBody());
        }

        templateRepository.save(template);

        return mapToListItem(template);
    }

    @Transactional
    public void deleteTemplate(UUID templateId) {

        if (!templateRepository.existsById(templateId)) {
            throw new RuntimeException("Template not found");
        }

        templateRepository.deleteById(templateId);
    }

    private List<String> extractPlaceholders(String content) {
        Pattern pattern = Pattern.compile("\\{\\{(.*?)}}");
        Matcher matcher = pattern.matcher(content);

        Set<String> keys = new LinkedHashSet<>();

        while (matcher.find()) {
            keys.add(matcher.group(1));
        }

        return new ArrayList<>(keys);
    }

    private TemplateListItemResponse mapToListItem(EmailTemplate template) {
        return new TemplateListItemResponse(
                template.getId(),
                template.getName(),
                template.getSubject(),
                template.getUpdatedAt()
        );
    }

    private TemplateDetailsResponse mapToDetails(EmailTemplate template) {

        List<String> placeholders =
                extractPlaceholders(template.getSubject() + template.getBody());

        return new TemplateDetailsResponse(
                template.getId(),
                template.getName(),
                template.getSubject(),
                template.getBody(),
                placeholders,
                template.getCreatedAt(),
                template.getUpdatedAt()
        );
    }
}