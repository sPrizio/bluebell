package com.bluebell.platform.models.core.nonentities.records.traderecord.controls;


import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * Class representation of a {@link TradeRecordControls} for a single year
 *
 * @param year year
 * @param monthEntries {@link List} of {@link TradeRecordControlsMonthEntry}
 * @author Stephen Prizio
 * @version 0.1.0
 */
@Schema(title = "TradeRecordControlsYearEntry", name = "TradeRecordControlsYearEntry", description = "A specific year filter and information about the months of that year")
public record TradeRecordControlsYearEntry(@Getter String year, @Getter List<TradeRecordControlsMonthEntry> monthEntries) {
}
