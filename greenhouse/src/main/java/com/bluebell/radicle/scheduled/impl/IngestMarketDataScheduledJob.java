package com.bluebell.radicle.scheduled.impl;

import com.bluebell.platform.enums.job.JobStatus;
import com.bluebell.platform.enums.job.JobType;
import com.bluebell.platform.models.core.entities.action.impl.Action;
import com.bluebell.platform.models.core.entities.job.impl.Job;
import com.bluebell.platform.models.core.entities.market.MarketPrice;
import com.bluebell.platform.util.DirectoryUtil;
import com.bluebell.radicle.enums.DataSource;
import com.bluebell.radicle.performable.impl.IngestMarketDataActionPerformable;
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

import java.io.File;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.TreeSet;

/**
 * Ingest {@link MarketPrice}s from the ingress
 *
 * @author Stephen Prizio
 * @version 0.1.5
 */
@Slf4j
@Component
public class IngestMarketDataScheduledJob extends AbstractScheduledJob implements GenericScheduledJob<Enum<JobType>> {

    @Autowired
    private ApplicationContext applicationContext;

    @Value("${bluebell.ingress.root}")
    private String dataRoot;

    @Resource(name = "actionRepository")
    private ActionRepository actionRepository;

    @Resource(name = "jobRepository")
    private JobRepository jobRepository;


    //  METHODS

    @Override
    @Async
    @Scheduled(cron = "0 0 7 * * *")
    public void execute() {

        checkForConcurrentJob(JobType.INGEST_MARKET_DATA);

        int count = 1;
        final Set<Action> actions = new TreeSet<>();
        for (final DataSource dataSource : DataSource.values()) {
            final File dataSourceDirectory = new File(DirectoryUtil.getIngressDataRootForDataSource(this.dataRoot, dataSource));
            if (dataSourceDirectory.exists() && dataSourceDirectory.isDirectory()) {
                final File[] symbols = dataSourceDirectory.listFiles();
                if (symbols != null) {
                    for (final File symbol : symbols) {
                        final IngestMarketDataActionPerformable actionPerformable = this.applicationContext.getBean(IngestMarketDataActionPerformable.class);
                        actionPerformable.setDataSource(dataSource);
                        actionPerformable.setSymbol(symbol.getName());

                        actions.add(
                                this.actionRepository.save(
                                        Action
                                                .builder()
                                                .priority(count)
                                                .name(String.format("IngestMarketDataAction_%s_%s_%s", dataSource.getDataRoot(), symbol.getName(), LocalDateTime.now()))
                                                .performableAction(actionPerformable)
                                                .build()
                                )
                        );
                    }
                }
            }

            count += 1;
        }

        if (actions.isEmpty()) {
            LOGGER.warn("No data to ingest. Skipping Job");
            return;
        }

        Job ingestMarketDataJob = Job
                .builder()
                .type(JobType.INGEST_MARKET_DATA)
                .name(String.format("IngestMarketDataJob_%s", LocalDateTime.now()))
                .build();

        ingestMarketDataJob = this.jobRepository.save(ingestMarketDataJob);

        for (final Action action : actions) {
            action.setJob(ingestMarketDataJob);
            ingestMarketDataJob.addAction(action);
        }

        ingestMarketDataJob.setExecutionTime(LocalDateTime.now());
        ingestMarketDataJob.setStatus(JobStatus.IN_PROGRESS);
        this.jobRepository.save(ingestMarketDataJob);
        this.actionRepository.saveAll(actions);

        executeJobAndHandleResult(ingestMarketDataJob);
    }

    @Override
    public Enum<JobType> getJobType() {
        return JobType.INGEST_MARKET_DATA;
    }
}
