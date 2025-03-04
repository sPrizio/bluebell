package com.bluebell.platform.models.api.dto.news;

import java.time.LocalDate;
import java.util.List;

import com.bluebell.platform.models.api.dto.GenericDTO;
import com.bluebell.platform.models.core.entities.news.MarketNews;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO representation of a {@link MarketNews}
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
@Getter
@Setter
@Schema(title = "MarketNewsDTO", name = "MarketNewsDTO", description = "Standard DTO for sharing market news")
public class MarketNewsDTO implements GenericDTO, Comparable<MarketNewsDTO> {

    private String uid;

    private LocalDate date;

    private List<MarketNewsSlotDTO> slots;

    private boolean active;

    private boolean past;

    private boolean future;


    //  METHODS
    @Override
    public int compareTo(MarketNewsDTO o) {
        return 0;
    }
}
