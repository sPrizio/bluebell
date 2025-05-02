package com.bluebell.platform.models.api.dto.account;

import com.bluebell.platform.models.api.dto.GenericDTO;
import com.bluebell.platform.models.api.dto.transaction.TransactionDTO;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.nonentities.data.EnumDisplay;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * A DTO representation for {@link Account}
 *
 * @author Stephen Prizio
 * @version 0.2.0
 */
@Getter
@Setter
@Builder
@Schema(title = "AccountDTO", name = "AccountDTO", description = "A client-facing reduction of the Account entity that displays key account information in a safe to read way.")
public class AccountDTO implements GenericDTO {

    @Schema(description = "Account uid")
    private @Builder.Default String uid = StringUtils.EMPTY;

    @Schema(description = "Portfolio uid")
    private @Builder.Default String portfolioUid = StringUtils.EMPTY;

    @Schema(description = "Is the account the default account for the portfolio")
    private boolean defaultAccount;

    @Schema(description = "Date & time of account opening")
    private LocalDateTime accountOpenTime;

    @Schema(description = "Date & time of account closure")
    private LocalDateTime accountCloseTime;

    @Schema(description = "Account starting balance")
    private double initialBalance;

    @Schema(description = "Current balance on the account")
    private double balance;

    @Schema(description = "Is the account active")
    private boolean active;

    @Schema(description = "Account name")
    private String name;

    @Schema(description = "Account number")
    private long accountNumber;

    @Schema(description = "Account's currency")
    private EnumDisplay currency;

    @Schema(description = "Account's broker")
    private EnumDisplay broker;

    @Schema(description = "Account type")
    private EnumDisplay accountType;

    @Schema(description = "Trading platform used on account")
    private EnumDisplay tradePlatform;

    @Schema(description = "Date & time of last trade")
    private LocalDateTime lastTraded;

    @Schema(description = "List of transactions on the account")
    private List<TransactionDTO> transactions;
}
