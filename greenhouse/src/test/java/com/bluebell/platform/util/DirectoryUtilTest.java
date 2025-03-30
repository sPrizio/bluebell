package com.bluebell.platform.util;

import com.bluebell.platform.exceptions.system.DirectoryNotFoundException;
import com.bluebell.radicle.enums.DataSource;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Testing class for {@link DirectoryUtil}
 *
 * @author Stephen Prizio
 * @version 0.1.5
 */
class DirectoryUtilTest {


    //  ----------------- getDirectory -----------------

    @Test
    void test_getOutputDirectory_success() {

        assertThatExceptionOfType(DirectoryNotFoundException.class).isThrownBy(() -> DirectoryUtil.getOutputDirectory("test", false));

        final String directoryPath = DirectoryUtil.getOutputDirectory("test-output", true);
        File directory = new File(directoryPath);

        assertThat(directory).exists();
        assertThat(directory).isDirectory();

        directory.delete();
    }

    @Test
    void test_getOutputDirectory_fallbackToCom() {
        assertThatExceptionOfType(DirectoryNotFoundException.class).isThrownBy(() -> DirectoryUtil.getOutputDirectory("test", false));
    }


    //  ----------------- getBaseProjectDirectory -----------------

    @Test
    void test_getBaseProjectDirectory_success() {
        String basePath = DirectoryUtil.getBaseProjectDirectory();
        assertThat(basePath).contains("bluebell");
    }


    //  ----------------- getTestingResourcesDirectory -----------------

    @Test
    void test_getTestingResourcesDirectory_success() {
        String path = DirectoryUtil.getTestingResourcesDirectory();
        assertThat(path).contains("test");
        assertThat(path).contains("resources");
    }


    //  ----------------- getIngressDataRoot -----------------

    @Test
    void test_getIngressDataRoot_success() {
        assertThat(new File(DirectoryUtil.getIngressDataRoot("/ingress"))).exists();
        assertThat(new File(DirectoryUtil.getIngressDataRoot("/empty-ingress"))).doesNotExist();
    }


    //  ----------------- getIngressDataRootForDataSource -----------------

    @Test
    void test_getIngressDataRootForDataSource_success() {
        assertThat(new File(DirectoryUtil.getIngressDataRootForDataSource("/src/test/resources/copy-ingress", DataSource.METATRADER4))).exists();
        assertThat(new File(DirectoryUtil.getIngressDataRootForDataSource("/src/test/resources/empty-ingress", DataSource.METATRADER4))).doesNotExist();
    }
}
