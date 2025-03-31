package com.bluebell.platform.util;

import lombok.experimental.UtilityClass;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Utility class for File system operations
 *
 * @author Stephen Prizio
 * @version 0.1.5
 */
@UtilityClass
public class FileUtil {


    //  METHODS

    /**
     * Unzips the file to the given destination directory
     *
     * @param pathToZip path to zip
     * @param destinationDirectory destination
     * @return directory containing the zipped file
     * @throws IOException io exception
     */
    public static File unzipFile(final String pathToZip, final String destinationDirectory) throws IOException {

        Path zipFilePath = Paths.get(pathToZip);
        if (!Files.exists(zipFilePath)) {
            throw new FileNotFoundException(String.format("%s file not found", pathToZip));
        }

        Path destinationDirPath = Paths.get(destinationDirectory);
        Files.createDirectories(destinationDirPath);

        try (ZipInputStream zip = new ZipInputStream(new FileInputStream(zipFilePath.toFile()))) {
            ZipEntry zipEntry;
            while ((zipEntry = zip.getNextEntry()) != null) {
                Path newFilePath = destinationDirPath.resolve(zipEntry.getName());

                if (zipEntry.isDirectory()) {
                    Files.createDirectories(newFilePath);
                } else {
                    Files.createDirectories(newFilePath.getParent());

                    try (OutputStream fos = Files.newOutputStream(newFilePath)) {
                        zip.transferTo(fos);
                    }
                }

                zip.closeEntry();
            }
        }

        return destinationDirPath.toFile();
    }
}
