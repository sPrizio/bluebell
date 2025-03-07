package com.bluebell.platform.models.core.nonentities.records.traderecord.controls;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

/**
 * Class representation of a {@link TradeRecordControls} for a single month
 *
 * @param monthNumber month number
 * @param month name of month
 * @param value value
 * @author Stephen Prizio
 * @version 0.1.1
 */
@Builder
@Schema(title = "TradeRecordControlsMonthEntry", name = "TradeRecordControlsMonthEntry", description = "A month filter data representation")
public record TradeRecordControlsMonthEntry(
        @Getter @Schema(description = "Month described as a number (March = 3)") int monthNumber,
        @Getter @Schema(description = "Name of the month") String month,
        @Getter @Schema(description = "Number of trade records for the given month") int value
) { }
