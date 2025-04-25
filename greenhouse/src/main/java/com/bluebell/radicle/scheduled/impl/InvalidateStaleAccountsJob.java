package com.bluebell.radicle.scheduled.impl;

import com.bluebell.platform.enums.job.JobType;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.entities.action.impl.Action;
import com.bluebell.platform.models.core.entities.job.impl.Job;
import com.bluebell.radicle.performable.impl.InvalidateStaleAccountsActionPerformable;
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
 * Job that runs once per week to clean up any stale {@link Account}s
 *
 * @author Stephen Prizio
 * @version 0.1.8
 */
@Slf4j
@Component
public class InvalidateStaleAccountsJob extends AbstractScheduledJob implements GenericScheduledJob<Enum<JobType>> {

    @Resource(name = "actionRepository")
    private ActionRepository actionRepository;

    @Resource(name = "invalidateStaleAccountsActionPerformable")
    private InvalidateStaleAccountsActionPerformable invalidateStaleAccountsActionPerformable;

    @Resource(name = "jobRepository")
    private JobRepository jobRepository;


    //  METHODS

    @Override
    @Async
    @Scheduled(cron = "0 0 3 * * 6")
    public void execute() {

        checkForConcurrentJob(JobType.INVALIDATE_STALE_ACCOUNTS);

        Action invalidateStaleAccountsAction = Action
                .builder()
                .priority(1)
                .name(String.format("InvalidateStaleAccountsAction_%s", LocalDateTime.now()))
                .performableAction(this.invalidateStaleAccountsActionPerformable)
                .build();

        invalidateStaleAccountsAction = this.actionRepository.save(invalidateStaleAccountsAction);

        Job invalidateStaleAccountsJob = Job
                .builder()
                .type(JobType.INVALIDATE_STALE_ACCOUNTS)
                .name(String.format("InvalidateStaleAccountsJob_%s", LocalDateTime.now()))
                .build();

        invalidateStaleAccountsJob = this.jobRepository.save(invalidateStaleAccountsJob);

        invalidateStaleAccountsAction.setJob(invalidateStaleAccountsJob);

        invalidateStaleAccountsJob.addAction(invalidateStaleAccountsAction);

        this.jobRepository.save(invalidateStaleAccountsJob);
        this.actionRepository.save(invalidateStaleAccountsAction);

        executeJobAndHandleResult(invalidateStaleAccountsJob);
    }

    @Override
    public Enum<JobType> getJobType() {
        return JobType.INVALIDATE_STALE_ACCOUNTS;
    }
}
