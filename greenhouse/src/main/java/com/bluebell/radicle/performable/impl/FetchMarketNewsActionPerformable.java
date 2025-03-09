package com.bluebell.radicle.performable.impl;

import com.bluebell.platform.models.core.nonentities.action.ActionData;
import com.bluebell.radicle.performable.ActionPerformable;
import com.bluebell.radicle.services.news.MarketNewsService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link ActionPerformable} that fetches market data
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
@Component("fetchMarketNewsActionPerformable")
public class FetchMarketNewsActionPerformable implements ActionPerformable {

    @Resource(name = "marketNewsService")
    private MarketNewsService marketNewsService;


    //  METHODS

    @Override
    public ActionData perform() {

        //  TODO: implement this
        return null;
    }
}
