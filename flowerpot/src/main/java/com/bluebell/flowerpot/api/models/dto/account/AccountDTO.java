package com.bluebell.flowerpot.api.models.dto.account;

import com.bluebell.flowerpot.api.models.dto.GenericDTO;
import com.bluebell.flowerpot.api.models.records.currency.CurrencyDisplay;
import com.bluebell.flowerpot.core.enums.trade.platform.TradePlatform;
import com.bluebell.flowerpot.core.models.entities.account.Account;
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
