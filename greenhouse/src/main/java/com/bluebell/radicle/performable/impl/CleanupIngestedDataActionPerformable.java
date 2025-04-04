package com.bluebell.radicle.performable.impl;

import com.bluebell.platform.models.core.nonentities.action.ActionData;
import com.bluebell.platform.util.DirectoryUtil;
import com.bluebell.radicle.performable.ActionPerformable;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Action that removes the processed content from the ingress data root
 *
 * @author Stephen Prizio
 * @version 0.1.5
 */
@Component("cleanupIngestedDataActionPerformable")
public class CleanupIngestedDataActionPerformable implements ActionPerformable {

    @Value("${bluebell.ingress.root}")
    private String dataRoot;


    //  METHODS

    @Override
    public ActionData perform() {
        try {
            final File processedDirectory = new File(String.format("%s%s%s", DirectoryUtil.getIngressDataRoot(this.dataRoot), File.separator, "/processed"));
            if (!processedDirectory.exists()) {
                return ActionData
                        .builder()
                        .success(true)
                        .data(true)
                        .logs("No data to cleanup.")
                        .build();
            } else {
                FileUtils.deleteDirectory(new File(String.format("%s%s%s", DirectoryUtil.getIngressDataRoot(this.dataRoot), File.separator, "/processed")));
                return ActionData
                        .builder()
                        .success(true)
                        .data(true)
                        .logs("Ingested data cleaned successfully")
                        .build();
            }
        } catch (Exception e) {
            return ActionData
                    .builder()
                    .success(false)
                    .data(false)
                    .logs(getStackTraceAsString(e))
                    .build();
        }
    }
}
