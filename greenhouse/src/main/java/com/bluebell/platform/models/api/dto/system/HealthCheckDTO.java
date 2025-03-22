package com.bluebell.platform.models.api.dto.system;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

/**
 * Class representation of basic system diagnostics & information
 *
 * @author Stephen Prizio
 * @version 0.1.3
 */
@Getter
@Builder
@Schema(title = "HealthCheckDTO", name = "HealthCheckDTO", description = "API information and schematics for external system consumption")
public class HealthCheckDTO {

    @Schema(description = "The base domain for the app")
    private String domain;

    @Schema(description = "The base domain for all api calls")
    private String baseApiDomain;

    @Schema(description = "The current deployed version of the application")
    private String version;

    @Schema(description = "The api version")
    private String apiVersion;
}
