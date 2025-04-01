package com.bluebell.platform.util;

import com.bluebell.platform.constants.CorePlatformConstants;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

/**
 * Utility class for file system operations specific to MetaTrader4 files
 *
 * @author Stephen Prizio
 * @version 0.1.5
 */
@Slf4j
@UtilityClass
public class MetaTrader4FileUtil {


    //  METHODS

    /**
     * Validates the given {@link File} is valid csv with a variable delimiter
     *
     * @param file {@link File}
     * @param delimiter delimiter
     * @return true if valid
     */
    public static boolean isValidCsvFile(final File file, final char delimiter) {

        if (!FileUtil.isValidCsvFile(file, delimiter)) {
            return false;
        }

        try (Stream<String> lines = Files.lines(file.toPath())) {
            return lines.allMatch(l -> validateLineOfData(l, delimiter));
        } catch (IOException e) {
            LOGGER.error("Error during data validation {}", e.getMessage(), e);
            return false;
        }
    }


    //  HELPERS

    /**
     * Validates the line of data
     *
     * @param string line
     * @param delimiter delimiter
     * @return true if data is as expected
     */
    private boolean validateLineOfData(final String string, final char delimiter) {

        if (StringUtils.isEmpty(string)) {
            LOGGER.error("Line was empty or blank");
            return false;
        }

        final String[] elements = string.split(String.valueOf(delimiter));
        if (elements.length < 7) {
            LOGGER.error("Line {} was not of the valid length, data was missing", string);
            return false;
        }

        try {
            LocalDate.parse(elements[0], DateTimeFormatter.ofPattern(CorePlatformConstants.MT4_DATE_FORMAT));
            LocalTime.parse(elements[1], DateTimeFormatter.ofPattern(CorePlatformConstants.MT4_TIME_FORMAT));
            Double.parseDouble(elements[2]);
            Double.parseDouble(elements[3]);
            Double.parseDouble(elements[4]);
            Double.parseDouble(elements[5]);
            Long.parseLong(elements[6]);

            return true;
        } catch (Exception e) {
            LOGGER.error("Error occurred during data validation {}", e.getMessage(), e);
            return false;
        }
    }
}
