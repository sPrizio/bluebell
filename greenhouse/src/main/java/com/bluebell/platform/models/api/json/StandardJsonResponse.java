package com.bluebell.platform.models.api.json;

import com.bluebell.platform.enums.account.AccountType;
import com.bluebell.platform.enums.account.Broker;
import com.bluebell.platform.enums.account.Currency;
import com.bluebell.platform.enums.system.Country;
import com.bluebell.platform.enums.system.Language;
import com.bluebell.platform.enums.system.PhoneType;
import com.bluebell.platform.enums.trade.TradePlatform;
import com.bluebell.platform.models.api.dto.account.AccountDTO;
import com.bluebell.platform.models.api.dto.news.MarketNewsDTO;
import com.bluebell.platform.models.api.dto.portfolio.PortfolioDTO;
import com.bluebell.platform.models.api.dto.security.UserDTO;
import com.bluebell.platform.models.api.dto.trade.PaginatedTradesDTO;
import com.bluebell.platform.models.api.dto.trade.TradeDTO;
import com.bluebell.platform.models.core.nonentities.apexcharts.ApexChartCandleStick;
import com.bluebell.platform.models.core.nonentities.data.PairEntry;
import com.bluebell.platform.models.core.nonentities.records.account.AccountDetails;
import com.bluebell.platform.models.core.nonentities.records.portfolio.PortfolioRecord;
import com.bluebell.platform.models.core.nonentities.records.tradelog.TradeLog;
import com.bluebell.platform.models.core.nonentities.records.traderecord.TradeRecordReport;
import com.bluebell.platform.models.core.nonentities.records.traderecord.controls.TradeRecordControls;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * Class representation of a standard json response
 *
 * @author Stephen Prizio
 * @version 0.1.2
 */
@Builder
@Schema(title = "StandardJsonResponse", name = "StandardJsonResponse", description = "Standard API response entity. All api calls will return this entity which includes a success flag, data, external & internal facing messages.")
public record StandardJsonResponse<T>(
        @Schema(description = "Indicates whether the response successfully completed") boolean success,
        @Schema(
                description = "The response data",
                type = "object",
                oneOf = {
                        PairEntry.class,
                        Currency.class,
                        Broker.class,
                        AccountType.class,
                        TradePlatform.class,
                        AccountDetails.class,
                        AccountDTO.class,
                        Boolean.class,
                        ApexChartCandleStick.class,
                        MarketNewsDTO.class,
                        PortfolioRecord.class,
                        UserDTO.class,
                        Country.class,
                        PhoneType.class,
                        Language.class,
                        TradeRecordReport.class,
                        TradeRecordControls.class,
                        TradeLog.class,
                        TradeDTO.class,
                        PaginatedTradesDTO.class,
                        PortfolioDTO.class
                }
        ) T data,
        @Schema(description = "External, client-facing message. Successful calls will usually have an empty message") String message,
        @Schema(description = "Internal, server-facing message") String internalMessage
) { }
