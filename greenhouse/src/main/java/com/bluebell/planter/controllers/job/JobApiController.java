package com.bluebell.planter.controllers.job;

import com.bluebell.planter.converters.job.JobDTOConverter;
import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.GenericEnum;
import com.bluebell.platform.enums.job.JobStatus;
import com.bluebell.platform.enums.job.JobType;
import com.bluebell.platform.models.api.dto.job.JobDTO;
import com.bluebell.platform.models.api.dto.job.PaginatedJobsDTO;
import com.bluebell.platform.models.api.json.StandardJsonResponse;
import com.bluebell.platform.models.core.entities.job.impl.Job;
import com.bluebell.radicle.security.aspects.ValidateApiToken;
import com.bluebell.radicle.services.job.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.bluebell.radicle.validation.GenericValidator.validateLocalDateFormat;

/**
 * Api controller for {@link Job}
 *
 * @author Stephen Prizio
 * @version 0.1.3
 */
@RestController
@RequestMapping("${bluebell.base.api.controller.endpoint}/job")
@Tag(name = "Job", description = "Handles endpoints & operations related to jobs.")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT})
public class JobApiController {

    @Resource(name = "jobDTOConverter")
    private JobDTOConverter jobDTOConverter;

    @Resource(name = "jobService")
    private JobService jobService;


    //  METHODS


    //  ----------------- GET REQUESTS -----------------

    /**
     * Returns a {@link StandardJsonResponse} containing the {@link JobDTO} for the given job id
     *
     * @param jobId job id
     * @param request {@link HttpServletRequest}
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Obtains the job for the given job id", description = "Returns the job by its job id.")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api cannot find the job for the given id",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "No job found for id")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully obtains job.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "401",
            description = "Response when the api call made was unauthorized.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "The API token was invalid.")
            )
    )
    @GetMapping("/get-by-id")
    public StandardJsonResponse<JobDTO> getJobForJobId(
            @Parameter(name = "Job ID", description = "The unique job id", example = "1234")
            final @RequestParam("jobId") String jobId,
            final HttpServletRequest request
    ) {

        final Optional<Job> job = this.jobService.findJobByJobId(jobId);
        if (job.isPresent()) {
            return StandardJsonResponse
                    .<JobDTO>builder()
                    .success(true)
                    .data(this.jobDTOConverter.convert(job.get()))
                    .build();
        } else {
            return StandardJsonResponse
                    .<JobDTO>builder()
                    .success(false)
                    .message(String.format("Job not found for job id : %s", jobId))
                    .build();
        }
    }

    /**
     * Returns a {@link StandardJsonResponse} containing {@link Job}s for the given interval of time and status
     *
     * @param request {@link HttpServletRequest}
     * @param start   start date & time
     * @param end     end date & time
     * @param jobStatus job status
     * @param page page
     * @param pageSize pageSize
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Obtains paginated jobs for the given interval of time and status", description = "Returns a list of paginated jobs filtered by their status.")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api call was made with an invalid start date.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "Invalid date")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api call was made with an invalid end date.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "Invalid date")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api call was made with an invalid status.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "Invalid filter")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api cannot find any jobs for the given interval of time and status.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "No jobs found for interval <start> : <end>")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully obtains the jobs within the given time interval for the given status.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "401",
            description = "Response when the api call made was unauthorized.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "The API token was invalid.")
            )
    )
    @GetMapping("/get-status-paged")
    public StandardJsonResponse<PaginatedJobsDTO> getJobsWithinIntervalByStatusPaged(
            @Parameter(name = "Job Status", description = "Job status to lookup", example = "COMPLETED")
            final @RequestParam("jobStatus") String jobStatus,
            @Parameter(name = "Start Date", description = "Start date of time period to analyze", example = "2025-01-01")
            final @RequestParam("start") String start,
            @Parameter(name = "End Date", description = "End date of time period to analyze", example = "2025-01-01")
            final @RequestParam("end") String end,
            @Parameter(name = "Page", description = "Current Page", example = "0")
            final @RequestParam(value = "page", defaultValue = "0") int page,
            @Parameter(name = "Page Size", description = "Size of page", example = "25")
            final @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            final HttpServletRequest request
    ) {
        validateLocalDateFormat(start, CorePlatformConstants.DATE_FORMAT, String.format(CorePlatformConstants.Validation.DateTime.START_DATE_INVALID_FORMAT, start, CorePlatformConstants.DATE_FORMAT));
        validateLocalDateFormat(end, CorePlatformConstants.DATE_FORMAT, String.format(CorePlatformConstants.Validation.DateTime.END_DATE_INVALID_FORMAT, end, CorePlatformConstants.DATE_FORMAT));

        final Page<Job> jobs = this.jobService.findJobsByStatusPaged(LocalDate.parse(start, DateTimeFormatter.ISO_DATE).atStartOfDay(), LocalDate.parse(end, DateTimeFormatter.ISO_DATE).atStartOfDay(), GenericEnum.getByCode(JobStatus.class, jobStatus), page, pageSize);
        return StandardJsonResponse
                .<PaginatedJobsDTO>builder()
                .success(true)
                .data(
                        PaginatedJobsDTO
                                .builder()
                                .page(jobs.getPageable().getPageNumber())
                                .pageSize(jobs.getPageable().getPageSize())
                                .jobs(jobs.map(jb -> this.jobDTOConverter.convert(jb)).stream().toList())
                                .totalElements(jobs.getNumberOfElements())
                                .totalPages(jobs.getTotalPages())
                                .build()
                )
                .build();
    }

    /**
     * Returns a {@link StandardJsonResponse} containing {@link Job}s for the given interval of time and type
     *
     * @param request {@link HttpServletRequest}
     * @param start   start date & time
     * @param end     end date & time
     * @param jobType job type
     * @param page page
     * @param pageSize pageSize
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Obtains paginated jobs for the given interval of time and type", description = "Returns a list of paginated jobs filtered by their type.")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api call was made with an invalid start date.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "Invalid date")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api call was made with an invalid end date.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "Invalid date")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api call was made with an invalid type.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "Invalid filter")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api cannot find any jobs for the given interval of time and type.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "No jobs found for interval <start> : <end>")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully obtains the jobs within the given time interval for the given type.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "401",
            description = "Response when the api call made was unauthorized.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "The API token was invalid.")
            )
    )
    @GetMapping("/get-type-paged")
    public StandardJsonResponse<PaginatedJobsDTO> getJobsWithinIntervalByTypePaged(
            @Parameter(name = "Job Type", description = "Job type to lookup", example = "FETCH_MARKET_NEWS")
            final @RequestParam("jobType") String jobType,
            @Parameter(name = "Start Date", description = "Start date of time period to analyze", example = "2025-01-01")
            final @RequestParam("start") String start,
            @Parameter(name = "End Date", description = "End date of time period to analyze", example = "2025-01-01")
            final @RequestParam("end") String end,
            @Parameter(name = "Page", description = "Current Page", example = "0")
            final @RequestParam(value = "page", defaultValue = "0") int page,
            @Parameter(name = "Page Size", description = "Size of page", example = "25")
            final @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            final HttpServletRequest request
    ) {
        validateLocalDateFormat(start, CorePlatformConstants.DATE_FORMAT, String.format(CorePlatformConstants.Validation.DateTime.START_DATE_INVALID_FORMAT, start, CorePlatformConstants.DATE_FORMAT));
        validateLocalDateFormat(end, CorePlatformConstants.DATE_FORMAT, String.format(CorePlatformConstants.Validation.DateTime.END_DATE_INVALID_FORMAT, end, CorePlatformConstants.DATE_FORMAT));

        final Page<Job> jobs = this.jobService.findJobsByTypePaged(LocalDate.parse(start, DateTimeFormatter.ISO_DATE).atStartOfDay(), LocalDate.parse(end, DateTimeFormatter.ISO_DATE).atStartOfDay(), GenericEnum.getByCode(JobType.class, jobType), page, pageSize);
        return StandardJsonResponse
                .<PaginatedJobsDTO>builder()
                .success(true)
                .data(
                        PaginatedJobsDTO
                                .builder()
                                .page(jobs.getPageable().getPageNumber())
                                .pageSize(jobs.getPageable().getPageSize())
                                .jobs(jobs.map(jb -> this.jobDTOConverter.convert(jb)).stream().toList())
                                .totalElements(jobs.getNumberOfElements())
                                .totalPages(jobs.getTotalPages())
                                .build()
                )
                .build();
    }

    /**
     * Returns a {@link StandardJsonResponse} containing {@link Job}s for the given interval of time and status, type
     *
     * @param request {@link HttpServletRequest}
     * @param start   start date & time
     * @param end     end date & time
     * @param jobStatus job status
     * @param jobType job type
     * @param page page
     * @param pageSize pageSize
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Obtains paginated jobs for the given interval of time and status, type", description = "Returns a list of paginated jobs filtered by their status and type.")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api call was made with an invalid start date.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "Invalid date")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api call was made with an invalid end date.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "Invalid date")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api call was made with an invalid status.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "Invalid filter")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api call was made with an invalid type.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "Invalid filter")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api cannot find any jobs for the given interval of time and status, type.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "No jobs found for interval <start> : <end>")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully obtains the jobs within the given time interval for the given status and type.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "401",
            description = "Response when the api call made was unauthorized.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "The API token was invalid.")
            )
    )
    @GetMapping("/get-status-type-paged")
    public StandardJsonResponse<PaginatedJobsDTO> getJobsWithinIntervalByStatusAndTypePaged(
            @Parameter(name = "Job Status", description = "Job status to lookup", example = "COMPLETED")
            final @RequestParam("jobStatus") String jobStatus,
            @Parameter(name = "Job Type", description = "Job type to lookup", example = "FETCH_MARKET_NEWS")
            final @RequestParam("jobType") String jobType,
            @Parameter(name = "Start Date", description = "Start date of time period to analyze", example = "2025-01-01")
            final @RequestParam("start") String start,
            @Parameter(name = "End Date", description = "End date of time period to analyze", example = "2025-01-01")
            final @RequestParam("end") String end,
            @Parameter(name = "Page", description = "Current Page", example = "0")
            final @RequestParam(value = "page", defaultValue = "0") int page,
            @Parameter(name = "Page Size", description = "Size of page", example = "25")
            final @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            final HttpServletRequest request
    ) {
        validateLocalDateFormat(start, CorePlatformConstants.DATE_FORMAT, String.format(CorePlatformConstants.Validation.DateTime.START_DATE_INVALID_FORMAT, start, CorePlatformConstants.DATE_FORMAT));
        validateLocalDateFormat(end, CorePlatformConstants.DATE_FORMAT, String.format(CorePlatformConstants.Validation.DateTime.END_DATE_INVALID_FORMAT, end, CorePlatformConstants.DATE_FORMAT));

        final Page<Job> jobs = this.jobService.findJobsByStatusAndTypePaged(LocalDate.parse(start, DateTimeFormatter.ISO_DATE).atStartOfDay(), LocalDate.parse(end, DateTimeFormatter.ISO_DATE).atStartOfDay(), GenericEnum.getByCode(JobStatus.class, jobStatus), GenericEnum.getByCode(JobType.class, jobType), page, pageSize);
        return StandardJsonResponse
                .<PaginatedJobsDTO>builder()
                .success(true)
                .data(
                        PaginatedJobsDTO
                                .builder()
                                .page(jobs.getPageable().getPageNumber())
                                .pageSize(jobs.getPageable().getPageSize())
                                .jobs(jobs.map(jb -> this.jobDTOConverter.convert(jb)).stream().toList())
                                .totalElements(jobs.getNumberOfElements())
                                .totalPages(jobs.getTotalPages())
                                .build()
                )
                .build();
    }
}
