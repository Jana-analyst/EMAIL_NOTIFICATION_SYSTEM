package com.example.CapStoneProject.ServiceUnitTest;

import com.example.CapStoneProject.models.EmailMessage;
import com.example.CapStoneProject.models.EmailStatus;
import com.example.CapStoneProject.enums.ProviderStatus;
import com.example.CapStoneProject.repository.EmailStatusRepository;

import com.example.CapStoneProject.service.EmailStatusService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailStatusServiceTest {

    @Mock
    EmailStatusRepository emailStatusRepository;

    @InjectMocks
    EmailStatusService emailStatusService;

    // ============================
    // createQueuedStatus → Success
    // ============================
    @Test
    void createQueuedStatus_shouldSaveStatus() {

        EmailMessage email = mock(EmailMessage.class);
        EmailStatus savedStatus = mock(EmailStatus.class);

        when(emailStatusRepository.save(any())).thenReturn(savedStatus);

        EmailStatus result = emailStatusService.createQueuedStatus(email);

        assertNotNull(result);
        verify(emailStatusRepository).save(any(EmailStatus.class));
    }

    // ============================
    // markSentToProvider → Success
    // ============================
    @Test
    void markSentToProvider_shouldUpdateAndSave() {

        UUID emailId = UUID.randomUUID();

        EmailStatus status = mock(EmailStatus.class);

        when(emailStatusRepository.findByEmail_Id(emailId))
                .thenReturn(Optional.of(status));

        emailStatusService.markSentToProvider(emailId);

        verify(status).markSentToProvider();
        verify(emailStatusRepository).save(status);
    }

    // ============================
    // markFailed → Success
    // ============================
    @Test
    void markFailed_shouldUpdateAndSave() {

        UUID emailId = UUID.randomUUID();
        EmailStatus status = mock(EmailStatus.class);

        when(emailStatusRepository.findByEmail_Id(emailId))
                .thenReturn(Optional.of(status));

        emailStatusService.markFailed(emailId, "ERROR");

        verify(status).markFailed("ERROR");
        verify(emailStatusRepository).save(status);
    }

    // ============================
    // updateProviderStatus → Success
    // ============================
    @Test
    void updateProviderStatus_shouldUpdateAndSave() {

        UUID emailId = UUID.randomUUID();
        EmailStatus status = mock(EmailStatus.class);

        when(emailStatusRepository.findByEmail_Id(emailId))
                .thenReturn(Optional.of(status));

        emailStatusService.updateProviderStatus(emailId, ProviderStatus.DELIVERED);

        verify(status).updateProviderStatus(ProviderStatus.DELIVERED);
        verify(emailStatusRepository).save(status);
    }

    // ============================
    // loadStatus → Exception Branch
    // ============================
    @Test
    void markSentToProvider_shouldThrowWhenMissing() {

        UUID emailId = UUID.randomUUID();

        when(emailStatusRepository.findByEmail_Id(emailId))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> emailStatusService.markSentToProvider(emailId));

        assertEquals("EmailStatus not found", ex.getMessage());
    }
}