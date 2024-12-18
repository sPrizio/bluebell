package com.bluebell.planter.core.models.nonentities.records.analysis;

import com.bluebell.planter.core.models.entities.account.Account;

/**
 * Class representation of a result of performing a certain analysis on an {@link Account}
 *
 * @param label label
 * @param value value
 * @param count count
 * @author Stephen Prizio
 * @version 0.0.7
 */
public record AnalysisResult(String label, double value, int count) implements Comparable<AnalysisResult> {


    //  METHODS

    @Override
    public int compareTo(AnalysisResult o) {
        return this.label.compareTo(o.label);
    }
}
