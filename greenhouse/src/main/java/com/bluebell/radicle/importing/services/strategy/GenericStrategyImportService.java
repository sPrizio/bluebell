package com.bluebell.radicle.importing.services.strategy;

import java.io.InputStream;

import static com.bluebell.radicle.validation.GenericValidator.validateParameterIsNotNull;

import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.strategy.StrategyPlatform;
import com.bluebell.platform.enums.trade.TradePlatform;
import com.bluebell.platform.models.core.entities.account.Account;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Generic importing service to handle incoming strategy files, will delegate to specific import services
 *
 * @author Stephen Prizio
 * @version 0.1.0
 */
@Service
public class GenericStrategyImportService {

    private final MetaTrader4StrategyImportService metaTrader4StrategyImportService;

    @Autowired
    public GenericStrategyImportService(final MetaTrader4StrategyImportService metaTrader4StrategyImportService) {
        this.metaTrader4StrategyImportService = metaTrader4StrategyImportService;
    }


    //  METHODS

    /**
     * Imports the strategy report
     *
     * @param inputStream {@link InputStream}
     * @param account {@link Account}
     * @return result
     */
    public String importReport(final InputStream inputStream, final Account account) {

        validateParameterIsNotNull(inputStream, CorePlatformConstants.Validation.Trade.IMPORT_STREAM_CANNOT_BE_NULL);
        validateParameterIsNotNull(account, CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);

        try {
            if (getStrategyPlatformForTradePlatform(account.getTradePlatform()).equals(StrategyPlatform.BLUEBELL)) {
                return StringUtils.EMPTY;
            } else if (getStrategyPlatformForTradePlatform(account.getTradePlatform()).equals(StrategyPlatform.METATRADER4)) {
                this.metaTrader4StrategyImportService.importTrades(inputStream, null, account);
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
