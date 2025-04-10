package com.bluebell.platform.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mockStatic;

/**
 * Testing class for {@link OSUtil}
 *
 * @author Stephen Prizio
 * @version 0.1.6
 */
class OSUtilTest {

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
        Files.createFile(this.tempDir.resolve(".env.dev"));
    }


    //  ----------------- generateEnv -----------------

    @Test
    void test_generateEnv_missingEnvFile() {
        try (MockedStatic<DirectoryUtil> directoryUtilMock = mockStatic(DirectoryUtil.class)) {
            directoryUtilMock.when(DirectoryUtil::getBaseProjectDirectory).thenReturn(this.tempDir.toString());

            assertThatExceptionOfType(IllegalStateException.class)
                    .isThrownBy(() -> OSUtil.generateEnv(new String[]{"--os-profile=staging"}))
                    .withMessageContaining(".env.staging not found!");
        }
    }

    @Test
    void test_generateEnv_noargs() {
        try (MockedStatic<DirectoryUtil> directoryUtilMock = mockStatic(DirectoryUtil.class)) {
            directoryUtilMock.when(DirectoryUtil::getBaseProjectDirectory).thenReturn(this.tempDir.toString());

            OSUtil.generateEnv(new String[]{""});
            final File copiedFile = this.tempDir.resolve(".env").toFile();

            assertThat(copiedFile).exists();
        }
    }

    @Test
    void test_generateEnv_missingArg() {
        try (MockedStatic<DirectoryUtil> directoryUtilMock = mockStatic(DirectoryUtil.class)) {
            directoryUtilMock.when(DirectoryUtil::getBaseProjectDirectory).thenReturn(this.tempDir.toString());

            OSUtil.generateEnv(new String[]{"--test-arg=true"});
            final File copiedFile = this.tempDir.resolve(".env").toFile();

            assertThat(copiedFile).exists();
        }
    }

    @Test
    void test_generateEnv_success() {
        try (MockedStatic<DirectoryUtil> directoryUtilMock = mockStatic(DirectoryUtil.class)) {
            directoryUtilMock.when(DirectoryUtil::getBaseProjectDirectory).thenReturn(this.tempDir.toString());

            OSUtil.generateEnv(new String[]{"--os-profile=dev"});
            final File copiedFile = this.tempDir.resolve(".env").toFile();

            assertThat(copiedFile).exists();
        }
    }
}
