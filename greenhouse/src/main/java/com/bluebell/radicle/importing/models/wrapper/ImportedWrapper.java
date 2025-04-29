package com.bluebell.radicle.importing.models.wrapper;


/**
 * Generic trade wrapper contract
 *
 * @author Stephen Prizio
 * @version 0.1.8
 */
public interface ImportedWrapper<W> {

    /**
     * Merges the given instance with this instance, typically to represent the case where a trade/transaction is represented twice,
     * once opened, and once closed
     *
     * @param wrapper {@link W}
     * @return updated {@link W}
     */
    W merge(W wrapper);
}
