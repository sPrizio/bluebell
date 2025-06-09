package com.bluebell.radicle.scheduled.impl;

import com.bluebell.platform.enums.job.JobType;
import com.bluebell.platform.models.core.entities.action.impl.Action;
import com.bluebell.platform.models.core.entities.job.impl.Job;
import com.bluebell.radicle.performable.impl.RemoveOldJobsActionPerformable;
import com.bluebell.radicle.performable.impl.RemoveStaleJobsInProgressActionPerformable;
import com.bluebell.radicle.repositories.action.ActionRepository;
import com.bluebell.radicle.repositories.job.JobRepository;
import com.bluebell.radicle.scheduled.AbstractScheduledJob;
import com.bluebell.radicle.scheduled.GenericScheduledJob;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Job that runs once per week to clean up any stale {@link Job}s
 *
 * @author Stephen Prizio
 * @version 0.2.4
 */
@Slf4j
@Component
public class CleanupJobsScheduledJob extends AbstractScheduledJob implements GenericScheduledJob<Enum<JobType>> {

    @Resource(name = "actionRepository")
    private ActionRepository actionRepository;

    @Resource(name = "jobRepository")
    private JobRepository jobRepository;

    @Resource(name = "removeOldJobsActionPerformable")
    private RemoveOldJobsActionPerformable removeOldJobsActionPerformable;

    @Resource(name = "removeStaleJobsInProgressActionPerformable")
    private RemoveStaleJobsInProgressActionPerformable removeStaleJobsInProgressActionPerformable;


    //  METHODS

    @Override
    @Async
    @Scheduled(cron = "0 0 16 * * 6")
    public void execute() {

        checkForConcurrentJob(JobType.CLEANUP_STALE_JOBS);

        Action cleanupInProgressAction = Action
                .builder()
                .priority(1)
                .name(String.format("CleanupInProgressJobsAction_%s", LocalDateTime.now()))
                .displayName("Cleanup jobs in progress")
                .performableAction(this.removeStaleJobsInProgressActionPerformable)
                .build();

        cleanupInProgressAction = this.actionRepository.save(cleanupInProgressAction);

        Action cleanupOldJobsActions = Action
                .builder()
                .priority(2)
                .name(String.format("CleanupOldJobsAction_%s", LocalDateTime.now()))
                .displayName("Cleanup old/stale jobs")
                .performableAction(this.removeOldJobsActionPerformable)
                .build();

        cleanupOldJobsActions = this.actionRepository.save(cleanupOldJobsActions);

        Job cleanupJobs = Job
                .builder()
                .type(JobType.CLEANUP_STALE_JOBS)
                .name(String.format("CleanupStaleJobsJob_%s", LocalDateTime.now()))
                .displayName("Cleanup stale jobs")
                .build();

        cleanupJobs = this.jobRepository.save(cleanupJobs);

        cleanupInProgressAction.setJob(cleanupJobs);
        cleanupOldJobsActions.setJob(cleanupJobs);

        cleanupJobs.addAction(cleanupInProgressAction);
        cleanupJobs.addAction(cleanupOldJobsActions);

        this.jobRepository.save(cleanupJobs);
        this.actionRepository.save(cleanupInProgressAction);
        this.actionRepository.save(cleanupOldJobsActions);

        executeJobAndHandleResult(cleanupJobs);
    }

    @Override
    public Enum<JobType> getJobType() {
        return JobType.CLEANUP_STALE_JOBS;
    }
}
