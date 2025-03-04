package com.bluebell.platform.enums.account;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * Enum representing a standard currency
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
@Getter
@Schema(title = "Currency", name = "Currency Enum", description = "List of Currencies supported in bluebell.")
public enum Currency {
    US_DOLLAR("USD", "US Dollars", "$"),
    EUROPEAN_EURO("EUR", "Euros", "€"),
    JAPANESE_YEN("JPY", "Japanese Yen", "¥"),
    UK_STERLING("GBP", "Sterling", "£"),
    SWISS_FRANC("CHF", "Swiss Francs", "₣"),
    CANADIAN_DOLLAR("CAD", "Canadian Dollars", "$"),
    AUSTRALIAN_DOLLAR("AUD", "Australian Dollars", "$"),
    NEW_ZEALAND_DOLLAR("NZD", "New Zealand Dollars", "$"),
    SOUTH_AFRICAN_RAND("ZAR", "South African Rand", "R"),
    ARGENTINE_PESO("ARS", "Argentine Peso", "$"),
    BRAZILIAN_REAL("BRL", "Brazilian Real", "R$"),
    CHINESE_RENMINBI("CNY", "Renminbi", "¥"),
    COLOMBIAN_PESO("COP", "Colombian Peso", "$"),
    CZECH_KORUNA("CZK", "Czech Koruna", "Kč"),
    DANISH_KRONE("DKK", "Danish Krone", "kr."),
    NORWEGIAN_KRONE("NOK", "Norwegian Krone", "kr"),
    RUSSIAN_RUBLE("RUB", "Russian Ruble", "₽"),
    SWEDISH_KRONA("SEK", "Swedish Krona", "kr"),
    NOT_APPLICABLE("N/A", "Not Applicable", "na");


    private final String isoCode;

    private final String label;

    private final String symbol;

    Currency(final String isoCode, final String label, final String symbol) {
        this.isoCode = isoCode;
        this.label = label;
        this.symbol = symbol;
    }

    /**
     * Converts a currency iso code to a {@link Currency}
     *
     * @param isoCode currency iso code
     * @return {@link Currency}
     */
    public static Currency get(final String isoCode) {
        return switch (isoCode.toUpperCase()) {
            case "EUR" -> EUROPEAN_EURO;
            case "JPY" -> JAPANESE_YEN;
            case "GBP" -> UK_STERLING;
            case "CHF" -> SWISS_FRANC;
            case "CAD" -> CANADIAN_DOLLAR;
            case "AUD" -> AUSTRALIAN_DOLLAR;
            case "NZD" -> NEW_ZEALAND_DOLLAR;
            case "ZAR" -> SOUTH_AFRICAN_RAND;
            case "ARS" -> ARGENTINE_PESO;
            case "BRL" -> BRAZILIAN_REAL;
            case "CNY" -> CHINESE_RENMINBI;
            case "COP" -> COLOMBIAN_PESO;
            case "CZK" -> CZECH_KORUNA;
            case "DKK" -> DANISH_KRONE;
            case "NOK" -> NORWEGIAN_KRONE;
            case "RUB" -> RUSSIAN_RUBLE;
            case "SEK" -> SWEDISH_KRONA;
            case "USD" -> US_DOLLAR;
            default -> NOT_APPLICABLE;
        };
    }
}
