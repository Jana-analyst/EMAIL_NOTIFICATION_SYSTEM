package com.example.CapStoneProject.ControllerUnitTest;

import com.example.CapStoneProject.controller.EmailController;
import com.example.CapStoneProject.dto.request.CreateEmailRequest;
import com.example.CapStoneProject.dto.request.TestEmailRequest;
import com.example.CapStoneProject.dto.response.EmailDetailsResponse;
import com.example.CapStoneProject.dto.response.EmailListItemResponse;
import com.example.CapStoneProject.dto.response.TestEmailResponse;
import com.example.CapStoneProject.service.EmailService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailControllerTest {

    private EmailService emailService;
    private EmailController emailController;

    @BeforeEach
    void setup() {
        emailService = mock(EmailService.class);
        emailController = new EmailController(emailService);
    }

    // ============================
    // sendEmail
    // ============================
    @Test
    void sendEmail_shouldDelegateToService() {

        UUID templateId = UUID.randomUUID();
        UUID emailId = UUID.randomUUID();

        CreateEmailRequest request =
                new CreateEmailRequest("user@mail.com", templateId, Map.of());

        EmailListItemResponse response =
                new EmailListItemResponse(
                        emailId,
                        "user@mail.com",
                        "Subject",
                        "QUEUED",
                        "PENDING",
                        null
                );

        when(emailService.sendEmail(request)).thenReturn(response);

        EmailListItemResponse result = emailController.sendEmail(request);

        assertNotNull(result);
        assertEquals(emailId, result.getEmailId());

        verify(emailService).sendEmail(request);
    }

    // ============================
    // sendTestEmail
    // ============================
    @Test
    void sendTestEmail_shouldDelegateToService() {

        UUID templateId = UUID.randomUUID();
        UUID emailId = UUID.randomUUID();

        TestEmailRequest request =
                new TestEmailRequest(templateId, Map.of());

        TestEmailResponse response =
                new TestEmailResponse(emailId, "Subject", "QUEUED", null);

        when(emailService.sendTestEmail(request)).thenReturn(response);

        TestEmailResponse result = emailController.sendTestEmail(request);

        assertNotNull(result);
        assertEquals(emailId, result.getEmailId());

        verify(emailService).sendTestEmail(request);
    }

    // ============================
    // listEmails
    // ============================
    @Test
    void listEmails_shouldReturnFromService() {

        EmailListItemResponse item =
                new EmailListItemResponse(
                        UUID.randomUUID(),
                        "user@mail.com",
                        "Subject",
                        "QUEUED",
                        "PENDING",
                        null
                );

        when(emailService.listEmails()).thenReturn(List.of(item));

        List<EmailListItemResponse> result = emailController.listEmails();

        assertEquals(1, result.size());

        verify(emailService).listEmails();
    }

    // ============================
    // getEmailDetails
    // ============================
    @Test
    void getEmailDetails_shouldReturnFromService() {

        UUID emailId = UUID.randomUUID();

        EmailDetailsResponse response =
                new EmailDetailsResponse(
                        emailId,
                        "user@mail.com",
                        "Subject",
                        "Body",
                        "QUEUED",
                        "PENDING",
                        null,
                        null
                );

        when(emailService.getEmailDetails(emailId)).thenReturn(response);

        EmailDetailsResponse result =
                emailController.getEmailDetails(emailId);

        assertNotNull(result);
        assertEquals(emailId, result.getEmailId());

        verify(emailService).getEmailDetails(emailId);
    }
}