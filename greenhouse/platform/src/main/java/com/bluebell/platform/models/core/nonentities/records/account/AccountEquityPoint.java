package com.bluebell.platform.models.core.nonentities.records.account;


import com.bluebell.platform.models.core.entities.account.Account;

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
 * @version 0.0.7
 */
public record AccountEquityPoint(LocalDateTime date, double amount, double points, double cumAmount, double cumPoints) {
}
