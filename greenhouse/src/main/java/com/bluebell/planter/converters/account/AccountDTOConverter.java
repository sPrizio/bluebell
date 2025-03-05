package com.bluebell.planter.converters.account;

import com.bluebell.planter.converters.GenericDTOConverter;
import com.bluebell.planter.converters.transaction.TransactionDTOConverter;
import com.bluebell.planter.services.UniqueIdentifierService;
import com.bluebell.platform.models.api.dto.account.AccountDTO;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.nonentities.data.EnumDisplay;
import com.bluebell.platform.services.MathService;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Converts {@link Account}s into {@link AccountDTO}s
 *
 * @author Stephen Prizio
 * @version 0.1.1
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
            return AccountDTO.builder().build();
        }

        return AccountDTO
                .builder()
                .uid(this.uniqueIdentifierService.generateUid(entity))
                .defaultAccount(entity.isDefaultAccount())
                .accountOpenTime(entity.getAccountOpenTime())
                .accountCloseTime(entity.getAccountCloseTime())
                .active(entity.isActive())
                .initialBalance(entity.getInitialBalance())
                .balance(this.mathService.getDouble(entity.getBalance()))
                .name(entity.getName())
                .currency(new EnumDisplay(entity.getCurrency().getIsoCode(), entity.getCurrency().getLabel()))
                .accountNumber(entity.getAccountNumber())
                .accountType(new EnumDisplay(entity.getAccountType().getCode(), entity.getAccountType().getLabel()))
                .broker(new EnumDisplay(entity.getBroker().getCode(), entity.getBroker().getName()))
                .lastTraded(entity.getLastTraded())
                .tradePlatform(new EnumDisplay(entity.getTradePlatform().getCode(), entity.getTradePlatform().getLabel()))
                .transactions(CollectionUtils.isEmpty(entity.getTransactions()) ? List.of() : this.transactionDTOConverter.convertAll(entity.getTransactions()))
                .build();
    }
}
