package com.bluebell.planter.api.controllers.trade;

import com.bluebell.planter.api.controllers.AbstractApiController;
import com.bluebell.planter.api.converters.trade.TradeDTOConverter;
import com.bluebell.planter.api.models.dto.trade.TradeDTO;
import com.bluebell.planter.api.models.records.json.StandardJsonResponse;
import com.bluebell.planter.api.models.records.trade.PagedTrades;
import com.bluebell.planter.core.constants.CoreConstants;
import com.bluebell.planter.core.enums.trade.info.TradeType;
import com.bluebell.planter.core.models.entities.security.User;
import com.bluebell.planter.core.models.entities.trade.Trade;
import com.bluebell.planter.core.services.trade.TradeService;
import com.bluebell.planter.importing.services.strategy.GenericStrategyImportService;
import com.bluebell.planter.importing.services.trade.GenericTradeImportService;
import com.bluebell.planter.security.aspects.ValidateApiToken;
import com.bluebell.planter.security.constants.SecurityConstants;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static com.bluebell.planter.core.validation.GenericValidator.*;
import static com.bluebell.planter.importing.validation.ImportValidator.validateImportFileExtension;


/**
 * Api controller for {@link Trade}
 *
 * @author Stephen Prizio
 * @version 0.0.7
 */
@RestController
@RequestMapping("${base.api.controller.endpoint}/trade")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT})
public class TradeApiController extends AbstractApiController {

    private static final String TRADE_ID = "tradeId";

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
    @GetMapping("/for-type")
    public StandardJsonResponse getTradesForTradeType(final HttpServletRequest request, final @RequestParam("accountNumber") long accountNumber, final @RequestParam("tradeType") String tradeType) {

        if (!EnumUtils.isValidEnumIgnoreCase(TradeType.class, tradeType)) {
            return new StandardJsonResponse(false, null, String.format("%s is not a valid trade type", tradeType));
        }

        TradeType type = TradeType.valueOf(tradeType.toUpperCase());
        final User user = (User) request.getAttribute(SecurityConstants.USER_REQUEST_KEY);
        List<Trade> trades = this.tradeService.findAllByTradeType(type, getAccountForId(user, accountNumber));
        validateIfAnyResult(trades, "No trades were found for type %s", type.name());

        return new StandardJsonResponse(true, this.tradeDTOConverter.convertAll(trades), StringUtils.EMPTY);
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
    @GetMapping("/for-interval")
    public StandardJsonResponse getTradesWithinInterval(final HttpServletRequest request, final @RequestParam("accountNumber") long accountNumber, final @RequestParam("start") String start, final @RequestParam("end") String end) {

        validateLocalDateTimeFormat(start, CoreConstants.DATE_TIME_FORMAT, String.format(CoreConstants.Validation.DateTime.START_DATE_INVALID_FORMAT, start, CoreConstants.DATE_TIME_FORMAT));
        validateLocalDateTimeFormat(end, CoreConstants.DATE_TIME_FORMAT, String.format(CoreConstants.Validation.DateTime.START_DATE_INVALID_FORMAT, end, CoreConstants.DATE_TIME_FORMAT));

        final User user = (User) request.getAttribute(SecurityConstants.USER_REQUEST_KEY);
        List<Trade> trades = this.tradeService.findAllTradesWithinTimespan(LocalDateTime.parse(start, DateTimeFormatter.ISO_DATE_TIME), LocalDateTime.parse(end, DateTimeFormatter.ISO_DATE_TIME), getAccountForId(user, accountNumber));
        validateIfAnyResult(trades, "No trades were found within interval: [%s, %s]", start, end);

        return new StandardJsonResponse(true, this.tradeDTOConverter.convertAll(trades), StringUtils.EMPTY);
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
    @GetMapping("/for-interval-paged")
    public StandardJsonResponse getTradesWithinIntervalPaged(
            final HttpServletRequest request,
            final @RequestParam("accountNumber") long accountNumber,
            final @RequestParam("start") String start,
            final @RequestParam("end") String end,
            final @RequestParam(value = "page", defaultValue = "0") int page,
            final @RequestParam(value = "pageSize", defaultValue = "10") int pageSize
    ) {
        validateLocalDateTimeFormat(start, CoreConstants.DATE_TIME_FORMAT, String.format(CoreConstants.Validation.DateTime.START_DATE_INVALID_FORMAT, start, CoreConstants.DATE_TIME_FORMAT));
        validateLocalDateTimeFormat(end, CoreConstants.DATE_TIME_FORMAT, String.format(CoreConstants.Validation.DateTime.START_DATE_INVALID_FORMAT, end, CoreConstants.DATE_TIME_FORMAT));

        final User user = (User) request.getAttribute(SecurityConstants.USER_REQUEST_KEY);
        Page<Trade> trades = this.tradeService.findAllTradesWithinTimespan(LocalDateTime.parse(start, DateTimeFormatter.ISO_DATE_TIME), LocalDateTime.parse(end, DateTimeFormatter.ISO_DATE_TIME), getAccountForId(user, accountNumber), page, pageSize);
        return new StandardJsonResponse(true, new PagedTrades(trades.getPageable().getPageNumber(), trades.getPageable().getPageSize(), trades.map(tr -> this.tradeDTOConverter.convert(tr)).stream().toList(), trades.getNumberOfElements(), trades.getTotalPages()), StringUtils.EMPTY);
    }


    /**
     * Returns a {@link StandardJsonResponse} containing a {@link Trade} for the given trade id
     *
     * @param request {@link HttpServletRequest}
     * @param tradeId trade id
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @GetMapping("/for-trade-id")
    public StandardJsonResponse getTradeForTradeId(final HttpServletRequest request, final @RequestParam("accountNumber") long accountNumber, final @RequestParam(TRADE_ID) String tradeId) {
        final User user = (User) request.getAttribute(SecurityConstants.USER_REQUEST_KEY);
        Optional<Trade> trade = this.tradeService.findTradeByTradeId(tradeId, getAccountForId(user, accountNumber));
        validateIfPresent(trade, "No trade was found with trade id: %s", tradeId);
        return trade.map(value -> new StandardJsonResponse(true, this.tradeDTOConverter.convert(value), StringUtils.EMPTY)).orElseGet(() -> new StandardJsonResponse(true, new TradeDTO(), StringUtils.EMPTY));
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
    @PostMapping("/import-trades")
    public StandardJsonResponse postImportTrades(final HttpServletRequest request, final @RequestParam("accountNumber") long accountNumber, final @RequestParam("isStrategy") boolean isStrategy, final @RequestParam("file") MultipartFile file) throws IOException {

        final User user = (User) request.getAttribute(SecurityConstants.USER_REQUEST_KEY);
        validateImportFileExtension(file, getAccountForId(user, accountNumber).getTradePlatform().getFormats(), "The given file %s was not of a valid format", file.getOriginalFilename());

        final String result = isStrategy ? this.genericStrategyImportService.importReport(file.getInputStream(), getAccountForId(user, accountNumber)) : this.genericTradeImportService.importTrades(file.getInputStream(), getAccountForId(user, accountNumber));
        if (StringUtils.isEmpty(result)) {
            return new StandardJsonResponse(true, true, StringUtils.EMPTY);
        }

        return new StandardJsonResponse(false, null, result);
    }
}
