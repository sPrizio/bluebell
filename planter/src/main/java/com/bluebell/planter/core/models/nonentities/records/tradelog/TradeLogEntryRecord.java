package com.bluebell.planter.core.models.nonentities.records.tradelog;

import com.bluebell.planter.core.models.entities.account.Account;
import com.bluebell.planter.core.models.nonentities.records.traderecord.TradeRecordReport;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Class representation of a line item inside a {@link TradeLogEntry} which contains all trade records and their totals for a specific {@link Account}
 *
 * @author Stephen Prizio
 * @version 0.0.7
 */
public record TradeLogEntryRecord(@JsonIgnore Account account, long accountNumber, String accountName, TradeRecordReport report) {
}
