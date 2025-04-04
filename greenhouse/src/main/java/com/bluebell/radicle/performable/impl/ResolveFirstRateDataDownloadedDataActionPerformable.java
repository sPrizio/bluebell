package com.bluebell.radicle.performable.impl;

import com.bluebell.platform.enums.time.MarketPriceTimeInterval;
import com.bluebell.platform.models.core.nonentities.action.ActionData;
import com.bluebell.platform.util.DirectoryUtil;
import com.bluebell.platform.util.FileUtil;
import com.bluebell.radicle.enums.DataSource;
import com.bluebell.radicle.performable.ActionPerformable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipFile;

/**
 * Action that, when performed, unzips and organizes FirstRateData files
 *
 * @author Stephen Prizio
 * @version 0.1.5
 */
@Slf4j
@Component("resolveFirstRateDataDownloadedDataActionPerformable")
public class ResolveFirstRateDataDownloadedDataActionPerformable implements ActionPerformable {

    @Value("${bluebell.ingress.root}")
    private String ingressRoot;


    //  METHODS

    @Override
    public ActionData perform() {

        final File ingressDirectory = new File(String.format("%s%s%s", DirectoryUtil.getBaseProjectDirectory(), File.separator, this.ingressRoot));
        final File[] files = ingressDirectory.listFiles();
        if (files == null || files.length == 0) {
            LOGGER.warn("Nothing to unzip");
            return ActionData
                    .builder()
                    .success(true)
                    .logs("Nothing to resolve")
                    .build();
        }

        final Set<File> ingressFiles = Arrays.stream(files).collect(Collectors.toSet());
        final List<File> zipFiles = Arrays.stream(files).filter(this::isZipFile).toList();

        if (CollectionUtils.isEmpty(zipFiles)) {
            LOGGER.warn("Nothing to unzip");
            return ActionData
                    .builder()
                    .success(true)
                    .logs("Nothing to resolve")
                    .build();
        }

        zipFiles.forEach(zipFile -> {
            try {
                FileUtil.unzipFile(zipFile.getAbsolutePath(), ingressDirectory.getAbsolutePath());
                FileUtils.delete(zipFile);
            } catch (IOException e) {
                LOGGER.error("Could not unzip file {}", zipFile.getName(), e);
            }
        });

        final Set<File> newZippedFiles = new HashSet<>();
        Arrays.stream(Objects.requireNonNull(ingressDirectory.listFiles())).forEach(file -> {
            if (!ingressFiles.contains(file)) {
                newZippedFiles.add(file);
            }
        });

        newZippedFiles.forEach(f -> {
            final String[] elements = f.getName().substring(0, f.getName().indexOf('.')).split("_");
            if (elements.length > 2) {
                final StringBuilder path =
                        new StringBuilder()
                                .append(DirectoryUtil.getBaseProjectDirectory())
                                .append(File.separator)
                                .append(this.ingressRoot)
                                .append(File.separator)
                                .append(DataSource.FIRST_RATE_DATA.getDataRoot())
                                .append(File.separator)
                                .append(elements[0])
                                .append(File.separator)
                                .append(MarketPriceTimeInterval.getForFirstRateDataLabel(elements[2]).getCode())
                                .append(File.separator)
                                .append(f.getName());

                try {
                    FileUtils.copyFile(f, new File(path.toString()));
                    FileUtils.delete(f);
                } catch (IOException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        });

        return ActionData
                .builder()
                .success(true)
                .logs("Data successfully resolved and organized")
                .data(true)
                .build();
    }


    //  HELPERS

    /**
     * Determines whether the given {@link File} is a zip file
     *
     * @param file {@link File}
     * @return true if archived
     */
    private boolean isZipFile(final File file) {
        try (ZipFile ignored = new ZipFile(file)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
