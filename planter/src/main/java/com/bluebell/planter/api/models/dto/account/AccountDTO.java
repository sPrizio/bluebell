package com.bluebell.planter.api.models.dto.account;

import com.bluebell.planter.api.models.dto.GenericDTO;
import com.bluebell.planter.api.models.dto.transaction.TransactionDTO;
import com.bluebell.planter.api.models.records.EnumDisplay;
import com.bluebell.planter.core.models.entities.account.Account;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * A DTO representation for {@link Account}
 *
 * @author Stephen Prizio
 * @version 0.0.7
 */
@Setter
@Getter
public class AccountDTO implements GenericDTO {

    private String uid;

    private boolean defaultAccount;

    private LocalDateTime accountOpenTime;

    private LocalDateTime accountCloseTime;

    private double initialBalance;

    private double balance;

    private boolean active;

    private String name;

    private long accountNumber;

    private EnumDisplay currency;

    private EnumDisplay broker;

    private EnumDisplay accountType;

    private EnumDisplay tradePlatform;

    private LocalDateTime lastTraded;

    private List<TransactionDTO> transactions;
}
