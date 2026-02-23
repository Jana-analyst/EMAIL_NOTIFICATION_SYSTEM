package com.example.CapStoneProject.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "email")
@Getter
@NoArgsConstructor
public class EmailMessage {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String recipient;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

    @Column(nullable = false)
    private UUID templateId;


    @Column(nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = OffsetDateTime.now();
    }

    public EmailMessage(String recipient, String subject, String body, UUID templateId) {
        this.recipient = recipient;
        this.subject = subject;
        this.body = body;
        this.templateId = templateId;
    }
}
