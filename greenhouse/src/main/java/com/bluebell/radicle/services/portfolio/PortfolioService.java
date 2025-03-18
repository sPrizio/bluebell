package com.bluebell.radicle.services.portfolio;

import com.bluebell.planter.services.UniqueIdentifierService;
import com.bluebell.platform.models.core.entities.portfolio.Portfolio;
import com.bluebell.platform.models.core.nonentities.records.portfolio.PortfolioRecord;
import com.bluebell.radicle.repositories.portfolio.PortfolioRepository;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service-layer for {@link PortfolioRecord}
 *
 * @author Stephen Prizio
 * @version 0.1.2
 */
@Service("portfolioService")
public class PortfolioService {

    @Resource(name = "portfolioRepository")
    private PortfolioRepository portfolioRepository;

    @Resource(name = "uniqueIdentifierService")
    private UniqueIdentifierService uniqueIdentifierService;


    //  METHODS

    /**
     * Retrieves a {@link Portfolio} for the given uid
     *
     * @param uid uid
     * @return {@link Optional} {@link Portfolio}
     */
    public Optional<Portfolio> findPortfolioByUid(final String uid) {

        if (StringUtils.isEmpty(uid)) {
            return Optional.empty();
        }

        return this.portfolioRepository.findById(this.uniqueIdentifierService.retrieveId(uid));
    }
}
