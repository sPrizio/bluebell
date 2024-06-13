package com.bluebell.radicle.services;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * General arithmetic and number formatting service
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
public class MathService {


    //  METHODS

    /**
     * Gets a rounded {@link Double} to 2 decimal places
     *
     * @param d {@link Double}
     * @return {@link Double}
     */
    public double getDouble(final double d) {
        return BigDecimal.valueOf(d).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
    }

    /**
     * Gets a rounded {@link Integer}
     *
     * @param d {@link Double}
     * @return {@link Integer}
     */
    public int getInteger(final double d) {
        return BigDecimal.valueOf(d).setScale(0, RoundingMode.HALF_EVEN).intValue();
    }

    /**
     * Performs a basic sum
     *
     * @param a {@link Double}
     * @param b {@link Double}
     * @return {@link Double}
     */
    public double add(final double a, final double b) {
        return getDouble(BigDecimal.valueOf(a).add(BigDecimal.valueOf(b)).doubleValue());
    }

    /**
     * Performs a basic difference
     *
     * @param a {@link Double}
     * @param b {@link Double}
     * @return {@link Double}
     */
    public double subtract(final double a, final double b) {
        return getDouble(BigDecimal.valueOf(a).subtract(BigDecimal.valueOf(b)).doubleValue());
    }

    /**
     * Performs a basic divide with safe dividend
     *
     * @param a {@link Double}
     * @param b {@link Double}
     * @return {@link Double}
     */
    public double divide(final double a, final double b) {

        if (b == 0.0) {
            return 0.0;
        }

        return getDouble(BigDecimal.valueOf(a).divide(BigDecimal.valueOf(b), 10, RoundingMode.HALF_EVEN).doubleValue());
    }

    /**
     * Performs a basic multiply
     *
     * @param a {@link Double}
     * @param b {@link Double}
     * @return {@link Double}
     */
    public double multiply(final double a, final double b) {
        return getDouble(BigDecimal.valueOf(a).multiply(BigDecimal.valueOf(b)).doubleValue());
    }

    /**
     * Obtains the percentage change rounded to a number larger than 1
     * Example: 9 / 87 would normally be 0.103448, this method would return 10.34
     *
     * @param a {@link Double}
     * @param b {@link Double}
     * @return {@link Double}
     */
    public double delta(final double a, final double b) {
        return getDouble(fullDivide(a, b).multiply(BigDecimal.valueOf(100.0)).doubleValue());
    }

    /**
     * Obtains a whole percentage rounded to the nearest integer
     * Example : 3 / 10 = 33
     *
     * @param a {@link Double}
     * @param b {@link Double}
     * @return {@link Double}
     */
    public int wholePercentage(final double a, final double b) {
        return getInteger(BigDecimal.valueOf(divide(a, b)).multiply(BigDecimal.valueOf(100.0)).doubleValue());
    }

    /**
     * Computes the increment change on a by b
     *
     * @param a        {@link Double} starter value
     * @param b        {@link Double} value to increment
     * @param absolute if true, b is considered fixed, else is treated like a percentage
     * @return a + increment (b)
     */
    public double computeIncrement(final double a, final double b, final boolean absolute) {

        if (absolute) {
            return add(a, b);
        }

        return
                BigDecimal.valueOf(getDouble(a))
                        .multiply(fullDivide(b, 100.0).add(BigDecimal.ONE))
                        .subtract(BigDecimal.valueOf(a))
                        .setScale(2, RoundingMode.HALF_EVEN)
                        .doubleValue();
    }

    /**
     * Obtains the percentage change between 2 numbers
     *
     * @param a {@link Double}
     * @param b {@link Double}
     * @return {@link Double} as a percentage
     */
    public double percentageChange(final double a, final double b) {

        if (b == 0.0) {
            return 0.0;
        }

        return
                BigDecimal.valueOf(a)
                        .subtract(BigDecimal.valueOf(b))
                        .divide(BigDecimal.valueOf(b), 10, RoundingMode.HALF_EVEN)
                        .multiply(BigDecimal.valueOf(100.0))
                        .setScale(2, RoundingMode.HALF_EVEN)
                        .doubleValue();
    }

    /**
     * Computes the average of 2 different averages
     *
     * @param a      first average
     * @param countA count a
     * @param b      second average
     * @param countB count b
     * @return average of both averages
     */
    public double weightedAverage(final double a, final int countA, final double b, final int countB) {

        double sum = add(getDouble(countA), getDouble(countB));
        BigDecimal averageA = fullDivide(countA, sum);
        BigDecimal averageB = fullDivide(countB, sum);

        return add(
                BigDecimal.valueOf(a).multiply(averageA).setScale(2, RoundingMode.HALF_EVEN).doubleValue(),
                BigDecimal.valueOf(b).multiply(averageB).setScale(2, RoundingMode.HALF_EVEN).doubleValue()
        );
    }


    //  HELPERS

    /**
     * Divides 2 numbers
     *
     * @param a {@link Double}
     * @param b {@link Double}
     * @return quotient
     */
    private BigDecimal fullDivide(final double a, final double b) {

        if (b == 0.0) {
            return BigDecimal.ZERO;
        }

        return BigDecimal.valueOf(a).divide(BigDecimal.valueOf(b), 10, RoundingMode.HALF_EVEN);
    }
}
