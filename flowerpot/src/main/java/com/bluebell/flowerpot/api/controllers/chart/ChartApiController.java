package com.bluebell.flowerpot.api.controllers.chart;

import com.bluebell.flowerpot.api.controllers.AbstractApiController;
import com.bluebell.flowerpot.api.models.records.json.StandardJsonResponse;
import com.bluebell.flowerpot.core.constants.CoreConstants;
import com.bluebell.flowerpot.core.enums.chart.IntradayInterval;
import com.bluebell.flowerpot.core.models.nonentities.apexcharts.ApexChartCandleStick;
import com.bluebell.flowerpot.core.services.chart.ChartService;
import com.bluebell.flowerpot.security.aspects.ValidateApiToken;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.bluebell.flowerpot.core.validation.GenericValidator.validateLocalDateFormat;

/**
 * API controller for providing charting capabilities based on historical data
 *
 * @author Stephen Prizio
 * @version 0.0.6
 */
@RestController
@RequestMapping("${base.api.controller.endpoint}/chart")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT})
public class ChartApiController extends AbstractApiController {

    @Resource(name = "apexChartService")
    private ChartService<ApexChartCandleStick> chartService;


    //  ----------------- GET REQUESTS -----------------

    @ValidateApiToken
    @GetMapping("/apex-data")
    public StandardJsonResponse getApexChartData(final @RequestParam("start") String start, final @RequestParam("end") String end, final @RequestParam("interval") String interval, final HttpServletRequest request) {

        validateLocalDateFormat(start, CoreConstants.DATE_FORMAT, String.format(CoreConstants.Validation.DateTime.START_DATE_INVALID_FORMAT, start, CoreConstants.DATE_FORMAT));
        validateLocalDateFormat(end, CoreConstants.DATE_FORMAT, String.format(CoreConstants.Validation.DateTime.START_DATE_INVALID_FORMAT, end, CoreConstants.DATE_FORMAT));

        final IntradayInterval intradayInterval = IntradayInterval.getByLabel(interval);
        if (intradayInterval == IntradayInterval.ONE_DAY) {
            return new StandardJsonResponse(true, this.chartService.getChartData(LocalDate.parse(start, DateTimeFormatter.ISO_DATE).minusMonths(1), LocalDate.parse(end, DateTimeFormatter.ISO_DATE), intradayInterval), StringUtils.EMPTY);
        } else if (intradayInterval == IntradayInterval.ONE_HOUR) {
            return new StandardJsonResponse(true, this.chartService.getChartData(LocalDate.parse(start, DateTimeFormatter.ISO_DATE).minusDays(5), LocalDate.parse(end, DateTimeFormatter.ISO_DATE), intradayInterval), StringUtils.EMPTY);
        }

        return new StandardJsonResponse(true, this.chartService.getChartData(LocalDate.parse(start, DateTimeFormatter.ISO_DATE), LocalDate.parse(end, DateTimeFormatter.ISO_DATE), intradayInterval), StringUtils.EMPTY);
    }
}
