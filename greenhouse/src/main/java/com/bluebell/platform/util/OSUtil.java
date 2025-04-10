package com.bluebell.platform.util;

import com.bluebell.platform.constants.CorePlatformConstants;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * System-level utility classes for operating system operations and jdk stuff
 *
 * @author Stephen Prizio
 * @version 0.1.6
 */
@Slf4j
@UtilityClass
public class OSUtil {


    //  METHODS

    /**
     * Generates the .env for the given active profile
     *
     * @param args runtime arguments
     */
    public void generateEnv(final String[] args) {

        final File env = new File(String.format("%s%s.env.%s", DirectoryUtil.getBaseProjectDirectory(), File.separator, getOSProfile(args)));
        if (!env.exists()) {
            throw new IllegalStateException(String.format("%s not found!", env.getName()));
        }

        LOGGER.info("Copying {} to .env", env.getName());
        try {
            Files.copy(env.toPath(), env.toPath().resolveSibling(".env"), StandardCopyOption.REPLACE_EXISTING);
            LOGGER.info("{} copied to .env", env.getName());
        } catch (IOException e) {
            LOGGER.error("Could not copy to .env file: {}", e.getMessage(), e);
            System.exit(1);
        }
    }


    //  HELPERS

    /**
     * Obtains the os profile runtime argument. Will default to dev if it is not found
     *
     * @param args java runtime arguments
     * @return os profile value
     */
    private static String getOSProfile(final String[] args) {

        if (args == null) {
            return "dev";
        }

        for (final String arg : args) {
            if (arg.startsWith(CorePlatformConstants.OS_PROFILE_ARGUMENT)) {
                return arg.split("=")[1];
            }
        }

        return "dev";
    }
}
