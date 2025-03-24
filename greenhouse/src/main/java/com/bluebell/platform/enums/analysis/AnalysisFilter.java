package com.bluebell.platform.enums.analysis;

import com.bluebell.platform.enums.GenericEnum;
import lombok.Getter;

/**
 * Enumeration of that various data filters that can be applied to the results of an analysis
 *
 * @author Stephen Prizio
 * @version 0.1.3
 */
@Getter
public enum AnalysisFilter implements GenericEnum<AnalysisFilter> {
    POINTS("POINTS", "Points"),
    PROFIT("PROFIT", "Profit"),
    WIN_PERCENTAGE("PERCENTAGE", "Win %");

    private final String code;

    private final String label;

    AnalysisFilter(final String code, final String label) {
        this.code = code;
        this.label = label;
    }
}
