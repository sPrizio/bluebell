package com.bluebell.platform.models.api.dto.news;

import com.bluebell.platform.models.api.dto.GenericDTO;
import com.bluebell.platform.models.core.entities.news.MarketNewsEntry;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

/**
 * A DTO representation of  {@link MarketNewsEntry}
 *
 * @author Stephen Prizio
 * @version 0.1.4
 */
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@Schema(title = "MarketNewsEntryDTO", name = "MarketNewsEntryDTO", description = "A piece of news on a given day at a given time")
public class MarketNewsEntryDTO implements GenericDTO, Comparable<MarketNewsEntryDTO> {

    @Schema(description = "Entry uid")
    private @Builder.Default String uid = StringUtils.EMPTY;

    @Schema(description = "News content")
    private @EqualsAndHashCode.Exclude String content;

    @Schema(description = "Severity of the news")
    private @EqualsAndHashCode.Exclude String severity;

    @Schema(description = "Severity expressed as an integer")
    private @EqualsAndHashCode.Exclude int severityLevel;

    @Schema(description = "Relevant country")
    private @EqualsAndHashCode.Exclude String country;

    @Schema(description = "Forecasted amount")
    private @EqualsAndHashCode.Exclude String forecast;

    @Schema(description = "Previous amount")
    private @EqualsAndHashCode.Exclude String previous;


    //  METHODS

    @Override
    public int compareTo(final @NonNull MarketNewsEntryDTO o) {
        return this.content.compareTo(o.content);
    }
}