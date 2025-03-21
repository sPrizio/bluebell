package com.bluebell.planter.converters.job;

import com.bluebell.planter.converters.GenericDTOConverter;
import com.bluebell.planter.services.UniqueIdentifierService;
import com.bluebell.platform.models.api.dto.job.JobResultDTO;
import com.bluebell.platform.models.core.entities.job.impl.JobResult;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * Converts {@link JobResult}s into {@link JobResultDTO}s
 *
 * @author Stephen Prizio
 * @version 0.1.3
 */
@Component("jobResultDTOConverter")
public class JobResultDTOConverter implements GenericDTOConverter<JobResult, JobResultDTO> {

    @Resource(name = "jobResultEntryDTOConverter")
    private JobResultEntryDTOConverter jobResultEntryDTOConverter;

    @Resource(name = "uniqueIdentifierService")
    private UniqueIdentifierService uniqueIdentifierService;


    //  METHODS

    @Override
    public JobResultDTO convert(final JobResult entity) {

        if (entity == null || entity.getJob() == null) {
            return JobResultDTO.builder().build();
        }

        return JobResultDTO
                .builder()
                .uid(this.uniqueIdentifierService.generateUid(entity))
                .jobId(entity.getJob().getJobId())
                .entries(this.jobResultEntryDTOConverter.convertAll(entity.getEntries()))
                .build();
    }
}
