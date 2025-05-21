package com.bluebell.radicle.repositories.job;

import com.bluebell.platform.enums.job.JobStatus;
import com.bluebell.platform.enums.job.JobType;
import com.bluebell.platform.models.core.entities.job.impl.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
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
 * @version 0.2.1
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
     * @param status  {@link JobStatus}
     * @param jobType {@link JobType}
     * @return {@link List} of {@link Job}
     */
    List<Job> findJobsByStatusAndType(final JobStatus status, final JobType jobType);

    /**
     * Returns a {@link List} of {@link Job}s
     *
     * @param start    {@link LocalDateTime} start of interval (inclusive)
     * @param end      {@link LocalDateTime} end of interval (exclusive)
     * @param pageable {@link Pageable}
     * @return {@link Page} of {@link Job}
     */
    @Query("SELECT j FROM Job j WHERE j.executionTime >= ?1 AND j.executionTime < ?2")
    Page<Job> findAllJobsWithinDatePaged(final LocalDateTime start, final LocalDateTime end, final Pageable pageable);

    /**
     * Returns a {@link List} of {@link Job}s by their status
     *
     * @param start    {@link LocalDateTime} start of interval (inclusive)
     * @param end      {@link LocalDateTime} end of interval (exclusive)
     * @param status   {@link JobStatus}
     * @param pageable {@link Pageable}
     * @return {@link Page} of {@link Job}
     */
    @Query("SELECT j FROM Job j WHERE j.executionTime >= ?1 AND j.executionTime < ?2 AND j.status = ?3 ORDER BY j.executionTime DESC")
    Page<Job> findAllJobsWithinDateByStatusPaged(final LocalDateTime start, final LocalDateTime end, final JobStatus status, final Pageable pageable);

    /**
     * Returns a {@link List} of {@link Job}s by their type
     *
     * @param start    {@link LocalDateTime} start of interval (inclusive)
     * @param end      {@link LocalDateTime} end of interval (exclusive)
     * @param jobType  {@link JobType}
     * @param pageable {@link Pageable}
     * @return {@link Page} of {@link Job}
     */
    @Query("SELECT j FROM Job j WHERE j.executionTime >= ?1 AND j.executionTime < ?2 AND j.type = ?3 ORDER BY j.executionTime DESC")
    Page<Job> findAllJobsWithinDateByTypePaged(final LocalDateTime start, final LocalDateTime end, final JobType jobType, final Pageable pageable);

    /**
     * Returns a {@link List} of {@link Job}s by their status and type
     *
     * @param start    {@link LocalDateTime} start of interval (inclusive)
     * @param end      {@link LocalDateTime} end of interval (exclusive)
     * @param status   {@link JobStatus}
     * @param jobType  {@link JobType}
     * @param pageable {@link Pageable}
     * @return {@link Page} of {@link Job}
     */
    @Query("SELECT j FROM Job j WHERE j.executionTime >= ?1 AND j.executionTime < ?2 AND j.status = ?3 AND j.type = ?4 ORDER BY j.executionTime DESC")
    Page<Job> findAllJobsWithinDateByStatusAndTypePaged(final LocalDateTime start, final LocalDateTime end, final JobStatus status, final JobType jobType, final Pageable pageable);

    /**
     * Returns a {@link List} of {@link Job}s that are matching the given statuses and whose completion time comes before the given date time
     *
     * @param statuses             {@link Collection} of {@link JobStatus}
     * @param completionTimeBefore lookback period {@link LocalDateTime}
     * @return {@link List} of {@link Job}
     */
    List<Job> findJobsByStatusInAndCompletionTimeBefore(final Collection<JobStatus> statuses, final LocalDateTime completionTimeBefore);
}
