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

    /**
     * Validates the given {@link File} is valid csv with a variable delimiter
     *
     * @param file {@link File}
     * @param delimiter delimiter
     * @return true if valid
     */
    public static boolean isValidCsvFile(final File file, final char delimiter) {

        if (file == null || !file.exists() || !file.isFile() || !file.getName().toLowerCase().endsWith(".csv")) {
            return false;
        }

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(file.getAbsolutePath()))) {
            String headerLine = reader.readLine();
            if (headerLine == null || headerLine.trim().isEmpty()) {
                return false;
            }

            String[] headers = headerLine.split(String.valueOf(delimiter));
            int columnCount = headers.length;

            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(String.valueOf(delimiter));
                if (values.length != columnCount) {
                    return false;
                }
            }
        } catch (IOException e) {
            return false;
        }

        return true;
    }
}
