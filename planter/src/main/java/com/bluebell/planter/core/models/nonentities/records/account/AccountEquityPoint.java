package com.bluebell.planter.core.models.nonentities.records.account;

import com.bluebell.planter.core.models.entities.account.Account;

import java.time.LocalDateTime;

/**
 * Class representation of an {@link Account}'s equity at a particular point in time, meant to be used on a chart
 *
 * @param date point in time
 * @param amount cumulative account balance
 * @param points cumulative account points
 * @author Stephen Prizio
 * @version 0.0.7
 */
public record AccountEquityPoint(LocalDateTime date, double amount, double points) {
}
