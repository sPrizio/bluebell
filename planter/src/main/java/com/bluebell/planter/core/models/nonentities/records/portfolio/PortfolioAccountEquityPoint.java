package com.bluebell.planter.core.models.nonentities.records.portfolio;

import com.bluebell.planter.core.models.entities.account.Account;

/**
 * Class representation of an {@link Account}'s value in a {@link Portfolio}
 *
 * @param name {@link Account} name
 * @param value {@link Account} value (balance)
 * @author Stephen Prizio
 * @version 0.0.7
 */
public record PortfolioAccountEquityPoint(String name, double value) {
}
