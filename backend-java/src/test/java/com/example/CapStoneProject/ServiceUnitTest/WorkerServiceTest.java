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
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkerServiceTest {

    @Mock EmailRepository emailRepository;
    @Mock EmailStatusRepository emailStatusRepository;
    @Mock SendGridService sendGridService;

    @InjectMocks WorkerService workerService;

    // ✅ SUCCESS PATH
    @Test
    void process_shouldMarkSent_whenSendGridSucceeds() throws Exception {

        UUID emailId = UUID.randomUUID();

        EmailMessage email = mock(EmailMessage.class);
        EmailStatus status = mock(EmailStatus.class);

        when(emailRepository.findById(emailId))
                .thenReturn(Optional.of(email));

        when(emailStatusRepository.findByEmail_Id(emailId))
                .thenReturn(Optional.of(status));

        doNothing().when(sendGridService).sendEmail(email);

        workerService.process(emailId);

        verify(status).markSentToProvider();
        verify(emailStatusRepository).save(status);
    }

    // ✅ FAILURE PATH
    @Test
    void process_shouldMarkFailed_whenSendGridThrows() throws Exception {

        UUID emailId = UUID.randomUUID();

        EmailMessage email = mock(EmailMessage.class);
        EmailStatus status = mock(EmailStatus.class);

        when(emailRepository.findById(emailId))
                .thenReturn(Optional.of(email));

        when(emailStatusRepository.findByEmail_Id(emailId))
                .thenReturn(Optional.of(status));

        doThrow(new Exception("SendGrid Error"))
                .when(sendGridService).sendEmail(email);

        workerService.process(emailId);

        verify(status).markFailed("SendGrid Error");
        verify(emailStatusRepository).save(status);
    }

    // ✅ EMAIL MISSING
    @Test
    void process_shouldThrow_whenEmailMissing() {

        UUID emailId = UUID.randomUUID();

        when(emailRepository.findById(emailId))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> workerService.process(emailId));
    }

    // ✅ STATUS MISSING
    @Test
    void process_shouldThrow_whenStatusMissing() {

        UUID emailId = UUID.randomUUID();

        EmailMessage email = mock(EmailMessage.class);

        when(emailRepository.findById(emailId))
                .thenReturn(Optional.of(email));

        when(emailStatusRepository.findByEmail_Id(emailId))
                .thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> workerService.process(emailId));
    }
}