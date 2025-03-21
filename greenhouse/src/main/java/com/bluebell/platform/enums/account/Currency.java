package com.bluebell.platform.enums.account;

import com.bluebell.platform.enums.GenericEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * Enum representing a standard currency
 *
 * @author Stephen Prizio
 * @version 0.1.3
 */
@Getter
@Schema(title = "Currency", name = "Currency Enum", description = "List of Currencies supported in bluebell.")
public enum Currency implements GenericEnum<Currency> {
    US_DOLLAR("USD", "USD", "US Dollars", "$"),
    EUROPEAN_EURO("EUR", "EUR", "Euros", "€"),
    JAPANESE_YEN("JPY", "JPY", "Japanese Yen", "¥"),
    UK_STERLING("GBP", "GBP", "Sterling", "£"),
    SWISS_FRANC("CHF", "CHF", "Swiss Francs", "₣"),
    CANADIAN_DOLLAR("CAD", "CAD", "Canadian Dollars", "$"),
    AUSTRALIAN_DOLLAR("AUD", "AUD", "Australian Dollars", "$"),
    NEW_ZEALAND_DOLLAR("NZD", "NZD", "New Zealand Dollars", "$"),
    SOUTH_AFRICAN_RAND("ZAR", "ZAR", "South African Rand", "R"),
    ARGENTINE_PESO("ARS", "ARS", "Argentine Peso", "$"),
    BRAZILIAN_REAL("BRL", "BRL", "Brazilian Real", "R$"),
    CHINESE_RENMINBI("CNY", "CNY", "Renminbi", "¥"),
    COLOMBIAN_PESO("COP", "COP", "Colombian Peso", "$"),
    CZECH_KORUNA("CZK", "CZK", "Czech Koruna", "Kč"),
    DANISH_KRONE("DKK", "DKK", "Danish Krone", "kr."),
    NORWEGIAN_KRONE("NOK", "NOK", "Norwegian Krone", "kr"),
    RUSSIAN_RUBLE("RUB", "RUB", "Russian Ruble", "₽"),
    SWEDISH_KRONA("SEK", "SEK", "Swedish Krona", "kr"),
    NOT_APPLICABLE("N/A", "N/A", "Not Applicable", "na");

    private final String code;

    private final String isoCode;

    private final String label;

    private final String symbol;

    Currency(final String code, final String isoCode, final String label, final String symbol) {
        this.code = code;
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
    public static Currency getByIsoCode(final String isoCode) {
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
