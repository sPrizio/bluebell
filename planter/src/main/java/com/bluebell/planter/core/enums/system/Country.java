package com.bluebell.planter.core.enums.system;

import com.bluebell.planter.core.enums.account.Currency;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * Enumeration of various countries
 *
 * @author Stephen Prizio
 * @version 0.0.4
 */
@Getter
public enum Country {

    ALL_COUNTRIES("ALL_COUNTRIES", "All Countries", Currency.NOT_APPLICABLE, "-1"),
    ARGENTINA("ARG", "Argentina", Currency.ARGENTINE_PESO, "54"),
    AUSTRALIA("AUS", "Australia", Currency.AUSTRALIAN_DOLLAR, "61"),
    AUSTRIA("AUT", "Austria", Currency.EUROPEAN_EURO, "43"),
    BELGIUM("BEL", "Belgium", Currency.EUROPEAN_EURO, "32"),
    BRAZIL("BRA", "Brazil", Currency.BRAZILIAN_REAL, "55"),
    CANADA("CAN", "Canada", Currency.CANADIAN_DOLLAR, "1"),
    CHINA("CHN", "China", Currency.CHINESE_RENMINBI, "86"),
    COLOMBIA("COL", "Colombia", Currency.COLOMBIAN_PESO, "57"),
    CZECHIA("CZE", "Czechia", Currency.CZECH_KORUNA, "420"),
    DENMARK("DNK", "Denmark", Currency.DANISH_KRONE, "45"),
    FINLAND("FIN", "Finland", Currency.EUROPEAN_EURO, "358"),
    FRANCE("FRA", "France", Currency.EUROPEAN_EURO, "33"),
    GERMANY("DEU", "Germany", Currency.EUROPEAN_EURO, "49"),
    UNITED_KINGDOM("GBR", "United Kingdom", Currency.UK_STERLING, "44"),
    GREECE("GRC", "Greece", Currency.EUROPEAN_EURO, "30"),
    IRELAND("IRL", "Ireland", Currency.EUROPEAN_EURO, "353"),
    ITALY("ITA", "Italy", Currency.EUROPEAN_EURO, "39"),
    JAPAN("JPN", "Japan", Currency.JAPANESE_YEN, "81"),
    NORWAY("NOR", "Norway", Currency.NORWEGIAN_KRONE, "47"),
    PORTUGAL("PRT", "Portugal", Currency.EUROPEAN_EURO, "351"),
    RUSSIA("RUS", "Russia", Currency.RUSSIAN_RUBLE, "7"),
    SPAIN("ESP", "Spain", Currency.EUROPEAN_EURO, "34"),
    SWEDEN("SWE", "Sweden", Currency.SWEDISH_KRONA, "46"),
    UNITED_STATES("USA", "United States", Currency.US_DOLLAR, "1"),
    NOT_APPLICABLE("N/A", "Not Applicable", Currency.NOT_APPLICABLE, "-1");

    private final String isoCode;

    private final String label;

    private final Currency currency;

    private final String phoneCode;

    Country(final String isoCode, final String label, final Currency currency, final String phoneCode) {
        this.isoCode = isoCode;
        this.label = label;
        this.currency = currency;
        this.phoneCode = phoneCode;
    }

    /**
     * Converts a currency iso code to a {@link Country}
     *
     * @param code currency iso code
     * @return {@link Country}
     */
    public static Country getByCurrencyCode(final String code) {

        if (StringUtils.isNotEmpty(code)) {
            Currency curr = Currency.get(code);
            return Arrays.stream(Country.values()).filter(country -> country.currency.equals(curr)).findFirst().orElse(UNITED_STATES);
        }

        return UNITED_STATES;
    }

    /**
     * Returns the {@link Country} for the given iso code
     *
     * @param code iso code
     * @return {@link Country}
     */
    public static Country getByIsoCode(final String code) {
        return switch (code.toUpperCase()) {
            case "ALL_COUNTRIES" -> ALL_COUNTRIES;
            case "ARG" -> ARGENTINA;
            case "AUS" -> AUSTRALIA;
            case "AUT" -> AUSTRIA;
            case "BEL" -> BELGIUM;
            case "BRA" -> BRAZIL;
            case "CAN" -> CANADA;
            case "CHN" -> CHINA;
            case "COL" -> COLOMBIA;
            case "CZE" -> CZECHIA;
            case "DNK" -> DENMARK;
            case "FIN" -> FINLAND;
            case "FRA" -> FRANCE;
            case "DEU" -> GERMANY;
            case "GBR" -> UNITED_KINGDOM;
            case "GRC" -> GREECE;
            case "IRL" -> IRELAND;
            case "ITA" -> ITALY;
            case "JPN" -> JAPAN;
            case "NOR" -> NORWAY;
            case "PRT" -> PORTUGAL;
            case "RUS" -> RUSSIA;
            case "ESP" -> SPAIN;
            case "SWE" -> SWEDEN;
            case "USA" -> UNITED_STATES;
            default -> NOT_APPLICABLE;
        };
    }
}
