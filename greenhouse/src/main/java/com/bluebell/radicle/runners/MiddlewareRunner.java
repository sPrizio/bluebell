package com.bluebell.radicle.runners;

import com.bluebell.platform.enums.middleware.Middleware;
import com.bluebell.platform.models.core.entities.system.IncomingPing;
import com.bluebell.radicle.repositories.system.IncomingPingRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Generates mocked incoming ping responses from the middleware systems
 *
 * @author Stephen Prizio
 * @version 0.1.9
 */
@Slf4j
@Component
@Order(10)
@Profile("dev")
@ConditionalOnProperty(name = "bluebell.cmdlr.infra.data", havingValue = "true", matchIfMissing = true)
public class MiddlewareRunner extends AbstractRunner implements CommandLineRunner {

    @Resource(name = "incomingPingRepository")
    private IncomingPingRepository incomingPingRepository;


    //  METHODS

    @Override
    public void run(final String... args) throws Exception {

        logStart();

        this.incomingPingRepository.save(
                IncomingPing
                        .builder()
                        .systemName(Middleware.MT4_TRADE_DATA_MIDDLEWARE.getCode())
                        .lastSignalReceived(LocalDateTime.now().minusMinutes(10))
                        .build()
        );

        this.incomingPingRepository.save(
                IncomingPing
                        .builder()
                        .systemName(Middleware.MT4_MARKET_DATA_MIDDLEWARE.getCode())
                        .lastSignalReceived(LocalDateTime.now().minusMinutes(8))
                        .build()
        );

        logEnd();
    }
}
