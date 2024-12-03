package com.bluebell.planter.api.controllers.portfolio;

import com.bluebell.planter.api.controllers.AbstractApiController;
import com.bluebell.planter.api.models.records.json.StandardJsonResponse;
import com.bluebell.planter.core.models.entities.security.User;
import com.bluebell.planter.core.models.nonentities.records.portfolio.Portfolio;
import com.bluebell.planter.core.services.portfolio.PortfolioService;
import com.bluebell.planter.security.aspects.ValidateApiToken;
import com.bluebell.planter.security.constants.SecurityConstants;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * API Controller for {@link Portfolio}
 *
 * @author Stephen Prizio
 * @version 0.0.7
 */
@RestController
@RequestMapping("${base.api.controller.endpoint}/portfolio")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT})
public class PortfolioApiController extends AbstractApiController {

    @Resource(name = "portfolioService")
    private PortfolioService portfolioService;


    //  METHODS


    //  ----------------- GET REQUESTS -----------------

    /**
     * Returns a {@link StandardJsonResponse} containing a {@link Portfolio}
     *
     * @return {@link StandardJsonResponse}
     */
    @ValidateApiToken
    @GetMapping("/get")
    public StandardJsonResponse getCurrencies(final HttpServletRequest request) {
        final User user = (User) request.getAttribute(SecurityConstants.USER_REQUEST_KEY);
        return new StandardJsonResponse(true, this.portfolioService.getPortfolio(user), StringUtils.EMPTY);
    }
}
