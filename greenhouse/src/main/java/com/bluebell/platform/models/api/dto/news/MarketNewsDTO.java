package com.bluebell.platform.models.api.dto.news;

import java.time.LocalDate;
import java.util.List;

import com.bluebell.platform.models.api.dto.GenericDTO;
import com.bluebell.platform.models.core.entities.news.MarketNews;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

/**
 * A DTO representation of a {@link MarketNews}
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
@Getter
@Setter
@Builder
@EqualsAndHashCode
@Schema(title = "MarketNewsDTO", name = "MarketNewsDTO", description = "Standard DTO for sharing market news")
public class MarketNewsDTO implements GenericDTO, Comparable<MarketNewsDTO> {

    @Schema(description = "Market news unique identifier")
    private @Builder.Default String uid = StringUtils.EMPTY;

    @Schema(description = "Date of news")
    private LocalDate date;

    @Schema(description = "List of time slots")
    private List<MarketNewsSlotDTO> slots;

    @Schema(description = "Is this news relevant for the present")
    private boolean active;

    @Schema(description = "Has this news passed?")
    private boolean past;

    @Schema(description = "Is this news referring to a future event")
    private boolean future;


    //  METHODS

    @Override
    public int compareTo(final @NonNull MarketNewsDTO o) {
        return 0;
    }
}
