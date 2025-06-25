package com.bluebell.platform.configuration;

import com.bluebell.platform.util.DirectoryUtil;
import io.github.cdimascio.dotenv.Dotenv;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * Configuration for bluebell
 *
 * @author Stephen Prizio
 * @version 1.0.0
 */
@Configuration
public class SystemConfiguration {

    @Bean
    public Dotenv dotenv() {
        return Dotenv
                .configure()
                .directory(DirectoryUtil.getBaseProjectDirectory())
                .ignoreIfMissing()
                .load();
    }

    @Bean
    public JavaMailSender getJavaMailSender(final Dotenv dotenv) {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        final String username = getEnvVar("EMAIL_APP_USERNAME", dotenv);
        final String password = getEnvVar("EMAIL_APP_PASSWORD", dotenv);

        if (StringUtils.isEmpty(username)) {
            throw new IllegalStateException("EMAIL_APP_USERNAME is required");
        }

        if (StringUtils.isEmpty(password)) {
            throw new IllegalStateException("EMAIL_APP_PASSWORD is required");
        }

        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }


    //  HELPERS

    /**
     * Checks system env first, then falls back to dotenv
     */
    private String getEnvVar(String key, Dotenv dotenv) {
        String val = System.getenv(key);
        if (StringUtils.isNotEmpty(val)) {
            return val;
        }

        return dotenv.get(key);
    }
}
