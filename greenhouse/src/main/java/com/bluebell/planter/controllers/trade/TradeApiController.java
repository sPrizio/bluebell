package com.bluebell.planter.controllers.trade;

import com.bluebell.planter.constants.ApiPaths;
import com.bluebell.planter.controllers.AbstractApiController;
import com.bluebell.planter.converters.trade.TradeDTOConverter;
import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.GenericEnum;
import com.bluebell.platform.enums.trade.TradeType;
import com.bluebell.platform.models.api.dto.trade.CreateUpdateTradeDTO;
import com.bluebell.platform.models.api.dto.trade.PaginatedTradesDTO;
import com.bluebell.platform.models.api.dto.trade.TradeDTO;
import com.bluebell.platform.models.api.json.StandardJsonResponse;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.entities.security.User;
import com.bluebell.platform.models.core.entities.trade.Trade;
import com.bluebell.platform.models.core.nonentities.records.trade.TradeInsights;
import com.bluebell.radicle.exceptions.validation.MissingRequiredDataException;
import com.bluebell.radicle.importing.services.strategy.GenericStrategyImportService;
import com.bluebell.radicle.importing.services.trade.GenericTradeImportService;
import com.bluebell.radicle.security.aspects.ValidateApiToken;
import com.bluebell.radicle.security.constants.SecurityConstants;
import com.bluebell.radicle.services.account.AccountService;
import com.bluebell.radicle.services.trade.TradeService;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static com.bluebell.radicle.importing.validation.ImportValidator.validateImportFileExtension;
import static com.bluebell.radicle.validation.GenericValidator.*;

/**
 * Api controller for {@link Trade}
 *
 * @author Stephen Prizio
 * @version 0.2.5
 */
@RestController
@RequestMapping("${bluebell.base.api.controller.endpoint}" + ApiPaths.Trade.BASE)
@Tag(name = "Trade", description = "Handles endpoints & operations related to trades.")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT})
public class TradeApiController extends AbstractApiController {

    private static final String TRADE_ID = "tradeId";
    private static final String NO_TRADE_FOR_TRADE_ID = "No trade found for trade id %s";

    @Resource(name = "accountService")
    private AccountService accountService;

    @Resource(name = "genericStrategyImportService")
    private GenericStrategyImportService genericStrategyImportService;

    @Resource(name = "genericTradeImportService")
    private GenericTradeImportService genericTradeImportService;

    @Resource(name = "tradeDTOConverter")
    private TradeDTOConverter tradeDTOConverter;

    @Resource(name = "tradeService")
    private TradeService tradeService;


    //  METHODS


    //  ----------------- GET REQUESTS -----------------

    /**
     * Returns a {@link StandardJsonResponse} containing {@link Trade}s for the given {@link TradeType}
     *
     * @param request   {@link HttpServletRequest}
     * @param tradeType {@link TradeType}
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Obtains trades based on their trade type", description = "Returns a list of trades filtered by their trade type.")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api call was made with an invalid trade type",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "Invalid trade type")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api cannot find any trades for the given trade type",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "No trades found for type buy")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully obtains the trades matching the given type.",
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
    @GetMapping(ApiPaths.Trade.GET_FOR_TYPE)
    public StandardJsonResponse<List<TradeDTO>> getTradesForTradeType(
            @Parameter(name = "accountNumber", description = "The unique identifier for your trading account", example = "1234")
            final @RequestParam("accountNumber") long accountNumber,
            @Parameter(name = "tradeType", description = "The type of trade to get", example = "SELL")
            final @RequestParam("tradeType") String tradeType,
            final HttpServletRequest request
    ) {

        if (!EnumUtils.isValidEnumIgnoreCase(TradeType.class, tradeType)) {
            return StandardJsonResponse
                    .<List<TradeDTO>>builder()
                    .success(false)
                    .message(String.format("%s is not a valid trade type", tradeType))
                    .build();
        }

        TradeType type = TradeType.valueOf(tradeType.toUpperCase());
        final User user = (User) request.getAttribute(SecurityConstants.USER_REQUEST_KEY);
        List<Trade> trades = this.tradeService.findAllByTradeType(type, getAccountForId(user, accountNumber));
        validateIfAnyResult(trades, "No trades were found for type %s", type.name());

        return StandardJsonResponse
                .<List<TradeDTO>>builder()
                .success(true)
                .data(this.tradeDTOConverter.convertAll(trades))
                .build();
    }

    /**
     * Returns a {@link StandardJsonResponse} containing {@link Trade}s for the given interval of time
     *
     * @param request {@link HttpServletRequest}
     * @param start   start date & time
     * @param end     end date & time
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Obtains trades for the given interval of time", description = "Returns a list of trades filtered by their trade open time.")
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
            description = "Response when the api cannot find any trades for the given interval of time.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "No trades found for interval <start> : <end>")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully obtains the trades within the given time interval.",
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
    @GetMapping(ApiPaths.Trade.GET_FOR_INTERVAL)
    public StandardJsonResponse<List<TradeDTO>> getTradesWithinInterval(
            @Parameter(name = "accountNumber", description = "The unique identifier for your trading account", example = "1234")
            final @RequestParam("accountNumber") long accountNumber,
            @Parameter(name = "start", description = "Start date of time period to analyze", example = "2025-01-01T00:00:00")
            final @RequestParam("start") String start,
            @Parameter(name = "end", description = "End date of time period to analyze", example = "2025-01-01T00:00:00")
            final @RequestParam("end") String end,
            final HttpServletRequest request
    ) {

        validateLocalDateTimeFormat(start, CorePlatformConstants.DATE_TIME_FORMAT, String.format(CorePlatformConstants.Validation.DateTime.START_DATE_INVALID_FORMAT, start, CorePlatformConstants.DATE_TIME_FORMAT));
        validateLocalDateTimeFormat(end, CorePlatformConstants.DATE_TIME_FORMAT, String.format(CorePlatformConstants.Validation.DateTime.START_DATE_INVALID_FORMAT, end, CorePlatformConstants.DATE_TIME_FORMAT));

        final User user = (User) request.getAttribute(SecurityConstants.USER_REQUEST_KEY);
        List<Trade> trades = this.tradeService.findAllTradesWithinTimespan(LocalDateTime.parse(start, DateTimeFormatter.ISO_DATE_TIME), LocalDateTime.parse(end, DateTimeFormatter.ISO_DATE_TIME), getAccountForId(user, accountNumber));
        validateIfAnyResult(trades, "No trades were found within interval: [%s, %s]", start, end);

        return StandardJsonResponse
                .<List<TradeDTO>>builder()
                .success(true)
                .data(this.tradeDTOConverter.convertAll(trades))
                .build();
    }

    /**
     * Returns a {@link StandardJsonResponse} containing {@link Trade}s for the given interval of time
     *
     * @param request  {@link HttpServletRequest}
     * @param start    start date & time
     * @param end      end date & time
     * @param page     page
     * @param pageSize pageSize
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Obtains paginated trades for the given interval of time", description = "Returns a list of paginated trades filtered by their trade open time.")
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
            description = "Response when the api cannot find any trades for the given interval of time.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "No trades found for interval <start> : <end>")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully obtains the trades within the given time interval.",
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
    @GetMapping(ApiPaths.Trade.GET_FOR_INTERVAL_PAGED)
    public StandardJsonResponse<PaginatedTradesDTO> getTradesWithinIntervalPaged(
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
            @Parameter(name = "tradeType", description = "Type of trade", example = "BUY")
            final @RequestParam(value = "tradeType", defaultValue = "ALL") String tradeType,
            @Parameter(name = "symbol", description = "Symbol", example = "NDX")
            final @RequestParam(value = "symbol", defaultValue = "ALL") String symbol,
            @Parameter(name = "sort", description = "Sort order", example = "asc")
            final @RequestParam(value = "sort", defaultValue = "asc") String sort,
            final HttpServletRequest request
    ) {
        validateLocalDateTimeFormat(start, CorePlatformConstants.DATE_TIME_FORMAT, String.format(CorePlatformConstants.Validation.DateTime.START_DATE_INVALID_FORMAT, start, CorePlatformConstants.DATE_TIME_FORMAT));
        validateLocalDateTimeFormat(end, CorePlatformConstants.DATE_TIME_FORMAT, String.format(CorePlatformConstants.Validation.DateTime.START_DATE_INVALID_FORMAT, end, CorePlatformConstants.DATE_TIME_FORMAT));

        final User user = (User) request.getAttribute(SecurityConstants.USER_REQUEST_KEY);
        final Triplet<TradeType, String, Sort> filters = resolveFilters(tradeType, symbol, sort);

        Page<Trade> trades;
        final LocalDateTime startTime = LocalDateTime.parse(start, DateTimeFormatter.ISO_DATE_TIME);
        final LocalDateTime endTime = LocalDateTime.parse(end, DateTimeFormatter.ISO_DATE_TIME);
        final Account account = getAccountForId(user, accountNumber);

        if (filters.getValue0() != null && StringUtils.isNotEmpty(filters.getValue1())) {
            //  filter by type and symbol
            trades = this.tradeService.findAllTradesForSymbolAndTradeTypeWithinTimespan(startTime, endTime, account, filters.getValue1(), filters.getValue0(), page, pageSize, filters.getValue2());
        } else if (filters.getValue0() != null) {
            //  filter by type only
            trades = this.tradeService.findAllTradesForTradeTypeWithinTimespan(startTime, endTime, account, filters.getValue0(), page, pageSize, filters.getValue2());
        } else if (StringUtils.isNotEmpty(filters.getValue1())) {
            //  filter by symbol only
            trades = this.tradeService.findAllTradesForSymbolWithinTimespan(startTime, endTime, account, filters.getValue1(), page, pageSize, filters.getValue2());
        } else {
            trades = this.tradeService.findAllTradesWithinTimespan(startTime, endTime, account, page, pageSize, filters.getValue2());
        }

        return StandardJsonResponse
                .<PaginatedTradesDTO>builder()
                .success(true)
                .data(
                        PaginatedTradesDTO
                                .builder()
                                .page(trades.getPageable().getPageNumber())
                                .pageSize(trades.getPageable().getPageSize())
                                .trades(trades.map(tr -> this.tradeDTOConverter.convert(tr)).stream().toList())
                                .totalElements(trades.getNumberOfElements())
                                .totalPages(trades.getTotalPages())
                                .build()
                )
                .build();
    }


    /**
     * Returns a {@link StandardJsonResponse} containing a {@link Trade} for the given trade id
     *
     * @param request {@link HttpServletRequest}
     * @param tradeId trade id
     * @param accountNumber account number
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Obtains an individual trade", description = "Returns an individual trade and its details.")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api cannot find the trade for the given trade id.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "No trade found for id 1234")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully obtains the trade for the given trade id.",
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
    @GetMapping(ApiPaths.Trade.GET_FOR_TRADE_ID)
    public StandardJsonResponse<TradeDTO> getTradeForTradeId(
            @Parameter(name = "accountNumber", description = "The unique identifier for your trading account", example = "1234")
            final @RequestParam("accountNumber") long accountNumber,
            @Parameter(name = TRADE_ID, description = "The unique identifier for the trade", example = "1234")
            final @RequestParam(TRADE_ID) String tradeId,
            final HttpServletRequest request
    ) {
        final User user = (User) request.getAttribute(SecurityConstants.USER_REQUEST_KEY);
        Optional<Trade> trade = this.tradeService.findTradeByTradeId(tradeId, getAccountForId(user, accountNumber));
        validateIfPresent(trade, NO_TRADE_FOR_TRADE_ID, tradeId);
        return trade
                .map(value -> StandardJsonResponse.<TradeDTO>builder().success(true).data(this.tradeDTOConverter.convert(value)).build())
                .orElseGet(() -> StandardJsonResponse.<TradeDTO>builder().success(false).data(TradeDTO.builder().build()).build());
    }

    /**
     * Returns a {@link StandardJsonResponse} containing {@link TradeInsights} for the given trade id
     *
     * @param request {@link HttpServletRequest}
     * @param tradeId trade id
     * @param accountNumber account number
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Obtains an individual trade's insights", description = "Returns an individual trade and its insights.")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api cannot find the trade for the given trade id.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "No trade found for id 1234")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully obtains the trade insights for the given trade id.",
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
    @GetMapping(ApiPaths.Trade.GET_TRADE_INSIGHTS)
    public StandardJsonResponse<TradeInsights> getTradeInsights(
            @Parameter(name = "accountNumber", description = "The unique identifier for your trading account", example = "1234")
            final @RequestParam("accountNumber") long accountNumber,
            @Parameter(name = TRADE_ID, description = "The unique identifier for the trade", example = "1234")
            final @RequestParam(TRADE_ID) String tradeId,
            final HttpServletRequest request
    ) {
        final User user = (User) request.getAttribute(SecurityConstants.USER_REQUEST_KEY);
        Optional<Trade> trade = this.tradeService.findTradeByTradeId(tradeId, getAccountForId(user, accountNumber));
        validateIfPresent(trade, NO_TRADE_FOR_TRADE_ID, tradeId);
        return trade
                .map(value -> StandardJsonResponse.<TradeInsights>builder().success(true).data(this.tradeService.generateTradeInsights(value)).build())
                .orElseGet(() -> StandardJsonResponse.<TradeInsights>builder().success(false).data(TradeInsights.builder().build()).build());
    }


    //  ----------------- POST REQUESTS -----------------

    /**
     * File upload endpoint to obtain import files to import {@link Trade}s into the system. The system will only accept CSV files
     *
     * @param request {@link HttpServletRequest}
     * @param file    {@link MultipartFile}
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Imports trades from a file", description = "Imports trades from a given file that must be a .csv or .htm file to the given account")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api was given a bad file format.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "Format .sdada is not a supported file format")
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully imports the trades into the system.",
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
    @PostMapping(ApiPaths.Trade.IMPORT_TRADES)
    public StandardJsonResponse<Boolean> postImportTrades(
            @Parameter(name = "accountNumber", description = "The unique identifier for your trading account", example = "1234")
            final @RequestParam("accountNumber") long accountNumber,
            @Parameter(name = "isStrategy", description = "If true, the import will treat the trades as a simulation import", example = "true")
            final @RequestParam("isStrategy") boolean isStrategy,
            @Parameter(name = "file", description = "The file containing your trades")
            final @RequestParam("file") MultipartFile file,
            final HttpServletRequest request
    ) throws IOException {

        final User user = (User) request.getAttribute(SecurityConstants.USER_REQUEST_KEY);
        validateImportFileExtension(file, getAccountForId(user, accountNumber).getTradePlatform().getFormats(), "The given file %s was not of a valid format", file.getOriginalFilename());

        final String result = isStrategy ? this.genericStrategyImportService.importReport(file.getInputStream(), getAccountForId(user, accountNumber)) : this.genericTradeImportService.importTrades(file.getInputStream(), getAccountForId(user, accountNumber));
        if (StringUtils.isEmpty(result)) {
            return StandardJsonResponse
                    .<Boolean>builder()
                    .success(true)
                    .data(true)
                    .build();
        }

        return StandardJsonResponse
                .<Boolean>builder()
                .success(false)
                .data(false)
                .message(result)
                .build();
    }

    /**
     * Creates a new {@link Trade} for the given account number
     *
     * @param data {@link CreateUpdateTradeDTO}}
     * @param accountNumber account number
     * @param request {@link HttpServletRequest}
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Creates a new trade", description = "Creates a new trade for the given account number.")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully creates a new trade.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "500",
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
    @PostMapping(ApiPaths.Trade.CREATE_TRADE)
    public StandardJsonResponse<TradeDTO> postCreateNewTrade(
            @Parameter(name = "Trade Payload", description = "Payload for creating or updating trades")
            final @RequestBody CreateUpdateTradeDTO data,
            @Parameter(name = "accountNumber", description = "The unique identifier for your trading account", example = "1234")
            final @RequestParam("accountNumber") long accountNumber,
            final HttpServletRequest request
    ) {
        if (data == null || StringUtils.isEmpty(data.tradeOpenTime())) {
            throw new MissingRequiredDataException("The required data for creating a Trade entity was null or empty");
        }

        final User user = (User) request.getAttribute(SecurityConstants.USER_REQUEST_KEY);
        final Account account = getAccountForId(user, accountNumber);
        final Trade newTrade = this.tradeService.createNewTrade(data, account);
        this.accountService.refreshAccount(account);

        return StandardJsonResponse
                .<TradeDTO>builder()
                .success(true)
                .data(this.tradeDTOConverter.convert(newTrade))
                .build();
    }


    //  ----------------- PUT REQUESTS -----------------

    /**
     * Updates an existing {@link Trade}
     *
     * @param tradeId trade id
     * @param data {@link CreateUpdateTradeDTO}}
     * @param accountNumber account number
     * @param request {@link HttpServletRequest}
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Updates an existing trade", description = "Updates an existing trade for the given account number and trade id.")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully creates a new trade.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class)
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api cannot find the trade for the given trade id.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "Trade not found.")
            )
    )
    @ApiResponse(
            responseCode = "500",
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
    @PutMapping(ApiPaths.Trade.UPDATE_TRADE)
    public StandardJsonResponse<TradeDTO> putUpdateTrade(
            @Parameter(name = "Trade Payload", description = "Payload for creating or updating trades")
            final @RequestBody CreateUpdateTradeDTO data,
            @Parameter(name = "accountNumber", description = "The unique identifier for your trading account", example = "1234")
            final @RequestParam("accountNumber") long accountNumber,
            @Parameter(name = TRADE_ID, description = "The unique identifier for the trade", example = "1234")
            final @RequestParam(TRADE_ID) String tradeId,
            final HttpServletRequest request
    ) {
        if (data == null || StringUtils.isEmpty(data.tradeId())) {
            throw new MissingRequiredDataException("The required data for updating a Trade entity was null or empty");
        }

        final User user = (User) request.getAttribute(SecurityConstants.USER_REQUEST_KEY);
        final Account account = getAccountForId(user, accountNumber);
        final Optional<Trade> trade = this.tradeService.findTradeByTradeId(tradeId, account);

        if (trade.isEmpty()) {
            return StandardJsonResponse
                    .<TradeDTO>builder()
                    .success(false)
                    .message(String.format(NO_TRADE_FOR_TRADE_ID, tradeId))
                    .build();
        }

        final Trade updatedTrade = this.tradeService.updateTrade(trade.get(), data, account);
        this.accountService.refreshAccount(account);
        return StandardJsonResponse
                .<TradeDTO>builder()
                .success(true)
                .data(this.tradeDTOConverter.convert(updatedTrade))
                .build();
    }


    //  ----------------- DELETE REQUESTS -----------------

    /**
     * Deletes a {@link Trade} for the given account number and trade id
     * @param accountNumber account number
     * @param tradeId trade id
     * @param request {@link HttpServletRequest}
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @Operation(summary = "Deletes an existing trade", description = "Deletes an existing trade for the given account number and trade id.")
    @ApiResponse(
            responseCode = "200",
            description = "Response when the api successfully deletes a trade.",
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
            responseCode = "200",
            description = "Response when the api cannot find the trade for the given trade id.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = StandardJsonResponse.class, example = "No trade was found for trade id 1234")
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
    @DeleteMapping(ApiPaths.Trade.DELETE_TRADE)
    public StandardJsonResponse<Boolean> deleteTrade(
            @Parameter(name = "accountNumber", description = "The unique identifier for your trading account", example = "1234")
            final @RequestParam("accountNumber") long accountNumber,
            @Parameter(name = TRADE_ID, description = "The unique identifier for the trade", example = "1234")
            final @RequestParam(TRADE_ID) String tradeId,
            final HttpServletRequest request
    ) {
        final User user = (User) request.getAttribute(SecurityConstants.USER_REQUEST_KEY);
        final Account account = getAccountForId(user, accountNumber);
        final Optional<Trade> trade = this.tradeService.findTradeByTradeId(tradeId, account);
        return trade.map(value -> {
            final boolean result = this.tradeService.deleteTrade(value);
            if (result) {
                this.accountService.refreshAccount(account);
            }

            return StandardJsonResponse.<Boolean>builder().success(result).data(result).build();
        }).orElseGet(() -> StandardJsonResponse.<Boolean>builder().success(false).data(false).message(String.format(NO_TRADE_FOR_TRADE_ID, tradeId)).build());
    }



    //  HELPERS

    /**
     * Resolves the paged trade filters
     *
     * @param type trade type
     * @param symbol symbol to filter on
     * @param sort sort order
     * @return {@link Triplet}
     */
    private Triplet<TradeType, String, Sort> resolveFilters(final String type, final String symbol, final String sort) {

        TradeType tradeType = null;
        String finalSymbol = null;
        final Sort finalSort;

        if (StringUtils.isNotEmpty(type) && EnumUtils.isValidEnumIgnoreCase(TradeType.class, type)) {
            tradeType = GenericEnum.getByCode(TradeType.class, type);
        }

        if (StringUtils.isNotEmpty(symbol) && !symbol.equalsIgnoreCase("ALL")) {
            finalSymbol = symbol;
        }

        if (sort.equalsIgnoreCase("asc")) {
            finalSort = Sort.by(Sort.Direction.ASC, "tradeOpenTime");
        } else {
            finalSort = Sort.by(Sort.Direction.DESC, "tradeOpenTime");
        }

        return Triplet.with(tradeType, finalSymbol, finalSort);
    }
}