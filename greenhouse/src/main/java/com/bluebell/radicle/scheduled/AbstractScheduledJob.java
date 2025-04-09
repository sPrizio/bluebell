package com.bluebell.radicle.scheduled;

import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.job.JobStatus;
import com.bluebell.platform.enums.job.JobType;
import com.bluebell.platform.exceptions.job.ConcurrentJobExecutionException;
import com.bluebell.platform.models.core.entities.job.impl.Job;
import com.bluebell.platform.models.core.entities.job.impl.JobResult;
import com.bluebell.platform.models.core.nonentities.email.EmailTemplate;
import com.bluebell.radicle.repositories.job.JobRepository;
import com.bluebell.radicle.services.email.EmailService;
import com.bluebell.radicle.services.job.JobService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * Parent-level scheduled job for common functionality
 *
 * @author Stephen Prizio
 * @version 0.1.6
 */
@Slf4j
public abstract class AbstractScheduledJob {

    @Value("${bluebell.email.system.recipient}")
    private String recipient;

    @Resource(name = "simpleEmailService")
    private EmailService emailService;

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
                sendFailureEmail(job, "An unknown error occurred. Refer to logs for possible hints.");
            }
        } catch (Exception e) {
            LOGGER.error("Job {}:{} crashed hard at {}. Please check the logs for more details", job.getName(), job.getJobId(), LocalDateTime.now());
            LOGGER.error(e.getMessage(), e);
            job.setStatus(JobStatus.FAILED);
            this.jobRepository.save(job);
            sendFailureEmail(job, e.getMessage());
        }
    }


    //  HELPERS

    /**
     * Sends an email when a {@link Job} fails
     *
     * @param job {@link Job}
     * @param details error details
     */
    private void sendFailureEmail(final Job job, final String details) {

        final EmailTemplate failedJobTemplate =
                EmailTemplate
                        .builder()
                        .template(CorePlatformConstants.EmailTemplates.FAILED_JOB_TEMPLATE)
                        .queryParams(Map.of("jobName", job.getName(), "timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern(CorePlatformConstants.DATE_TIME_FORMAT)), "errorDetails", details))
                        .build();

        this.emailService.sendEmail(this.recipient, String.format("%s job failure", job.getType().getLabel()), failedJobTemplate);

    }
}
