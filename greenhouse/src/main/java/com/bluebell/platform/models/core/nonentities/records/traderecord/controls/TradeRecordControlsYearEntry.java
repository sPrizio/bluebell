package com.bluebell.platform.models.core.nonentities.records.traderecord.controls;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * Class representation of a {@link TradeRecordControls} for a single year
 *
 * @param year year
 * @param monthEntries {@link List} of {@link TradeRecordControlsMonthEntry}
 * @author Stephen Prizio
 * @version 0.1.1
 */
@Builder
@Schema(title = "TradeRecordControlsYearEntry", name = "TradeRecordControlsYearEntry", description = "A specific year filter and information about the months of that year")
public record TradeRecordControlsYearEntry(
        @Getter @Schema(description = "The year to filter on") String year,
        @Getter @Schema(description = "List of month associated filters") List<TradeRecordControlsMonthEntry> monthEntries
) { }
