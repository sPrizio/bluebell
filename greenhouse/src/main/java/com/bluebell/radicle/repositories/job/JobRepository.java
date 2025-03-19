package com.bluebell.radicle.repositories.job;

import com.bluebell.platform.enums.job.JobStatus;
import com.bluebell.platform.enums.job.JobType;
import com.bluebell.platform.models.core.entities.job.impl.Job;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * Data-access layer for {@link Job}
 *
 * @author Stephen Prizio
 * @version 0.1.2
 */
@Repository
public interface JobRepository extends PagingAndSortingRepository<Job, Long>, CrudRepository<Job, Long> {

    /**
     * Finds a {@link Job} by its job is
     *
     * @param jobId job id
     * @return {@link Job}
     */
    Job findJobByJobId(final String jobId);

    /**
     * Returns a {@link List} of {@link Job}s by their status
     *
     * @param status {@link JobStatus}
     * @return {@link List} of {@link Job}
     */
    List<Job> findJobsByStatus(final JobStatus status);

    /**
     * Returns a {@link List} of {@link Job}s by their type
     *
     * @param jobType {@link JobType}
     * @return {@link List} of {@link Job}
     */
    List<Job> findJobsByType(final JobType jobType);

    /**
     * Returns a {@link List} of {@link Job}s by their status and type
     *
     * @param status {@link JobStatus}
     * @param jobType {@link JobType}
     * @return {@link List} of {@link Job}
     */
    List<Job> findJobsByStatusAndType(final JobStatus status, final JobType jobType);

    /**
     * Returns a {@link List} of {@link Job}s that are matching the given statuses and whose completion time comes before the given date time
     *
     * @param statuses {@link Collection} of {@link JobStatus}
     * @param completionTimeBefore lookback period {@link LocalDateTime}
     * @return {@link List} of {@link Job}
     */
    List<Job> findJobsByStatusInAndCompletionTimeBefore(final Collection<JobStatus> statuses, final LocalDateTime completionTimeBefore);
}
