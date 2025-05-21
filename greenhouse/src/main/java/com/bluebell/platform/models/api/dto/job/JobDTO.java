package com.bluebell.platform.models.api.dto.job;

import com.bluebell.platform.models.api.dto.GenericDTO;
import com.bluebell.platform.models.api.dto.action.ActionDTO;
import com.bluebell.platform.models.core.entities.job.impl.Job;
import com.bluebell.platform.models.core.nonentities.data.EnumDisplay;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO representation of {@link Job}
 *
 * @author Stephen Prizio
 * @version 0.2.1
 */
@Getter
@Setter
@Builder
@Schema(title = "JobDTO", name = "JobDTO", description = "A client-facing reduction of the Job entity that displays key account information in a safe to read way.")
public class JobDTO implements GenericDTO {

    @Schema(description = "Job uid")
    private @Builder.Default String uid = StringUtils.EMPTY;

    @Schema(description = "Job pk")
    private Long id;

    @Schema(description = "Job id")
    private String jobId;

    @Schema(description = "Job name")
    private String name;

    @Schema(description = "Job execution/start time")
    private LocalDateTime executionTime;

    @Schema(description = "Job completion time")
    private LocalDateTime completionTime;

    @Schema(description = "Job status")
    private EnumDisplay status;

    @Schema(description = "Job type")
    private EnumDisplay type;

    @Schema(description = "Job actions, things the job will do")
    private Set<ActionDTO> actions;

    @Schema(description = "Result of running the job")
    private JobResultDTO jobResult;
}
