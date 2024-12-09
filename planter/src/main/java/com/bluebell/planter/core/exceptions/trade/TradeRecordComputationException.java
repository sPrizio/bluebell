package com.bluebell.planter.core.exceptions.trade;

import com.bluebell.planter.core.models.nonentities.records.tradeRecord.TradeRecord;

/**
 * Exception thrown when an occurs computing {@link TradeRecord}s
 *
 * @author Stephen Prizio
 * @version 0.0.7
 */
public class TradeRecordComputationException extends RuntimeException {

    public TradeRecordComputationException(final String message) {
        super(message);
    }

    public TradeRecordComputationException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
