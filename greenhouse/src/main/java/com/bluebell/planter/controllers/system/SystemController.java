package com.bluebell.planter.controllers.system;

import com.bluebell.planter.constants.ApiPaths;
import com.bluebell.planter.controllers.AbstractApiController;
import com.bluebell.platform.enums.security.UserRole;
import com.bluebell.platform.models.api.dto.system.HealthCheckDTO;
import com.bluebell.platform.models.api.json.StandardJsonResponse;
import com.bluebell.radicle.security.aspects.ValidateApiToken;
import com.bluebell.radicle.services.system.IncomingPingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.bluebell.radicle.validation.GenericValidator.validateJsonIntegrity;

/**
 * Controller to handle system functions
 *
 * @author Stephen Prizio
 * @version 0.2.0
 */
@RestController
@RequestMapping("${bluebell.base.api.controller.endpoint}" + ApiPaths.System.BASE)
@Tag(name = "System", description = "Handles endpoints & operations related to the bluebell system.")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT})
public class SystemController extends AbstractApiController {

    @Value("${bluebell.base.api.controller.endpoint}")
    private String baseApiDomain;

    @Value("${bluebell.domain}")
    private String domain;

    @Value("${bluebell.version}")
    private String version;

    @Value("${bluebell.api.version}")
    private String apiVersion;

    @Resource(name = "incomingPingService")
    private IncomingPingService incomingPingService;


    //  METHODS


    //  ----------------- GET REQUESTS -----------------

    /**
     * Returns the application's information and diagnostics
     *
     * @return {@link StandardJsonResponse}
     */
    @Operation(summary = "Fetches the system's information", description = "Returns system & api information about the currently running bluebell application")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully returns system information.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class)
            )
    )
    @GetMapping(ApiPaths.System.HEALTHCHECK)
    public StandardJsonResponse<HealthCheckDTO> getHealthCheck() {
        return StandardJsonResponse
                .<HealthCheckDTO>builder()
                .success(true)
                .data(
                        HealthCheckDTO
                                .builder()
                                .domain(this.domain)
                                .baseApiDomain(this.baseApiDomain)
                                .version(this.version)
                                .apiVersion(this.apiVersion)
                                .build()
                )
                .build();
    }


    //  ----------------- POST REQUESTS -----------------

    /**
     * Handles the contact us form submission
     *
     * @param data input data
     * @return {@link StandardJsonResponse}
     */
    @Operation(summary = "Submits a contact form query", description = "Sends a client message to bluebell.")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api call was made with an invalid request object.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully submits the contact form query.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class)
            )
    )
    @PostMapping(ApiPaths.System.CONTACT)
    public StandardJsonResponse<String> postContact(final @RequestBody Map<String, Object> data) {
        validateJsonIntegrity(data, List.of("contact"), "json did not contain of the required keys : %s", List.of("contact").toString());
        return StandardJsonResponse
                .<String>builder()
                .success(true)
                .data("Thanks for your message! We will get back to you shortly.")
                .build();
    }

    /**
     * Handles the report issue form submission
     *
     * @param data input data
     * @return {@link StandardJsonResponse}
     */
    @Operation(summary = "Submits a contact form query", description = "Sends a client message to bluebell.")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api call was made with an invalid request object.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully submits the report form.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class)
            )
    )
    @PostMapping(ApiPaths.System.REPORT)
    public StandardJsonResponse<String> postReport(final @RequestBody Map<String, Object> data) {
        validateJsonIntegrity(data, List.of("report"), "json did not contain of the required keys : %s", List.of("report").toString());
        return StandardJsonResponse
                .<String>builder()
                .success(true)
                .data("Thanks for your message! We will get back to you shortly.")
                .build();
    }

    /**
     * Registers an incoming ping to the system
     *
     * @param systemName system name
     * @param request {@link HttpServletRequest}
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken(role = UserRole.SYSTEM)
    @Operation(summary = "Registers an incoming ping", description = "Acknowledges an incoming ping from an external system as a form of health check")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api was given a bad system name.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "<bad_system_name> was not found")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully acknowledges the ping.",
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
    @PostMapping(ApiPaths.System.ACKNOWLEDGE)
    public StandardJsonResponse<Boolean> postAcknowledgeIncomingPing(
            @Parameter(name = "systemName", description = "The system doing the pinging")
            final @RequestParam("systemName") String systemName,
            final HttpServletRequest request
    ) {
        final boolean result = this.incomingPingService.acknowledgeIncomingPing(systemName);
        return StandardJsonResponse
                .<Boolean>builder()
                .success(result)
                .data(result)
                .message(result ? String.format("%s was acknowledged successfully", systemName) : String.format("%s was not acknowledged.", systemName))
                .build();
    }
}