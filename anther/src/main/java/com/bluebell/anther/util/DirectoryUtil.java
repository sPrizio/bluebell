package com.bluebell.anther.util;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.net.URL;
import java.util.Objects;

/**
 * Utility class for file and directory operations
 *
 * @author Stephen Prizio
 * @version 0.0.1
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
}
