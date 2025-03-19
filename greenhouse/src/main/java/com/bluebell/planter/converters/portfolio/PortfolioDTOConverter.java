package com.bluebell.planter.converters.portfolio;

import com.bluebell.planter.converters.GenericDTOConverter;
import com.bluebell.planter.converters.account.AccountDTOConverter;
import com.bluebell.planter.services.UniqueIdentifierService;
import com.bluebell.platform.models.api.dto.portfolio.PortfolioDTO;
import com.bluebell.platform.models.core.entities.portfolio.Portfolio;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * Converts {@link Portfolio}s into {@link PortfolioDTO}s
 *
 * @author Stephen Prizio
 * @version 0.1.2
 */
@Component("portfolioDTOConverter")
public class PortfolioDTOConverter implements GenericDTOConverter<Portfolio, PortfolioDTO> {

    @Resource(name = "accountDTOConverter")
    private AccountDTOConverter accountDTOConverter;

    @Resource(name = "uniqueIdentifierService")
    private UniqueIdentifierService uniqueIdentifierService;


    //  METHODS

    @Override
    public PortfolioDTO convert(final Portfolio entity) {

        if (entity == null) {
            return PortfolioDTO.builder().build();
        }

        return PortfolioDTO
                .builder()
                .uid(this.uniqueIdentifierService.generateUid(entity))
                .name(entity.getName())
                .active(entity.isActive())
                .created(entity.getCreated())
                .defaultPortfolio(entity.isDefaultPortfolio())
                .accounts(this.accountDTOConverter.convertAll(entity.getAccounts()))
                .build();
    }
}
