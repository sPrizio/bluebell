package com.bluebell.radicle.runners;

import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.models.core.nonentities.email.EmailTemplate;
import com.bluebell.radicle.services.email.impl.SimpleEmailService;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Generates an email to notify that the app has started in development mode
 *
 * @author Stephen Prizio
 * @version 0.2.4
 */
@Slf4j
@Component
@Profile("dev")
@ConditionalOnProperty(name = "bluebell.cmdlr.infra.data", havingValue = "true", matchIfMissing = true)
public class EmailRunner extends AbstractRunner implements CommandLineRunner, Ordered {

    @Value("${bluebell.cmdlr.order.email}")
    private int order;

    @Autowired
    private Dotenv dotenv;

    @Value("${bluebell.notify}")
    private String shouldNotify;

    @Resource(name = "simpleEmailService")
    private SimpleEmailService simpleEmailService;


    //  METHODS

    @Override
    public void run(final String... args) throws Exception {

        logStart();

        if (Boolean.parseBoolean(this.shouldNotify)) {
            final String recipient = this.dotenv.get("EMAIL_APP_RECIPIENT");
            if (StringUtils.isEmpty(recipient)) {
                throw new IllegalStateException("EMAIL_APP_RECIPIENT is not set");
            }

            this.simpleEmailService.sendEmail(
                    recipient,
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

    @Override
    public int getOrder() {
        return this.order;
    }
}
