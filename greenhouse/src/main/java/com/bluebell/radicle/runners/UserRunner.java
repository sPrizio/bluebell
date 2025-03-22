package com.bluebell.radicle.runners;

import com.bluebell.platform.enums.security.UserRole;
import com.bluebell.platform.enums.system.PhoneType;
import com.bluebell.platform.models.core.entities.security.User;
import com.bluebell.platform.models.core.entities.system.PhoneNumber;
import com.bluebell.radicle.repositories.security.UserRepository;
import com.bluebell.radicle.repositories.system.PhoneNumberRepository;
import jakarta.annotation.Resource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * First data runner in the order, creates {@link User}s & {@link PhoneNumber}s
 *
 * @author Stephen Prizio
 * @version 0.1.3
 */
@Component
@Order(1)
@Profile("dev")
public class UserRunner extends AbstractRunner implements CommandLineRunner {

    @Resource(name = "phoneNumberRepository")
    private PhoneNumberRepository phoneNumberRepository;

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
                .email("s.prizio@hotmail.com")
                .firstName("Stephen")
                .lastName("Test")
                .username("s.test")
                .roles(List.of(UserRole.TRADER, UserRole.ADMINISTRATOR))
                .build();

        user.setPassword("admin");

        user = this.userRepository.save(user);

        PhoneNumber pn1 = PhoneNumber
                .builder()
                .countryCode((short) 1)
                .phoneType(PhoneType.MOBILE)
                .telephoneNumber(5144546565L)
                .user(user)
                .build();

        this.phoneNumberRepository.save(pn1);

        PhoneNumber pn2 = PhoneNumber
                .builder()
                .countryCode((short) 1)
                .phoneType(PhoneType.HOME)
                .telephoneNumber(5144777565L)
                .user(user)
                .build();

        this.phoneNumberRepository.save(pn2);

        logEnd();
    }
}
