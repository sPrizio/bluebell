package com.bluebell.radicle.repositories.job;

import com.bluebell.platform.models.core.entities.job.impl.JobResult;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Data-access layer for {@link JobResult}
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
@Repository
public interface JobResultRepository extends PagingAndSortingRepository<JobResult, Long>, CrudRepository<JobResult, Long> {
}
