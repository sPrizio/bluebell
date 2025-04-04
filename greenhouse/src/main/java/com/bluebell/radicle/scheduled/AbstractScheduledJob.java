package com.bluebell.radicle.scheduled;

import com.bluebell.platform.enums.job.JobStatus;
import com.bluebell.platform.enums.job.JobType;
import com.bluebell.platform.exceptions.job.ConcurrentJobExecutionException;
import com.bluebell.platform.models.core.entities.job.impl.Job;
import com.bluebell.platform.models.core.entities.job.impl.JobResult;
import com.bluebell.radicle.repositories.job.JobRepository;
import com.bluebell.radicle.services.job.JobService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Parent-level scheduled job for common functionality
 *
 * @author Stephen Prizio
 * @version 0.1.5
 */
@Slf4j
public abstract class AbstractScheduledJob {

    @Resource(name = "jobRepository")
    private JobRepository jobRepository;

    @Resource(name = "jobService")
    private JobService jobService;


    //  METHODS

    /**
     * Checks for an already running job of the same type, will throw an exception if found
     *
     * @param jobType {@link JobType}
     */
    public void checkForConcurrentJob(final JobType jobType) {
        final List<Job> runningJobs = this.jobService.findJobsByStatusAndType(JobStatus.IN_PROGRESS, jobType);
        if (CollectionUtils.isNotEmpty(runningJobs)) {
            LOGGER.info("A {} job is already running. Cancelling this job", jobType.getCode());
            throw new ConcurrentJobExecutionException(String.format("A %s is already running. Cancelling this job", jobType.getCode()));
        }
    }

    /**
     * Executes and handles the given {@link Job}
     *
     * @param job {@link Job}
     */
    public void executeJobAndHandleResult(final Job job) {
        try {
            final JobResult result = this.jobService.executeJob(job);
            if (result.wasSuccessful()) {
                LOGGER.info("Job {}:{} successfully completed at {}", job.getName(), job.getJobId(), LocalDateTime.now());
            } else {
                LOGGER.error("Job {}:{} failed at {}. Please check the logs for more details", job.getName(), job.getJobId(), LocalDateTime.now());
            }
        } catch (Exception e) {
            LOGGER.error("Job {}:{} crashed hard at {}. Please check the logs for more details", job.getName(), job.getJobId(), LocalDateTime.now());
            LOGGER.error(e.getMessage(), e);
            job.setStatus(JobStatus.FAILED);
            this.jobRepository.save(job);
        }
    }
}
