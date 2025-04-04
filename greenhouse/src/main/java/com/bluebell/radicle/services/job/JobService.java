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
import com.bluebell.radicle.performable.ActionPerformable;
import com.bluebell.radicle.repositories.job.JobRepository;
import com.bluebell.radicle.repositories.job.JobResultEntryRepository;
import com.bluebell.radicle.repositories.job.JobResultRepository;
import com.bluebell.radicle.services.action.ActionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static com.bluebell.radicle.validation.GenericValidator.validateDatesAreNotMutuallyExclusive;
import static com.bluebell.radicle.validation.GenericValidator.validateParameterIsNotNull;

/**
 * Service-layer for {@link Job}
 *
 * @author Stephen Prizio
 * @version 0.1.5
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

    @Resource(name = "jobResultEntryRepository")
    private JobResultEntryRepository jobResultEntryRepository;

    @Value("${bluebell.stale.job.lookback}")
    private long lookbackPeriod;


    //  METHODS

    /**
     * Executes the given {@link Job}
     *
     * @param job {@link Job}
     * @return {@link JobResult}
     * @throws JobExecutionException   thrown when an issue arises due to the job exception
     * @throws JsonProcessingException thrown when the action data cannot be resolved
     */
    @Transactional
    public JobResult executeJob(Job job) throws JobExecutionException, JsonProcessingException {

        validateParameterIsNotNull(job, CorePlatformConstants.Validation.Job.JOB_CANNOT_BE_NULL);

        if (CollectionUtils.isEmpty(job.getActions())) {
            throw new JobExecutionException(String.format("Cannot execute Job %s, job has no actions!", job.getName()));
        }

        LOGGER.info("Executing Job {}", job.getName());

        job.setExecutionTime(LocalDateTime.now());
        job.setStatus(JobStatus.IN_PROGRESS);
        final Job previousVersion = job;
        job = this.jobRepository.save(job);
        job = cleanUpPerformable(previousVersion, job);

        JobResult jobResult = JobResult.builder().build();
        jobResult.setJob(job);
        jobResult = this.jobResultRepository.save(jobResult);

        job.setJobResult(jobResult);
        this.jobRepository.save(job);

        jobResult.setJob(job);
        this.jobResultRepository.save(jobResult);

        final List<JobResultEntry> jobResultEntries = new ArrayList<>();
        for (final Action action : job.getActions()) {
            final ActionResult result = this.actionService.performAction(action);
            if (result.getStatus() == ActionStatus.FAILURE) {
                job.setStatus(JobStatus.FAILED);
                job.setCompletionTime(LocalDateTime.now());

                JobResultEntry entry = JobResultEntry
                        .builder()
                        .success(false)
                        .data(safeGetData(result))
                        .logs(safeGetLogs(result))
                        .build();

                entry.setJobResult(jobResult);
                jobResultEntries.add(entry);

                LOGGER.error("Job {} failed at action {}", job.getName(), action.getName());

                this.jobRepository.save(job);
                jobResult.setEntries(jobResultEntries);
                return this.jobResultRepository.save(jobResult);
            } else if (result.getStatus() == ActionStatus.SUCCESS) {

                JobResultEntry entry = JobResultEntry
                        .builder()
                        .success(true)
                        .data(safeGetData(result))
                        .logs(safeGetLogs(result))
                        .build();

                entry.setJobResult(jobResult);

                jobResultEntries.add(entry);
                this.jobResultEntryRepository.save(entry);
            }
        }

        job.setStatus(JobStatus.COMPLETED);
        job.setCompletionTime(LocalDateTime.now());

        LOGGER.info("Job {} has completed successfully", job.getName());

        this.jobRepository.save(job);
        jobResult.setEntries(jobResultEntries);
        jobResult = this.jobResultRepository.save(jobResult);

        job.setJobResult(jobResult);
        this.jobRepository.save(job);

        return this.jobResultRepository.save(jobResult);
    }

    /**
     * Deletes jobs that are in progress and have been for over 1 week
     *
     * @return count of deleted jobs
     */
    @Transactional
    public int deleteStaleInProgressJobs() {
        int count = 0;
        final List<Job> jobs = this.jobRepository.findJobsByStatus(JobStatus.IN_PROGRESS);
        if (CollectionUtils.isNotEmpty(jobs)) {
            final List<Job> staleInProgress = jobs
                    .stream()
                    .filter(job -> job.getExecutionTime() != null)
                    .filter(job -> job.getExecutionTime().isBefore(LocalDateTime.now().minusWeeks(1)))
                    .toList();

            if (CollectionUtils.isNotEmpty(staleInProgress)) {
                for (final Job job : staleInProgress) {
                    this.jobRepository.delete(job);
                    count++;
                }
            }
        }

        return count;
    }

    /**
     * Deletes jobs that are both completed or failed and have been so for the configured lookback period (default is 1 year)
     *
     * @return count of deleted jobs
     */
    @Transactional
    public int deleteOldJobs() {
        int count = 0;
        final List<Job> oldJobs = this.jobRepository.findJobsByStatusInAndCompletionTimeBefore(List.of(JobStatus.COMPLETED, JobStatus.FAILED), LocalDateTime.now().minusSeconds(this.lookbackPeriod));
        if (CollectionUtils.isNotEmpty(oldJobs)) {
            for (final Job job : oldJobs) {
                this.jobRepository.delete(job);
                count++;
            }
        }

        return count;
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
     * @param status  {@link JobStatus}
     * @param jobType {@link JobType}
     * @return {@link List} of {@link Job}
     */
    public List<Job> findJobsByStatusAndType(final JobStatus status, final JobType jobType) {
        validateParameterIsNotNull(status, CorePlatformConstants.Validation.Job.JOB_STATUS_CANNOT_BE_NULL);
        validateParameterIsNotNull(jobType, CorePlatformConstants.Validation.Job.JOB_TYPE_CANNOT_BE_NULL);
        return this.jobRepository.findJobsByStatusAndType(status, jobType);
    }

    /**
     * Returns a {@link List} of {@link Job}s by their status
     *
     * @param start    {@link LocalDateTime} start of interval (inclusive)
     * @param end      {@link LocalDateTime} end of interval (exclusive)
     * @param status   {@link JobStatus}
     * @param page     page number
     * @param pageSize page size
     * @return {@link Page} of {@link Job}
     */
    public Page<Job> findJobsByStatusPaged(final LocalDateTime start, final LocalDateTime end, final JobStatus status, final int page, final int pageSize) {

        validateParameterIsNotNull(start, CorePlatformConstants.Validation.DateTime.START_DATE_CANNOT_BE_NULL);
        validateParameterIsNotNull(end, CorePlatformConstants.Validation.DateTime.END_DATE_CANNOT_BE_NULL);
        validateDatesAreNotMutuallyExclusive(start, end, CorePlatformConstants.Validation.DateTime.MUTUALLY_EXCLUSIVE_DATES);
        validateParameterIsNotNull(status, CorePlatformConstants.Validation.Job.JOB_STATUS_CANNOT_BE_NULL);

        return this.jobRepository.findAllJobsWithinDateByStatusPaged(start, end, status, PageRequest.of(page, pageSize));
    }

    /**
     * Returns a {@link List} of {@link Job}s by their type
     *
     * @param start    {@link LocalDateTime} start of interval (inclusive)
     * @param end      {@link LocalDateTime} end of interval (exclusive)
     * @param jobType  {@link JobType}
     * @param page     page number
     * @param pageSize page size
     * @return {@link Page} of {@link Job}
     */
    public Page<Job> findJobsByTypePaged(final LocalDateTime start, final LocalDateTime end, final JobType jobType, final int page, final int pageSize) {

        validateParameterIsNotNull(start, CorePlatformConstants.Validation.DateTime.START_DATE_CANNOT_BE_NULL);
        validateParameterIsNotNull(end, CorePlatformConstants.Validation.DateTime.END_DATE_CANNOT_BE_NULL);
        validateDatesAreNotMutuallyExclusive(start, end, CorePlatformConstants.Validation.DateTime.MUTUALLY_EXCLUSIVE_DATES);
        validateParameterIsNotNull(jobType, CorePlatformConstants.Validation.Job.JOB_TYPE_CANNOT_BE_NULL);

        return this.jobRepository.findAllJobsWithinDateByTypePaged(start, end, jobType, PageRequest.of(page, pageSize));
    }

    /**
     * Returns a {@link List} of {@link Job}s by their status and type
     *
     * @param start    {@link LocalDateTime} start of interval (inclusive)
     * @param end      {@link LocalDateTime} end of interval (exclusive)
     * @param status   {@link JobStatus}
     * @param jobType  {@link JobType}
     * @param page     page number
     * @param pageSize page size
     * @return {@link Page} of {@link Job}
     */
    public Page<Job> findJobsByStatusAndTypePaged(final LocalDateTime start, final LocalDateTime end, final JobStatus status, final JobType jobType, final int page, final int pageSize) {

        validateParameterIsNotNull(start, CorePlatformConstants.Validation.DateTime.START_DATE_CANNOT_BE_NULL);
        validateParameterIsNotNull(end, CorePlatformConstants.Validation.DateTime.END_DATE_CANNOT_BE_NULL);
        validateDatesAreNotMutuallyExclusive(start, end, CorePlatformConstants.Validation.DateTime.MUTUALLY_EXCLUSIVE_DATES);
        validateParameterIsNotNull(status, CorePlatformConstants.Validation.Job.JOB_STATUS_CANNOT_BE_NULL);
        validateParameterIsNotNull(jobType, CorePlatformConstants.Validation.Job.JOB_TYPE_CANNOT_BE_NULL);

        return this.jobRepository.findAllJobsWithinDateByStatusAndTypePaged(start, end, status, jobType, PageRequest.of(page, pageSize));
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
            final ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper.writeValueAsString(actionResult.getData().getData());
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

    /**
     * Persists the transient field action performable across saves
     *
     * @param previous previous version of the job
     * @param job      current version of the job
     * @return updated {@link Job}
     */
    private Job cleanUpPerformable(final Job previous, final Job job) {
        Map<String, ActionPerformable> map = new HashMap<>();
        previous.getActions().forEach(action -> map.put(action.getActionId(), action.getPerformableAction()));

        job.getActions().forEach(action -> action.setPerformableAction(map.get(action.getActionId())));
        return job;
    }
}
