package com.bluebell.planter.converters.job;

import com.bluebell.planter.converters.GenericDTOConverter;
import com.bluebell.planter.converters.action.ActionDTOConverter;
import com.bluebell.planter.services.UniqueIdentifierService;
import com.bluebell.platform.models.api.dto.job.JobDTO;
import com.bluebell.platform.models.core.entities.job.impl.Job;
import com.bluebell.platform.models.core.nonentities.data.EnumDisplay;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.TreeSet;

/**
 * Converts {@link Job}s  into {@link JobDTO}s
 *
 * @author Stephen Prizio
 * @version 0.2.4
 */
@Component("jobDTOConverter")
public class JobDTOConverter implements GenericDTOConverter<Job, JobDTO> {

    @Resource(name = "actionDTOConverter")
    private ActionDTOConverter actionDTOConverter;

    @Resource(name = "jobResultDTOConverter")
    private JobResultDTOConverter jobResultDTOConverter;

    @Resource(name = "uniqueIdentifierService")
    private UniqueIdentifierService uniqueIdentifierService;


    //  METHODS

    @Override
    public JobDTO convert(final Job entity) {

        if (entity == null) {
            return JobDTO.builder().build();
        }

        return JobDTO
                .builder()
                .uid(this.uniqueIdentifierService.generateUid(entity))
                .id(entity.getId())
                .jobId(entity.getJobId())
                .name(entity.getName())
                .displayName(entity.getDisplayName())
                .executionTime(entity.getExecutionTime())
                .completionTime(entity.getCompletionTime())
                .status(EnumDisplay.builder().code(entity.getStatus().getCode()).label(entity.getStatus().getLabel()).build())
                .type(EnumDisplay.builder().code(entity.getType().getCode()).label(entity.getType().getLabel()).build())
                .actions(new TreeSet<>(this.actionDTOConverter.convertAll(entity.getActions())))
                .jobResult(this.jobResultDTOConverter.convert(entity.getJobResult()))
                .build();
    }
}
