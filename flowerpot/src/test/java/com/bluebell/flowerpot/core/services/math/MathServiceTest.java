package com.bluebell.flowerpot.core.services.math;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testing class for {@link MathService}
 *
 * @author Stephen Prizio
 * @version 0.0.3
 */
public class MathServiceTest {

    private final MathService mathService = new MathService();


    //  ----------------- getDouble -----------------

    @Test
    public void test_getDouble_success() {
        assertThat(this.mathService.getDouble(6.6666667676767))
                .isEqualTo(6.67);
    }


    //  ----------------- getInteger -----------------

    @Test
    public void test_getInteger_success() {
        assertThat(this.mathService.getInteger(6.6666667676767))
                .isEqualTo(7);
    }


    //  ----------------- add -----------------

    @Test
    public void test_add_success() {
        assertThat(this.mathService.add(89.145, 612.57))
                .isEqualTo(701.72);
    }

    //  ----------------- subtract -----------------

    @Test
    public void test_subtract_success() {
        assertThat(this.mathService.subtract(89.145, 612.57))
                .isEqualTo(-523.42);
    }


    //  ----------------- divide -----------------

    @Test
    public void test_divide_success() {
        assertThat(this.mathService.divide(19, 53))
                .isEqualTo(0.36);
    }

    @Test
    public void test_divide_byZero_success() {
        assertThat(this.mathService.divide(1, 0))
                .isEqualTo(0.0);
    }


    //  ----------------- multiply -----------------

    @Test
    public void test_multiply_success() {
        assertThat(this.mathService.multiply(34.87, 963.253))
                .isEqualTo(33588.63);
    }


    //  ----------------- delta -----------------

    @Test
    public void test_delta_success() {
        assertThat(this.mathService.delta(36.98, 3133.33))
                .isEqualTo(1.18);
    }

    @Test
    public void test_delta_byZero_success() {
        assertThat(this.mathService.delta(36.98, 0.0))
                .isEqualTo(0.0);
    }


    //  ----------------- wholePercentage -----------------

    @Test
    public void test_wholePercentage_success() {
        assertThat(this.mathService.wholePercentage(10, 125))
                .isEqualTo(8);
    }


    //  ----------------- computeIncrement -----------------

    @Test
    public void test_computeIncrement_success_absolute() {
        assertThat(this.mathService.computeIncrement(1000.0, 1.25, true))
                .isEqualTo(1001.25);
    }

    @Test
    public void test_computeIncrement_success_relative() {
        assertThat(this.mathService.computeIncrement(1000.0, 1.25, false))
                .isEqualTo(12.50);
    }


    //  ----------------- percentageChange -----------------

    @Test
    public void test_percentageChange_zero_success() {
        assertThat(this.mathService.percentageChange(1.0, 0.0))
                .isEqualTo(0.0);
    }

    @Test
    public void test_percentageChange_success() {
        assertThat(this.mathService.percentageChange(107.82, 128.37))
                .isEqualTo(-16.01);
    }


    //  ----------------- weightedAverage -----------------

    @Test
    public void test_weightedAverage_success() {
        assertThat(this.mathService.weightedAverage(4, 835, 3.5, 4579))
                .isEqualTo(3.58);
    }
}
