package com.example.CapStoneProject.service;

import com.example.CapStoneProject.models.EmailMessage;
import com.example.CapStoneProject.models.EmailStatus;
import com.example.CapStoneProject.enums.SystemStatus;
import com.example.CapStoneProject.enums.ProviderStatus;
import com.example.CapStoneProject.repository.EmailStatusRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class EmailStatusService {

    private final EmailStatusRepository emailStatusRepository;

    public EmailStatusService(EmailStatusRepository emailStatusRepository) {
        this.emailStatusRepository = emailStatusRepository;
    }

    @Transactional
    public EmailStatus createQueuedStatus(EmailMessage email) {

        EmailStatus status = new EmailStatus(email, SystemStatus.QUEUED);
        return emailStatusRepository.save(status);
    }

    @Transactional
    public void markSentToProvider(UUID emailId) {

        EmailStatus status = loadStatus(emailId);
        status.markSentToProvider();

        emailStatusRepository.save(status);
    }

    @Transactional
    public void markFailed(UUID emailId, String reason) {

        EmailStatus status = loadStatus(emailId);
        status.markFailed(reason);

        emailStatusRepository.save(status);
    }

//    @Transactional
//    public void updateProviderStatus(UUID emailId, ProviderStatus providerStatus) {
//
//        EmailStatus status = loadStatus(emailId);
//        status.updateProviderStatus(providerStatus);
//
//        emailStatusRepository.save(status);
//    }

    private EmailStatus loadStatus(UUID emailId) {
        return emailStatusRepository.findByEmail_Id(emailId)
                .orElseThrow(() -> new RuntimeException("EmailStatus not found"));
    }

    @Transactional
    public void updateProviderStatus(UUID emailId, ProviderStatus newStatus) {
        EmailStatus status = loadStatus(emailId);

        // Logic: Don't let 'PROCESSED' overwrite 'DELIVERED' or 'OPENED'
        if (shouldUpdate(status.getProviderStatus(), newStatus)) {
            status.updateProviderStatus(newStatus);
            emailStatusRepository.save(status);
        }
    }

    private boolean shouldUpdate(ProviderStatus current, ProviderStatus next) {
        if (current == null) return true;

        // Define the hierarchy (you can expand this)
        // OPENED (3) > DELIVERED (2) > PROCESSED (1)
        int currentRank = getRank(current);
        int nextRank = getRank(next);

        return nextRank >= currentRank;
    }

    private int getRank(ProviderStatus s) {
        return switch(s) {
            case OPENED, CLICKED -> 3;
            case DELIVERED -> 2;
            case PROCESSED -> 1;
            default -> 0;
        };
    }
}