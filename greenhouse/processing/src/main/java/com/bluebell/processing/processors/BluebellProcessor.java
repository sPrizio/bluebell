package com.bluebell.processing.processors;

import java.io.IOException;

/**
 * Defines the contract for all generated source files
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
public interface BluebellProcessor {

    /**
     * Executes the processing class to generate code
     *
     * @return true if successful, false otherwise
     */
    boolean process();

    /**
     * Ensures that all processors will update their import/export dependencies inside the module-info.java
     *
     * @throws IOException file reading / writing exception
     */
    void updateDependencies() throws IOException;
}
