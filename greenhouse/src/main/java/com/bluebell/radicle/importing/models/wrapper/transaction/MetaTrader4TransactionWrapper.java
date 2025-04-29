package com.bluebell.radicle.importing.models.wrapper.transaction;

import com.bluebell.platform.enums.transaction.TransactionType;
import com.bluebell.radicle.importing.models.wrapper.ImportedWrapper;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * A wrapper class for MetaTrader4 transactions
 *
 * @author Stephen Prizio
 * @version 0.1.8
 */
@Builder
public record MetaTrader4TransactionWrapper(
        @Getter LocalDateTime dateTime,
        @Getter TransactionType type,
        @Getter String name,
        @Getter double amount
) implements ImportedWrapper<MetaTrader4TransactionWrapper> {

    @Override
    public MetaTrader4TransactionWrapper merge(final MetaTrader4TransactionWrapper wrapper) {
        return wrapper;
    }
}
