package com.bluebell.planter.integration.translators.forexfactory;

import com.bluebell.planter.AbstractGenericTest;
import com.bluebell.planter.integration.models.dto.forexfactory.CalendarNewsDayEntryDTO;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testing class for {@link }
 *
 * @author Stephen Prizio
 * @version 0.0.4
 */
public class CalendarNewsDayEntryTranslatorTest extends AbstractGenericTest {

    private final CalendarNewsDayEntryTranslator calendarNewsDayEntryTranslator = new CalendarNewsDayEntryTranslator();


    //  ----------------- translate -----------------

    @Test
    public void test_translate_empty() {

        CalendarNewsDayEntryDTO dayEntryDTO = this.calendarNewsDayEntryTranslator.translate(null);

        assertThat(dayEntryDTO)
                .isNotNull();

        assertThat(dayEntryDTO.isEmpty())
                .isTrue();
    }

    @Test
    public void test_translate_success() {
        assertThat(this.calendarNewsDayEntryTranslator.translate(generateCalendarNewsEntryResponse()))
                .isNotNull();
    }
}
