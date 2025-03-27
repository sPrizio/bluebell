package com.bluebell.radicle.runners;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Parent-level CommandLineRunner
 *
 * @author Stephen Prizio
 * @version 0.1.4
 */
@Slf4j
public abstract class AbstractRunner {

    private LocalDateTime startTime = null;


    //  METHODS

    /**
     * Tracks the start time of the execution
     */
    public void logStart() {
        this.startTime = LocalDateTime.now();
    }


    /**
     * Logs the total execution time
     */
    public void logEnd() {

        final LocalDateTime endTime = LocalDateTime.now();
        if (this.startTime != null) {
            if (ChronoUnit.SECONDS.between(this.startTime, endTime) < 1) {
                LOGGER.info("{} generated all test data in {} milliseconds", this.getClass().getSimpleName(), ChronoUnit.MILLIS.between(this.startTime, endTime));
            } else if (ChronoUnit.MINUTES.between(this.startTime, endTime) < 2) {
                LOGGER.info("{} generated all test data in {} seconds", this.getClass().getSimpleName(), ChronoUnit.SECONDS.between(this.startTime, endTime));
            } else {
                LOGGER.info("{} generated all test data in {} minutes and {} seconds", this.getClass().getSimpleName(), ChronoUnit.MINUTES.between(this.startTime, endTime), ChronoUnit.SECONDS.between(this.startTime, endTime));
            }
            this.startTime = null;
        } else {
            LOGGER.warn("Time tracking not started. Call logStart() to begin time tracking");
        }
    }
}
