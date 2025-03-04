package com.bluebell.platform.util;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.bluebell.platform.exceptions.system.DirectoryNotFoundException;

/**
 * Utility class for file and directory operations
 *
 * @author Stephen Prizio
 * @version 0.1.0
 */
public class DirectoryUtil {

    private DirectoryUtil() {
        throw new UnsupportedOperationException(String.format("%s classes should not be instantiated", getClass().getName()));
    }


    //  METHODS

    /**
     * Returns the root folder for the given folder name
     *
     * @return sample data path
     */
    public static String getOutputDirectory(final String root, final String module, final boolean create) {

        final String base = getBaseProjectDirectory() + File.separator + module;
        if (!new File(base).exists()) {
            throw new DirectoryNotFoundException(String.format("Module %s not found in project", module));
        }

        final String target = base + String.format("%s%s%s%s", File.separator, "target", File.separator, root);
        final File directory = new File(target);

        if (!directory.exists() && create) {
            directory.mkdirs();
        } else if (!directory.exists()) {
            throw new DirectoryNotFoundException(String.format("Directory %s not found in module %s", target, module));
        }

        return directory.getAbsolutePath();
    }

    /**
     * Returns a path to the base project directory
     *
     * @return file path to base project directory
     */
    public static String getBaseProjectDirectory() {

        Path currentPath = Paths.get(new File("").getAbsoluteFile().getPath());
        while (!currentPath.endsWith("greenhouse")) {
            currentPath = currentPath.getParent();
        }

        return currentPath.toAbsolutePath().toString();
    }
}
