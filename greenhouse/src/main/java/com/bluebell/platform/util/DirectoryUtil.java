package com.bluebell.platform.util;

import com.bluebell.platform.exceptions.system.DirectoryNotFoundException;
import com.bluebell.radicle.enums.DataSource;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility class for file and directory operations
 *
 * @author Stephen Prizio
 * @version 1.0.0
 */
@Slf4j
@UtilityClass
public class DirectoryUtil {


    //  METHODS

    /**
     * Returns the output directory
     *
     * @return output directory path
     */
    public static String getOutputDirectory() {
        return getBaseProjectDirectory() + String.format("%s%s", File.separator, "output");
    }

    /**
     * Returns the root folder for the given folder name
     *
     * @return sample data path
     */
    public static String getTargetDirectory(final String root, final boolean create) {

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

        Path currentPath = Paths.get("").toAbsolutePath();
        if (Files.exists(currentPath.resolve("pom.xml"))) {
            return currentPath.toString();
        }

        Path possibleSubdir = currentPath.resolve("greenhouse");
        if (Files.exists(possibleSubdir.resolve("pom.xml"))) {
            return possibleSubdir.toString();
        }

        Path pathToCheck = currentPath.getParent();
        while (pathToCheck != null) {
            if (Files.exists(pathToCheck.resolve("pom.xml"))) {
                return pathToCheck.toString();
            }

            pathToCheck = pathToCheck.getParent();
        }

        throw new IllegalStateException("Unable to find base project directory with pom.xml");
    }


    /**
     * Returns the path to the base project's testing resources
     *
     * @return file path to the base project's testing resources
     */
    public static String getTestingResourcesDirectory() {
        return getBaseProjectDirectory() + String.format("%ssrc%stest%sresources", File.separator, File.separator, File.separator);
    }

    /**
     * Returns the path to the ingress root
     *
     * @param dataRoot ingress root
     * @return path
     */
    public static String getIngressDataRoot(final String dataRoot) {
        return getBaseProjectDirectory() + String.format("%s%s", File.separator, dataRoot);
    }

    /**
     * Returns the path to a particular {@link DataSource}'s data root
     *
     * @param dataRoot   ingress root
     * @param dataSource {@link DataSource}
     * @return path
     */
    public static String getIngressDataRootForDataSource(final String dataRoot, final DataSource dataSource) {
        return getBaseProjectDirectory() + String.format("%s%s%s%s", File.separator, dataRoot, File.separator, dataSource.getDataRoot());
    }
}
