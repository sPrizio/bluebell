package com.bluebell.planter.converters.job;

import com.bluebell.planter.converters.GenericDTOConverter;
import com.bluebell.planter.services.UniqueIdentifierService;
import com.bluebell.platform.models.api.dto.job.JobResultEntryDTO;
import com.bluebell.platform.models.core.entities.job.impl.JobResultEntry;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * Converts {@link JobResultEntry}s into {@link JobResultEntry}
 *
 * @author Stephen Prizio
 * @version 0.1.3
 */
@Component("jobResultEntryDTOConverter")
public class JobResultEntryDTOConverter implements GenericDTOConverter<JobResultEntry, JobResultEntryDTO> {

    @Resource(name = "uniqueIdentifierService")
    private UniqueIdentifierService uniqueIdentifierService;


    //  METHODS

    @Override
    public JobResultEntryDTO convert(final JobResultEntry entity) {

        if (entity == null) {
            return JobResultEntryDTO.builder().build();
        }

        return JobResultEntryDTO
                .builder()
                .uid(this.uniqueIdentifierService.generateUid(entity))
                .success(entity.isSuccess())
                .data(entity.getData())
                .logs(entity.getLogs())
                .build();
    }
}
