package com.bluebell.planter.controllers.security;

import com.bluebell.planter.constants.ApiPaths;
import com.bluebell.planter.converters.security.UserDTOConverter;
import com.bluebell.platform.models.api.dto.security.UserDTO;
import com.bluebell.platform.models.api.dto.security.UsernamePasswordRequestDTO;
import com.bluebell.platform.models.api.json.StandardJsonResponse;
import com.bluebell.platform.models.core.entities.security.User;
import com.bluebell.radicle.security.services.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.springframework.web.bind.annotation.*;

/**
 * API controller for security operations
 *
 * @author Stephen Prizio
 * @version 0.2.6
 */
@RestController
@RequestMapping("${bluebell.base.api.controller.endpoint}" + ApiPaths.Security.BASE)
@Tag(name = "Security", description = "Handles endpoints & operations related to security & authentication.")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT})
public class SecurityApiController {

    @Resource(name = "securityService")
    private SecurityService securityService;

    @Resource(name = "userDTOConverter")
    private UserDTOConverter userDTOConverter;


    //  METHODS


    //  ----------------- POST REQUESTS -----------------

    /**
     * Checks if a user exists
     *
     * @param data {@link UsernamePasswordRequestDTO}
     * @return {@link StandardJsonResponse}
     */
    @Operation(summary = "Checks if a user exists", description = "Checks if a user exists for the given username or email.")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully checks the user information.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class)
            )
    )
    @PostMapping(ApiPaths.Security.IS_USER_TAKEN)
    public StandardJsonResponse<Boolean> postIsUserTaken(final @RequestBody UsernamePasswordRequestDTO data) {
        final Triplet<Boolean, Boolean, User> result = this.securityService.isUserTaken(data.usernameEmail());
        return StandardJsonResponse
                .<Boolean>builder()
                .success(result.getValue0() || result.getValue1())
                .data(result.getValue0() || result.getValue1())
                .build();
    }

    /**
     * Logs a user into bluebell
     *
     * @param data {@link UsernamePasswordRequestDTO}
     * @return {@link StandardJsonResponse}
     */
    @Operation(summary = "Logs a user in", description = "Attempts to log a user into bluebell.")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully logs the user in.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class)
            )
    )
    @PostMapping(ApiPaths.Security.LOGIN)
    public StandardJsonResponse<UserDTO> postLogin(final @RequestBody UsernamePasswordRequestDTO data) {
        final Pair<String, User> login = this.securityService.login(data.usernameEmail(), data.password());
        return StandardJsonResponse
                .<UserDTO>builder()
                .success(StringUtils.isEmpty(login.getValue0()))
                .data(this.userDTOConverter.convert(login.getValue1()))
                .message(login.getValue0())
                .build();
    }
}
