package com.bluebell.platform.models.core.nonentities.records.account;


import com.bluebell.platform.models.core.entities.account.Account;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Class representation of an {@link Account}'s equity at a particular point in time, meant to be used on a chart
 *
 * @param date point in time
 * @param amount account balance
 * @param points account points
 * @param cumAmount cumulative account balance
 * @param cumPoints cumulative points
 * @author Stephen Prizio
 * @version 0.1.0
 */
public record AccountEquityPoint(
        @Getter LocalDateTime date,
        @Getter double amount,
        @Getter double points,
        @Getter double cumAmount,
        @Getter double cumPoints
) { }
