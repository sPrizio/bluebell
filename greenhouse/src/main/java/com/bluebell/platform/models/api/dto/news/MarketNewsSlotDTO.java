package com.bluebell.platform.models.api.dto.news;

import java.time.LocalTime;
import java.util.List;

import com.bluebell.platform.models.api.dto.GenericDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

/**
 * A DTO representation of a {@link MarketNewsSlotDTO}
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
@Getter
@Setter
@Builder
@EqualsAndHashCode
@Schema(title = "MarketNewsSlotDTO", name = "MarketNewsSlotDTO", description = "Represents a slot in a day and time that can contain different news pieces")
public class MarketNewsSlotDTO implements GenericDTO, Comparable<MarketNewsSlotDTO> {

    @Schema(description = "Slot UID")
    private @Builder.Default String uid = StringUtils.EMPTY;

    @Schema(description = "Time of news")
    private LocalTime time;

    @Schema(description = "List of news pieces")
    private List<MarketNewsEntryDTO> entries;

    @Schema(description = "Active or not, i.e. relevant for today")
    private boolean active;


    //  METHODS

    @Override
    public int compareTo(final @NonNull MarketNewsSlotDTO o) {
        return this.time.compareTo(o.time);
    }
}
