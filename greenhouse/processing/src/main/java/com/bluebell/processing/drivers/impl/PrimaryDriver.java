package com.bluebell.processing.drivers.impl;

import com.bluebell.processing.drivers.Driver;

/**
 * Acts as the defacto {@link Driver} that will call all other {@link Driver}s
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
public class PrimaryDriver implements Driver {


    //  METHODS

    /**
     * Executes the code generation
     *
     * @param args system arguments
     */
    public static void main(String[] args) {
        GenerateApiExamplesDriver.main(args);
    }

    @Override
    public String getName() {
        return "PrimaryDriver";
    }

    @Override
    public String getPurpose() {
        return "Executes all other Drivers, initiating Code Generation.";
    }
}
