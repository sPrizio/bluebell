package com.bluebell.platform.models.core.nonentities.records.traderecord.controls;


import lombok.Getter;

import java.util.List;

/**
 * Class representation of a {@link TradeRecordControls} for a single year
 *
 * @param year year
 * @param monthEntries {@link List} of {@link TradeRecordControlsMonthEntry}
 * @author Stephen Prizio
 * @version 0.1.0
 */
public record TradeRecordControlsYearEntry(@Getter String year, @Getter List<TradeRecordControlsMonthEntry> monthEntries) {
}
