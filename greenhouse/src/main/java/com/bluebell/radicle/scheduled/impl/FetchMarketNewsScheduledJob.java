package com.bluebell.radicle.scheduled.impl;

import com.bluebell.platform.enums.job.JobType;
import com.bluebell.platform.models.core.entities.action.impl.Action;
import com.bluebell.platform.models.core.entities.job.impl.Job;
import com.bluebell.platform.models.core.entities.news.MarketNews;
import com.bluebell.radicle.performable.impl.FetchMarketNewsActionPerformable;
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
 * Fetches {@link MarketNews} periodically every Sunday night at 11pm Eastern
 *
 * @author Stephen Prizio
 * @version 0.2.4
 */
@Slf4j
@Component
public class FetchMarketNewsScheduledJob extends AbstractScheduledJob implements GenericScheduledJob<Enum<JobType>> {

    @Resource(name = "actionRepository")
    private ActionRepository actionRepository;

    @Resource(name = "fetchMarketNewsActionPerformable")
    private FetchMarketNewsActionPerformable fetchMarketNewsActionPerformable;

    @Resource(name = "jobRepository")
    private JobRepository jobRepository;


    //  METHODS

    @Override
    @Async
    @Scheduled(cron = "0 0 1 * * 1")
    public void execute() {

        checkForConcurrentJob(JobType.FETCH_MARKET_NEWS);

        Action fetchMarketNewsAction = Action
                .builder()
                .priority(1)
                .name("FetchMarketNewsAction_" + LocalDateTime.now())
                .displayName("Fetch market news")
                .performableAction(this.fetchMarketNewsActionPerformable)
                .build();

        fetchMarketNewsAction = this.actionRepository.save(fetchMarketNewsAction);

        Job fetchMarketNewsJob = Job
                .builder()
                .type(JobType.FETCH_MARKET_NEWS)
                .name("FetchMarketNewsJob_" + LocalDateTime.now())
                .displayName("Fetch market news job")
                .build();

        fetchMarketNewsJob = this.jobRepository.save(fetchMarketNewsJob);

        fetchMarketNewsAction.setJob(fetchMarketNewsJob);
        fetchMarketNewsJob.addAction(fetchMarketNewsAction);

        this.jobRepository.save(fetchMarketNewsJob);
        this.actionRepository.save(fetchMarketNewsAction);

        executeJobAndHandleResult(fetchMarketNewsJob);
    }

    @Override
    public Enum<JobType> getJobType() {
        return JobType.FETCH_MARKET_NEWS;
    }
}
