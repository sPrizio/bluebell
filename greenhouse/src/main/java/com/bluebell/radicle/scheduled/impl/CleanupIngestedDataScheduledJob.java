package com.bluebell.radicle.scheduled.impl;

import com.bluebell.platform.enums.job.JobType;
import com.bluebell.platform.models.core.entities.action.impl.Action;
import com.bluebell.platform.models.core.entities.job.impl.Job;
import com.bluebell.radicle.performable.impl.CleanupIngestedDataActionPerformable;
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
 * Job that runs once per week to clean up the processed ingress archive
 *
 * @author Stephen Prizio
 * @version 0.2.4
 */
@Slf4j
@Component
public class CleanupIngestedDataScheduledJob extends AbstractScheduledJob implements GenericScheduledJob<Enum<JobType>> {

    @Resource(name = "actionRepository")
    private ActionRepository actionRepository;

    @Resource(name = "cleanupIngestedDataActionPerformable")
    private CleanupIngestedDataActionPerformable cleanupIngestedDataActionPerformable;

    @Resource(name = "jobRepository")
    private JobRepository jobRepository;


    //  METHODS

    @Override
    @Async
    @Scheduled(cron = "0 0 2 * * 6")
    public void execute() {

        checkForConcurrentJob(JobType.CLEANUP_INGESTED_DATA);

        Action cleanupIngestedDataAction = Action
                .builder()
                .priority(1)
                .name(String.format("CleanupIngestedDataAction_%s", LocalDateTime.now()))
                .displayName("Cleanup ingested data")
                .performableAction(this.cleanupIngestedDataActionPerformable)
                .build();

        cleanupIngestedDataAction = this.actionRepository.save(cleanupIngestedDataAction);

        Job cleanupJob = Job
                .builder()
                .type(JobType.CLEANUP_INGESTED_DATA)
                .name(String.format("CleanupIngestedDataJob_%s", LocalDateTime.now()))
                .displayName("Cleanup ingested data job")
                .build();

        cleanupJob = this.jobRepository.save(cleanupJob);
        cleanupIngestedDataAction.setJob(cleanupJob);
        cleanupJob.addAction(cleanupIngestedDataAction);

        this.jobRepository.save(cleanupJob);
        this.actionRepository.save(cleanupIngestedDataAction);

        executeJobAndHandleResult(cleanupJob);
    }

    @Override
    public Enum<JobType> getJobType() {
        return JobType.CLEANUP_INGESTED_DATA;
    }
}
