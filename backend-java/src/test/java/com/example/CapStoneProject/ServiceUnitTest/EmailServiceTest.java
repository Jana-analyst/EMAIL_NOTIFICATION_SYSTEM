package com.example.CapStoneProject.ServiceUnitTest;

import com.example.CapStoneProject.dto.request.CreateEmailRequest;
import com.example.CapStoneProject.dto.request.TestEmailRequest;
import com.example.CapStoneProject.dto.response.EmailDetailsResponse;
import com.example.CapStoneProject.dto.response.EmailListItemResponse;
import com.example.CapStoneProject.dto.response.TestEmailResponse;
import com.example.CapStoneProject.messaging.EmailJobPublisher;
import com.example.CapStoneProject.models.EmailMessage;
import com.example.CapStoneProject.models.EmailStatus;
import com.example.CapStoneProject.models.EmailTemplate;
import com.example.CapStoneProject.enums.SystemStatus;
import com.example.CapStoneProject.enums.ProviderStatus;
import com.example.CapStoneProject.repository.EmailRepository;
import com.example.CapStoneProject.repository.EmailStatusRepository;
import com.example.CapStoneProject.repository.TemplateRepository;

import com.example.CapStoneProject.service.EmailService;
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
class EmailServiceTest {

    @Mock
    EmailRepository emailRepository;

    @Mock
    EmailStatusRepository emailStatusRepository;

    @Mock
    TemplateRepository templateRepository;

    @Mock
    EmailJobPublisher emailProducer;

    @InjectMocks
    EmailService emailService;

    private UUID templateId;
    private UUID emailId;

    @BeforeEach
    void setup() {
        templateId = UUID.randomUUID();
        emailId = UUID.randomUUID();
    }

    // ============================
    // sendEmail → Success Path
    // ============================
    @Test
    void sendEmail_shouldQueueAndPublish() {

        CreateEmailRequest request =
                new CreateEmailRequest(
                        "test@mail.com",
                        templateId,
                        Map.of("name", "John")
                );

        EmailTemplate template = mock(EmailTemplate.class);
        when(template.getSubject()).thenReturn("Hello {{name}}");
        when(template.getBody()).thenReturn("Body {{name}}");

        when(templateRepository.findById(templateId))
                .thenReturn(Optional.of(template));

        EmailMessage savedEmail = mock(EmailMessage.class);
        when(savedEmail.getId()).thenReturn(emailId);
        when(savedEmail.getCreatedAt()).thenReturn(null);

        when(emailRepository.save(any())).thenReturn(savedEmail);

        when(emailStatusRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        EmailListItemResponse response = emailService.sendEmail(request);

        assertNotNull(response);

        verify(emailRepository).save(any());
        verify(emailStatusRepository).save(any());
        verify(emailProducer).publish(emailId);
    }

    // ============================
    // sendEmail → Template Missing
    // ============================
    @Test
    void sendEmail_shouldThrowWhenTemplateMissing() {

        CreateEmailRequest request =
                new CreateEmailRequest(
                        "test@mail.com",
                        templateId,
                        Collections.emptyMap()
                );

        when(templateRepository.findById(templateId))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> emailService.sendEmail(request));

        assertEquals("Template not found", ex.getMessage());

        verify(emailRepository, never()).save(any());
        verify(emailProducer, never()).publish(any());
    }

    // ============================
    // sendTestEmail → Success Path
    // ============================
    @Test
    void sendTestEmail_shouldSaveQueueAndPublish() {

        TestEmailRequest request =
                new TestEmailRequest(
                        templateId,
                        Map.of("code", "123")
                );

        EmailTemplate template = mock(EmailTemplate.class);
        when(template.getSubject()).thenReturn("Code {{code}}");
        when(template.getBody()).thenReturn("Body {{code}}");

        when(templateRepository.findById(templateId))
                .thenReturn(Optional.of(template));

        EmailMessage savedEmail = mock(EmailMessage.class);
        when(savedEmail.getId()).thenReturn(emailId);

        /* THIS STUB IS MANDATORY */
        when(emailRepository.save(any())).thenReturn(savedEmail);

        when(emailStatusRepository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));

        TestEmailResponse response = emailService.sendTestEmail(request);

        assertNotNull(response);

        verify(emailRepository).save(any());
        verify(emailStatusRepository).save(any());
        verify(emailProducer).publish(emailId);
    }

    // ============================
    // getEmailDetails → Success
    // ============================
    @Test
    void getEmailDetails_shouldReturnDetails() {

        EmailMessage email = mock(EmailMessage.class);
        EmailStatus status = mock(EmailStatus.class);

        when(emailRepository.findById(emailId))
                .thenReturn(Optional.of(email));

        when(emailStatusRepository.findByEmail_Id(emailId))
                .thenReturn(Optional.of(status));

        when(status.getSystemStatus()).thenReturn(SystemStatus.QUEUED);
        when(status.getProviderStatus()).thenReturn(ProviderStatus.PENDING);

        EmailDetailsResponse response = emailService.getEmailDetails(emailId);

        assertNotNull(response);

        verify(emailRepository).findById(emailId);
        verify(emailStatusRepository).findByEmail_Id(emailId);
    }

    // ============================
    // getEmailDetails → Missing Email
    // ============================
    @Test
    void getEmailDetails_shouldThrowWhenMissing() {

        when(emailRepository.findById(emailId))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> emailService.getEmailDetails(emailId));

        assertEquals("Email not found", ex.getMessage());
    }

    // ============================
    // listEmails → Happy Path
    // ============================
    @Test
    void listEmails_shouldMapStatuses() {

        EmailMessage email = mock(EmailMessage.class);
        when(email.getId()).thenReturn(emailId);
        when(email.getRecipient()).thenReturn("user@mail.com");
        when(email.getSubject()).thenReturn("Subject");
        when(email.getCreatedAt()).thenReturn(null);

        EmailStatus status = mock(EmailStatus.class);
        when(status.getSystemStatus()).thenReturn(SystemStatus.QUEUED);
        when(status.getProviderStatus()).thenReturn(ProviderStatus.PENDING);

        when(emailRepository.findAll()).thenReturn(List.of(email));
        when(emailStatusRepository.findByEmail_Id(emailId))
                .thenReturn(Optional.of(status));

        List<EmailListItemResponse> responses = emailService.listEmails();

        assertEquals(1, responses.size());

        verify(emailRepository).findAll();
        verify(emailStatusRepository).findByEmail_Id(emailId);
    }
}