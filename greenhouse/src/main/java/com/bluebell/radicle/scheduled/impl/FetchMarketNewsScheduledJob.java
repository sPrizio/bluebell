package com.bluebell.radicle.scheduled.impl;

import com.bluebell.platform.enums.job.JobStatus;
import com.bluebell.platform.enums.job.JobType;
import com.bluebell.platform.models.core.entities.action.impl.Action;
import com.bluebell.platform.models.core.entities.job.impl.Job;
import com.bluebell.platform.models.core.entities.job.impl.JobResult;
import com.bluebell.platform.models.core.entities.news.MarketNews;
import com.bluebell.radicle.performable.impl.FetchMarketNewsActionPerformable;
import com.bluebell.radicle.repositories.action.ActionRepository;
import com.bluebell.radicle.repositories.job.JobRepository;
import com.bluebell.radicle.scheduled.GenericScheduledJob;
import com.bluebell.radicle.services.job.JobService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Fetches {@link MarketNews} periodically every Sunday night at 11pm Eastern
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
@Slf4j
@Component
public class FetchMarketNewsScheduledJob implements GenericScheduledJob<Enum<JobType>> {

    @Resource(name = "actionRepository")
    private ActionRepository actionRepository;

    @Resource(name = "fetchMarketNewsActionPerformable")
    private FetchMarketNewsActionPerformable fetchMarketNewsActionPerformable;

    @Resource(name = "jobRepository")
    private JobRepository jobRepository;

    @Resource(name = "jobService")
    private JobService jobService;


    //  METHODS

    @Override
    @Async
    @Scheduled(cron = "0 0 23 * * 7")
    public void execute() {

        final List<Job> runningJobs = this.jobService.findJobsByStatusAndType(JobStatus.IN_PROGRESS, JobType.FETCH_MARKET_NEWS);
        if (CollectionUtils.isNotEmpty(runningJobs)) {
            LOGGER.info("A FetchMarketNewsJob is already running. Cancelling this job");
            return;
        }

        Action fetchMarketNewsAction = Action
                .builder()
                .priority(1)
                .name("FetchMarketNewsAction_" + LocalDateTime.now())
                .performableAction(this.fetchMarketNewsActionPerformable)
                .build();

        fetchMarketNewsAction = this.actionRepository.save(fetchMarketNewsAction);

        Job fetchMarketNewsJob = Job
                .builder()
                .type(JobType.FETCH_MARKET_NEWS)
                .name("FetchMarketNewsJob" + LocalDateTime.now())
                .build();

        fetchMarketNewsJob = this.jobRepository.save(fetchMarketNewsJob);

        fetchMarketNewsAction.setJob(fetchMarketNewsJob);
        fetchMarketNewsJob.addAction(fetchMarketNewsAction);

        this.jobRepository.save(fetchMarketNewsJob);
        this.actionRepository.save(fetchMarketNewsAction);

        try {
            final JobResult result = this.jobService.executeJob(fetchMarketNewsJob);
            if (result.wasSuccessful()) {
                LOGGER.info("Job {}:{} successfully completed at {}", fetchMarketNewsJob.getName(), fetchMarketNewsJob.getJobId(), LocalDateTime.now());
            } else {
                LOGGER.error("Job {}:{} failed at {}. Please check the logs for more details", fetchMarketNewsJob.getName(), fetchMarketNewsJob.getJobId(), LocalDateTime.now());
            }
        } catch (Exception e) {
            LOGGER.error("Job {}:{} crashed hard at {}. Please check the logs for more details", fetchMarketNewsJob.getName(), fetchMarketNewsJob.getJobId(), LocalDateTime.now());
            LOGGER.error(e.getMessage(), e);
            fetchMarketNewsJob.setStatus(JobStatus.FAILED);
            this.jobRepository.save(fetchMarketNewsJob);
        }
    }

    @Override
    public Enum<JobType> getJobType() {
        return JobType.FETCH_MARKET_NEWS;
    }
}
