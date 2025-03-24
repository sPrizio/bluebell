package com.bluebell.platform.models.api.dto.job;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * Class representation of a collection of {@link JobDTO}s with associated page information
 *
 * @param page          current page
 * @param pageSize      page size
 * @param jobs        {@link List} of {@link JobDTO}
 * @param totalElements total trades count
 * @param totalPages    number of pages
 * @author Stephen Prizio
 * @version 0.1.3
 */
@Builder
@Schema(title = "PaginatedJobsDTO", name = "PaginatedJobsDTO", description = "Data representation of a collection of jobs organized by page")
public record PaginatedJobsDTO(
        @Getter @Schema(description = "Current page") int page,
        @Getter @Schema(description = "Page size") int pageSize,
        @Getter @Schema(description = "List of jobs in the page") List<JobDTO> jobs,
        @Getter @Schema(description = "Total elements across all pages") int totalElements,
        @Getter @Schema(description = "Total pages") int totalPages
) { }
