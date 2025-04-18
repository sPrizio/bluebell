package com.bluebell.radicle.scheduled;

import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.job.JobStatus;
import com.bluebell.platform.enums.job.JobType;
import com.bluebell.platform.exceptions.job.ConcurrentJobExecutionException;
import com.bluebell.platform.models.core.entities.job.impl.Job;
import com.bluebell.platform.models.core.entities.job.impl.JobResult;
import com.bluebell.platform.models.core.entities.job.impl.JobResultEntry;
import com.bluebell.platform.models.core.nonentities.email.EmailTemplate;
import com.bluebell.radicle.repositories.job.JobRepository;
import com.bluebell.radicle.services.email.EmailService;
import com.bluebell.radicle.services.job.JobService;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.PrintWriter;
import java.io.StringWriter;
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

    @Autowired
    private Dotenv dotenv;

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
                sendFailedJobEmail(job, result);
            }
        } catch (Exception e) {
            LOGGER.error("Job {}:{} crashed hard at {}. Please check the logs for more details", job.getName(), job.getJobId(), LocalDateTime.now());
            LOGGER.error(e.getMessage(), e);
            job.setStatus(JobStatus.FAILED);
            this.jobRepository.save(job);
            sendCrashedJobEmail(job, e);
        }
    }


    //  HELPERS

    /**
     * Sends an email when a {@link Job} fails
     *
     * @param job {@link Job}
     * @param result {@link JobResult}
     */
    private void sendFailedJobEmail(final Job job, final JobResult result) {

        if (result == null || result.wasSuccessful()) {
            return;
        }

        final JobResultEntry failedEntry = result.getEntries().stream().filter(e -> !e.getJobResult().wasSuccessful()).findFirst().orElse(null);
        if (failedEntry == null) {
            return;
        }

        final EmailTemplate failedJobTemplate =
                EmailTemplate
                        .builder()
                        .template(CorePlatformConstants.EmailTemplates.FAILED_JOB_TEMPLATE)
                        .queryParams(
                                Map.of(
                                        "jobName", job.getName(),
                                        "timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern(CorePlatformConstants.DATE_TIME_FORMAT)),
                                        "errorDetails", failedEntry.getData(),
                                        "detailedMessage", failedEntry.getLogs()
                                )
                        )
                        .build();

        final String recipient = this.dotenv.get("EMAIL_APP_RECIPIENT");
        if (StringUtils.isEmpty(recipient)) {
            throw new IllegalStateException("EMAIL_APP_RECIPIENT is not set");
        }

        this.emailService.sendEmail(recipient, String.format("%s job failure", job.getType().getLabel()), failedJobTemplate);
    }

    /**
     * Sends an email when a {@link Job} crashes
     *
     * @param job {@link Job}
     * @param exception exception
     */
    private void sendCrashedJobEmail(final Job job, final Exception exception) {

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);

        final EmailTemplate failedJobTemplate =
                EmailTemplate
                        .builder()
                        .template(CorePlatformConstants.EmailTemplates.FAILED_JOB_TEMPLATE)
                        .queryParams(
                                Map.of(
                                        "jobName", job.getName(),
                                        "timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern(CorePlatformConstants.DATE_TIME_FORMAT)),
                                        "errorDetails", exception.getMessage(),
                                        "detailedMessage", sw.toString()
                                )
                        )
                        .build();

        final String recipient = this.dotenv.get("EMAIL_APP_RECIPIENT");
        if (StringUtils.isEmpty(recipient)) {
            throw new IllegalStateException("EMAIL_APP_RECIPIENT is not set");
        }

        this.emailService.sendEmail(recipient, String.format("%s job failure", job.getType().getLabel()), failedJobTemplate);
    }
}
