package com.bluebell.platform.util;

import com.bluebell.platform.exceptions.system.DirectoryNotFoundException;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility class for file and directory operations
 *
 * @author Stephen Prizio
 * @version 0.1.5
 */
@UtilityClass
public class DirectoryUtil {


    //  METHODS

    /**
     * Returns the root folder for the given folder name
     *
     * @return sample data path
     */
    public static String getOutputDirectory(final String root, final boolean create) {

        final String base = getBaseProjectDirectory();
        if (!new File(base).exists()) {
            throw new DirectoryNotFoundException(String.format("Base %s not found in project", base));
        }

        final String target = base + String.format("%s%s%s%s", File.separator, "target", File.separator, root);
        final File directory = new File(target);

        if (!directory.exists() && create) {
            directory.mkdirs();
        } else if (!directory.exists()) {
            throw new DirectoryNotFoundException(String.format("Directory %s not found in target %s", target, base));
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
        if (currentPath.endsWith("bluebell")) {
            return currentPath + File.separator + "greenhouse";
        }

        while (!currentPath.endsWith("greenhouse")) {
            currentPath = currentPath.getParent();
        }

        return currentPath.toAbsolutePath().toString();
    }

    /**
     * Returns the path to the base project's testing resources
     *
     * @return file path to the base project's testing resources
     */
    public static String getTestingResourcesDirectory() {
        return getBaseProjectDirectory() + String.format("%ssrc%stest%sresources", File.separator, File.separator, File.separator);
    }
}
