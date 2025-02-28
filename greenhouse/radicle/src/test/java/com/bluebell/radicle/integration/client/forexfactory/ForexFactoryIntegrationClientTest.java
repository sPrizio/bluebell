package com.bluebell.radicle.integration.client.forexfactory;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testing class for {@link ForexFactoryIntegrationClient}
 *
 * @author Stephen Prizio
 * @version 0.1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ForexFactoryIntegrationClientTest {

    @Resource(name = "forexFactoryIntegrationClient")
    private ForexFactoryIntegrationClient forexFactoryIntegrationClient;


    //  ----------------- get -----------------

    @Test
    public void test_get_success() {
        assertThat(this.forexFactoryIntegrationClient.get("https://www.forexfactory.com/", new LinkedMultiValueMap<>()))
                .isNotEmpty();
    }
}
