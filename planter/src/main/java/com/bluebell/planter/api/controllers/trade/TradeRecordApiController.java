package com.bluebell.planter.api.controllers.trade;

import com.bluebell.planter.api.controllers.AbstractApiController;
import com.bluebell.planter.api.models.records.json.StandardJsonResponse;
import com.bluebell.planter.core.constants.CoreConstants;
import com.bluebell.planter.core.enums.system.FlowerpotTimeInterval;
import com.bluebell.planter.core.models.entities.security.User;
import com.bluebell.planter.core.models.nonentities.records.trade.TradeLog;
import com.bluebell.planter.core.models.nonentities.records.trade.TradeRecord;
import com.bluebell.planter.core.models.nonentities.records.trade.TradeRecordReport;
import com.bluebell.planter.core.services.trade.TradeRecordService;
import com.bluebell.planter.security.aspects.ValidateApiToken;
import com.bluebell.planter.security.constants.SecurityConstants;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Api controller for {@link TradeRecord}
 *
 * @author Stephen Prizio
 * @version 0.0.7
 */
@RestController
@RequestMapping("${base.api.controller.endpoint}/trade-record")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT})
public class TradeRecordApiController extends AbstractApiController {

    @Resource(name = "tradeRecordService")
    private TradeRecordService tradeRecordService;


    //  METHODS


    //  ----------------- GET REQUESTS -----------------

    /**
     * Returns a {@link List} of {@link TradeRecord}
     *
     * @param request       {@link HttpServletRequest}
     * @param accountNumber account number
     * @param start         start date
     * @param end           end date
     * @param interval      interval
     * @param count         limit
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @GetMapping("/for-interval")
    public StandardJsonResponse getTradeRecordsWithinInterval(
            final HttpServletRequest request,
            final @RequestParam("accountNumber") long accountNumber,
            final @RequestParam("start") String start,
            final @RequestParam("end") String end,
            final @RequestParam("interval") String interval,
            final @RequestParam(value = "count", defaultValue = "" + CoreConstants.DEFAULT_TRADE_RECORD_COLLECTION_SIZE, required = false) int count
    ) {

        validate(start, end);
        if (!EnumUtils.isValidEnumIgnoreCase(FlowerpotTimeInterval.class, interval)) {
            return new StandardJsonResponse(false, null, String.format(CoreConstants.Validation.DataIntegrity.INVALID_INTERVAL, interval));
        }

        final User user = (User) request.getAttribute(SecurityConstants.USER_REQUEST_KEY);
        final TradeRecordReport records = this.tradeRecordService.getTradeRecords(LocalDate.parse(start, DateTimeFormatter.ISO_DATE), LocalDate.parse(end, DateTimeFormatter.ISO_DATE), getAccountForId(user, accountNumber), FlowerpotTimeInterval.getInterval(interval), count);
        return new StandardJsonResponse(true, records, StringUtils.EMPTY);
    }

    /**
     * Returns a {@link List} of the most recent {@link TradeRecord}s for the given account
     *
     * @param request       {@link HttpServletRequest}
     * @param accountNumber account number
     * @param interval      interval
     * @param count         limit
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @GetMapping("/recent")
    public StandardJsonResponse getRecentTradeRecords(
            final HttpServletRequest request,
            final @RequestParam("accountNumber") long accountNumber,
            final @RequestParam("interval") String interval,
            final @RequestParam(value = "count", defaultValue = "" + CoreConstants.DEFAULT_TRADE_RECORD_COLLECTION_SIZE, required = false) int count
    ) {

        if (!EnumUtils.isValidEnumIgnoreCase(FlowerpotTimeInterval.class, interval)) {
            return new StandardJsonResponse(false, null, String.format(CoreConstants.Validation.DataIntegrity.INVALID_INTERVAL, interval));
        }

        final User user = (User) request.getAttribute(SecurityConstants.USER_REQUEST_KEY);
        final TradeRecordReport records = this.tradeRecordService.getRecentTradeRecords(getAccountForId(user, accountNumber), FlowerpotTimeInterval.getInterval(interval), count);
        return new StandardJsonResponse(true, records, StringUtils.EMPTY);
    }

    //  TODO: fix max loss bug (not showing the right number)

    //  TODO: include drawdown percentages next to max loss

    /**
     * Returns a {@link TradeLog}
     *
     * @param request  {@link HttpServletRequest}
     * @param start    start date
     * @param end      end date
     * @param interval interval
     * @param count    limit
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @GetMapping("/trade-log")
    public StandardJsonResponse getTradeLog(
            final HttpServletRequest request,
            final @RequestParam("start") String start,
            final @RequestParam("end") String end,
            final @RequestParam("interval") String interval,
            final @RequestParam(value = "count", defaultValue = "" + CoreConstants.DEFAULT_TRADE_RECORD_COLLECTION_SIZE, required = false) int count) {

        validate(start, end);
        if (!EnumUtils.isValidEnumIgnoreCase(FlowerpotTimeInterval.class, interval)) {
            return new StandardJsonResponse(false, null, String.format(CoreConstants.Validation.DataIntegrity.INVALID_INTERVAL, interval));
        }

        final User user = (User) request.getAttribute(SecurityConstants.USER_REQUEST_KEY);
        return new StandardJsonResponse(true, this.tradeRecordService.getTradeLog(user, LocalDate.parse(start, DateTimeFormatter.ISO_DATE), LocalDate.parse(end, DateTimeFormatter.ISO_DATE), FlowerpotTimeInterval.getInterval(interval), count), StringUtils.EMPTY);
    }
}
