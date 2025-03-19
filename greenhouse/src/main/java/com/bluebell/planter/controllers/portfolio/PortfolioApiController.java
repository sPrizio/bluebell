package com.bluebell.planter.controllers.portfolio;

import com.bluebell.planter.controllers.AbstractApiController;
import com.bluebell.platform.models.core.nonentities.records.portfolio.PortfolioRecord;
import com.bluebell.radicle.services.portfolio.PortfolioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * API Controller for {@link PortfolioRecord}
 *
 * @author Stephen Prizio
 * @version 0.1.2
 */
@RestController
@RequestMapping("${base.api.controller.endpoint}/portfolio")
@Tag(name = "Portfolio", description = "Handles endpoints & operations related to obtaining portfolio information.")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT})
public class PortfolioApiController extends AbstractApiController {

    @Resource(name = "portfolioService")
    private PortfolioService portfolioService;


    //  METHODS


    //  ----------------- GET REQUESTS -----------------

    //  TODO: CRUD METHODS
    //  TODO: TESTS
    //  TODO: PORTFOLIO RUNNER
}
