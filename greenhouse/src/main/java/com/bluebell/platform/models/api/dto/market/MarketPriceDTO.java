package com.bluebell.platform.models.api.dto.market;

import com.bluebell.platform.models.api.dto.GenericDTO;
import com.bluebell.platform.models.core.entities.market.MarketPrice;
import com.bluebell.platform.models.core.nonentities.data.EnumDisplay;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;

/**
 * DTO representation of {@link MarketPrice}
 *
 * @author Stephen Prizio
 * @version 0.1.4
 */
@Getter
@Setter
@Builder
@Schema(title = "MarketPriceDTO", name = "MarketPriceDTO", description = "A client-facing reduction of the MarketPrice entity that displays key account information in a safe to ready way.")
public class MarketPriceDTO implements GenericDTO {

    @Schema(description = "Market Price uid")
    private @Builder.Default String uid = StringUtils.EMPTY;

    @Schema(description = "Date and time of price")
    private LocalDateTime date;

    @Schema(description = "Time interval, i.e. 30-minute, etc.")
    private EnumDisplay timeInterval;

    @Schema(description = "Price at start of period")
    private double open;

    @Schema(description = "Highest price achieved for period")
    private double high;

    @Schema(description = "Lowest price experienced for period")
    private double low;

    @Schema(description = "Final price for period")
    private double close;

    @Schema(description = "Volume for period")
    private double volume;

    @Schema(description = "Source of the market price")
    private EnumDisplay dataSource;
}
