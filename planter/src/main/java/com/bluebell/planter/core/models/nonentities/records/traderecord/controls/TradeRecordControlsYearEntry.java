package com.bluebell.planter.core.models.nonentities.records.traderecord.controls;

import java.util.List;

/**
 * Class representation of a {@link TradeRecordControls} for a single year
 *
 * @author Stephen Prizio
 * @version 0.0.7
 */
public record TradeRecordControlsYearEntry(String year, List<TradeRecordControlsMonthEntry> monthEntries) {
}
