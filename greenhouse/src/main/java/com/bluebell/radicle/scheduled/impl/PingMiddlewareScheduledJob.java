package com.bluebell.radicle.scheduled.impl;


import com.bluebell.platform.enums.job.JobType;
import com.bluebell.platform.enums.middleware.Middleware;
import com.bluebell.platform.models.core.entities.action.impl.Action;
import com.bluebell.platform.models.core.entities.job.impl.Job;
import com.bluebell.radicle.performable.impl.PingMiddlewareActionPerformable;
import com.bluebell.radicle.repositories.action.ActionRepository;
import com.bluebell.radicle.repositories.job.JobRepository;
import com.bluebell.radicle.scheduled.AbstractScheduledJob;
import com.bluebell.radicle.scheduled.GenericScheduledJob;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;
import java.util.TreeSet;

/**
 * Pings all middleware services for their statuses. Reports errors
 *
 * @author Stephen Prizio
 * @version 0.1.6
 */
@Slf4j
@Component
public class PingMiddlewareScheduledJob extends AbstractScheduledJob implements GenericScheduledJob<Enum<JobType>> {

    @Autowired
    private ApplicationContext applicationContext;

    @Resource(name = "actionRepository")
    private ActionRepository actionRepository;

    @Resource(name = "jobRepository")
    private JobRepository jobRepository;


    //  METHODS

    @Override
    @Async
    @Scheduled(cron = "0 0 8,22 * * *")
    public void execute() {

        checkForConcurrentJob(JobType.PING_MIDDLEWARE);

        int count = 1;
        final Set<Action> actions = new TreeSet<>();
        for (final Middleware middleware : Middleware.values()) {
            final PingMiddlewareActionPerformable actionPerformable = this.applicationContext.getBean(PingMiddlewareActionPerformable.class);
            actionPerformable.setSystemName(middleware.getCode());

            actions.add(
                    this.actionRepository.save(
                            Action
                                    .builder()
                                    .priority(count)
                                    .name(String.format("PingMiddlewareAction_%s_%s", middleware.getCode(), LocalDateTime.now()))
                                    .performableAction(actionPerformable)
                                    .build()
                    )
            );

            count += 1;
        }

        Job pingMiddlewareJob = Job
                .builder()
                .type(JobType.PING_MIDDLEWARE)
                .name(String.format("PingMiddlewareJob_%s", LocalTime.now()))
                .build();

        pingMiddlewareJob = this.jobRepository.save(pingMiddlewareJob);

        for (Action action : actions) {
            action.setJob(pingMiddlewareJob);
            pingMiddlewareJob.addAction(action);
        }

        this.jobRepository.save(pingMiddlewareJob);
        this.actionRepository.saveAll(actions);

        executeJobAndHandleResult(pingMiddlewareJob);
    }

    @Override
    public Enum<JobType> getJobType() {
        return JobType.PING_MIDDLEWARE;
    }
}
