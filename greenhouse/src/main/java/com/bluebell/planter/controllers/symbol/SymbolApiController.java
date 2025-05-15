package com.bluebell.planter.controllers.symbol;

import com.bluebell.planter.constants.ApiPaths;
import com.bluebell.planter.controllers.AbstractApiController;
import com.bluebell.platform.models.api.json.StandardJsonResponse;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.radicle.security.aspects.ValidateApiToken;
import com.bluebell.radicle.services.account.AccountService;
import com.bluebell.radicle.services.symbol.SymbolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

/**
 * API controller for symbols & equities
 *
 * @author Stephen Prizio
 * @version 0.2.0
 */
@RestController
@RequestMapping("${bluebell.base.api.controller.endpoint}" + ApiPaths.Symbol.BASE)
@Tag(name = "Symbol", description = "Handles endpoints & operations related to obtaining symbol/equities information.")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT})
public class SymbolApiController extends AbstractApiController {

    private static final String NO_ACCOUNT_FOR_ACCOUNT_NUMBER = "No account was found for account number %d";

    @Resource(name = "accountService")
    private AccountService accountService;

    @Resource(name = "symbolService")
    private SymbolService symbolService;


    //  METHODS


    //  ----------------- GET REQUESTS -----------------

    @ValidateApiToken
    @Operation(summary = "Get the symbols traded on the account", description = "Returns a set of symbols/equities traded on the given account")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully retrieves the traded symbols.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api cannot find the account  for the given number.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "No account was found for number 1234")
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
    @GetMapping(ApiPaths.Symbol.GET_TRADED_SYMBOLS)
    public StandardJsonResponse<Set<String>> getTradedSymbols(
            @Parameter(name = "accountNumber", description = "The number for the account to lookup", example = "1234")
            final @RequestParam("accountNumber") long accountNumber,
            final HttpServletRequest request
    ) {
        final Optional<Account> account = this.accountService.findAccountByAccountNumber(accountNumber);
        return account.map(value -> StandardJsonResponse.<Set<String>>builder().success(true).data(this.symbolService.getTradedSymbolsForAccount(value)).build()).orElseGet(() -> StandardJsonResponse.<Set<String>>builder().success(false).message(String.format(NO_ACCOUNT_FOR_ACCOUNT_NUMBER, accountNumber)).build());
    }
}
