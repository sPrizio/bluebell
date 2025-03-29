package com.bluebell.radicle.services.data;

import com.bluebell.AbstractGenericTest;
import com.bluebell.platform.models.core.entities.market.MarketPrice;
import com.bluebell.platform.util.DirectoryUtil;
import com.bluebell.radicle.enums.DataSource;
import org.apache.commons.io.FileUtils;
import org.javatuples.Triplet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Testing class for {@link MarketDataIngestionService}
 *
 * @author Stephen Prizio
 * @version 0.1.5
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class MarketDataIngestionServiceTest extends AbstractGenericTest {

    @Autowired
    private MarketDataIngestionService marketDataIngestionService;

    @Value("${bluebell.ingress.root}")
    private String dataRoot;

    @BeforeEach
    void setUp() throws Exception {
        FileUtils.copyDirectory(
                new File(String.format("%s%s%s", DirectoryUtil.getTestingResourcesDirectory(), File.separator, "/copy-ingress")),
                new File(String.format("%s%s%s", DirectoryUtil.getTestingResourcesDirectory(), File.separator, this.dataRoot))
        );
        this.marketDataIngestionService.setTest(true);
    }

    @AfterEach
    void tearDown() throws Exception {
        FileUtils.deleteDirectory(new File(String.format("%s%s%s", DirectoryUtil.getTestingResourcesDirectory(), File.separator, this.dataRoot)));
    }


    //  ----------------- ingest -----------------

    @Test
    void test_ingest_badData() {
        assertThat(this.marketDataIngestionService.ingest(null, null, null).getValue0()).isFalse();
        assertThat(this.marketDataIngestionService.ingest(DataSource.FIRST_RATE_DATA, null, null).getValue0()).isFalse();
        assertThat(this.marketDataIngestionService.ingest(DataSource.FIRST_RATE_DATA, "TEST", null).getValue0()).isFalse();
        assertThat(this.marketDataIngestionService.ingest(DataSource.FIRST_RATE_DATA, "TEST", "/test-not-ingress").getValue0()).isFalse();
        assertThat(this.marketDataIngestionService.ingest(DataSource.FIRST_RATE_DATA, "TEST", "/empty-ingress").getValue0()).isFalse();
    }

    @Test
    void test_ingest_noSymbol() {
        final Triplet<Boolean, String, Set<MarketPrice>> result = this.marketDataIngestionService.ingest(DataSource.FIRST_RATE_DATA, "Bad Symbol", this.dataRoot);
        assertThat(result.getValue0()).isFalse();
        assertThat(result.getValue1()).contains("does not exist");
        assertThat(result.getValue2()).isEmpty();
    }

    @Test
    void test_ingest_emptySymbol() {
        final Triplet<Boolean, String, Set<MarketPrice>> result = this.marketDataIngestionService.ingest(DataSource.METATRADER4, "EMPTY", this.dataRoot);
        assertThat(result.getValue0()).isFalse();
        assertThat(result.getValue1()).contains("does not have any data");
        assertThat(result.getValue2()).isEmpty();
    }

    @Test
    void test_ingest_success() {
        final Triplet<Boolean, String, Set<MarketPrice>> result = this.marketDataIngestionService.ingest(DataSource.TRADING_VIEW, "US100", this.dataRoot);
        assertThat(result.getValue0()).isTrue();
        assertThat(result.getValue2()).isNotEmpty();
        assertThat(new File(String.format("%s%s%s%s%s", DirectoryUtil.getTestingResourcesDirectory(), File.separator, this.dataRoot, File.separator, DataSource.TRADING_VIEW.getDataRoot()))).doesNotExist();
    }

    @Test
    void test_ingest_parsingError() {
        final Triplet<Boolean, String, Set<MarketPrice>> result = this.marketDataIngestionService.ingest(DataSource.FIRST_RATE_DATA, "NDX", this.dataRoot);
        assertThat(result.getValue0()).isFalse();
        assertThat(result.getValue2()).isEmpty();
    }
}
