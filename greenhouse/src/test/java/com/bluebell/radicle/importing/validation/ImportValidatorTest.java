package com.bluebell.radicle.importing.validation;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.bluebell.radicle.importing.exceptions.FileExtensionNotSupportedException;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

/**
 * Testing class for {@link ImportValidator}
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
public class ImportValidatorTest {

    final String[] formats = new String[]{".csv"};


    //  ----------------- validateImportFileExtension -----------------

    @Test
    public void test_validateImportFileExtension_multipart_success() {
        MockMultipartFile testFile = new MockMultipartFile("file", "hello.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());

        assertThatExceptionOfType(FileExtensionNotSupportedException.class)
                .isThrownBy(() -> ImportValidator.validateImportFileExtension(testFile, formats, "This is an empty multipart file test"))
                .withMessage("This is an empty multipart file test");
    }

    @Test
    public void test_validateImportFileExtension_success() {
        File file = new File("test.txt");
        assertThatExceptionOfType(FileExtensionNotSupportedException.class)
                .isThrownBy(() -> ImportValidator.validateImportFileExtension(file, formats, "This is an empty file test"))
                .withMessage("This is an empty file test");
        file.delete();
    }
}
