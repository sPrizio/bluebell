package com.bluebell.radicle.performable.impl;

import com.bluebell.platform.models.core.entities.market.MarketPrice;
import com.bluebell.platform.models.core.nonentities.action.ActionData;
import com.bluebell.radicle.enums.DataSource;
import com.bluebell.radicle.enums.IngestionStatus;
import com.bluebell.radicle.performable.ActionPerformable;
import com.bluebell.radicle.services.data.MarketDataIngestionService;
import com.bluebell.radicle.services.market.MarketPriceService;
import jakarta.annotation.Resource;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.javatuples.Triplet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Implementation of {@link ActionPerformable} that ingests market data files
 *
 * @author Stephen Prizio
 * @version 0.1.5
 */
@Slf4j
@Component("ingestMarketDataActionPerformable")
@Scope("prototype")
public class IngestMarketDataActionPerformable implements ActionPerformable {

    @Setter
    private DataSource dataSource = null;

    @Setter
    private String symbol;

    @Value("${bluebell.ingress.root}")
    private String dataRoot;

    @Resource(name = "marketDataIngestionService")
    private MarketDataIngestionService marketDataIngestionService;

    @Resource(name = "marketPriceService")
    private MarketPriceService marketPriceService;


    //  METHODS

    @Override
    public ActionData perform() {

        final Triplet<IngestionStatus, String, Set<MarketPrice>> ingested =
                this.marketDataIngestionService.ingest(this.dataSource, this.symbol, this.dataRoot);

        if (ingested.getValue0().equals(IngestionStatus.FAILED)) {
            return ActionData
                    .builder()
                    .success(false)
                    .logs(ingested.getValue1())
                    .build();
        }

        if (ingested.getValue0().equals(IngestionStatus.SKIPPED)) {
            return ActionData
                    .builder()
                    .success(true)
                    .data(IngestionStatus.SKIPPED)
                    .logs(ingested.getValue1())
                    .build();
        }

        int count = 0;
        if (CollectionUtils.isNotEmpty(ingested.getValue2())) {
            count = this.marketPriceService.saveAllSet(ingested.getValue2());
        }

        return ActionData
                .builder()
                .success(true)
                .logs(ingested.getValue1())
                .data(count)
                .build();
    }
}
