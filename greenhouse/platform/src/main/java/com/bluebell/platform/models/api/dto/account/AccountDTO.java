package com.bluebell.platform.models.api.dto.account;

import com.bluebell.platform.models.api.dto.GenericDTO;
import com.bluebell.platform.models.api.dto.transaction.TransactionDTO;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.nonentities.data.EnumDisplay;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * A DTO representation for {@link Account}
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
@Setter
@Getter
@Schema(description = "A client-facing reduction of the Account entity that displays key account information in a safe to read way.")
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
