package com.bluebell.radicle.integration.client.firstratedata;

import com.bluebell.platform.util.DirectoryUtil;
import com.bluebell.radicle.exceptions.validation.IllegalParameterException;
import com.bluebell.radicle.integration.exceptions.IntegrationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;

import java.io.File;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.doReturn;

/**
 * Testing class {@link FirstRateDataIntegrationClient}
 *
 * @author Stephen Prizio
 * @version 0.1.5
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class FirstRateDataIntegrationClientTest {

    @MockitoSpyBean
    private FirstRateDataIntegrationClient firstRateDataIntegrationClient;

    @BeforeEach
    void setUp() {
        doReturn(null).when(this.firstRateDataIntegrationClient).downloadFile("www.bad.com", CollectionUtils.toMultiValueMap(Map.of("fileUrlId", List.of("value1"))), "bad");
        doReturn(new File(DirectoryUtil.getTestingResourcesDirectory())).when(this.firstRateDataIntegrationClient).downloadFile("www.test.com", CollectionUtils.toMultiValueMap(Map.of("fileUrlId", List.of("value1"))), "good");
    }


    //  ----------------- downloadDataFiles -----------------

    @Test
    void test_downloadDataFiles_badData() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.firstRateDataIntegrationClient.downloadDataFiles(null, new LinkedMultiValueMap<>(), ""))
                .withMessageContaining("url cannot be null");
        assertThatExceptionOfType(IntegrationException.class)
                .isThrownBy(() -> this.firstRateDataIntegrationClient.downloadDataFiles("www.test.com", new LinkedMultiValueMap<>(), ""))
                .withMessageContaining("cannot be null");
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.firstRateDataIntegrationClient.downloadDataFiles("www.test.com", CollectionUtils.toMultiValueMap(Map.of("fileUrlId", List.of("value1"))), null))
                .withMessageContaining("target file name cannot be null");
        assertThatExceptionOfType(IntegrationException.class)
                .isThrownBy(() -> this.firstRateDataIntegrationClient.downloadDataFiles("www.bad.com", CollectionUtils.toMultiValueMap(Map.of("fileUrlId", List.of("value1"))), "bad"))
                .withMessageContaining("No file was received");
    }

    @Test
    void test_downloadDataFiles_success() {
        final File file = this.firstRateDataIntegrationClient.downloadDataFiles("www.test.com", CollectionUtils.toMultiValueMap(Map.of("fileUrlId", List.of("value1"))), "good");
        assertThat(file).exists();
    }


    //  ----------------- get -----------------

    @Test
    void test_get_success() {
        assertThat(this.firstRateDataIntegrationClient.get("https://www.forexfactory.com/", new LinkedMultiValueMap<>()))
                .isNotEmpty();
    }
}
