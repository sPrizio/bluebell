package com.bluebell.platform.models.core.nonentities.records.traderecord.controls;


import java.util.List;

import com.bluebell.platform.models.core.nonentities.records.traderecord.TradeRecord;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * Class representation of a {@link TradeRecordControls}, filters for {@link TradeRecord}s
 *
 * @author Stephen Prizio
 * @version 0.1.0
 */
@Schema(title = "TradeRecordControls", name = "TradeRecordControls", description = "A collection of controls used to filter trade records on an account")
public record TradeRecordControls(
        @Getter @Schema(description = "A list of years and their months info that can be filtered") List<TradeRecordControlsYearEntry> yearEntries
) { }
