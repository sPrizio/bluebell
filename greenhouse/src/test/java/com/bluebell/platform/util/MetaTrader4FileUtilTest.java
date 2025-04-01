package com.bluebell.platform.util;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testing class for {@link MetaTrader4FileUtilTest}
 *
 * @author Stephen Prizio
 * @version 0.1.5
 */
class MetaTrader4FileUtilTest {


    //  ----------------- isValidCsvFile -----------------

    @Test
    void test_isValidCsvFile_success() {
        assertThat(MetaTrader4FileUtil.isValidCsvFile(null, ',')).isFalse();
        assertThat(MetaTrader4FileUtil.isValidCsvFile(new File("asdasdada"), ',')).isFalse();
        assertThat(MetaTrader4FileUtil.isValidCsvFile(new File(String.format("%s%s%s%s%s", DirectoryUtil.getTestingResourcesDirectory(), File.separator, "copy-ingress", File.separator, "15825.zip")), ',')).isFalse();
        assertThat(MetaTrader4FileUtil.isValidCsvFile(new File(String.format("%s%s%s%s%s", DirectoryUtil.getTestingResourcesDirectory(), File.separator, "copy-ingress", File.separator, "mt4/NDAQ100/THIRTY_MINUTE/NDAQ10030_with_malformed_data.csv")), ',')).isFalse();
        assertThat(MetaTrader4FileUtil.isValidCsvFile(new File(String.format("%s%s%s%s%s", DirectoryUtil.getTestingResourcesDirectory(), File.separator, "copy-ingress", File.separator, "mt4/NDAQ100/THIRTY_MINUTE/NDAQ10030_with_small_row.csv")), ',')).isFalse();
        assertThat(MetaTrader4FileUtil.isValidCsvFile(new File(String.format("%s%s%s%s%s", DirectoryUtil.getTestingResourcesDirectory(), File.separator, "copy-ingress", File.separator, "mt4/NDAQ100/THIRTY_MINUTE/NDAQ10030.csv")), ',')).isTrue();
    }
}
