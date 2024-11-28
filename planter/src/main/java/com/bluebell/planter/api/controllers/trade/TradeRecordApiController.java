package com.bluebell.planter.api.controllers.trade;

import com.bluebell.planter.api.controllers.AbstractApiController;
import com.bluebell.planter.api.models.records.json.StandardJsonResponse;
import com.bluebell.planter.core.constants.CoreConstants;
import com.bluebell.planter.core.enums.system.FlowerpotTimeInterval;
import com.bluebell.planter.core.models.entities.security.User;
import com.bluebell.planter.core.models.nonentities.records.trade.TradeRecord;
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

import static com.bluebell.planter.core.validation.GenericValidator.validateLocalDateFormat;

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
     * @param request {@link HttpServletRequest}
     * @param accountNumber account number
     * @param start start date
     * @param end end date
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

        validateLocalDateFormat(start, CoreConstants.DATE_FORMAT, String.format(CoreConstants.Validation.DateTime.START_DATE_INVALID_FORMAT, start, CoreConstants.DATE_FORMAT));
        validateLocalDateFormat(end, CoreConstants.DATE_FORMAT, String.format(CoreConstants.Validation.DateTime.START_DATE_INVALID_FORMAT, end, CoreConstants.DATE_FORMAT));

        if (!EnumUtils.isValidEnumIgnoreCase(FlowerpotTimeInterval.class, interval)) {
            return new StandardJsonResponse(false, null, String.format("%s is not a valid time interval", interval));
        }

        final User user = (User) request.getAttribute(SecurityConstants.USER_REQUEST_KEY);
        final List<TradeRecord> records = this.tradeRecordService.getTradeRecords(LocalDate.parse(start, DateTimeFormatter.ISO_DATE), LocalDate.parse(end, DateTimeFormatter.ISO_DATE), getAccountForId(user, accountNumber), FlowerpotTimeInterval.getInterval(interval), count);
        return new StandardJsonResponse(true, records, StringUtils.EMPTY);
    }
}
