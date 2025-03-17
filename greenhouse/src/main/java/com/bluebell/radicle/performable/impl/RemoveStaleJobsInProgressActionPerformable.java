package com.bluebell.radicle.performable.impl;

import com.bluebell.platform.models.core.nonentities.action.ActionData;
import com.bluebell.radicle.performable.ActionPerformable;
import com.bluebell.radicle.services.job.JobService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * Action that removes jobs that might be stuck in an in-progress state or just bad data
 *
 * @author Stephen Prizio
 * @version 0.1.2
 */
@Component("removeStaleJobsInProgressActionPerformable")
public class RemoveStaleJobsInProgressActionPerformable implements ActionPerformable {

    @Resource(name = "jobService")
    private JobService jobService;


    //  METHODS

    @Override
    public ActionData perform() {
        try {
            final int count = this.jobService.deleteStaleInProgressJobs();
            if (count > 0) {
                return ActionData
                        .builder()
                        .success(true)
                        .data(String.format("%d stale jobs in progress deleted.", count))
                        .build();
            } else {
                return ActionData
                        .builder()
                        .success(true)
                        .data("No stale jobs were found.")
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
