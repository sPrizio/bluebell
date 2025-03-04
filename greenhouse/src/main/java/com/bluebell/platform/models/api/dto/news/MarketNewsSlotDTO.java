package com.bluebell.platform.models.api.dto.news;

import java.time.LocalTime;
import java.util.List;

import com.bluebell.platform.models.api.dto.GenericDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO representation of a {@link MarketNewsSlotDTO}
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
@Getter
@Setter
@Schema(title = "MarketNewsSlotDTO", name = "MarketNewsSlotDTO", description = "Represents a slot in a day and time that can contain different news pieces")
public class MarketNewsSlotDTO implements GenericDTO, Comparable<MarketNewsSlotDTO> {

    @Schema(description = "Slot UID")
    private String uid;

    @Schema(description = "Time of news")
    private LocalTime time;

    @Schema(description = "List of news pieces")
    private List<MarketNewsEntryDTO> entries;

    @Schema(description = "Active or not, i.e. relevant for today")
    private boolean active;


    //  METHODS

    @Override
    public int compareTo(MarketNewsSlotDTO o) {
        return this.time.compareTo(o.time);
    }
}
