package com.bluebell.planter.api.converters.account;

import com.bluebell.planter.api.converters.GenericDTOConverter;
import com.bluebell.planter.api.converters.transaction.TransactionDTOConverter;
import com.bluebell.planter.api.models.dto.account.AccountDTO;
import com.bluebell.planter.api.models.records.EnumDisplay;
import com.bluebell.planter.core.models.entities.account.Account;
import com.bluebell.planter.core.services.platform.UniqueIdentifierService;
import com.bluebell.radicle.services.MathService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * Converts {@link Account}s into {@link AccountDTO}s
 *
 * @author Stephen Prizio
 * @version 0.0.7
 */
@Component("accountDTOConverter")
public class AccountDTOConverter implements GenericDTOConverter<Account, AccountDTO> {

    private final MathService mathService = new MathService();

    @Resource(name = "transactionDTOConverter")
    private TransactionDTOConverter transactionDTOConverter;

    @Resource(name = "uniqueIdentifierService")
    private UniqueIdentifierService uniqueIdentifierService;


    //  METHODS

    @Override
    public AccountDTO convert(final Account entity) {

        if (entity == null) {
            return new AccountDTO();
        }

        AccountDTO accountDTO = new AccountDTO();

        accountDTO.setUid(this.uniqueIdentifierService.generateUid(entity));
        accountDTO.setDefaultAccount(entity.isDefaultAccount());
        accountDTO.setAccountOpenTime(entity.getAccountOpenTime());
        accountDTO.setAccountCloseTime(entity.getAccountCloseTime());
        accountDTO.setActive(entity.isActive());
        accountDTO.setBalance(this.mathService.getDouble(entity.getBalance()));
        accountDTO.setName(entity.getName());
        accountDTO.setCurrency(new EnumDisplay(entity.getCurrency().getIsoCode(), entity.getCurrency().getLabel()));
        accountDTO.setAccountNumber(entity.getAccountNumber());
        accountDTO.setAccountType(new EnumDisplay(entity.getAccountType().getCode(), entity.getAccountType().getLabel()));
        accountDTO.setBroker(new EnumDisplay(entity.getBroker().getCode(), entity.getBroker().getName()));
        accountDTO.setLastTraded(entity.getLastTraded());
        accountDTO.setTradePlatform(new EnumDisplay(entity.getTradePlatform().getCode(), entity.getTradePlatform().getLabel()));
        accountDTO.setTransactions(this.transactionDTOConverter.convertAll(entity.getTransactions()));

        return accountDTO;
    }
}
