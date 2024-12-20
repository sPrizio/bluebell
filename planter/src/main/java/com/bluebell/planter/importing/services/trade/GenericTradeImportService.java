package com.bluebell.planter.importing.services.trade;

import com.bluebell.planter.core.constants.CoreConstants;
import com.bluebell.planter.core.enums.trade.platform.TradePlatform;
import com.bluebell.planter.core.models.entities.account.Account;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

import static com.bluebell.planter.core.validation.GenericValidator.validateParameterIsNotNull;


/**
 * Generic importing service to handle incoming trade files, will delegate to specific import services
 *
 * @author Stephen Prizio
 * @version 0.0.7
 */
@Service
public class GenericTradeImportService {

    private final CMCMarketsTradeImportService cmcMarketsTradeImportService;

    private final MetaTrader4TradeImportService metaTrader4TradeImportService;

    @Autowired
    public GenericTradeImportService(final CMCMarketsTradeImportService cmcMarketsTradeImportService, final MetaTrader4TradeImportService metaTrader4TradeImportService) {
        this.cmcMarketsTradeImportService = cmcMarketsTradeImportService;
        this.metaTrader4TradeImportService = metaTrader4TradeImportService;
    }


    //  TODO: implement FTMO CSV import service

    //  METHODS

    /**
     * Imports a {@link MultipartFile} for the given {@link TradePlatform}
     *
     * @param inputStream {@link InputStream}
     * @param account     {@link Account}
     * @return import message
     */
    public String importTrades(final InputStream inputStream, final Account account) {

        validateParameterIsNotNull(inputStream, CoreConstants.Validation.Trade.IMPORT_STREAM_CANNOT_BE_NULL);

        try {
            if (account.getTradePlatform().equals(TradePlatform.CMC_MARKETS)) {
                this.cmcMarketsTradeImportService.importTrades(inputStream, ',', account);
                return StringUtils.EMPTY;
            } else if (account.getTradePlatform().equals(TradePlatform.METATRADER4)) {
                this.metaTrader4TradeImportService.importTrades(inputStream, null, account);
                return StringUtils.EMPTY;
            }
        } catch (Exception e) {
            return e.getMessage();
        }

        return String.format("Trading platform %s is not currently supported", account.getTradePlatform());
    }
}
