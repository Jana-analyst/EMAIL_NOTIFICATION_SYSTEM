package com.example.CapStoneProject.service;

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
import com.example.CapStoneProject.repository.EmailRepository;
import com.example.CapStoneProject.repository.EmailStatusRepository;
import com.example.CapStoneProject.repository.TemplateRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class EmailService {

    private final EmailRepository emailRepository;
    private final EmailStatusRepository emailStatusRepository;
    private final TemplateRepository templateRepository;
    private final EmailJobPublisher emailProducer;

    @Value("${sendgrid.from-email}")
    private String fromEmail;

    public EmailService(EmailRepository emailRepository,
                        EmailStatusRepository emailStatusRepository,
                        TemplateRepository templateRepository,
                        EmailJobPublisher emailProducer) {
        this.emailRepository = emailRepository;
        this.emailStatusRepository = emailStatusRepository;
        this.templateRepository = templateRepository;
        this.emailProducer = emailProducer;
    }

    @Transactional
    public EmailListItemResponse sendEmail(CreateEmailRequest request) {

        EmailTemplate template = templateRepository.findById(request.getTemplateId())
                .orElseThrow(() -> new RuntimeException("Template not found"));

        String subject = render(template.getSubject(), request.getVariables());
        String body = render(template.getBody(), request.getVariables());

        EmailMessage email = new EmailMessage(
                request.getRecipient(),
                subject,
                body,
                request.getTemplateId()
        );

        EmailMessage savedEmail = emailRepository.save(email);

        EmailStatus status = new EmailStatus(savedEmail, SystemStatus.QUEUED);
        emailStatusRepository.save(status);

        emailProducer.publish(savedEmail.getId());

        return mapToListItem(savedEmail, status);
    }

    @Transactional
    public TestEmailResponse sendTestEmail(TestEmailRequest request) {

        EmailTemplate template = templateRepository.findById(request.getTemplateId())
                .orElseThrow(() -> new RuntimeException("Template not found"));

        String subject = render(template.getSubject(), request.getVariables());
        String body = render(template.getBody(), request.getVariables());

        EmailMessage email = new EmailMessage(
                fromEmail,
                subject,
                body,
                request.getTemplateId()
        );

        EmailMessage savedEmail = emailRepository.save(email);   // CRITICAL FIX

        EmailStatus status = new EmailStatus(savedEmail, SystemStatus.QUEUED);
        emailStatusRepository.save(status);

        emailProducer.publish(savedEmail.getId());               // CRITICAL FIX

        return new TestEmailResponse(
                email.getId(),
                email.getSubject(),
                status.getSystemStatus().name(),
                email.getCreatedAt()
        );
    }

    @Transactional(readOnly = true)
    public List<EmailListItemResponse> listEmails() {
        return emailRepository.findAll()
                .stream()
                .map(email -> {
                    return emailStatusRepository.findByEmail_Id(email.getId())
                            .map(status -> mapToListItem(email, status))
                            .orElse(null);   // skip if missing
                })
                .filter(java.util.Objects::nonNull)
                .toList();
    }

    @Transactional(readOnly = true)
    public EmailDetailsResponse getEmailDetails(UUID emailId) {

        EmailMessage email = emailRepository.findById(emailId)
                .orElseThrow(() -> new RuntimeException("Email not found"));

        EmailStatus status = emailStatusRepository.findByEmail_Id(emailId)
                .orElseThrow();

        return new EmailDetailsResponse(
                email.getId(),
                email.getRecipient(),
                email.getSubject(),
                email.getBody(),
                status.getSystemStatus().name(),
                status.getProviderStatus().name(),
                email.getCreatedAt(),
                status.getUpdatedAt()
        );
    }

    private String render(String template, Map<String, String> variables) {

        String result = template;

        for (Map.Entry<String, String> entry : variables.entrySet()) {
            result = result.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }

        return result;
    }

    private EmailListItemResponse mapToListItem(EmailMessage email, EmailStatus status) {
        return new EmailListItemResponse(
                email.getId(),
                email.getRecipient(),
                email.getSubject(),
                status.getSystemStatus().name(),
                status.getProviderStatus().name(),
                email.getCreatedAt()
        );
    }
}