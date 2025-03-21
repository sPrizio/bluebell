package com.bluebell.platform.enums.system;

import com.bluebell.platform.enums.GenericEnum;
import com.bluebell.platform.enums.account.Currency;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * Enumeration of various countries
 *
 * @author Stephen Prizio
 * @version 0.1.1
 */
@Getter
@Schema(title = "Country", name = "Country", description = "Countries supported by bluebell")
public enum Country implements GenericEnum<Country> {
    ALL_COUNTRIES("ALL_COUNTRIES", "ALL_COUNTRIES", "All Countries", Currency.NOT_APPLICABLE, "-1"),
    ARGENTINA("ARG", "ARG", "Argentina", Currency.ARGENTINE_PESO, "54"),
    AUSTRALIA("AUS", "AUS", "Australia", Currency.AUSTRALIAN_DOLLAR, "61"),
    AUSTRIA("AUT", "AUT", "Austria", Currency.EUROPEAN_EURO, "43"),
    BELGIUM("BEL", "BEL", "Belgium", Currency.EUROPEAN_EURO, "32"),
    BRAZIL("BRA", "BRA", "Brazil", Currency.BRAZILIAN_REAL, "55"),
    CANADA("CAN", "CAN", "Canada", Currency.CANADIAN_DOLLAR, "1"),
    CHINA("CHN", "CHN", "China", Currency.CHINESE_RENMINBI, "86"),
    COLOMBIA("COL", "COL", "Colombia", Currency.COLOMBIAN_PESO, "57"),
    CZECHIA("CZE", "CZE", "Czechia", Currency.CZECH_KORUNA, "420"),
    DENMARK("DNK", "DNK", "Denmark", Currency.DANISH_KRONE, "45"),
    FINLAND("FIN", "FIN", "Finland", Currency.EUROPEAN_EURO, "358"),
    FRANCE("FRA", "FRA", "France", Currency.EUROPEAN_EURO, "33"),
    GERMANY("DEU", "DEU", "Germany", Currency.EUROPEAN_EURO, "49"),
    UNITED_KINGDOM("GBR", "GBR", "United Kingdom", Currency.UK_STERLING, "44"),
    GREECE("GRC", "GRC", "Greece", Currency.EUROPEAN_EURO, "30"),
    IRELAND("IRL", "IRL", "Ireland", Currency.EUROPEAN_EURO, "353"),
    ITALY("ITA", "ITA", "Italy", Currency.EUROPEAN_EURO, "39"),
    JAPAN("JPN", "JPN", "Japan", Currency.JAPANESE_YEN, "81"),
    NORWAY("NOR", "NOR", "Norway", Currency.NORWEGIAN_KRONE, "47"),
    PORTUGAL("PRT", "PRT", "Portugal", Currency.EUROPEAN_EURO, "351"),
    RUSSIA("RUS", "RUS", "Russia", Currency.RUSSIAN_RUBLE, "7"),
    SPAIN("ESP", "ESP", "Spain", Currency.EUROPEAN_EURO, "34"),
    SWEDEN("SWE", "SWE", "Sweden", Currency.SWEDISH_KRONA, "46"),
    UNITED_STATES("USA", "USA", "United States", Currency.US_DOLLAR, "1"),
    NOT_APPLICABLE("N/A", "N/A", "Not Applicable", Currency.NOT_APPLICABLE, "-1");

    private final String code;

    private final String isoCode;

    private final String label;

    private final Currency currency;

    private final String phoneCode;

    Country(final String code, final String isoCode, final String label, final Currency currency, final String phoneCode) {
        this.code = code;
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
            Currency curr = Currency.getByIsoCode(code);
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
