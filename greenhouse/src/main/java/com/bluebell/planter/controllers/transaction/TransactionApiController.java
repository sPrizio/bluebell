package com.bluebell.planter.controllers.transaction;

import com.bluebell.planter.constants.ApiPaths;
import com.bluebell.planter.controllers.AbstractApiController;
import com.bluebell.planter.converters.transaction.TransactionDTOConverter;
import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.GenericEnum;
import com.bluebell.platform.enums.transaction.TransactionStatus;
import com.bluebell.platform.enums.transaction.TransactionType;
import com.bluebell.platform.models.api.dto.transaction.CreateUpdateTransactionDTO;
import com.bluebell.platform.models.api.dto.transaction.PaginatedTransactionsDTO;
import com.bluebell.platform.models.api.dto.transaction.TransactionDTO;
import com.bluebell.platform.models.api.json.StandardJsonResponse;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.entities.security.User;
import com.bluebell.platform.models.core.entities.transaction.Transaction;
import com.bluebell.radicle.exceptions.validation.MissingRequiredDataException;
import com.bluebell.radicle.security.aspects.ValidateApiToken;
import com.bluebell.radicle.security.constants.SecurityConstants;
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
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.javatuples.Triplet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static com.bluebell.radicle.validation.GenericValidator.validateLocalDateTimeFormat;

/**
 * Api Controller for {@link Transaction}
 *
 * @author Stephen Prizio
 * @version 0.2.5
 */
@RestController
@RequestMapping("${bluebell.base.api.controller.endpoint}" + ApiPaths.Transaction.BASE)
@Tag(name = "Transaction", description = "Handles endpoints & operations related to an account's transactions")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT})
public class TransactionApiController extends AbstractApiController {

    private static final String NO_ACCOUNT_FOR_ACCOUNT_NUMBER = "No account was found for account number %d";
    private static final String NO_TRANSACTION_FOR_ACCOUNT_AND_NUMBER = "No transaction was found for account %d and transaction number %d";

    @Resource(name = "accountService")
    private AccountService accountService;

    @Resource(name = "transactionDTOConverter")
    private TransactionDTOConverter transactionDTOConverter;

    @Resource(name = "transactionService")
    private TransactionService transactionService;


    //  METHODS


    //  ----------------- GET REQUESTS -----------------

    /**
     * Returns a {@link List} of recent (within 6 months) {@link Transaction}s for the given {@link Account} number
     *
     * @param accountNumber {@link Account} number
     * @param request {@link HttpServletRequest}
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Obtains recent transactions for the account", description = "Returns a list of recent transactions for the given account.")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api cannot find any recent transactions for the given account",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "No transactions found for type deposit")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api cannot find the account for the given number",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "No account found for number <bad_number>")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully obtains the recent transactions for the given account.",
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
    @GetMapping(ApiPaths.Transaction.GET_RECENT_FOR_ACCOUNT)
    public StandardJsonResponse<List<TransactionDTO>> getRecentTransactionsForAccount(
            @Parameter(name = "accountNumber", description = "Account number", example = "1234")
            final @RequestParam("accountNumber") long accountNumber,
            final HttpServletRequest request
    ) {
        final Optional<Account> account = this.accountService.findAccountByAccountNumber(accountNumber);
        if (account.isPresent()) {
            final List<Transaction> transactions = this.transactionService.findRecentTransactions(account.get());
            return StandardJsonResponse
                    .<List<TransactionDTO>>builder()
                    .success(true)
                    .data(this.transactionDTOConverter.convertAll(transactions))
                    .build();
        }

        return StandardJsonResponse
                .<List<TransactionDTO>>builder()
                .success(false)
                .message(String.format(NO_ACCOUNT_FOR_ACCOUNT_NUMBER, accountNumber))
                .build();
    }

    /**
     * Returns a {@link List} of {@link Transaction}s for the given {@link Account} number
     *
     * @param accountNumber {@link Account} number
     * @param request {@link HttpServletRequest}
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Obtains all transactions for the account", description = "Returns a list of transactions for the given account.")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api cannot find any transactions for the given account",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "No transactions found for type deposit")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api cannot find the account for the given number",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "No account found for number <bad_number>")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully obtains the transactions for the given account.",
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
    @GetMapping(ApiPaths.Transaction.GET_FOR_ACCOUNT)
    public StandardJsonResponse<List<TransactionDTO>> getAllTransactionsForAccount(
            @Parameter(name = "accountNumber", description = "Account number", example = "1234")
            final @RequestParam("accountNumber") long accountNumber,
            final HttpServletRequest request
    ) {
        final Optional<Account> account = this.accountService.findAccountByAccountNumber(accountNumber);
        if (account.isPresent()) {
            final List<Transaction> transactions = this.transactionService.findAllTransactionsForAccount(account.get());
            return StandardJsonResponse
                    .<List<TransactionDTO>>builder()
                    .success(true)
                    .data(this.transactionDTOConverter.convertAll(transactions))
                    .build();
        }

        return StandardJsonResponse
                .<List<TransactionDTO>>builder()
                .success(false)
                .message(String.format(NO_ACCOUNT_FOR_ACCOUNT_NUMBER, accountNumber))
                .build();
    }

    /**
     * Returns a {@link List} of {@link Transaction}s filtered by their type
     *
     * @param transactionType {@link TransactionType}
     * @param accountNumber {@link Account} number
     * @param request {@link HttpServletRequest}
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Obtains transactions based on their type", description = "Returns a list of transactions filtered by their type.")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api call was made with an invalid transaction type",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "Invalid transaction type")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api cannot find any transactions for the given transaction type",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "No transactions found for type deposit")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api cannot find the account for the given number",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "No account found for number <bad_number>")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully obtains the transactions matching the given type.",
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
    @GetMapping(ApiPaths.Transaction.GET_BY_TYPE_FOR_ACCOUNT)
    public StandardJsonResponse<List<TransactionDTO>> getTransactionsByTypeForAccount(
            @Parameter(name = "transactionType", description = "Transaction type", example = "DEPOSIT")
            final @RequestParam("transactionType") String transactionType,
            @Parameter(name = "accountNumber", description = "Account number", example = "1234")
            final @RequestParam("accountNumber") long accountNumber,
            final HttpServletRequest request
    ) {
        if (!EnumUtils.isValidEnumIgnoreCase(TransactionType.class, transactionType)) {
            return StandardJsonResponse
                    .<List<TransactionDTO>>builder()
                    .success(false)
                    .message(String.format("%s is not a valid transaction type", transactionType))
                    .build();
        }

        final Optional<Account> account = this.accountService.findAccountByAccountNumber(accountNumber);
        if (account.isPresent()) {
            final List<Transaction> transactions = this.transactionService.findAllTransactionsByTypeForAccount(GenericEnum.getByCode(TransactionType.class, transactionType), account.get());
            return StandardJsonResponse
                    .<List<TransactionDTO>>builder()
                    .success(true)
                    .data(this.transactionDTOConverter.convertAll(transactions))
                    .build();
        }

        return StandardJsonResponse
                .<List<TransactionDTO>>builder()
                .success(false)
                .message(String.format(NO_ACCOUNT_FOR_ACCOUNT_NUMBER, accountNumber))
                .build();
    }

    /**
     * Returns a {@link List} of {@link Transaction}s filtered by their status
     *
     * @param transactionStatus {@link TransactionStatus}
     * @param accountNumber {@link Account} number
     * @param request {@link HttpServletRequest}
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Obtains transactions based on their status", description = "Returns a list of transactions filtered by their status.")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api call was made with an invalid transaction status",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "Invalid transaction status")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api cannot find any transactions for the given transaction status",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "No transactions found for status completed")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api cannot find the account for the given number",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "No account found for number <bad_number>")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully obtains the transactions matching the given status.",
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
    @GetMapping(ApiPaths.Transaction.GET_BY_STATUS_FOR_ACCOUNT)
    public StandardJsonResponse<List<TransactionDTO>> getTransactionsByStatusForAccount(
            @Parameter(name = "transactionStatus", description = "Transaction status", example = "COMPLETED")
            final @RequestParam("transactionStatus") String transactionStatus,
            @Parameter(name = "accountNumber", description = "Account number", example = "1234")
            final @RequestParam("accountNumber") long accountNumber,
            final HttpServletRequest request
    ) {
        if (!EnumUtils.isValidEnumIgnoreCase(TransactionStatus.class, transactionStatus)) {
            return StandardJsonResponse
                    .<List<TransactionDTO>>builder()
                    .success(false)
                    .message(String.format("%s is not a valid transaction status", transactionStatus))
                    .build();
        }

        final Optional<Account> account = this.accountService.findAccountByAccountNumber(accountNumber);
        if (account.isPresent()) {
            final List<Transaction> transactions = this.transactionService.findAllTransactionsByStatusForAccount(GenericEnum.getByCode(TransactionStatus.class, transactionStatus), account.get());
            return StandardJsonResponse
                    .<List<TransactionDTO>>builder()
                    .success(true)
                    .data(this.transactionDTOConverter.convertAll(transactions))
                    .build();
        }

        return StandardJsonResponse
                .<List<TransactionDTO>>builder()
                .success(false)
                .message(String.format(NO_ACCOUNT_FOR_ACCOUNT_NUMBER, accountNumber))
                .build();
    }

    /**
     * Returns a {@link List} of {@link Transaction}s within the given time span
     *
     * @param start start time
     * @param end end time
     * @param accountNumber {@link Account} number
     * @param request {@link HttpServletRequest}
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Obtains transactions for the given interval of time", description = "Returns a list of transactions filtered by their transaction date & time.")
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
            description = "Response when the api cannot find any transactions for the given interval of time.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "No transactions found for interval <start> : <end>")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api cannot find the account for the given number",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "No account found for number <bad_number>")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully obtains the transactions within the given time interval.",
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
    @GetMapping(ApiPaths.Transaction.GET_WITHIN_TIMESPAN_FOR_ACCOUNT)
    public StandardJsonResponse<List<TransactionDTO>> getTransactionsWithinTimespanForAccount(
            @Parameter(name = "start", description = "Start date of time period to analyze", example = "2025-01-01")
            final @RequestParam("start") String start,
            @Parameter(name = "end", description = "End date of time period to analyze", example = "2025-01-01")
            final @RequestParam("end") String end,
            @Parameter(name = "accountNumber", description = "Account number", example = "1234")
            final @RequestParam("accountNumber") long accountNumber,
            final HttpServletRequest request
    ) {
        validateLocalDateTimeFormat(start, CorePlatformConstants.DATE_TIME_FORMAT, String.format(CorePlatformConstants.Validation.DateTime.START_DATE_INVALID_FORMAT, start, CorePlatformConstants.DATE_TIME_FORMAT));
        validateLocalDateTimeFormat(end, CorePlatformConstants.DATE_TIME_FORMAT, String.format(CorePlatformConstants.Validation.DateTime.START_DATE_INVALID_FORMAT, end, CorePlatformConstants.DATE_TIME_FORMAT));

        final Optional<Account> account = this.accountService.findAccountByAccountNumber(accountNumber);
        if (account.isPresent()) {
            final List<Transaction> transactions = this.transactionService.findTransactionsWithinTimespanForAccount(
                    LocalDateTime.parse(start, DateTimeFormatter.ISO_DATE_TIME),
                    LocalDateTime.parse(end, DateTimeFormatter.ISO_DATE_TIME),
                    account.get()
            );
            return StandardJsonResponse
                    .<List<TransactionDTO>>builder()
                    .success(true)
                    .data(this.transactionDTOConverter.convertAll(transactions))
                    .build();
        }

        return StandardJsonResponse
                .<List<TransactionDTO>>builder()
                .success(false)
                .message(String.format(NO_ACCOUNT_FOR_ACCOUNT_NUMBER, accountNumber))
                .build();
    }

    /**
     * Returns a {@link Transaction} for the given name and account
     *
     * @param transactionNumber transaction number
     * @param accountNumber {@link Account} name
     * @param request {@link HttpServletRequest}
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Obtains the transaction based on its name and account", description = "Returns a transaction for the name and account.")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api cannot find the account for the given number",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "No account found for number <bad_number>")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api cannot find the transaction for the name and account",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "No transaction found for name <test_name>")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully obtains the transaction with given name for the account.",
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
    @GetMapping(ApiPaths.Transaction.GET_BY_NUMBER_FOR_ACCOUNT)
    public StandardJsonResponse<TransactionDTO> getTransactionForNumberAndAccount(
            @Parameter(name = "transactionNumber", description = "The unique identifier for the transaction", example = "1234")
            final @RequestParam("transactionNumber") long transactionNumber,
            @Parameter(name = "accountNumber", description = "Account number", example = "1234")
            final @RequestParam("accountNumber") long accountNumber,
            final HttpServletRequest request
    ) {
        final Optional<Account> account = this.accountService.findAccountByAccountNumber(accountNumber);
        if (account.isPresent()) {
            final Optional<Transaction> transaction = this.transactionService.findTransactionForNumber(transactionNumber, account.get());
            return transaction.map(value -> StandardJsonResponse.<TransactionDTO>builder().success(true).data(this.transactionDTOConverter.convert(transaction.get())).build()).orElseGet(() -> StandardJsonResponse.<TransactionDTO>builder().success(false).message(String.format(NO_TRANSACTION_FOR_ACCOUNT_AND_NUMBER, accountNumber, transactionNumber)).build());
        }

        return StandardJsonResponse
                .<TransactionDTO>builder()
                .success(false)
                .message(String.format(NO_ACCOUNT_FOR_ACCOUNT_NUMBER, accountNumber))
                .build();
    }

    /**
     * Returns a {@link StandardJsonResponse} containing {@link Transaction}s for the given interval of time
     *
     * @param request  {@link HttpServletRequest}
     * @param start    start date & time
     * @param end      end date & time
     * @param page     page
     * @param pageSize pageSize
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Obtains paginated transactions for the given interval of time", description = "Returns a list of paginated transactions filtered by their transaction time.")
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
            description = "Response when the api cannot find any transactions for the given interval of time.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "No transactions found for interval <start> : <end>")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully obtains the transactions within the given time interval.",
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
    @GetMapping(ApiPaths.Transaction.GET_FOR_INTERVAL_PAGED)
    public StandardJsonResponse<PaginatedTransactionsDTO> getTransactionsWithinIntervalPaged(
            @Parameter(name = "accountNumber", description = "The unique identifier for your trading account", example = "1234")
            final @RequestParam("accountNumber") long accountNumber,
            @Parameter(name = "start", description = "Start date of time period to analyze", example = "2025-01-01T00:00:00")
            final @RequestParam("start") String start,
            @Parameter(name = "end", description = "End date of time period to analyze", example = "2025-01-01T00:00:00")
            final @RequestParam("end") String end,
            @Parameter(name = "page", description = "Current Page", example = "0")
            final @RequestParam(value = "page", defaultValue = "0") int page,
            @Parameter(name = "pageSize", description = "Size of page", example = "25")
            final @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @Parameter(name = "transactionType", description = "Type of transaction", example = "DEPOSIT")
            final @RequestParam(value = "transactionType", defaultValue = "ALL") String transactionType,
            @Parameter(name = "transactionStatus", description = "Transaction status", example = "PENDING")
            final @RequestParam(value = "transactionStatus", defaultValue = "ALL") String transactionStatus,
            @Parameter(name = "sort", description = "Sort order", example = "asc")
            final @RequestParam(value = "sort", defaultValue = "asc") String sort,
            final HttpServletRequest request
    ) {
        validateLocalDateTimeFormat(start, CorePlatformConstants.DATE_TIME_FORMAT, String.format(CorePlatformConstants.Validation.DateTime.START_DATE_INVALID_FORMAT, start, CorePlatformConstants.DATE_TIME_FORMAT));
        validateLocalDateTimeFormat(end, CorePlatformConstants.DATE_TIME_FORMAT, String.format(CorePlatformConstants.Validation.DateTime.START_DATE_INVALID_FORMAT, end, CorePlatformConstants.DATE_TIME_FORMAT));

        final User user = (User) request.getAttribute(SecurityConstants.USER_REQUEST_KEY);
        final Triplet<TransactionType, TransactionStatus, Sort> filters = resolveFilters(transactionType, transactionStatus, sort);

        Page<Transaction> transactions;
        final LocalDateTime startTime = LocalDateTime.parse(start, DateTimeFormatter.ISO_DATE_TIME);
        final LocalDateTime endTime = LocalDateTime.parse(end, DateTimeFormatter.ISO_DATE_TIME);
        final Account account = getAccountForId(user, accountNumber);

        if (filters.getValue0() != null && filters.getValue1() != null) {
            //  filter by type and status
            transactions = this.transactionService.findAllTransactionsForTypeAndStatusWithinTimespan(startTime, endTime, filters.getValue0(), filters.getValue1(), account, page, pageSize, filters.getValue2());
        } else if (filters.getValue0() != null) {
            //  filter by type
            transactions = this.transactionService.findAllTransactionsForTypeWithinDatePaged(startTime, endTime, account, filters.getValue0(), page, pageSize, filters.getValue2());
        } else if (filters.getValue1() != null) {
            //  filter by status
            transactions = this.transactionService.findAllTransactionsForStatusWithinDatePaged(startTime, endTime, account, filters.getValue1(), page, pageSize, filters.getValue2());
        } else {
            transactions = this.transactionService.findAllTransactionsWithinDatePaged(startTime, endTime, account, page, pageSize, filters.getValue2());
        }

        return StandardJsonResponse
                .<PaginatedTransactionsDTO>builder()
                .success(true)
                .data(
                        PaginatedTransactionsDTO
                                .builder()
                                .page(transactions.getPageable().getPageNumber())
                                .pageSize(transactions.getPageable().getPageSize())
                                .transactions(transactions.map(tr -> this.transactionDTOConverter.convert(tr)).stream().toList())
                                .totalElements(transactions.getNumberOfElements())
                                .totalPages(transactions.getTotalPages())
                                .build()
                )
                .build();
    }


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
    @PostMapping(ApiPaths.Transaction.CREATE_TRANSACTION)
    public StandardJsonResponse<TransactionDTO> postCreateNewAccount(
            @Parameter(name = "transactionPayload", description = "Payload for creating or updating transactions")
            final @RequestBody CreateUpdateTransactionDTO data,
            @Parameter(name = "accountNumber", description = "Account number to add the transaction to", example = "1234")
            final @RequestParam("accountNumber") long accountNumber,
            final HttpServletRequest request
    ) {
        if (data == null || StringUtils.isEmpty(data.name())) {
            throw new MissingRequiredDataException("The required data for creating a Transaction entity was null or empty");
        }

        final User user = (User) request.getAttribute(SecurityConstants.USER_REQUEST_KEY);
        final Account account = getAccountForId(user, accountNumber);
        final Transaction transaction = this.transactionService.createNewTransaction(data, account);
        this.accountService.refreshAccount(account);

        return StandardJsonResponse
                .<TransactionDTO>builder()
                .success(true)
                .data(this.transactionDTOConverter.convert(transaction))
                .build();
    }


    //  ----------------- PUT REQUESTS -----------------

    /**
     * Returns an updated {@link Transaction}
     *
     * @param data {@link CreateUpdateTransactionDTO}
     * @param accountNumber account number
     * @param transactionNumber transaction number
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
    @PutMapping(ApiPaths.Transaction.UPDATE_TRANSACTION)
    public StandardJsonResponse<TransactionDTO> putUpdateTransaction(
            @Parameter(name = "accountNumber", description = "The unique identifier for your trading account", example = "1234")
            final @RequestParam("accountNumber") long accountNumber,
            @Parameter(name = "transactionNumber", description = "The unique identifier for the transaction", example = "1234")
            final @RequestParam("transactionNumber") long transactionNumber,
            @Parameter(name = "Transaction Payload", description = "Payload for creating or updating transactions")
            final @RequestBody CreateUpdateTransactionDTO data,
            final HttpServletRequest request
    ) {
        if (data == null || StringUtils.isEmpty(data.name()) || StringUtils.isEmpty(data.originalName())) {
            throw new MissingRequiredDataException("The required data for updating a Transaction entity was null or empty");
        }

        final User user = (User) request.getAttribute(SecurityConstants.USER_REQUEST_KEY);
        final Account account = getAccountForId(user, accountNumber);
        final Optional<Transaction> transaction = this.transactionService.findTransactionForNumber(transactionNumber, account);

        if (transaction.isEmpty()) {
            return StandardJsonResponse
                    .<TransactionDTO>builder()
                    .success(false)
                    .message(String.format(NO_TRANSACTION_FOR_ACCOUNT_AND_NUMBER, accountNumber, transactionNumber))
                    .build();
        }

        final Transaction updated = this.transactionService.updateTransaction(transaction.get(), data, account);
        this.accountService.refreshAccount(account);
        return StandardJsonResponse
                .<TransactionDTO>builder()
                .success(true)
                .data(this.transactionDTOConverter.convert(updated))
                .build();
    }


    //  ----------------- DELETE REQUESTS -----------------

    /**
     * Deletes a {@link Transaction} with the matching name & account
     *
     * @param transactionNumber transaction number
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
    @DeleteMapping(ApiPaths.Transaction.DELETE_TRANSACTION)
    public StandardJsonResponse<Boolean> deleteTransaction(
            @Parameter(name = "transactionNumber", description = "The unique identifier for the transaction", example = "1234")
            final @RequestParam("transactionNumber") long transactionNumber,
            @Parameter(name = "accountNumber", description = "The unique identifier for your trading account", example = "1234")
            final @RequestParam("accountNumber") long accountNumber,
            final HttpServletRequest request
    ) {

        final User user = (User) request.getAttribute(SecurityConstants.USER_REQUEST_KEY);
        final Account account = getAccountForId(user, accountNumber);
        final Optional<Transaction> transaction = this.transactionService.findTransactionForNumber(transactionNumber, account);

        return transaction.map(value -> {
            final boolean result = this.transactionService.deleteTransaction(value);
            if (result) {
                this.accountService.refreshAccount(account);
            }

            return StandardJsonResponse.<Boolean>builder().success(result).data(result).build();
        }).orElseGet(() -> StandardJsonResponse.<Boolean>builder().success(false).data(false).message(String.format(NO_TRANSACTION_FOR_ACCOUNT_AND_NUMBER, accountNumber, transactionNumber)).build());
    }


    //  HELPERS

    /**
     * Resolves the paged transaction filters
     *
     * @param type transaction type
     * @param status transaction status
     * @param sort sort order
     * @return {@link Triplet}
     */
    private Triplet<TransactionType, TransactionStatus, Sort> resolveFilters(final String type, final String status, final String sort) {

        TransactionType transactionType = null;
        TransactionStatus transactionStatus = null;
        final Sort finalSort;

        if (StringUtils.isNotEmpty(type) && EnumUtils.isValidEnumIgnoreCase(TransactionType.class, type)) {
            transactionType = GenericEnum.getByCode(TransactionType.class, type);
        }

        if (StringUtils.isNotEmpty(status) && EnumUtils.isValidEnumIgnoreCase(TransactionStatus.class, status)) {
            transactionStatus = GenericEnum.getByCode(TransactionStatus.class, status);
        }

        if (sort.equalsIgnoreCase("asc")) {
            finalSort = Sort.by(Sort.Direction.ASC, "transactionDate");
        } else {
            finalSort = Sort.by(Sort.Direction.DESC, "transactionDate");
        }

        return Triplet.with(transactionType, transactionStatus, finalSort);
    }
}
