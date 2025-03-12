package com.bluebell.radicle.services.job;

import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.action.ActionStatus;
import com.bluebell.platform.enums.job.JobStatus;
import com.bluebell.platform.enums.job.JobType;
import com.bluebell.platform.exceptions.job.JobExecutionException;
import com.bluebell.platform.models.core.entities.action.impl.Action;
import com.bluebell.platform.models.core.entities.job.impl.Job;
import com.bluebell.platform.models.core.entities.job.impl.JobResult;
import com.bluebell.platform.models.core.entities.job.impl.JobResultEntry;
import com.bluebell.platform.models.core.nonentities.action.ActionResult;
import com.bluebell.radicle.repositories.job.JobRepository;
import com.bluebell.radicle.repositories.job.JobResultRepository;
import com.bluebell.radicle.services.action.ActionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.bluebell.radicle.validation.GenericValidator.validateParameterIsNotNull;

/**
 * Service-layer for {@link Job}
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
@Slf4j
@Service
public class JobService {

    @Resource(name = "actionService")
    private ActionService actionService;

    @Resource(name = "jobRepository")
    private JobRepository jobRepository;

    @Resource(name = "jobResultRepository")
    private JobResultRepository jobResultRepository;


    //  METHODS

    /**
     * Executes the given {@link Job}
     *
     * @param job {@link Job}
     * @return {@link JobResult}
     * @throws JobExecutionException thrown when an issue arises due to the job exception
     * @throws JsonProcessingException thrown when the action data cannot be resolved
     */
    public JobResult executeJob(final Job job) throws JobExecutionException, JsonProcessingException {

        validateParameterIsNotNull(job, CorePlatformConstants.Validation.Job.JOB_CANNOT_BE_NULL);

        if (CollectionUtils.isEmpty(job.getActions())) {
            throw new JobExecutionException(String.format("Cannot execute Job %s, job has no actions!", job.getName()));
        }

        LOGGER.info("Executing Job {}", job.getName());

        job.setExecutionTime(LocalDateTime.now());
        job.setStatus(JobStatus.IN_PROGRESS);

        final List<JobResultEntry> jobResultEntries = new ArrayList<>();
        for (final Action action : job.getActions()) {
            final ActionResult result = this.actionService.performAction(action);
            if (result.getStatus() == ActionStatus.FAILURE) {
                job.setStatus(JobStatus.FAILED);
                job.setCompletionTime(LocalDateTime.now());

                jobResultEntries.add(
                        JobResultEntry
                                .builder()
                                .success(false)
                                .data(safeGetData(result))
                                .logs(safeGetLogs(result))
                                .build()
                );

                LOGGER.error("Job {} failed at action {}", job.getName(), action.getName());

                this.jobRepository.save(job);
                return this.jobResultRepository.save(JobResult.builder().entries(jobResultEntries).build());
            } else if (result.getStatus() == ActionStatus.SUCCESS) {
                jobResultEntries.add(
                        JobResultEntry
                                .builder()
                                .success(true)
                                .data(safeGetData(result))
                                .logs(safeGetLogs(result))
                                .build()
                );
            }
        }

        job.setStatus(JobStatus.COMPLETED);
        job.setCompletionTime(LocalDateTime.now());

        LOGGER.info("Job {} has completed successfully", job.getName());

        this.jobRepository.save(job);
        final JobResult jobResult = this.jobResultRepository.save(JobResult.builder().entries(jobResultEntries).build());

        job.setJobResult(jobResult);
        this.jobRepository.save(job);

        return this.jobResultRepository.save(jobResult);
    }

    /**
     * Finds a {@link Job} by its job is
     *
     * @param jobId job id
     * @return {@link Job}
     */
    public Optional<Job> findJobByJobId(final String jobId) {
        validateParameterIsNotNull(jobId, CorePlatformConstants.Validation.Job.JOB_ID_CANNOT_BE_NULL);
        return Optional.ofNullable(this.jobRepository.findJobByJobId(jobId));
    }

    /**
     * Returns a {@link List} of {@link Job}s by their status
     *
     * @param status {@link JobStatus}
     * @return {@link List} of {@link Job}
     */
    public List<Job> findJobsByStatus(final JobStatus status) {
        validateParameterIsNotNull(status, CorePlatformConstants.Validation.Job.JOB_STATUS_CANNOT_BE_NULL);
        return this.jobRepository.findJobsByStatus(status);
    }

    /**
     * Returns a {@link List} of {@link Job}s by their type
     *
     * @param jobType {@link JobType}
     * @return {@link List} of {@link Job}
     */
    public List<Job> findJobsByType(final JobType jobType) {
        validateParameterIsNotNull(jobType, CorePlatformConstants.Validation.Job.JOB_TYPE_CANNOT_BE_NULL);
        return this.jobRepository.findJobsByType(jobType);
    }

    /**
     * Returns a {@link List} of {@link Job}s by their status and type
     *
     * @param status {@link JobStatus}
     * @param jobType {@link JobType}
     * @return {@link List} of {@link Job}
     */
    public List<Job> findJobsByStatusAndType(final JobStatus status, final JobType jobType) {
        validateParameterIsNotNull(status, CorePlatformConstants.Validation.Job.JOB_STATUS_CANNOT_BE_NULL);
        validateParameterIsNotNull(jobType, CorePlatformConstants.Validation.Job.JOB_TYPE_CANNOT_BE_NULL);
        return this.jobRepository.findJobsByStatusAndType(status, jobType);
    }


    //  HELPERS

    /**
     * Safely gets the data from an action result
     *
     * @param actionResult {@link ActionResult}
     * @return object as a json string
     * @throws JsonProcessingException json processing error if object cannot be parsed into json
     */
    private String safeGetData(final ActionResult actionResult) throws JsonProcessingException {

        if (actionResult != null && actionResult.getData() != null && actionResult.getData().getData() != null) {
            return new ObjectMapper().writeValueAsString(actionResult.getData().data());
        }

        return "No data to display.";
    }

    /**
     * Safely gets the logs from an action result
     *
     * @param actionResult {@link ActionResult}
     * @return string
     */
    private String safeGetLogs(final ActionResult actionResult) {

        if (actionResult != null && actionResult.getData() != null && StringUtils.isNotEmpty(actionResult.getData().logs())) {
            return actionResult.getData().logs();
        }

        return "No logs to display.";
    }
}
