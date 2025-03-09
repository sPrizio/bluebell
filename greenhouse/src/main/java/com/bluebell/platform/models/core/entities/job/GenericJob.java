package com.bluebell.platform.models.core.entities.job;

import com.bluebell.platform.models.core.entities.GenericEntity;
import com.bluebell.platform.models.core.entities.job.impl.Job;

/**
 * Contract for {@link Job}s
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
public interface GenericJob extends GenericEntity {

    /**
     * Executes the job
     */
    void executeJob();
}
