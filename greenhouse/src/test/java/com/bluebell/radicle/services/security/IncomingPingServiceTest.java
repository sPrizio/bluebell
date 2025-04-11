package com.bluebell.radicle.services.security;

import com.bluebell.AbstractGenericTest;
import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.radicle.exceptions.validation.IllegalParameterException;
import com.bluebell.radicle.repositories.system.IncomingPingRepository;
import com.bluebell.radicle.services.system.IncomingPingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Testing class for {@link IncomingPingService}
 *
 * @author Stephen Prizio
 * @version 0.1.6
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class IncomingPingServiceTest extends AbstractGenericTest {

    @Autowired
    private IncomingPingRepository incomingPingRepository;

    @Autowired
    private IncomingPingService incomingPingService;

    @BeforeEach
    void setUp() {
        this.incomingPingRepository.deleteAll();
    }


    //  ----------------- findBySystemName -----------------

    @Test
    void test_findBySystemName_missingParam() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.incomingPingService.findBySystemName(null))
                .withMessage(CorePlatformConstants.Validation.System.IncomingPing.SYSTEM_NAME_CANNOT_BE_NULL);
    }

    @Test
    void test_findBySystemName_success() {

        assertThat(this.incomingPingRepository.count()).isEqualTo(0);
        this.incomingPingRepository.save(generateTestIncomingPing());

        assertThat(this.incomingPingService.findBySystemName("test1")).isEmpty();
        assertThat(this.incomingPingRepository.count()).isEqualTo(1);

        assertThat(this.incomingPingService.findBySystemName("Test System Name")).isNotEmpty();
    }


    //  ----------------- acknowledgeIncomingPing -----------------

    @Test
    void test_acknowledgeIncomingPing_missingParam() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.incomingPingService.acknowledgeIncomingPing(null))
                .withMessage(CorePlatformConstants.Validation.System.IncomingPing.SYSTEM_NAME_CANNOT_BE_NULL);
    }

    @Test
    void test_acknowledgeIncomingPing_success() {

        assertThat(this.incomingPingRepository.count()).isEqualTo(0);
        assertThat(this.incomingPingService.acknowledgeIncomingPing("Test System Name")).isTrue();
        assertThat(this.incomingPingService.acknowledgeIncomingPing("Test System Name")).isTrue();
        assertThat(this.incomingPingService.acknowledgeIncomingPing("Test System Name")).isTrue();
        assertThat(this.incomingPingService.acknowledgeIncomingPing("Test System Name")).isTrue();
        assertThat(this.incomingPingService.acknowledgeIncomingPing("Test System Name")).isTrue();
        assertThat(this.incomingPingRepository.count()).isEqualTo(1);

        assertThat(this.incomingPingService.acknowledgeIncomingPing("test2")).isTrue();
        assertThat(this.incomingPingRepository.count()).isEqualTo(2);
    }
}
