package com.example.CapStoneProject.ServiceUnitTest;

import com.example.CapStoneProject.models.EmailMessage;

import com.example.CapStoneProject.service.SendGridService;
import com.sendgrid.SendGrid;
import com.sendgrid.Request;
import com.sendgrid.Response;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SendGridServiceTest {

    // Utility method to inject mock SendGrid
    private void injectMock(Object target, String fieldName, Object mock) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, mock);
    }

    // ============================
    // Success Path
    // ============================
    @Test
    void sendEmail_shouldSucceed_whenSendGridReturns2xx() throws Exception {

        SendGridService service =
                new SendGridService("dummy-key", "from@mail.com");

        SendGrid mockSendGrid = mock(SendGrid.class);
        Response mockResponse = mock(Response.class);

        when(mockResponse.getStatusCode()).thenReturn(202);

        when(mockSendGrid.api(any(Request.class)))
                .thenReturn(mockResponse);

        injectMock(service, "sendGrid", mockSendGrid);

        EmailMessage email = mock(EmailMessage.class);
        when(email.getRecipient()).thenReturn("to@mail.com");
        when(email.getBody()).thenReturn("<b>Hello</b>");
        when(email.getSubject()).thenReturn("Test");
        when(email.getId()).thenReturn(UUID.randomUUID());

        assertDoesNotThrow(() -> service.sendEmail(email));

        verify(mockSendGrid).api(any(Request.class));
    }

    // ============================
    // Failure Path
    // ============================
    @Test
    void sendEmail_shouldThrow_whenSendGridReturnsError() throws Exception {

        SendGridService service =
                new SendGridService("dummy-key", "from@mail.com");

        SendGrid mockSendGrid = mock(SendGrid.class);
        Response mockResponse = mock(Response.class);

        when(mockResponse.getStatusCode()).thenReturn(500);
        when(mockResponse.getBody()).thenReturn("Internal Error");

        when(mockSendGrid.api(any(Request.class)))
                .thenReturn(mockResponse);

        injectMock(service, "sendGrid", mockSendGrid);

        EmailMessage email = mock(EmailMessage.class);
        when(email.getRecipient()).thenReturn("to@mail.com");
        when(email.getBody()).thenReturn("<b>Hello</b>");
        when(email.getSubject()).thenReturn("Test");
        when(email.getId()).thenReturn(UUID.randomUUID());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.sendEmail(email));

        assertTrue(ex.getMessage().contains("SendGrid failure"));

        verify(mockSendGrid).api(any(Request.class));
    }
}