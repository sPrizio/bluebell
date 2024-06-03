package com.bluebell.flowerpot.core.validation;

import com.bluebell.flowerpot.core.exceptions.calculator.UnexpectedNegativeValueException;
import com.bluebell.flowerpot.core.exceptions.calculator.UnexpectedZeroValueException;
import com.bluebell.flowerpot.core.exceptions.validation.IllegalParameterException;
import com.bluebell.flowerpot.core.exceptions.validation.JsonMissingPropertyException;
import com.bluebell.flowerpot.core.exceptions.validation.NoResultFoundException;
import com.bluebell.flowerpot.core.exceptions.validation.NonUniqueItemFoundException;
import org.junit.Test;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Testing class for {@link GenericValidator}
 *
 * @author Stephen Prizio
 * @version 0.0.3
 */
public class GenericValidatorTest {


    //  ----------------- validateParameterIsNotNull -----------------

    @Test
    public void test_validateParameterIsNotNull_success() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> GenericValidator.validateParameterIsNotNull(null, "This is a null test"))
                .withMessage("This is a null test");
    }


    //  ----------------- validateIfSingleResult -----------------

    @Test
    public void test_validateIfSingleResult_success() {
        List<String> list = List.of("one", "two");
        assertThatExceptionOfType(NonUniqueItemFoundException.class)
                .isThrownBy(() -> GenericValidator.validateIfSingleResult(list, "This is a single test"))
                .withMessage("This is a single test");
    }


    //  ----------------- validateIfAnyResult -----------------

    @Test
    public void test_validateIfAnyResult_success() {
        List<Object> list = List.of();
        assertThatExceptionOfType(NoResultFoundException.class)
                .isThrownBy(() -> GenericValidator.validateIfAnyResult(list, "This is an empty collection test"))
                .withMessage("This is an empty collection test");
    }


    //  ----------------- validateDatesAreNotMutuallyExclusive -----------------

    @Test
    public void test_validateDatesAreNotMutuallyExclusive_success() {
        LocalDateTime test1 = LocalDate.of(2022, 1, 1).atStartOfDay();
        LocalDateTime test2 = LocalDate.of(2021, 1, 1).atStartOfDay();

        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> GenericValidator.validateDatesAreNotMutuallyExclusive(test1, test2, "This is a two bad dates test"))
                .withMessage("This is a two bad dates test");
    }


    //  ----------------- validateLocalDateTimeFormat -----------------

    @Test
    public void test_validateLocalDateTimeFormat_success() {
        assertThatExceptionOfType(DateTimeException.class)
                .isThrownBy(() -> GenericValidator.validateLocalDateTimeFormat("1/1/1999", "yyyy-MM-dd'T'HH:mm:ss", "This is a bad date & time test"))
                .withMessage("This is a bad date & time test");
    }


    //  ----------------- validateLocalDateFormat -----------------

    @Test
    public void test_validateLocalDateFormat_success() {
        assertThatExceptionOfType(DateTimeException.class)
                .isThrownBy(() -> GenericValidator.validateLocalDateFormat("1/1/1999", "yyyy-MM-dd", "This is a bad date test"))
                .withMessage("This is a bad date test");
    }


    //  ----------------- validateIfPresent -----------------

    @Test
    public void test_validateIfPresent_success() {
        Optional<Integer> optional = Optional.empty();
        assertThatExceptionOfType(NoResultFoundException.class)
                .isThrownBy(() -> GenericValidator.validateIfPresent(optional, "This is an empty optional test"))
                .withMessage("This is an empty optional test");
    }


    //  ----------------- validateJsonIntegrity -----------------

    @Test
    public void test_validateJsonIntegrity_success() {
        Map<String, Object> map = Map.of("test", "value");
        List<String> list = List.of("missing");

        assertThatExceptionOfType(JsonMissingPropertyException.class)
                .isThrownBy(() -> GenericValidator.validateJsonIntegrity(map, list, "This is an empty json test"))
                .withMessage("This is an empty json test");
    }


    //  ----------------- validateNonNegativeValue -----------------

    @Test
    public void test_validateNonNegativeValue_success() {
        assertThatExceptionOfType(UnexpectedNegativeValueException.class)
                .isThrownBy(() -> GenericValidator.validateNonNegativeValue(-1, "This is an empty number int"))
                .withMessage("This is an empty number int");

        assertThatExceptionOfType(UnexpectedNegativeValueException.class)
                .isThrownBy(() -> GenericValidator.validateNonNegativeValue(-1L, "This is an empty number long"))
                .withMessage("This is an empty number long");

        assertThatExceptionOfType(UnexpectedNegativeValueException.class)
                .isThrownBy(() -> GenericValidator.validateNonNegativeValue(-1.0F, "This is an empty number float"))
                .withMessage("This is an empty number float");

        assertThatExceptionOfType(UnexpectedNegativeValueException.class)
                .isThrownBy(() -> GenericValidator.validateNonNegativeValue(-1.0, "This is an empty number double"))
                .withMessage("This is an empty number double");

        AtomicInteger integer = new AtomicInteger(5);
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> GenericValidator.validateNonNegativeValue(integer, "Called validateNonNegativeValue() with an unsupported numerical type. Supported types are: [Integer, Long, Float, Double]"))
                .withMessageContaining("[Integer, Long, Float, Double]");
    }


    //  ----------------- validateNonZeroValue -----------------

    @Test
    public void test_validateNonZeroValue_success() {
        assertThatExceptionOfType(UnexpectedZeroValueException.class)
                .isThrownBy(() -> GenericValidator.validateNonZeroValue((short) 0, "This is a zero number short"))
                .withMessage("This is a zero number short");

        assertThatExceptionOfType(UnexpectedZeroValueException.class)
                .isThrownBy(() -> GenericValidator.validateNonZeroValue(0, "This is a zero number int"))
                .withMessage("This is a zero number int");

        assertThatExceptionOfType(UnexpectedZeroValueException.class)
                .isThrownBy(() -> GenericValidator.validateNonZeroValue(0L, "This is a zero number long"))
                .withMessage("This is a zero number long");

        assertThatExceptionOfType(UnexpectedZeroValueException.class)
                .isThrownBy(() -> GenericValidator.validateNonZeroValue(0.0F, "This is a zero number float"))
                .withMessage("This is a zero number float");

        assertThatExceptionOfType(UnexpectedZeroValueException.class)
                .isThrownBy(() -> GenericValidator.validateNonZeroValue(0.0, "This is a zero number double"))
                .withMessage("This is a zero number double");

        AtomicInteger integer = new AtomicInteger(5);
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> GenericValidator.validateNonZeroValue(integer, "Called validateNonZeroValue() with an unsupported numerical type. Supported types are: [Integer, Long, Float, Double]"))
                .withMessageContaining("[Integer, Long, Float, Double]");
    }


    //  ----------------- validateAcceptableYear -----------------


    @Test
    public void test_validateAcceptableYear_success() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> GenericValidator.validateAcceptableYear(1000000000, ""))
                .withMessage("The given year 1000000000 was higher than the maximum allowable 999999999");
    }
}
