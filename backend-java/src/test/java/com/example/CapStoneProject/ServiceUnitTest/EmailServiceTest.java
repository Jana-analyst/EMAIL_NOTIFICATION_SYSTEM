package com.example.CapStoneProject.ServiceUnitTest;

import com.example.CapStoneProject.dto.request.CreateEmailRequest;
import com.example.CapStoneProject.dto.request.TestEmailRequest;
import com.example.CapStoneProject.dto.response.*;
import com.example.CapStoneProject.messaging.EmailJobPublisher;
import com.example.CapStoneProject.models.*;
import com.example.CapStoneProject.enums.*;
import com.example.CapStoneProject.repository.*;
import com.example.CapStoneProject.service.EmailService;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock EmailRepository emailRepository;
    @Mock EmailStatusRepository emailStatusRepository;
    @Mock TemplateRepository templateRepository;
    @Mock EmailJobPublisher emailProducer;

    @InjectMocks EmailService emailService;

    private UUID templateId;
    private UUID emailId;

    @BeforeEach
    void setup() {
        templateId = UUID.randomUUID();
        emailId = UUID.randomUUID();
    }

    // ✅ sendEmail SUCCESS
    @Test
    void sendEmail_shouldQueueAndPublish() {

        CreateEmailRequest request =
                new CreateEmailRequest("test@mail.com", templateId, Map.of("name", "John"));

        EmailTemplate template = mock(EmailTemplate.class);
        when(template.getSubject()).thenReturn("Hello {{name}}");
        when(template.getBody()).thenReturn("Body {{name}}");

        when(templateRepository.findById(templateId))
                .thenReturn(Optional.of(template));

        EmailMessage savedEmail = mock(EmailMessage.class);
        when(savedEmail.getId()).thenReturn(emailId);

        when(emailRepository.save(any())).thenReturn(savedEmail);

        when(emailStatusRepository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));

        EmailListItemResponse response = emailService.sendEmail(request);

        assertNotNull(response);

        verify(emailProducer).publish(emailId);
    }

    // ✅ TEMPLATE MISSING
    @Test
    void sendEmail_shouldThrowWhenTemplateMissing() {

        CreateEmailRequest request =
                new CreateEmailRequest("test@mail.com", templateId, Map.of());

        when(templateRepository.findById(templateId))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> emailService.sendEmail(request));

        assertEquals("Template not found", ex.getMessage());
    }

    // ✅ sendTestEmail SUCCESS
    @Test
    void sendTestEmail_shouldSaveQueueAndPublish() {

        ReflectionTestUtils.setField(emailService, "fromEmail", "system@test.com");

        TestEmailRequest request =
                new TestEmailRequest(templateId, Map.of());

        EmailTemplate template = mock(EmailTemplate.class);
        when(template.getSubject()).thenReturn("Test");
        when(template.getBody()).thenReturn("Body");

        when(templateRepository.findById(templateId))
                .thenReturn(Optional.of(template));

        EmailMessage savedEmail = mock(EmailMessage.class);
        when(savedEmail.getId()).thenReturn(emailId);

        when(emailRepository.save(any())).thenReturn(savedEmail);

        when(emailStatusRepository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));

        TestEmailResponse response = emailService.sendTestEmail(request);

        assertNotNull(response);

        verify(emailProducer).publish(emailId);
    }

    // ✅ getEmailDetails SUCCESS
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
    }

    // ✅ EMAIL MISSING
    @Test
    void getEmailDetails_shouldThrowWhenMissing() {

        when(emailRepository.findById(emailId))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> emailService.getEmailDetails(emailId));

        assertEquals("Email not found", ex.getMessage());
    }

    // ✅ listEmails SUCCESS
    @Test
    void listEmails_shouldMapStatuses() {

        EmailMessage email = mock(EmailMessage.class);
        when(email.getId()).thenReturn(emailId);

        EmailStatus status = mock(EmailStatus.class);
        when(status.getSystemStatus()).thenReturn(SystemStatus.QUEUED);
        when(status.getProviderStatus()).thenReturn(ProviderStatus.PENDING);

        when(emailRepository.findAll()).thenReturn(List.of(email));
        when(emailStatusRepository.findByEmail_Id(emailId))
                .thenReturn(Optional.of(status));

        List<EmailListItemResponse> responses = emailService.listEmails();

        assertEquals(1, responses.size());
    }
}