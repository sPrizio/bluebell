package com.bluebell.radicle.performable.impl;

import com.bluebell.AbstractGenericTest;
import com.bluebell.platform.models.core.nonentities.action.ActionData;
import com.bluebell.radicle.services.news.MarketNewsService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testing class for {@link FetchMarketNewsActionPerformable}
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class FetchMarketNewsActionPerformableTest extends AbstractGenericTest {

    @MockitoBean
    private MarketNewsService marketNewsService;

    @Autowired
    private FetchMarketNewsActionPerformable fetchMarketNewsActionPerformable;


    //  ----------------- perform -----------------

    @Test
    void test_perform_failed() {
        Mockito.when(this.marketNewsService.fetchMarketNews()).thenReturn(false);
        assertThat(this.fetchMarketNewsActionPerformable.perform())
                .extracting("success", "logs")
                .containsExactly(false, "Market News could not be fetched. Please refer to the logs for additional information");
    }

    @Test
    void test_perform_success() {
        Mockito.when(this.marketNewsService.fetchMarketNews()).thenReturn(true);
        assertThat(this.fetchMarketNewsActionPerformable.perform())
                .extracting("success", "data", "logs")
                .containsExactly(true, true, "Market News fetched successfully.");
    }

    @Test
    void test_perform_exception() {
        Mockito.when(this.marketNewsService.fetchMarketNews()).thenThrow(new UnsupportedOperationException("Something went wrong"));

        final ActionData actionData = this.fetchMarketNewsActionPerformable.perform();
        assertThat(actionData.success()).isFalse();
        assertThat(actionData.logs()).contains("Something went wrong");
    }
}
