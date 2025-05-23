package com.bluebell.platform.models.core.nonentities.records.analysis;


import com.bluebell.platform.models.core.entities.account.Account;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

/**
 * Class representation of a result of performing a certain analysis on an {@link Account}
 *
 * @param label label for the individual analysis segment
 * @param value numerical value of the analysis
 * @param count sum of elements/computations comprising the analysis value
 * @author Stephen Prizio
 * @version 0.1.1
 */
@Builder
@Schema(title = "AnalysisResult", name = "AnalysisResult", description = "Represents a result of analysis for a particular piece of information. In other words, let's say we're analysis performance for each weekday, an individual AnalysisResult will represent 1 weekday")
public record AnalysisResult(
        @Getter @Schema(description = "The label for the individual analysis segment") String label,
        @Getter @Schema(description = "The numerical value of the analysis") double value,
        @Getter @Schema(description = "The sum of elements/computations comprising the analysis value") int count
) implements Comparable<AnalysisResult> {


    //  METHODS

    @Override
    public int compareTo(AnalysisResult o) {
        return this.label.compareTo(o.label);
    }
}
