package com.bluebell.platform.models.api.dto.news;

import com.bluebell.platform.models.api.dto.GenericDTO;
import com.bluebell.platform.models.core.entities.news.MarketNews;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * A DTO representation of a {@link MarketNews}
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
@Getter
@Setter
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
