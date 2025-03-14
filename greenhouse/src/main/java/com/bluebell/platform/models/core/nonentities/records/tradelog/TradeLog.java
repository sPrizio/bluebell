package com.bluebell.platform.models.core.nonentities.records.tradelog;


import com.bluebell.platform.models.core.nonentities.records.traderecord.TradeRecord;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * Class representation of a {@link TradeLog}, which is a collection of {@link TradeRecord}s for various accounts within a time span
 *
 * @param entries {@link List} of {@link TradeLogEntry}
 * @author Stephen Prizio
 * @version 0.1.1
 */
@Builder
@Schema(title = "TradeLog", name = "TradeLog", description = "Lists trades taken from various accounts over a period of time")
public record TradeLog(
        @Getter @Schema(description = "List of trade log entries") List<TradeLogEntry> entries
) { }
