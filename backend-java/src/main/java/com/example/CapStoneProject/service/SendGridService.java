package com.example.CapStoneProject.service;

import com.example.CapStoneProject.models.EmailMessage;

import com.sendgrid.SendGrid;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.Method;

import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Personalization;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SendGridService {

    private final SendGrid sendGrid;
    private final String fromEmail;

    public SendGridService(@Value("${sendgrid.api-key}") String apiKey,
                           @Value("${sendgrid.from-email}") String fromEmail) {
        this.sendGrid = new SendGrid(apiKey);
        this.fromEmail = fromEmail;
    }

    public void sendEmail(EmailMessage email) throws Exception {

        Email from = new Email(fromEmail);
        Email to   = new Email(email.getRecipient());

        Content content = new Content("text/html", email.getBody());

        Mail mail = new Mail();
        mail.setFrom(from);
        mail.setSubject(email.getSubject());
        mail.addContent(content);

        Personalization personalization = new Personalization();
        personalization.addTo(to);
        mail.addPersonalization(personalization);

        /* CRITICAL FIX — MAIL LEVEL */
        mail.addCustomArg("emailId", email.getId().toString());

        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        Response response = sendGrid.api(request);

        if (response.getStatusCode() >= 400) {
            throw new RuntimeException("SendGrid failure: " + response.getBody());
        }
    }
}