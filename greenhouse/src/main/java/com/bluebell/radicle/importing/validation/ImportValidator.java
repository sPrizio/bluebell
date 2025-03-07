package com.bluebell.radicle.importing.validation;

import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.radicle.importing.exceptions.FileExtensionNotSupportedException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;

/**
 * Validator class for the import package
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
public class ImportValidator {

    private ImportValidator() {
        throw new UnsupportedOperationException(String.format(CorePlatformConstants.NO_INSTANTIATION, getClass().getName()));
    }


    //  METHODS

    /**
     * Validates that the given {@link MultipartFile}'s extension matches the expected one
     *
     * @param file               {@link MultipartFile}
     * @param expectedExtensions expected extension (.docx, .txt, etc...)
     * @param message            error message
     * @param values             error message values
     * @throws FileExtensionNotSupportedException if file extension is not supported
     */
    public static void validateImportFileExtension(final MultipartFile file, final String[] expectedExtensions, final String message, final Object... values) {

        if (expectedExtensions != null) {
            Arrays.stream(expectedExtensions).filter(expectedExtension -> FilenameUtils.getExtension(file.getOriginalFilename()).equals(expectedExtension.replace(".", ""))).findFirst().orElseThrow(() -> new FileExtensionNotSupportedException(String.format(message, values)));
        }
    }

    /**
     * Validates that the given {@link File}'s extension matches the expected one
     *
     * @param file               {@link File}
     * @param expectedExtensions expected extension (.docx, .txt, etc...)
     * @param message            error message
     * @param values             error message values
     * @throws FileExtensionNotSupportedException is file extension is not supported
     */
    public static void validateImportFileExtension(final File file, final String[] expectedExtensions, final String message, final Object... values) {

        if (expectedExtensions != null) {
            Arrays.stream(expectedExtensions).filter(expectedExtension -> FilenameUtils.getExtension(file.getName()).equals(expectedExtension.replace(".", ""))).findFirst().orElseThrow(() -> new FileExtensionNotSupportedException(String.format(message, values)));
        }
    }
}
