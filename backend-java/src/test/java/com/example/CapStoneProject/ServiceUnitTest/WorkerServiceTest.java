package com.example.CapStoneProject.ServiceUnitTest;

import com.example.CapStoneProject.models.EmailMessage;
import com.example.CapStoneProject.models.EmailStatus;
import com.example.CapStoneProject.repository.EmailRepository;
import com.example.CapStoneProject.repository.EmailStatusRepository;

import com.example.CapStoneProject.service.SendGridService;
import com.example.CapStoneProject.service.WorkerService;
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
class WorkerServiceTest {

    @Mock
    EmailRepository emailRepository;

    @Mock
    EmailStatusRepository emailStatusRepository;

    @Mock
    SendGridService sendGridService;

    @InjectMocks
    WorkerService workerService;

    // ============================
    // Success Path
    // ============================
    @Test
    void process_shouldMarkSent_whenSendGridSucceeds() throws Exception {

        UUID emailId = UUID.randomUUID();

        EmailMessage email = mock(EmailMessage.class);
        EmailStatus status = mock(EmailStatus.class);

        when(emailRepository.findById(emailId))
                .thenReturn(Optional.of(email));

        when(emailStatusRepository.findByEmail_Id(emailId))
                .thenReturn(Optional.of(status));

        workerService.process(emailId);

        verify(sendGridService).sendEmail(email);
        verify(status).markSentToProvider();
        verify(emailStatusRepository).save(status);
    }

    // ============================
    // Failure Path
    // ============================
    @Test
    void process_shouldMarkFailed_whenSendGridThrows() throws Exception {

        UUID emailId = UUID.randomUUID();

        EmailMessage email = mock(EmailMessage.class);
        EmailStatus status = mock(EmailStatus.class);

        when(emailRepository.findById(emailId))
                .thenReturn(Optional.of(email));

        when(emailStatusRepository.findByEmail_Id(emailId))
                .thenReturn(Optional.of(status));

        doThrow(new RuntimeException("SendGrid Error"))
                .when(sendGridService).sendEmail(email);

        workerService.process(emailId);

        verify(status).markFailed("SendGrid Error");
        verify(emailStatusRepository).save(status);
    }

    // ============================
    // Email Missing Branch
    // ============================
    @Test
    void process_shouldThrow_whenEmailMissing() throws Exception {

        UUID emailId = UUID.randomUUID();

        when(emailRepository.findById(emailId))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> workerService.process(emailId));

        assertEquals("Email not found", ex.getMessage());

        verify(sendGridService, never()).sendEmail(any());
    }
}