package com.bluebell.radicle.importing;

import java.io.InputStream;

import com.bluebell.platform.models.core.entities.account.Account;

/**
 * Defines the import service architecture for importing trades into the system
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
public interface ImportService {

    /**
     * Imports trades from a CSV file using a file path
     *
     * @param filePath  file path
     * @param delimiter delimiter
     * @param account   {@link Account}
     */
    void importTrades(final String filePath, final Character delimiter, final Account account);

    /**
     * Imports trades from a CSV file using an {@link InputStream}
     *
     * @param inputStream {@link InputStream}
     * @param delimiter   unit delimiter
     * @param account     {@link Account}
     */
    void importTrades(final InputStream inputStream, final Character delimiter, final Account account);
}
