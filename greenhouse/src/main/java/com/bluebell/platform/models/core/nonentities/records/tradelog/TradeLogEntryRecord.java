package com.bluebell.platform.models.core.nonentities.records.tradelog;

import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.nonentities.records.traderecord.TradeRecordReport;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

/**
 * Class representation of a line item inside a {@link TradeLogEntry} which contains all trade records and their totals for a specific {@link Account}
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
@Builder
@Schema(title = "TradeLogEntryRecord", name = "TradeLogEntryRecord", description = "A report of trades taken on an account for a time period")
public record TradeLogEntryRecord(
        @JsonIgnore Account account,
        @Getter @Schema(description = "Account number") long accountNumber,
        @Getter @Schema(description = "Account name") String accountName,
        @Getter @Schema(description = "Report of trading activity for this account and period") TradeRecordReport report
) { }
