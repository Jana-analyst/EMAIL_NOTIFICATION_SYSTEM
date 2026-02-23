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

    private void injectMockSendGrid(SendGridService service, SendGrid mock) throws Exception {
        Field field = SendGridService.class.getDeclaredField("sendGrid");
        field.setAccessible(true);
        field.set(service, mock);
    }

    // ✅ SUCCESS PATH
    @Test
    void sendEmail_shouldSucceed_whenSendGridReturns2xx() throws Exception {

        SendGridService service = new SendGridService("dummy", "from@mail.com");

        SendGrid mockSendGrid = mock(SendGrid.class);
        Response mockResponse = mock(Response.class);

        when(mockResponse.getStatusCode()).thenReturn(202);
        when(mockSendGrid.api(any(Request.class))).thenReturn(mockResponse);

        injectMockSendGrid(service, mockSendGrid);

        EmailMessage email = mock(EmailMessage.class);
        when(email.getRecipient()).thenReturn("to@mail.com");
        when(email.getBody()).thenReturn("<b>Hello</b>");
        when(email.getSubject()).thenReturn("Test");
        when(email.getId()).thenReturn(UUID.randomUUID());

        assertDoesNotThrow(() -> service.sendEmail(email));

        verify(mockSendGrid).api(any(Request.class));
    }

    // ✅ FAILURE PATH (Coverage Booster)
    @Test
    void sendEmail_shouldThrow_whenSendGridReturnsError() throws Exception {

        SendGridService service = new SendGridService("dummy", "from@mail.com");

        SendGrid mockSendGrid = mock(SendGrid.class);
        Response mockResponse = mock(Response.class);

        when(mockResponse.getStatusCode()).thenReturn(500);
        when(mockResponse.getBody()).thenReturn("Internal Error");
        when(mockSendGrid.api(any(Request.class))).thenReturn(mockResponse);

        injectMockSendGrid(service, mockSendGrid);

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