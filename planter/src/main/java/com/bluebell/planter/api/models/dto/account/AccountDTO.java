package com.bluebell.planter.api.models.dto.account;

import com.bluebell.planter.api.models.dto.GenericDTO;
import com.bluebell.planter.api.models.records.currency.CurrencyDisplay;
import com.bluebell.planter.core.enums.trade.platform.TradePlatform;
import com.bluebell.planter.core.models.entities.account.Account;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * A DTO representation for {@link Account}
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
@Setter
@Getter
public class AccountDTO implements GenericDTO {

    private String uid;

    private boolean defaultAccount;

    private LocalDateTime accountOpenTime;

    private LocalDateTime accountCloseTime;

    private double balance;

    private boolean active;

    private String name;

    private long accountNumber;

    private CurrencyDisplay currency;

    private String broker;

    private String accountType;

    private TradePlatform tradePlatform;

    private LocalDateTime lastTraded;
}
