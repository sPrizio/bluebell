package com.bluebell.anther.services.reporting.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.bluebell.anther.models.metadata.MetaData;
import com.bluebell.anther.services.reporting.ReportingService;
import com.bluebell.platform.util.DirectoryUtil;

/**
 * Service that allows reports to be generated for strategy results
 *
 * @author Stephen Prizio
 * @version 0.1.0
 */
public class MetaDataReportingService implements ReportingService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMMM d yyyy");


    //  METHODS

    /**
     * Generates a meta data report to a text file
     *
     * @param metaData {@link List} of {@link MetaData}
     */
    public void generateMetadataReport(final List<MetaData> metaData) {

        final StringBuilder stringBuilder = new StringBuilder();
        for (final MetaData md : metaData) {
            final File tempFile = new File(DirectoryUtil.getOutputDirectory(String.format("output%smetadata%s", File.separator, File.separator), "anther", true) + String.format("metadata-%s.txt", md.getStart().format(DateTimeFormatter.ISO_DATE)));
            try (FileOutputStream os = new FileOutputStream(tempFile)) {
                stringBuilder
                        .append("Period: ").append(md.getStart().format(DATE_FORMATTER)).append(" to ").append(md.getEnd().format(DATE_FORMATTER))
                        .append("\n")
                        .append("\n");

                stringBuilder
                        .append(md)
                        .append("\n");

                os.write(stringBuilder.toString().getBytes());
                stringBuilder.setLength(0);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }

}
