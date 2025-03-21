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

import java.util.Collections;

/**
 * Converts {@link Account}s into {@link AccountDTO}s
 *
 * @author Stephen Prizio
 * @version 0.1.3
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
                .currency(EnumDisplay.builder().code(entity.getCurrency().getIsoCode()).label(entity.getCurrency().getLabel()).build())
                .accountNumber(entity.getAccountNumber())
                .accountType(EnumDisplay.builder().code(entity.getAccountType().getCode()).label(entity.getAccountType().getLabel()).build())
                .broker(EnumDisplay.builder().code(entity.getBroker().getCode()).label(entity.getBroker().getLabel()).build())
                .lastTraded(entity.getLastTraded())
                .tradePlatform(EnumDisplay.builder().code(entity.getTradePlatform().getCode()).label(entity.getTradePlatform().getLabel()).build())
                .transactions(CollectionUtils.isEmpty(entity.getTransactions()) ? Collections.emptyList() : this.transactionDTOConverter.convertAll(entity.getTransactions()))
                .build();
    }
}
