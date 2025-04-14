package com.bluebell.radicle.services.email.impl;

import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.models.core.nonentities.email.EmailTemplate;
import com.bluebell.radicle.exceptions.validation.IllegalParameterException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

/**
 * Testing class for {@link SimpleEmailService}
 *
 * @author Stephen Prizio
 * @version 0.1.6
 */
@SpringBootTest(properties = "bluebell.notify=true")
@RunWith(SpringRunner.class)
class SimpleEmailServiceTest {

    @MockitoBean
    private JavaMailSender mailSender;

    @Autowired
    private SimpleEmailService simpleEmailService;


    //  ----------------- interpolate -----------------

    @Test
    void test_interpolate_missingData() {

        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.simpleEmailService.sendEmail(null, "Subject", EmailTemplate.builder().build()))
                .withMessageContaining(CorePlatformConstants.Validation.Email.TO_CANNOT_BE_NULL);

        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.simpleEmailService.sendEmail("test@email.com", null, EmailTemplate.builder().build()))
                .withMessageContaining(CorePlatformConstants.Validation.Email.SUBJECT_CANNOT_BE_NULL);

        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.simpleEmailService.sendEmail("test@email.com", "Subject", null))
                .withMessageContaining(CorePlatformConstants.Validation.Email.EMAIL_TEMPLATE_CANNOT_BE_NULL);

        final String input1 = "Hello ${name}, welcome to ${place}!";
        final Map<String, String> values1 = Map.of(
                "name", "Alice"
        );

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(this.mailSender.createMimeMessage()).thenReturn(mimeMessage);

        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.simpleEmailService.sendEmail("test@email.com", "Subject", EmailTemplate.builder().template(input1).queryParams(values1).build()))
                .withMessage("Missing replacements for placeholders: [${place}]");

        final String input2 = "Hello ${name}, welcome to my ${verb} ${place}!";
        final Map<String, String> values2 = Map.of("name", "Alice");

        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.simpleEmailService.sendEmail("test@email.com", "Subject", EmailTemplate.builder().template(input2).queryParams(values2).build()))
                .withMessage("Missing replacements for placeholders: [${verb}, ${place}]");

    }


    //  ----------------- sendEmail -----------------

    @Test
    void test_sendEmail_success() throws Exception {
        String to = "test@example.com";
        String subject = "Test Subject";
        EmailTemplate emailTemplate = EmailTemplate.builder().template("<html><body>Hello ${name}, welcome to ${place}!</body></html>").queryParams(Map.of("name", "Stephen", "place", "Canada")).build();

        MimeMessage mimeMessage = new MimeMessage((Session) null);
        when(this.mailSender.createMimeMessage()).thenReturn(mimeMessage);

        this.simpleEmailService.sendEmail(to, subject, emailTemplate);

        ArgumentCaptor<MimeMessage> captor = ArgumentCaptor.forClass(MimeMessage.class);
        verify(this.mailSender).send(captor.capture());

        MimeMessage sentMessage = captor.getValue();

        assertThat(sentMessage.getAllRecipients()).extracting(Objects::toString).containsExactly(to);
        assertThat(sentMessage.getSubject()).isEqualTo(subject);
        assertThat(sentMessage.getContent().toString()).contains("Hello Stephen, welcome to Canada!");
    }
}
