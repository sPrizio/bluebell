package com.bluebell.platform.models.api.dto.job;

import com.bluebell.platform.models.api.dto.GenericDTO;
import com.bluebell.platform.models.core.entities.job.impl.JobResultEntry;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * DTO representation of {@link JobResultEntry}
 *
 * @author Stephen Prizio
 * @version 0.1.3
 */
@Getter
@Setter
@Builder
@Schema(title = "JobResultEntryDTO", name = "JobResultEntryDTO", description = "A client-facing reduction of the JobResultEntry entity that displays key account information in a safe to read way.")
public class JobResultEntryDTO implements GenericDTO {

    @Schema(description = "JobResultEntry uid")
    private @Builder.Default String uid = StringUtils.EMPTY;

    @Schema(description = "Flag for whether the action performed successfully")
    private boolean success;

    @Schema(description = "The data obtained from running this action")
    private String data;

    @Schema(description = "The logs for this entry, i.e. the result of running a specific action")
    private String logs;
}
