package com.bluebell.radicle.runners;

import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.models.core.nonentities.email.EmailTemplate;
import com.bluebell.radicle.services.email.impl.SimpleEmailService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Testing
 *
 * @author Stephen Prizio
 * @version 0.1.6
 */
@Slf4j
@Component
@Order(9)
@Profile("dev")
public class EmailRunner extends AbstractRunner implements CommandLineRunner {

    @Value("${bluebell.notify}")
    private String shouldNotify;

    @Value("${bluebell.email.system.recipient}")
    private String recipient;

    @Resource(name = "simpleEmailService")
    private SimpleEmailService simpleEmailService;


    //  METHODS

    @Override
    public void run(final String... args) throws Exception {

        logStart();

        if (Boolean.parseBoolean(this.shouldNotify)) {
            this.simpleEmailService.sendEmail(
                    this.recipient,
                    "bluebell Dev Instance Startup",
                    EmailTemplate
                            .builder()
                            .template(CorePlatformConstants.EmailTemplates.SIMPLE_TEMPLATE)
                            .queryParams(Map.of("body", String.format("A bluebell development instance has successfully started at %s", LocalDateTime.now().format(DateTimeFormatter.ofPattern(CorePlatformConstants.DATE_TIME_FORMAT))))).build()
            );
        } else {
            LOGGER.warn("Notifications have been turned off for bluebell");
        }

        logEnd();
    }
}
