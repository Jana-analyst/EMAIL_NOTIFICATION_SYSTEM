package com.example.CapStoneProject.models;

import com.example.CapStoneProject.enums.ProviderStatus;
import com.example.CapStoneProject.enums.SystemStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "email_status")
@Getter
@NoArgsConstructor
public class EmailStatus {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "email_id", nullable = false, unique = true)
    private EmailMessage email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SystemStatus systemStatus;

    @Enumerated(EnumType.STRING)
    private ProviderStatus providerStatus;

    @Column(nullable = false)
    private OffsetDateTime updatedAt;

    @Column
    private String failureReason;

    public EmailStatus(EmailMessage email, SystemStatus systemStatus) {
        this.email = email;
        this.systemStatus = systemStatus;
        this.providerStatus = ProviderStatus.UNKNOWN;
    }

    @PrePersist
    @PreUpdate
    public void updateTimestamp() {
        updatedAt = OffsetDateTime.now();
    }

    public void markSentToProvider() {
        this.systemStatus = SystemStatus.SENT_TO_PROVIDER;
    }

    public void markFailed(String reason) {
        this.systemStatus = SystemStatus.FAILED;
        this.failureReason = reason;
    }

    public void updateProviderStatus(ProviderStatus providerStatus) {
        this.providerStatus = providerStatus;
    }
}