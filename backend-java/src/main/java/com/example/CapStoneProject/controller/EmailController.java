package com.example.CapStoneProject.controller;

import com.example.CapStoneProject.dto.request.CreateEmailRequest;
import com.example.CapStoneProject.dto.request.TestEmailRequest;
import com.example.CapStoneProject.dto.response.EmailDetailsResponse;
import com.example.CapStoneProject.dto.response.EmailListItemResponse;
import com.example.CapStoneProject.dto.response.TestEmailResponse;
import com.example.CapStoneProject.service.EmailService;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/emails")
@CrossOrigin(origins = "*")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping
    public EmailListItemResponse sendEmail(
            @RequestBody CreateEmailRequest request) {

        return emailService.sendEmail(request);
    }

    @PostMapping("/test")
    public TestEmailResponse sendTestEmail(
            @RequestBody TestEmailRequest request) {

        return emailService.sendTestEmail(request);
    }

    @GetMapping
    public List<EmailListItemResponse> listEmails() {
        return emailService.listEmails();
    }

    @GetMapping("/{emailId}")
    public EmailDetailsResponse getEmailDetails(@PathVariable UUID emailId) {
        return emailService.getEmailDetails(emailId);
    }
}