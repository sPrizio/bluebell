package com.bluebell.platform.models.core.nonentities.records.traderecord.controls;


import com.bluebell.platform.models.core.nonentities.records.traderecord.TradeRecord;
import lombok.Getter;

import java.util.List;

/**
 * Class representation of a {@link TradeRecordControls}, filters for {@link TradeRecord}s
 *
 * @author Stephen Prizio
 * @version 0.1.0
 */
public record TradeRecordControls(@Getter List<TradeRecordControlsYearEntry> yearEntries) { }
