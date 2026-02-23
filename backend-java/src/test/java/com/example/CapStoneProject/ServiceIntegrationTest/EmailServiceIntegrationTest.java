package com.example.CapStoneProject.ServiceIntegrationTest;
import org.springframework.test.context.ActiveProfiles;
import com.example.CapStoneProject.dto.request.CreateEmailRequest;
import com.example.CapStoneProject.dto.response.EmailDetailsResponse;
import com.example.CapStoneProject.dto.response.EmailListItemResponse;
import com.example.CapStoneProject.models.EmailTemplate;
import com.example.CapStoneProject.models.EmailMessage;
import com.example.CapStoneProject.models.EmailStatus;
import com.example.CapStoneProject.repository.TemplateRepository;
import com.example.CapStoneProject.repository.EmailRepository;
import com.example.CapStoneProject.repository.EmailStatusRepository;
import com.example.CapStoneProject.service.EmailService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class EmailServiceIntegrationTest {

    @Autowired
    EmailService emailService;

    @Autowired
    TemplateRepository templateRepository;

    @Autowired
    EmailRepository emailRepository;

    @Autowired
    EmailStatusRepository emailStatusRepository;

    // ============================
    // sendEmail → Full Flow
    // ============================
    @Test
    void sendEmail_shouldPersistEmailAndStatus() {

        EmailTemplate template = new EmailTemplate(
                "Welcome",
                "Hello {{name}}",
                "Body {{name}}"
        );

        templateRepository.save(template);

        CreateEmailRequest request =
                new CreateEmailRequest(
                        "user@mail.com",
                        template.getId(),
                        Map.of("name", "John")
                );

        EmailListItemResponse response = emailService.sendEmail(request);

        assertNotNull(response.getEmailId());

        EmailMessage savedEmail =
                emailRepository.findById(response.getEmailId())
                        .orElseThrow();

        assertEquals("user@mail.com", savedEmail.getRecipient());
        assertEquals("Hello John", savedEmail.getSubject());

        EmailStatus status =
                emailStatusRepository.findByEmail_Id(savedEmail.getId())
                        .orElseThrow();

        assertNotNull(status);
    }

    // ============================
    // getEmailDetails → DB Read
    // ============================
    @Test
    void getEmailDetails_shouldReturnCorrectData() {

        EmailTemplate template = new EmailTemplate(
                "Test",
                "Subject",
                "Body"
        );

        templateRepository.save(template);

        CreateEmailRequest request =
                new CreateEmailRequest(
                        "test@mail.com",
                        template.getId(),
                        Map.of()
                );

        EmailListItemResponse listItem = emailService.sendEmail(request);

        EmailDetailsResponse details =
                emailService.getEmailDetails(listItem.getEmailId());

        assertEquals("test@mail.com", details.getRecipient());
        assertEquals("Subject", details.getSubject());
    }

    // ============================
    // Exception Branch
    // ============================
    @Test
    void sendEmail_shouldThrow_whenTemplateMissing() {

        CreateEmailRequest request =
                new CreateEmailRequest(
                        "user@mail.com",
                        UUID.randomUUID(),
                        Map.of()
                );

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> emailService.sendEmail(request));

        assertEquals("Template not found", ex.getMessage());
    }
}