package com.example.CapStoneProject.ServiceIntegrationTest;

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

import com.example.CapStoneProject.enums.SystemStatus;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class EmailServiceIntegrationTest {

    @Autowired EmailService emailService;
    @Autowired TemplateRepository templateRepository;
    @Autowired EmailRepository emailRepository;
    @Autowired EmailStatusRepository emailStatusRepository;

    // ✅ FULL FLOW + RENDER LOGIC
    @Test
    void sendEmail_shouldPersistEmailAndRenderVariables() {

        EmailTemplate template = new EmailTemplate(
                "Welcome-" + UUID.randomUUID(),    // avoid unique constraint collisions
                "Hello {{name}} {{code}}",
                "Body {{name}} {{code}}"
        );

        templateRepository.save(template);

        CreateEmailRequest request = new CreateEmailRequest(
                "user@mail.com",
                template.getId(),
                Map.of("name", "John", "code", "007")
        );

        EmailListItemResponse response = emailService.sendEmail(request);

        EmailMessage savedEmail = emailRepository.findById(response.getEmailId())
                .orElseThrow();

        assertEquals("Hello John 007", savedEmail.getSubject());
        assertEquals("Body John 007", savedEmail.getBody());

        EmailStatus status = emailStatusRepository.findByEmail_Id(savedEmail.getId())
                .orElseThrow();

        assertEquals(SystemStatus.QUEUED, status.getSystemStatus());
    }

    // ✅ listEmails() BRANCH COVERAGE
    @Test
    void listEmails_shouldReturnMappedResponses() {

        EmailTemplate template = new EmailTemplate(
                "List-" + UUID.randomUUID(),
                "Subject",
                "Body"
        );

        templateRepository.save(template);

        emailService.sendEmail(new CreateEmailRequest(
                "a@mail.com",
                template.getId(),
                Map.of()
        ));

        List<EmailListItemResponse> list = emailService.listEmails();

        assertFalse(list.isEmpty());
        assertNotNull(list.get(0).getEmailId());
    }

    // ✅ getEmailDetails() SUCCESS
    @Test
    void getEmailDetails_shouldReturnCorrectData() {

        EmailTemplate template = new EmailTemplate(
                "Details-" + UUID.randomUUID(),
                "Subject",
                "Body"
        );

        templateRepository.save(template);

        EmailListItemResponse listItem = emailService.sendEmail(
                new CreateEmailRequest(
                        "details@mail.com",
                        template.getId(),
                        Map.of()
                )
        );

        EmailDetailsResponse details =
                emailService.getEmailDetails(listItem.getEmailId());

        assertEquals("details@mail.com", details.getRecipient());
        assertEquals("Subject", details.getSubject());
        assertNotNull(details.getSystemStatus());
    }

    // ✅ getEmailDetails() EXCEPTION PATH
    @Test
    void getEmailDetails_shouldThrow_whenEmailMissing() {

        UUID randomId = UUID.randomUUID();

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> emailService.getEmailDetails(randomId));

        assertEquals("Email not found", ex.getMessage());
    }

    // ✅ TEMPLATE MISSING BRANCH
    @Test
    void sendEmail_shouldThrow_whenTemplateMissing() {

        CreateEmailRequest request = new CreateEmailRequest(
                "missing@mail.com",
                UUID.randomUUID(),
                Map.of()
        );

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> emailService.sendEmail(request));

        assertEquals("Template not found", ex.getMessage());
    }
}