package com.bluebell.radicle.runners;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Parent-level CommandLineRunner
 *
 * @author Stephen Prizio
 * @version 0.1.7
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
            long totalSeconds = ChronoUnit.SECONDS.between(this.startTime, endTime);
            if (totalSeconds < 1) {
                LOGGER.info("{} generated all test data in {} milliseconds", this.getClass().getSimpleName(), ChronoUnit.MILLIS.between(this.startTime, endTime));
            } else if (ChronoUnit.MINUTES.between(this.startTime, endTime) < 2) {
                LOGGER.info("{} generated all test data in {} seconds", this.getClass().getSimpleName(), ChronoUnit.SECONDS.between(this.startTime, endTime));
            } else {
                int hours = (int) totalSeconds / 3600;
                int remaining = (int) totalSeconds % 3600;
                int minutes = remaining / 60;
                int seconds = remaining % 60;

                if (hours > 0) {
                    LOGGER.info("{} generated all test data in {} hours, {} minutes and {} seconds", this.getClass().getSimpleName(), hours, minutes, seconds);
                } else {
                    LOGGER.info("{} generated all test data in {} minutes and {} seconds", this.getClass().getSimpleName(), minutes, seconds);
                }
            }

            this.startTime = null;
        } else {
            LOGGER.warn("Time tracking not started. Call logStart() to begin time tracking");
        }
    }
}
