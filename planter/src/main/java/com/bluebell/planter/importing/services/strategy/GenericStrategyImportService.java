package com.bluebell.planter.importing.services.strategy;

import com.bluebell.planter.core.constants.CoreConstants;
import com.bluebell.planter.core.enums.strategy.StrategyPlatform;
import com.bluebell.planter.core.enums.trade.platform.TradePlatform;
import com.bluebell.planter.core.models.entities.account.Account;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.InputStream;

import static com.bluebell.planter.core.validation.GenericValidator.validateParameterIsNotNull;

/**
 * Generic importing service to handle incoming strategy files, will delegate to specific import services
 *
 * @author Stephen Prizio
 * @version 0.0.7
 */
@Service
public class GenericStrategyImportService {


    //  METHODS

    public String importReport(final InputStream inputStream, final Account account) {

        validateParameterIsNotNull(inputStream, CoreConstants.Validation.Trade.IMPORT_STREAM_CANNOT_BE_NULL);

        try {
            if (getStrategyPlatformForTradePlatform(account.getTradePlatform()).equals(TradePlatform.BLUEBELL)) {
                //this.cmcMarketsTradesImportService.importTrades(inputStream, ',', account);
                return StringUtils.EMPTY;
            } else if (getStrategyPlatformForTradePlatform(account.getTradePlatform()).equals(TradePlatform.METATRADER4)) {
                //this.metaTrader4TradesImportService.importTrades(inputStream, null, account);
                return StringUtils.EMPTY;
            }
        } catch (Exception e) {
            return e.getMessage();
        }

        return String.format("Strategy platform %s is not currently supported", account.getTradePlatform());
    }


    //  HELPERS

    /**
     * Translates a {@link TradePlatform} into a {@link StrategyPlatform} where applicable
     *
     * @param tradePlatform {@link TradePlatform}
     * @return {@link StrategyPlatform}
     */
    private StrategyPlatform getStrategyPlatformForTradePlatform(final TradePlatform tradePlatform) {
        return switch (tradePlatform) {
            case METATRADER4 -> StrategyPlatform.METATRADER4;
            case BLUEBELL -> StrategyPlatform.BLUEBELL;
            default -> StrategyPlatform.UNDEFINED;
        };
    }
}
