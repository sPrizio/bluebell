package com.bluebell.platform.models.core.nonentities.records.account;


import java.time.LocalDateTime;

import com.bluebell.platform.models.core.entities.account.Account;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

/**
 * Class representation of an {@link Account}'s equity at a particular point in time, meant to be used on a chart
 *
 * @param date point in time
 * @param amount account balance
 * @param points account points
 * @param cumAmount cumulative account balance
 * @param cumPoints cumulative points
 * @author Stephen Prizio
 * @version 0.1.1
 */
@Builder
@Schema(title = "AccountEquityPoint", name = "AccountEquityPoint", description = "Account's value at a given point in time")
public record AccountEquityPoint(
        @Getter @Schema(description = "Date") LocalDateTime date,
        @Getter @Schema(description = "Account value") double amount,
        @Getter @Schema(description = "Points gained/lost") double points,
        @Getter @Schema(description = "Cumulative gain/loss") double cumAmount,
        @Getter @Schema(description = "Cumulative points gained/lost") double cumPoints
) { }
