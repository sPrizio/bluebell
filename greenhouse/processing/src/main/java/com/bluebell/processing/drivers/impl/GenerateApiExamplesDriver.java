package com.bluebell.processing.drivers.impl;

import com.bluebell.processing.drivers.Driver;
import com.bluebell.processing.processors.ApiExamplesProcessor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * Concrete implementation of a {@link Driver}, specifically for generating api example documentation for use within swagger
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
@Slf4j
public class GenerateApiExamplesDriver implements Driver {


    //  METHODS

    /**
     * Generates the api examples and updates appropriate dependencies
     *
     * @param args system arguments
     */
    public static void main(String[] args) {

        LOGGER.info("Generating API examples...");
        final ApiExamplesProcessor processor = new ApiExamplesProcessor();
        final boolean result = processor.process();
        if (result) {
            LOGGER.info("API Examples successfully generated.");

            try {
                processor.updateDependencies();
            } catch (IOException e) {
                LOGGER.error("IOError occurred during dependency update.");
            }
        } else {
            LOGGER.error("API Examples could not be generated. Refer to the logs for additional information.");
        }
    }

    @Override
    public String getName() {
        return "GenerateApiExamples";
    }

    @Override
    public String getPurpose() {
        return "Generates JSON strings for api endpoints as a means of prettier documentation within Swagger.";
    }
}
