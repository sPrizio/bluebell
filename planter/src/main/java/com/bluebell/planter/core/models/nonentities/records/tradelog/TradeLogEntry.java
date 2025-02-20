package com.bluebell.planter.core.models.nonentities.records.tradelog;

import com.bluebell.planter.core.models.entities.account.Account;
import com.bluebell.planter.core.models.nonentities.records.traderecord.TradeRecord;

import java.time.LocalDate;
import java.util.List;

/**
 * Class representation of line item entry inside a {@link TradeLog}, consisting of a collection of {@link TradeRecord}s for an {@link Account} within a time span and a sum of their totals
 *
 * @param start start of time span
 * @param end end of time span
 * @param records {@link List} of {@link TradeLogEntryRecord}s
 * @param totals {@link TradeLogEntryRecordTotals}
 * @author Stephen Prizio
 * @version 0.0.7
 */
public record TradeLogEntry(LocalDate start, LocalDate end, List<TradeLogEntryRecord> records, TradeLogEntryRecordTotals totals) {
}
