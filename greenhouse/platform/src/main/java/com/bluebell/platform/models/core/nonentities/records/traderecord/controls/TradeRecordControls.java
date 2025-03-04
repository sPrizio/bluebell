package com.bluebell.platform.models.core.nonentities.records.traderecord.controls;


import com.bluebell.platform.models.core.nonentities.records.traderecord.TradeRecord;

import java.util.List;

/**
 * Class representation of a {@link TradeRecordControls}, filters for {@link TradeRecord}s
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
public record TradeRecordControls(List<TradeRecordControlsYearEntry> yearEntries) {
}
