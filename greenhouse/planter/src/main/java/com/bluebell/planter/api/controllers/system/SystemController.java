package com.bluebell.planter.api.controllers.system;

import com.bluebell.planter.api.controllers.AbstractApiController;
import com.bluebell.platform.models.api.json.StandardJsonResponse;
import com.bluebell.processing.api.examples.ApiErrorExamples;
import com.bluebell.processing.api.examples.ApiPrimitiveExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.bluebell.planter.core.validation.GenericValidator.validateJsonIntegrity;

/**
 * Controller to handle system functions
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
@RestController
@RequestMapping("${base.api.controller.endpoint}/system")
@Tag(name = "System", description = "Handles endpoints & operations related to the bluebell system.")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT})
public class SystemController extends AbstractApiController {


    //  METHODS


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
            description = "Response when the api call was made with an request object.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class),
                    examples = @ExampleObject(value = ApiErrorExamples.BAD_QUERY_EXAMPLE)
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully submits the contact form query.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class),
                    examples = @ExampleObject(value = ApiPrimitiveExamples.STRING_EXAMPLE)
            )
    )
    @PostMapping("/contact")
    public StandardJsonResponse<String> postContact(final @RequestBody Map<String, Object> data) {
        validateJsonIntegrity(data, List.of("contact"), "json did not contain of the required keys : %s", List.of("contact").toString());
        return new StandardJsonResponse<>(true, "Thanks for your message! We will get back to you shortly.", StringUtils.EMPTY);
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
            description = "Response when the api call was made with an request object.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class),
                    examples = @ExampleObject(value = ApiErrorExamples.BAD_QUERY_EXAMPLE)
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully submits the report form.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class),
                    examples = @ExampleObject(value = ApiPrimitiveExamples.STRING_EXAMPLE)
            )
    )
    @PostMapping("/report")
    public StandardJsonResponse<String> postReport(final @RequestBody Map<String, Object> data) {
        validateJsonIntegrity(data, List.of("report"), "json did not contain of the required keys : %s", List.of("report").toString());
        return new StandardJsonResponse<>(true, "Thanks for your message! We will get back to you shortly.", StringUtils.EMPTY);
    }
}
