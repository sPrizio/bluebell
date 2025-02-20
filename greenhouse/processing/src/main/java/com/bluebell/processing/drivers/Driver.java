package com.bluebell.processing.drivers;

/**
 * A Driver is a unique concept to bluebell. It is a class meant to drive code generation
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
public interface Driver {

    /**
     * Executes the driver
     *
     * @param args system arguments
     */
    static void main(String[] args) {}

    /**
     * Returns the name of the driver
     *
     * @return name
     */
    String getName();

    /**
     * Returns the purpose or designation of this driver
     *
     * @return purpose / description
     */
    String getPurpose();
}
