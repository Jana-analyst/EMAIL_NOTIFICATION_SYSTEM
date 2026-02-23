package com.example.CapStoneProject.controller;

import com.example.CapStoneProject.dto.request.SendGridRequest;
import com.example.CapStoneProject.enums.ProviderStatus;
import com.example.CapStoneProject.service.EmailStatusService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/webhooks/sendgrid")
public class SendGridWebhookController {

    private final EmailStatusService emailStatusService;

    public SendGridWebhookController(EmailStatusService emailStatusService) {
        this.emailStatusService = emailStatusService;
    }

    @PostMapping
    public void handleEvents(@RequestBody List<SendGridRequest> events) {
        System.out.println("WEBHOOK RECEIVED. Event Count: " + events.size());

        for (SendGridRequest event : events) {
            // Log the captured customArgs to verify emailId is there
            System.out.println("Processing Event: " + event.getEvent() + " | Args: " + event.getCustomArgs());

            String emailIdStr = event.getCustomArgs().get("emailId");

            if (emailIdStr == null) {
                System.out.println("Skipping event: No emailId found in payload");
                continue;
            }

            try {
                UUID emailId = UUID.fromString(emailIdStr);
                ProviderStatus providerStatus = mapEvent(event.getEvent());

                if (providerStatus != null) {
                    emailStatusService.updateProviderStatus(emailId, providerStatus);
                    System.out.println("Successfully updated DB for Email ID: " + emailId);
                }
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid UUID format received: " + emailIdStr);
            } catch (Exception e) {
                System.err.println("Error updating status: " + e.getMessage());
            }
        }
    }

    private ProviderStatus mapEvent(String event) {
        if (event == null) return null;

        return switch (event.toLowerCase()) {
            case "delivered" -> ProviderStatus.DELIVERED;
            case "bounce"    -> ProviderStatus.BOUNCED;
            case "dropped"   -> ProviderStatus.DROPPED;
            case "deferred"  -> ProviderStatus.DEFERRED;
            case "open"      -> ProviderStatus.OPENED;
            case "processed" -> ProviderStatus.PROCESSED;
            case "click"     -> ProviderStatus.CLICKED;
            default -> null;
        };
    }
}