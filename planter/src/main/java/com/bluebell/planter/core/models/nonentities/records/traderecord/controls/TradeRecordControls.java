package com.bluebell.planter.core.models.nonentities.records.traderecord.controls;

import com.bluebell.planter.core.models.nonentities.records.traderecord.TradeRecord;

import java.util.List;

/**
 * Class representation of a {@link TradeRecordControls}, filters for {@link TradeRecord}s
 *
 * @author Stephen Prizio
 * @version 0.0.7
 */
public record TradeRecordControls(List<TradeRecordControlsYearEntry> yearEntries) {
}
