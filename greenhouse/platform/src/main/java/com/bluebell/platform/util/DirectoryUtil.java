package com.bluebell.platform.util;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * Utility class for file and directory operations
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
public class DirectoryUtil {

    private DirectoryUtil() {
        throw new UnsupportedOperationException(String.format("%s classes should not be instantiated", getClass().getName()));
    }


    //  METHODS

    /**
     * Returns the root folder for reports
     *
     * @return sample data path
     */
    public static String getDirectory(final String root) {

        URL result = DirectoryUtil.class.getClassLoader().getResource(root);
        if (result == null) {
            result = DirectoryUtil.class.getClassLoader().getResource("com");
        }

        String directoryResult = Objects.requireNonNull(result).getFile().replace("/com", StringUtils.EMPTY);
        if (!directoryResult.contains(root)) {
            directoryResult += String.format("/%s/", root);
        }

        final File directory = new File(directoryResult);
        if (!directory.exists()){
            directory.mkdirs();
        }

        return directoryResult;
    }

    /**
     * Returns a path to the base project directory
     *
     * @return file path to base project directory
     */
    public static String getBaseProjectDirectory() {

        Path currentPath = Paths.get(new File("").getAbsoluteFile().getPath());
        while (!currentPath.endsWith("bluebell")) {
            currentPath = currentPath.getParent();
        }

        return currentPath.toAbsolutePath().toString();
    }
}
