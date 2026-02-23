package com.example.CapStoneProject.ControllerUnitTest;

import com.example.CapStoneProject.controller.SendGridWebhookController;
import com.example.CapStoneProject.dto.request.SendGridRequest;
import com.example.CapStoneProject.enums.ProviderStatus;
import com.example.CapStoneProject.service.EmailStatusService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SendGridWebhookControllerTest {

    @Mock
    EmailStatusService emailStatusService;

    @InjectMocks
    SendGridWebhookController controller;

    // ✅ VALID EVENT → DB UPDATE
    @Test
    void handleEvents_shouldUpdateStatus_whenValidPayload() {

        UUID emailId = UUID.randomUUID();

        SendGridRequest event = mock(SendGridRequest.class);

        when(event.getEvent()).thenReturn("delivered");
        when(event.getCustomArgs()).thenReturn(Map.of("emailId", emailId.toString()));

        controller.handleEvents(List.of(event));

        verify(emailStatusService).updateProviderStatus(emailId, ProviderStatus.DELIVERED);
    }

    // ✅ MISSING emailId → SKIP
    @Test
    void handleEvents_shouldSkip_whenEmailIdMissing() {

        SendGridRequest event = mock(SendGridRequest.class);

        when(event.getEvent()).thenReturn("delivered");
        when(event.getCustomArgs()).thenReturn(new HashMap<>());

        controller.handleEvents(List.of(event));

        verify(emailStatusService, never()).updateProviderStatus(any(), any());
    }

    // ✅ INVALID UUID → NO CRASH
    @Test
    void handleEvents_shouldIgnore_whenUuidInvalid() {

        SendGridRequest event = mock(SendGridRequest.class);

        when(event.getEvent()).thenReturn("delivered");
        when(event.getCustomArgs()).thenReturn(Map.of("emailId", "INVALID_UUID"));

        controller.handleEvents(List.of(event));

        verify(emailStatusService, never()).updateProviderStatus(any(), any());
    }

    // ✅ UNKNOWN EVENT → mapEvent → null → SKIP
    @Test
    void handleEvents_shouldSkip_whenEventUnknown() {

        UUID emailId = UUID.randomUUID();

        SendGridRequest event = mock(SendGridRequest.class);

        when(event.getEvent()).thenReturn("some-random-event");
        when(event.getCustomArgs()).thenReturn(Map.of("emailId", emailId.toString()));

        controller.handleEvents(List.of(event));

        verify(emailStatusService, never()).updateProviderStatus(any(), any());
    }

    // ✅ NULL EVENT STRING → SKIP
    @Test
    void handleEvents_shouldSkip_whenEventNull() {

        UUID emailId = UUID.randomUUID();

        SendGridRequest event = mock(SendGridRequest.class);

        when(event.getEvent()).thenReturn(null);
        when(event.getCustomArgs()).thenReturn(Map.of("emailId", emailId.toString()));

        controller.handleEvents(List.of(event));

        verify(emailStatusService, never()).updateProviderStatus(any(), any());
    }

    // ✅ SERVICE THROWS EXCEPTION → HANDLED
    @Test
    void handleEvents_shouldHandleServiceException() {

        UUID emailId = UUID.randomUUID();

        SendGridRequest event = mock(SendGridRequest.class);

        when(event.getEvent()).thenReturn("delivered");
        when(event.getCustomArgs()).thenReturn(Map.of("emailId", emailId.toString()));

        doThrow(new RuntimeException("DB Error"))
                .when(emailStatusService)
                .updateProviderStatus(any(), any());

        controller.handleEvents(List.of(event));

        verify(emailStatusService).updateProviderStatus(emailId, ProviderStatus.DELIVERED);
    }
}