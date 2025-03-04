package com.bluebell.platform.models.core.nonentities.records.traderecord.controls;


import lombok.Getter;

/**
 * Class representation of a {@link TradeRecordControls} for a single month
 *
 * @param monthNumber month number
 * @param month name of month
 * @param value value
 * @author Stephen Prizio
 * @version 0.1.0
 */
public record TradeRecordControlsMonthEntry(
        @Getter int monthNumber,
        @Getter String month,
        @Getter int value
) { }
