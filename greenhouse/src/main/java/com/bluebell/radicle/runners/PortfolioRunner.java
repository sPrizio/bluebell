package com.bluebell.radicle.runners;

import com.bluebell.platform.models.core.entities.portfolio.Portfolio;
import com.bluebell.platform.models.core.entities.security.User;
import com.bluebell.radicle.repositories.portfolio.PortfolioRepository;
import com.bluebell.radicle.repositories.security.UserRepository;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Generates testing {@link Portfolio}s
 *
 * @author Stephen Prizio
 * @version 0.2.4
 */
@Component
@Profile("dev")
@ConditionalOnProperty(name = "bluebell.cmdlr.portfolio.data", havingValue = "true", matchIfMissing = true)
public class PortfolioRunner extends AbstractRunner implements CommandLineRunner, Ordered {

    @Value("${bluebell.cmdlr.order.portfolio}")
    private int order;

    @Resource(name = "portfolioRepository")
    private PortfolioRepository portfolioRepository;

    @Resource(name = "userRepository")
    private UserRepository userRepository;


    //  METHODS

    @Override
    @Transactional
    public void run(String... args) {

        logStart();

        final User user = this.userRepository.findUserByUsername("s.test");
        this.portfolioRepository.save(Portfolio
                .builder()
                .name("Test Portfolio")
                .portfolioNumber(1234L)
                .active(true)
                .defaultPortfolio(true)
                .user(user)
                .build()
        );

        logEnd();
    }

    @Override
    public int getOrder() {
        return this.order;
    }
}
