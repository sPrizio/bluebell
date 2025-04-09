package com.bluebell.radicle.services.email.impl;

import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.models.core.entities.job.impl.Job;
import com.bluebell.platform.models.core.nonentities.email.EmailTemplate;
import com.bluebell.radicle.services.email.EmailService;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import static com.bluebell.radicle.validation.GenericValidator.validateParameterIsNotNull;

/**
 * Implementation of the {@link EmailService} for sending emails about {@link Job}s
 *
 * @author Stephen Prizio
 * @version 0.1.6
 */
@Slf4j
@Service
public class SimpleEmailService implements EmailService {

    @Autowired
    private Dotenv dotenv;

    @Autowired
    private JavaMailSender mailSender;


    //  METHODS

    @Override
    public void sendEmail(final String to, final String subject, final EmailTemplate emailTemplate) {

        validateParameterIsNotNull(to, CorePlatformConstants.Validation.Email.TO_CANNOT_BE_NULL);
        validateParameterIsNotNull(subject, CorePlatformConstants.Validation.Email.SUBJECT_CANNOT_BE_NULL);
        validateParameterIsNotNull(emailTemplate, CorePlatformConstants.Validation.Email.EMAIL_TEMPLATE_CANNOT_BE_NULL);

        final String sender = this.dotenv.get("EMAIL_APP_SENDER");
        if (StringUtils.isEmpty(sender)) {
            throw new IllegalStateException("EMAIL_APP_SENDER is not set");
        }

        final String body = interpolate(emailTemplate);
        try {
            MimeMessage message = this.mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

            helper.setFrom(sender);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            this.mailSender.send(message);
        } catch (Exception e) {
            LOGGER.error("Could not send email due to {}", e.getMessage(), e);
        }
    }
}
