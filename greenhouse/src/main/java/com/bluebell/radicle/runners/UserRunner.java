package com.bluebell.radicle.runners;

import com.bluebell.platform.enums.security.UserRole;
import com.bluebell.platform.models.core.entities.security.User;
import com.bluebell.radicle.repositories.security.UserRepository;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * First data runner in the order, creates {@link User}s
 *
 * @author Stephen Prizio
 * @version 0.2.6
 */
@Component
@Profile("dev")
@ConditionalOnProperty(name = "bluebell.cmdlr.user.data", havingValue = "true", matchIfMissing = true)
public class UserRunner extends AbstractRunner implements CommandLineRunner, Ordered {

    @Value("${bluebell.cmdlr.order.user}")
    private int order;

    @Resource(name = "userRepository")
    private UserRepository userRepository;


    //  METHODS

    @Override
    @Transactional
    public void run(String... args) {

        logStart();

        User user = User
                .builder()
                .apiToken("Zmxvd2VycG90X2FwaV90b2tlbiZzLnByaXppb0Bob3RtYWlsLmNvbSYyMDI0LTExLTIwVDEzOjU2OjE1")
                .dateRegistered(LocalDateTime.of(2024, 11, 20, 13, 56, 15))
                .email("test.user@bluebell.com")
                .firstName("Test")
                .lastName("Test")
                .username("t.test")
                .roles(List.of(UserRole.TRADER, UserRole.ADMINISTRATOR, UserRole.SYSTEM))
                .build();

        user.setPassword("admin");

        this.userRepository.save(user);

        logEnd();
    }

    @Override
    public int getOrder() {
        return this.order;
    }
}
