package com.example.CapStoneProject.service;

import com.example.CapStoneProject.models.EmailMessage;
import com.example.CapStoneProject.models.EmailStatus;
import com.example.CapStoneProject.repository.EmailRepository;
import com.example.CapStoneProject.repository.EmailStatusRepository;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class WorkerService {

    private final EmailRepository emailRepository;
    private final EmailStatusRepository emailStatusRepository;
    private final SendGridService sendGridService;

    public WorkerService(EmailRepository emailRepository,
                         EmailStatusRepository emailStatusRepository,
                         SendGridService sendGridService) {
        this.emailRepository = emailRepository;
        this.emailStatusRepository = emailStatusRepository;
        this.sendGridService = sendGridService;
    }

    public void process(UUID emailId) {

        EmailMessage email = emailRepository.findById(emailId)
                .orElseThrow(() -> new RuntimeException("Email not found"));

        EmailStatus status = emailStatusRepository.findByEmail_Id(emailId)
                .orElseThrow();

        try {

            sendGridService.sendEmail(email);

            status.markSentToProvider();
            emailStatusRepository.save(status);

        } catch (Exception ex) {

            status.markFailed(ex.getMessage());
            emailStatusRepository.save(status);
        }
    }
}