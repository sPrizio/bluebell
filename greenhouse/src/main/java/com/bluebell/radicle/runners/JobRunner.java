package com.bluebell.radicle.runners;

import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.action.ActionStatus;
import com.bluebell.platform.enums.job.JobStatus;
import com.bluebell.platform.enums.job.JobType;
import com.bluebell.platform.models.core.entities.action.impl.Action;
import com.bluebell.platform.models.core.entities.job.impl.Job;
import com.bluebell.platform.models.core.entities.job.impl.JobResult;
import com.bluebell.platform.models.core.entities.job.impl.JobResultEntry;
import com.bluebell.radicle.performable.impl.FetchMarketNewsActionPerformable;
import com.bluebell.radicle.repositories.action.ActionRepository;
import com.bluebell.radicle.repositories.job.JobRepository;
import com.bluebell.radicle.repositories.job.JobResultEntryRepository;
import com.bluebell.radicle.repositories.job.JobResultRepository;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Generates testing {@link Job}s
 *
 * @author Stephen Prizio
 * @version 0.1.9
 */
@Component
@Order(7)
@Profile("dev")
@ConditionalOnProperty(name = "bluebell.cmdlr.infra.data", havingValue = "true", matchIfMissing = true)
public class JobRunner extends AbstractRunner implements CommandLineRunner {

    private static final List<String> WORDS = new ArrayList<>(CorePlatformConstants.RANDOM_WORDS);
    private final Random random = new Random();

    @Resource(name = "actionRepository")
    private ActionRepository actionRepository;

    @Resource(name = "fetchMarketNewsActionPerformable")
    private FetchMarketNewsActionPerformable fetchMarketNewsActionPerformable;

    @Resource(name = "jobRepository")
    private JobRepository jobRepository;

    @Resource(name = "jobResultRepository")
    private JobResultRepository jobResultRepository;

    @Resource(name = "jobResultEntryRepository")
    private JobResultEntryRepository jobResultEntryRepository;


    //  METHODS

    @Override
    @Transactional
    public void run(String... args) {

        logStart();

        final int jobCount = this.random.nextInt(15) + 1;
        for (int i = 0; i < jobCount; i++) {
            Job job = new Job();
            job.setName(getRandomName("Job"));
            job.setExecutionTime(LocalDateTime.now().minusMinutes(this.random.nextInt(500)));
            job.setCompletionTime(LocalDateTime.now().plusMinutes(this.random.nextInt(500)));
            job.setType(JobType.FETCH_MARKET_NEWS);
            job = this.jobRepository.save(job);

            JobResult jobResult = new JobResult();
            jobResult.setJob(job);
            jobResult = this.jobResultRepository.save(jobResult);

            final int actionCount = this.random.nextInt(5) + 1;
            final List<JobResultEntry> entries = new ArrayList<>();
            for (int j = 0; j < actionCount; j++) {
                final Action action = Action.builder().build();
                boolean success = true;
                action.setPriority(j + 1);
                action.setName(getRandomName("Action"));

                if (j == actionCount - 1) {
                    if (this.random.nextInt(100) % 2 == 0) {
                        action.setStatus(ActionStatus.SUCCESS);
                    } else {
                        action.setStatus(ActionStatus.FAILURE);
                        success = false;
                    }
                } else {
                    action.setStatus(ActionStatus.SUCCESS);
                }

                JobResultEntry jobResultEntry;
                if (success) {
                    jobResultEntry = JobResultEntry
                            .builder()
                            .success(true)
                            .data(getRandomString(StringUtils.EMPTY))
                            .logs("Action completed successfully")
                            .build();
                } else {
                    jobResultEntry = JobResultEntry
                            .builder()
                            .success(false)
                            .logs(getRandomLongString(StringUtils.EMPTY))
                            .build();
                }

                entries.add(this.jobResultEntryRepository.save(jobResultEntry));
                action.setJob(job);
                action.setPerformableAction(this.fetchMarketNewsActionPerformable);

                this.actionRepository.save(action);
            }

            jobResult.setEntries(entries);
            jobResult = this.jobResultRepository.save(jobResult);
            job.setStatus(jobResult.wasSuccessful() ? JobStatus.COMPLETED : JobStatus.FAILED);
            job.setJobResult(jobResult);
            this.jobRepository.save(job);
        }

        logEnd();
    }


    //  HELPERS

    /**
     * Generates a random name
     * @param suffix string
     * @return string
     */
    private String getRandomName(final String suffix) {

        Collections.shuffle(WORDS);
        final List<String> selectedWords = WORDS
                .subList(0, 1 + this.random.nextInt(3))
                .stream()
                .map(StringUtils::capitalize)
                .toList();

        return String.join(" ", selectedWords) + " " + suffix;
    }

    /**
     * Generates a random string
     * @param suffix string
     * @return string
     */
    private String getRandomString(final String suffix) {

        Collections.shuffle(WORDS);
        final List<String> selectedWords = WORDS
                .subList(0, 3 + this.random.nextInt(10))
                .stream()
                .map(StringUtils::capitalize)
                .toList();

        return String.join(" ", selectedWords) + " " + suffix;
    }

    /**
     * Generates a random longer string
     * @param suffix string
     * @return string
     */
    private String getRandomLongString(final String suffix) {

        Collections.shuffle(WORDS);
        final List<String> selectedWords = WORDS
                .subList(0, 100 + this.random.nextInt(145))
                .stream()
                .map(StringUtils::capitalize)
                .toList();

        return String.join(" ", selectedWords) + " " + suffix;
    }
}
