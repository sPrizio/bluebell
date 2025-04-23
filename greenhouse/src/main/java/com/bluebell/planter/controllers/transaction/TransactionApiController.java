package com.bluebell.planter.controllers.transaction;

import com.bluebell.planter.controllers.AbstractApiController;
import com.bluebell.planter.converters.transaction.TransactionDTOConverter;
import com.bluebell.platform.models.api.dto.transaction.CreateUpdateTransactionDTO;
import com.bluebell.platform.models.api.dto.transaction.TransactionDTO;
import com.bluebell.platform.models.api.json.StandardJsonResponse;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.entities.transaction.Transaction;
import com.bluebell.radicle.exceptions.validation.MissingRequiredDataException;
import com.bluebell.radicle.security.aspects.ValidateApiToken;
import com.bluebell.radicle.services.account.AccountService;
import com.bluebell.radicle.services.transaction.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Api Controller for {@link Transaction}
 *
 * @author Stephen Prizio
 * @version 0.1.7
 */
@RestController
@RequestMapping("${bluebell.base.api.controller.endpoint}/transaction")
@Tag(name = "Transaction", description = "Handles endpoints & operations related to an account's transactions")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT})
public class TransactionApiController extends AbstractApiController {

    private static final String NO_ACCOUNT_FOR_ACCOUNT_NUMBER = "No account was found for account number %d";
    private static final String NO_TRANSACTION_FOR_ACCOUNT_AND_NAME = "No transaction was found for account %d and transaction name %s";

    @Resource(name = "accountService")
    private AccountService accountService;

    @Resource(name = "transactionDTOConverter")
    private TransactionDTOConverter transactionDTOConverter;

    @Resource(name = "transactionService")
    private TransactionService transactionService;


    //  METHODS


    //  ----------------- POST REQUESTS -----------------

    /**
     * Creates a {@link Transaction}
     *
     * @param request     {@link HttpServletRequest}
     * @param data {@link CreateUpdateTransactionDTO}
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Creates a new transaction", description = "Creates a new transaction for the given account")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully creates a new transaction.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api cannot not find the requested account",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "Account not found.")
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
    @PostMapping("/create-transaction")
    public StandardJsonResponse<TransactionDTO> postCreateNewAccount(
            @Parameter(name = "Transaction Payload", description = "Payload for creating or updating transactions")
            final @RequestBody CreateUpdateTransactionDTO data,
            @Parameter(name = "Account Number", description = "Account number to add the transaction to", example = "1234")
            final @RequestParam("accountNumber") long accountNumber,
            final HttpServletRequest request
    ) {
        if (data == null || StringUtils.isEmpty(data.name())) {
            throw new MissingRequiredDataException("The required data for creating a Transaction entity was null or empty");
        }

        final Optional<Account> account = this.accountService.findAccountByAccountNumber(accountNumber);
        if (account.isPresent()) {
            return StandardJsonResponse
                    .<TransactionDTO>builder()
                    .success(true)
                    .data(this.transactionDTOConverter.convert(this.transactionService.createNewTransaction(data, account.get())))
                    .build();
        }

        return StandardJsonResponse
                .<TransactionDTO>builder()
                .success(false)
                .message(String.format("Account not found for account number %d", accountNumber))
                .build();
    }


    //  ----------------- PUT REQUESTS -----------------

    /**
     * Returns an updated {@link Transaction}
     *
     * @param data {@link CreateUpdateTransactionDTO}
     * @param accountNumber account number
     * @param request {@link HttpServletRequest}
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Updates an existing transaction", description = "Updates an existing transaction for the given account.")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully updates the transaction.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api cannot not find the requested transaction",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "Transaction not found.")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api cannot find the account for the given account number.",
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
    @PutMapping("/update-transaction")
    public StandardJsonResponse<TransactionDTO> putUpdateTransaction(
            @Parameter(name = "Account Number", description = "The unique identifier for your trading account", example = "1234")
            final @RequestParam("accountNumber") long accountNumber,
            @Parameter(name = "Transaction Payload", description = "Payload for creating or updating transactions")
            final @RequestBody CreateUpdateTransactionDTO data,
            final HttpServletRequest request
    ) {
        if (data == null || StringUtils.isEmpty(data.name()) || StringUtils.isEmpty(data.originalName())) {
            throw new MissingRequiredDataException("The required data for updating a Transaction entity was null or empty");
        }

        final Optional<Account> account = this.accountService.findAccountByAccountNumber(accountNumber);
        if (account.isEmpty()) {
            return StandardJsonResponse
                    .<TransactionDTO>builder()
                    .success(false)
                    .message(String.format("Account not found for account number %d", accountNumber))
                    .build();
        }

        final Optional<Transaction> transaction = this.transactionService.findTransactionForNameAndAccount(data.originalName(), account.get());
        return transaction.map(value -> {
            final Transaction updated = this.transactionService.updateTransaction(value, data, account.get());
            return StandardJsonResponse.<TransactionDTO>builder().success(true).data(this.transactionDTOConverter.convert(updated)).build();
        }).orElseGet(() -> StandardJsonResponse.<TransactionDTO>builder().success(false).message(String.format(NO_TRANSACTION_FOR_ACCOUNT_AND_NAME, accountNumber, data.originalName())).build());
    }


    //  ----------------- DELETE REQUESTS -----------------

    /**
     * Deletes a {@link Transaction} with the matching name & account
     *
     * @param transactionName transaction name
     * @param accountNumber account number
     * @param request {@link HttpServletRequest}
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Deletes an account's transaction", description = "Deletes the account transaction with the matching name.")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully deletes a transaction.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api cannot find the account for the given account number.",
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
    @DeleteMapping("/delete-transaction")
    public StandardJsonResponse<Boolean> deleteTransaction(
            @Parameter(name = "Transaction Name", description = "The transaction's name")
            final @RequestParam("transactionName") String transactionName,
            @Parameter(name = "Account Number", description = "The unique identifier for your trading account", example = "1234")
            final @RequestParam("accountNumber") long accountNumber,
            final HttpServletRequest request
    ) {
        final Optional<Account> account = this.accountService.findAccountByAccountNumber(accountNumber);
        if (account.isEmpty()) {
            return StandardJsonResponse
                    .<Boolean>builder()
                    .success(false)
                    .data(false)
                    .message(String.format(NO_ACCOUNT_FOR_ACCOUNT_NUMBER, accountNumber))
                    .build();
        }

        final Optional<Transaction> transaction = this.transactionService.findTransactionForNameAndAccount(transactionName, account.get());
        return transaction.map(value -> {
            final boolean result = this.transactionService.deleteTransaction(value);
            return StandardJsonResponse.<Boolean>builder().success(result).data(result).build();
        }).orElseGet(() -> StandardJsonResponse.<Boolean>builder().success(false).data(false).message(String.format(NO_TRANSACTION_FOR_ACCOUNT_AND_NAME, accountNumber, transactionName)).build());
    }
}
