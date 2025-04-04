package com.bluebell.radicle.scheduled.impl;

import com.bluebell.platform.enums.job.JobType;
import com.bluebell.platform.models.core.entities.action.impl.Action;
import com.bluebell.platform.models.core.entities.job.impl.Job;
import com.bluebell.radicle.performable.impl.FetchMarketDataFromFirstRateDataActionPerformable;
import com.bluebell.radicle.performable.impl.ResolveFirstRateDataDownloadedDataActionPerformable;
import com.bluebell.radicle.repositories.action.ActionRepository;
import com.bluebell.radicle.repositories.job.JobRepository;
import com.bluebell.radicle.scheduled.AbstractScheduledJob;
import com.bluebell.radicle.scheduled.GenericScheduledJob;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.TreeSet;

/**
 * Job that fetches market data from firstratedata
 *
 * @author Stephen Prizio
 * @version 0.1.5
 */
@Slf4j
@Component
public class FetchMarketDataFromFirstRateDataScheduledJob extends AbstractScheduledJob implements GenericScheduledJob<Enum<JobType>> {

    @Value("${bluebell.frd.fullSet.fileUrlIDs}")
    private String fullSet;

    @Autowired
    private ApplicationContext applicationContext;

    @Resource(name = "actionRepository")
    private ActionRepository actionRepository;

    @Resource(name = "jobRepository")
    private JobRepository jobRepository;

    @Resource(name = "resolveFirstRateDataDownloadedDataActionPerformable")
    private ResolveFirstRateDataDownloadedDataActionPerformable resolveFirstRateDataDownloadedDataActionPerformable;


    //  METHODS

    @Override
    @Async
    @Scheduled(cron = "0 0 5 * * *")
    public void execute() {

        checkForConcurrentJob(JobType.FETCH_MARKET_DATA_FROM_FIRST_DATA);

        int count = 1;
        final Set<Action> actions = new TreeSet<>();
        final String[] sets = this.fullSet.trim().split(",");
        for (final String set : sets) {
            final FetchMarketDataFromFirstRateDataActionPerformable actionPerformable = this.applicationContext.getBean(FetchMarketDataFromFirstRateDataActionPerformable.class);
            actionPerformable.setFileUrlId(set);

            actions.add(
                    this.actionRepository.save(
                            Action
                                    .builder()
                                    .priority(count)
                                    .name(String.format("DownloadFirstRateDataAction_%s_%s", set, LocalDateTime.now()))
                                    .performableAction(actionPerformable)
                                    .build()
                    )
            );

            count += 1;
        }

        if (sets.length > 0) {
            actions.add(
                    this.actionRepository.save(
                            Action
                                    .builder()
                                    .priority(count + 1)
                                    .name(String.format("ResolveFirstRateDataDownloadedDataAction_%s", LocalDateTime.now()))
                                    .performableAction(this.resolveFirstRateDataDownloadedDataActionPerformable)
                                    .build()
                    )
            );
        }

        if (actions.isEmpty()) {
            LOGGER.warn("No data targeted for download. Skipping Job");
            return;
        }

        Job downloadDataJob = Job
                .builder()
                .type(JobType.FETCH_MARKET_DATA_FROM_FIRST_DATA)
                .name(String.format("DownloadFirstRateDataJob_%s", LocalDateTime.now()))
                .build();

        downloadDataJob = this.jobRepository.save(downloadDataJob);

        for (final Action action : actions) {
            action.setJob(downloadDataJob);
            downloadDataJob.addAction(action);
        }

        this.jobRepository.save(downloadDataJob);
        this.actionRepository.saveAll(actions);

        executeJobAndHandleResult(downloadDataJob);
    }

    @Override
    public Enum<JobType> getJobType() {
        return JobType.FETCH_MARKET_DATA_FROM_FIRST_DATA;
    }
}
