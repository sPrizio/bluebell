package com.bluebell.platform.util;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Testing class for {@link FileUtil}
 *
 * @author Stephen Prizio
 * @version 0.1.5
 */
class FileUtilTest {


    //  ----------------- unzipFile -----------------

    @Test
    void test_unzipFile_success() throws IOException {
        assertThatExceptionOfType(FileNotFoundException.class)
                .isThrownBy(() -> FileUtil.unzipFile("tadhajkshd", "asdasdad"))
                .withMessageContaining("file not found");

        final File file1 = FileUtil.unzipFile(
                String.format("%s%s%s%s%s", DirectoryUtil.getTestingResourcesDirectory(), File.separator, "copy-ingress", File.separator, "15825.zip"),
                String.format("%s%s%s", DirectoryUtil.getTestingResourcesDirectory(), File.separator, "copy-ingress")
        );

        final File file2 = FileUtil.unzipFile(
                String.format("%s%s%s%s%s", DirectoryUtil.getTestingResourcesDirectory(), File.separator, "copy-ingress", File.separator, "15825.zip"),
                String.format("%s%s%s", DirectoryUtil.getTestingResourcesDirectory(), File.separator, "copy-ingress/dirOne/dirTwo")
        );

        assertThat(file1).exists();
        assertThat(file2).exists();

        Files.delete(new File(file1.getAbsolutePath() + File.separator + "NDX_full_1day.txt").toPath());
        FileUtils.deleteDirectory(file2.getParentFile());
    }


    //  ----------------- isValidCsvFile -----------------

    @Test
    void test_isValidCsvFile_success() {
        assertThat(FileUtil.isValidCsvFile(null, ',')).isFalse();
        assertThat(FileUtil.isValidCsvFile(new File("asdasdada"), ',')).isFalse();
        assertThat(FileUtil.isValidCsvFile(new File(String.format("%s%s%s%s%s", DirectoryUtil.getTestingResourcesDirectory(), File.separator, "copy-ingress", File.separator, "15825.zip")), ',')).isFalse();
        assertThat(FileUtil.isValidCsvFile(new File(String.format("%s%s%s%s%s", DirectoryUtil.getTestingResourcesDirectory(), File.separator, "copy-ingress", File.separator, "firstratedata/NDX/ONE_DAY/NDX_1day_sample_with_csv_errors.csv")), ',')).isFalse();
        assertThat(FileUtil.isValidCsvFile(new File(String.format("%s%s%s%s%s", DirectoryUtil.getTestingResourcesDirectory(), File.separator, "copy-ingress", File.separator, "firstratedata/NDX/ONE_DAY/NDX_1day_sample.csv")), ',')).isTrue();
    }
}
