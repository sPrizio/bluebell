package com.bluebell.radicle.services.portfolio;

import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.models.api.dto.portfolio.CreateUpdatePortfolioDTO;
import com.bluebell.platform.models.core.entities.portfolio.Portfolio;
import com.bluebell.platform.models.core.entities.security.User;
import com.bluebell.platform.models.core.nonentities.records.portfolio.PortfolioRecord;
import com.bluebell.radicle.exceptions.system.EntityCreationException;
import com.bluebell.radicle.exceptions.system.EntityModificationException;
import com.bluebell.radicle.exceptions.validation.MissingRequiredDataException;
import com.bluebell.radicle.repositories.portfolio.PortfolioRepository;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.bluebell.radicle.validation.GenericValidator.validateParameterIsNotNull;

/**
 * Service-layer for {@link PortfolioRecord}
 *
 * @author Stephen Prizio
 * @version 0.2.0
 */
@Service("portfolioService")
public class PortfolioService {

    @Resource(name = "portfolioRepository")
    private PortfolioRepository portfolioRepository;


    //  METHODS

    /**
     * Retrieves a {@link Portfolio} for the given portfolio number
     *
     * @param portfolioNumber portfolio number
     * @return {@link Optional} {@link Portfolio}
     */
    public Optional<Portfolio> findPortfolioForPortfolioNumber(final long portfolioNumber) {
        return Optional.ofNullable(this.portfolioRepository.findPortfolioByPortfolioNumber(portfolioNumber));
    }

    /**
     * Creates a new {@link Portfolio} with the given data
     *
     * @param data {@link CreateUpdatePortfolioDTO}
     * @param user {@link User}
     * @return new {@link Portfolio}
     */
    public Portfolio createPortfolio(final CreateUpdatePortfolioDTO data, final User user) {

        validateParameterIsNotNull(user, CorePlatformConstants.Validation.Security.User.USER_CANNOT_BE_NULL);

        if (data == null || StringUtils.isEmpty(data.name())) {
            throw new MissingRequiredDataException("The required data for creating a Portfolio entity was null or empty");
        }

        try {
            return applyChanges(Portfolio.builder().build(), data, user);
        } catch (Exception e) {
            throw new EntityCreationException(String.format("A Portfolio could not be created : %s", e.getMessage()), e);
        }
    }

    /**
     * Updates an existing {@link Portfolio}
     *
     * @param portfolio {@link Portfolio} to update
     * @param data      {@link CreateUpdatePortfolioDTO}
     * @param user      {@link User}
     * @return updated {@link Portfolio}
     */
    public Portfolio updatePortfolio(final Portfolio portfolio, final CreateUpdatePortfolioDTO data, final User user) {

        validateParameterIsNotNull(portfolio, CorePlatformConstants.Validation.Portfolio.PORTFOLIO_CANNOT_BE_NULL);
        validateParameterIsNotNull(user, CorePlatformConstants.Validation.Security.User.USER_CANNOT_BE_NULL);

        if (data == null || StringUtils.isEmpty(data.name())) {
            throw new MissingRequiredDataException("The required data for updating a Portfolio was null or empty");
        }

        try {
            return applyChanges(portfolio, data, user);
        } catch (Exception e) {
            throw new EntityModificationException(String.format("An error occurred while modifying the Portfolio : %s", e.getMessage()), e);
        }
    }

    /**
     * Deletes the given {@link Portfolio}. Will not delete the last active portfolio
     *
     * @param portfolio {@link Portfolio}
     * @return true if deleted
     */
    public boolean deletePortfolio(final Portfolio portfolio) {

        validateParameterIsNotNull(portfolio, CorePlatformConstants.Validation.Portfolio.PORTFOLIO_CANNOT_BE_NULL);
        validateParameterIsNotNull(portfolio.getUser(), CorePlatformConstants.Validation.Security.User.USER_CANNOT_BE_NULL);

        final User user = portfolio.getUser();
        final List<Portfolio> activePortfolios = user.getActivePortfolios();
        final List<Portfolio> inactivePortfolios = user.getPortfolios().stream().filter(p -> !p.isActive()).toList();

        if (CollectionUtils.isNotEmpty(activePortfolios) && activePortfolios.contains(portfolio)) {
            if (activePortfolios.size() == 1) {
                throw new UnsupportedOperationException("Cannot delete only active portfolio");
            } else {
                this.portfolioRepository.delete(portfolio);
                return true;
            }
        } else if (CollectionUtils.isNotEmpty(inactivePortfolios) && inactivePortfolios.contains(portfolio)) {
            this.portfolioRepository.delete(portfolio);
            return true;
        }

        return false;
    }

    /**
     * Applies the changes to the given {@link Portfolio}
     *
     * @param portfolio {@link Portfolio}
     * @param data      {@link CreateUpdatePortfolioDTO}
     * @param user      {@link User}
     * @return updated {@link Portfolio}
     */
    private Portfolio applyChanges(Portfolio portfolio, final CreateUpdatePortfolioDTO data, final User user) {

        if (!data.active()) {
            final boolean hasMultipleActive = user.getPortfolios().stream().filter(Portfolio::isActive).count() > 1;
            if (hasMultipleActive) {
                portfolio.setActive(false);
            }
        } else {
            portfolio.setActive(true);
        }

        portfolio.setName(data.name());
        portfolio.setUser(user);

        if (data.defaultPortfolio() && CollectionUtils.isNotEmpty(user.getPortfolios())) {
            user.getPortfolios().forEach(p -> {
                p.setDefaultPortfolio(false);
                this.portfolioRepository.save(p);
            });

            portfolio.setDefaultPortfolio(true);
        } else {
            portfolio.setDefaultPortfolio(data.defaultPortfolio());
        }

        portfolio = this.portfolioRepository.save(portfolio);
        portfolio.setPortfolioNumber(generatePortfolioNumber(portfolio.getId()));

        return this.portfolioRepository.save(portfolio);
    }

    /**
     * Generates the portfolio number
     *
     * @param seed base seed
     * @return number
     */
    private long generatePortfolioNumber(final long seed) {
        return seed * 1000L;
    }
}
