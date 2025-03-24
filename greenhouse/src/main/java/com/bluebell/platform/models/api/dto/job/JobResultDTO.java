package com.bluebell.platform.models.api.dto.job;

import com.bluebell.platform.models.api.dto.GenericDTO;
import com.bluebell.platform.models.core.entities.job.impl.JobResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO representation of {@link JobResult}
 *
 * @author Stephen Prizio
 * @version 0.1.3
 */
@Getter
@Setter
@Builder
@Schema(title = "JobResultDTO", name = "JobResultDTO", description = "A client-facing reduction of the JobResult entity that displays key account information in a safe to read way.")
public class JobResultDTO implements GenericDTO {

    @Schema(description = "JobResult uid")
    private @Builder.Default String uid = StringUtils.EMPTY;

    @Schema(description = "Job id")
    private String jobId;

    @Schema(description = "The individual result entries representing the results of each action on the job")
    private @Builder.Default List<JobResultEntryDTO> entries = new ArrayList<>();
}
