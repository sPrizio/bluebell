package com.bluebell.platform.cleaners.impl;

import com.bluebell.platform.cleaners.Cleaner;
import lombok.extern.slf4j.Slf4j;

/**
 * Main cleaner that executes all other {@link Cleaner}s
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
@Slf4j
public class PrimaryCleaner implements Cleaner {


    //  METHODS

    /**
     * Generates the api examples and updates appropriate dependencies
     *
     * @param args system arguments
     */
    public static void main(String[] args) {
        GenerateApiExamplesCleaner.main(args);
    }
}
