package com.bluebell.anther.services.reporting;

import com.bluebell.anther.strategies.impl.Bloom;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.net.URL;
import java.util.Objects;

/**
 * Reporting services are designed to take data from around anther and generate human-readable
 * reports. These can take the form of text files, excel sheets, etc.
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
public interface ReportingService {

    /**
     * Returns the root folder for reports
     *
     * @return sample data path
     */
    default String getDataRoot(final String root) {

        URL result = getClass().getClassLoader().getResource(root);
        if (result == null) {
            result = getClass().getClassLoader().getResource("com");
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
