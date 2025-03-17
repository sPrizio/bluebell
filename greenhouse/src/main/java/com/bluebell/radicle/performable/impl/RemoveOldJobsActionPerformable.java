package com.bluebell.radicle.performable.impl;

import com.bluebell.platform.models.core.nonentities.action.ActionData;
import com.bluebell.radicle.performable.ActionPerformable;
import com.bluebell.radicle.services.job.JobService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * Removes old jobs that are longer than a certain period
 *
 * @author Stephen Prizio
 * @version 0.1.2
 */
@Component("removeOldJobsActionPerformable")
public class RemoveOldJobsActionPerformable implements ActionPerformable {

    @Resource(name = "jobService")
    private JobService jobService;


    //  METHODS

    @Override
    public ActionData perform() {
        try {
            final int count = this.jobService.deleteOldJobs();
            if (count > 0) {
                return ActionData
                        .builder()
                        .success(true)
                        .data(String.format("%d old jobs deleted.", count))
                        .build();
            } else {
                return ActionData
                        .builder()
                        .success(true)
                        .data("No old jobs were found.")
                        .build();
            }
        } catch (Exception e) {
            return ActionData
                    .builder()
                    .success(false)
                    .logs(getStackTraceAsString(e))
                    .build();
        }
    }
}
