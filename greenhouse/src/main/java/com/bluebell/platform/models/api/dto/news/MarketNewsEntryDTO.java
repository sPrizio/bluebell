package com.bluebell.platform.models.api.dto.news;

import com.bluebell.platform.models.api.dto.GenericDTO;
import com.bluebell.platform.models.core.entities.news.MarketNewsEntry;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO representation of  {@link MarketNewsEntry}
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
@Getter
@Setter
@Schema(title = "MarketNewsEntryDTO", name = "MarketNewsEntryDTO", description = "A piece of news on a given day at a given time")
public class MarketNewsEntryDTO implements GenericDTO, Comparable<MarketNewsEntryDTO> {

    @Schema(description = "Entry uid")
    private String uid;

    @Schema(description = "News content")
    private String content;

    @Schema(description = "Severity of the news")
    private String severity;

    @Schema(description = "Severity expressed as an integer")
    private int severityLevel;

    @Schema(description = "Relevant country")
    private String country;

    @Schema(description = "Forecasted amount")
    private String forecast;

    @Schema(description = "Previous amount")
    private String previous;


    //  METHODS

    @Override
    public int compareTo(MarketNewsEntryDTO o) {
        return this.content.compareTo(o.content);
    }
}