package com.bluebell.radicle.importing.models;


/**
 * Generic trade wrapper contract
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
public interface ImportedTradeWrapper<W> {

    /**
     * Merges the given instance with this instance, typically to represent the case where a trade is represented twice,
     * once open and once closed
     *
     * @param wrapper {@link W}
     * @return updated {@link W}
     */
    W merge(W wrapper);
}
