package com.bluebell.radicle.exceptions.trade;

import com.bluebell.platform.models.core.nonentities.records.traderecord.TradeRecord;

/**
 * Exception thrown when an occurs computing {@link TradeRecord}s
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
public class TradeRecordComputationException extends RuntimeException {

    public TradeRecordComputationException(final String message) {
        super(message);
    }

    public TradeRecordComputationException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
