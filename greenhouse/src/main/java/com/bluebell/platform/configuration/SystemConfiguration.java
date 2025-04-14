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
 * @version 0.1.6
 */
@Configuration
public class SystemConfiguration {

    @Bean
    public Dotenv dotenv() {
        return Dotenv.configure().directory(DirectoryUtil.getBaseProjectDirectory()).load();
    }

    @Bean
    public JavaMailSender getJavaMailSender() {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        final String username = dotenv().get("EMAIL_APP_USERNAME");
        final String password = dotenv().get("EMAIL_APP_PASSWORD");

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
}
