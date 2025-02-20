package com.bluebell.processing.processors;

import com.bluebell.platform.enums.account.AccountType;
import com.bluebell.platform.enums.account.Broker;
import com.bluebell.platform.enums.account.Currency;
import com.bluebell.platform.enums.platform.TradePlatform;
import com.bluebell.platform.enums.system.Country;
import com.bluebell.platform.enums.system.Language;
import com.bluebell.platform.enums.system.PhoneType;
import com.bluebell.platform.models.api.dto.account.AccountDTO;
import com.bluebell.platform.models.api.dto.security.UserDTO;
import com.bluebell.platform.models.api.dto.trade.PaginatedTradesDTO;
import com.bluebell.platform.models.api.dto.trade.TradeDTO;
import com.bluebell.platform.models.api.dto.transaction.TransactionDTO;
import com.bluebell.platform.models.api.json.StandardJsonResponse;
import com.bluebell.platform.models.core.entities.news.MarketNews;
import com.bluebell.platform.models.core.nonentities.apexcharts.ApexChartCandleStick;
import com.bluebell.platform.models.core.nonentities.data.PairEntry;
import com.bluebell.platform.models.core.nonentities.records.account.AccountDetails;
import com.bluebell.platform.models.core.nonentities.records.analysis.AnalysisResult;
import com.bluebell.platform.models.core.nonentities.records.portfolio.Portfolio;
import com.bluebell.platform.models.core.nonentities.records.tradelog.TradeLog;
import com.bluebell.platform.models.core.nonentities.records.traderecord.TradeRecord;
import com.bluebell.platform.models.core.nonentities.records.traderecord.TradeRecordReport;
import com.bluebell.platform.models.core.nonentities.records.traderecord.controls.TradeRecordControls;
import com.bluebell.processing.enums.DependencyType;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.*;
import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Annotation Processor for providing api example values for Swagger documentation
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
public class ApiExamplesProcessor extends AbstractBluebellProcessor implements BluebellProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiExamplesProcessor.class);
    private static final String BASE_API_PACKAGE = "com.bluebell.processing.api.examples";


    //  METHODS

    @Override
    public boolean process() {

        try {
            generateApiErrorExamples();
            generatePrimitiveApiExamples();
            generateAccountApiExamples();

            return true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public void updateDependencies() throws IOException {
        updateModuleInfo(DependencyType.EXPORTS, BASE_API_PACKAGE);
    }


    //  HELPERS

    /**
     * Generates api examples for error return types
     */
    private void generateApiErrorExamples() {

        try {
            final StandardJsonResponse<List<PairEntry>> request = new StandardJsonResponse<>(false, null, "Unauthorized Request.");
            final StandardJsonResponse<?> missingAccount = new StandardJsonResponse<>(false, null, "No account was found for account number <bad_account_number>");
            final StandardJsonResponse<?> badFilter = new StandardJsonResponse<>(false, null, "<bad_value> was not a valid filter.");
            final StandardJsonResponse<?> badWeekday = new StandardJsonResponse<>(false, null, "<bad_value> was not a valid weekday.");
            final StandardJsonResponse<?> badDate = new StandardJsonResponse<>(false, null, "The date <bad_date> was not of the expected format <expected_format>.");
            final StandardJsonResponse<?> badStartInterval = new StandardJsonResponse<>(false, null, "The start date <bad_start_date> was not of the expected format <expected_format>.");
            final StandardJsonResponse<?> badEndInterval = new StandardJsonResponse<>(false, null, "The end date <bad_start_date> was not of the expected format <expected_format>.");
            final StandardJsonResponse<?> badQuery = new StandardJsonResponse<>(false, null, "The request object did not contain one of the required keys : <required_keys_list>");
            final StandardJsonResponse<?> noUser = new StandardJsonResponse<>(false, null, "No user found for username : <username>");
            final StandardJsonResponse<?> badTradeType = new StandardJsonResponse<>(false, null, "<bad_trade_type> is not a valid trade type");
            final StandardJsonResponse<?> badTrade = new StandardJsonResponse<>(false, null, "No trade was found with trade id: <bad_trade_id>");
            final StandardJsonResponse<?> noTradesType = new StandardJsonResponse<>(false, null, "No trades were found for type <trade_type>");
            final StandardJsonResponse<?> noTradesForInterval = new StandardJsonResponse<>(false, null, "No trades were found within interval [start, end]");
            final StandardJsonResponse<?> badFileFormat = new StandardJsonResponse<>(false, null, "The given file <bad_file> was not of a valid format");
            final StandardJsonResponse<?> badTradeRecordTimeInterval = new StandardJsonResponse<>(false, null, "<bad_trade_record_time_interval> was not a valid interval.");

            TypeSpec examples =
                    TypeSpec
                            .classBuilder("ApiErrorExamples")
                            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                            .addField(generateGlobalStaticField("UNAUTHORIZED_REQUEST", request))
                            .addField(generateGlobalStaticField("ACCOUNT_NOT_FOUND_EXAMPLE", missingAccount))
                            .addField(generateGlobalStaticField("BAD_FILTER_EXAMPLE", badFilter))
                            .addField(generateGlobalStaticField("BAD_WEEKDAY_EXAMPLE", badWeekday))
                            .addField(generateGlobalStaticField("BAD_DATE_EXAMPLE", badDate))
                            .addField(generateGlobalStaticField("BAD_START_INTERVAL_EXAMPLE", badStartInterval))
                            .addField(generateGlobalStaticField("BAD_END_INTERVAL_EXAMPLE", badEndInterval))
                            .addField(generateGlobalStaticField("BAD_QUERY_EXAMPLE", badQuery))
                            .addField(generateGlobalStaticField("NO_USER_EXAMPLE", noUser))
                            .addField(generateGlobalStaticField("BAD_TRADE_TYPE_EXAMPLE", badTradeType))
                            .addField(generateGlobalStaticField("BAD_TRADE_EXAMPLE", badTrade))
                            .addField(generateGlobalStaticField("NO_TRADES_FOR_TYPE_EXAMPLE", noTradesType))
                            .addField(generateGlobalStaticField("NO_TRADES_FOR_INTERVAL_EXAMPLE", noTradesForInterval))
                            .addField(generateGlobalStaticField("BAD_FILE_FORMAT_EXAMPLE", badFileFormat))
                            .addField(generateGlobalStaticField("BAD_TRADE_RECORD_TIME_INTERVAL_EXAMPLE", badTradeRecordTimeInterval))
                            .build();

            // Generate the file
            JavaFile javaFile =
                    JavaFile
                            .builder(BASE_API_PACKAGE, examples)
                            .build();

            javaFile.writeTo(Path.of(getGeneratedSourcePath()));
            LOGGER.info("Generated Api Error Examples.");
        } catch (IOException e) {
            if (!(e instanceof FilerException)) {
                LOGGER.error("Code generation failed for Api Error Examples : {}", e.getMessage());
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    /**
     * Generates api examples for primitive return types
     */
    private void generatePrimitiveApiExamples() {

        try {
            final StandardJsonResponse<String> string = new StandardJsonResponse<>(true, "Hello World!", StringUtils.EMPTY);
            final StandardJsonResponse<Integer> integer = new StandardJsonResponse<>(true, 91, StringUtils.EMPTY);
            final StandardJsonResponse<Long> longg = new StandardJsonResponse<>(true, 53153415876453135L, StringUtils.EMPTY);
            final StandardJsonResponse<Double> doublee = new StandardJsonResponse<>(true, 91.5, StringUtils.EMPTY);
            final StandardJsonResponse<Boolean> booleann = new StandardJsonResponse<>(true, true, StringUtils.EMPTY);

            TypeSpec primitiveApiExamples =
                    TypeSpec
                            .classBuilder("ApiPrimitiveExamples")
                            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                            .addField(generateGlobalStaticField("STRING_EXAMPLE", string))
                            .addField(generateGlobalStaticField("INTEGER_EXAMPLE", integer))
                            .addField(generateGlobalStaticField("LONG_EXAMPLE", longg))
                            .addField(generateGlobalStaticField("DOUBLE_EXAMPLE", doublee))
                            .addField(generateGlobalStaticField("BOOLEAN_EXAMPLE", booleann))
                            .build();

            // Generate the file
            JavaFile javaFile =
                    JavaFile
                            .builder(BASE_API_PACKAGE, primitiveApiExamples)
                            .build();

            javaFile.writeTo(Path.of(getGeneratedSourcePath()));
            LOGGER.info("Generated Api Primitive Examples.");
        } catch (IOException e) {
            if (!(e instanceof FilerException)) {
                LOGGER.error("Code generation failed for API Primitive Examples : {}", e.getMessage());
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    /**
     * TODO: Generates api examples for the AccountApiController
     */
    private void generateAccountApiExamples() {

        try {
            final StandardJsonResponse<List<PairEntry>> currencyEntries = new StandardJsonResponse<>(true, Arrays.stream(Currency.values()).map(c -> new PairEntry(c.getIsoCode(), c.getLabel(), c.getSymbol())).toList(), StringUtils.EMPTY);
            final StandardJsonResponse<List<PairEntry>> accountTypeEntries = new StandardJsonResponse<>(true, Arrays.stream(AccountType.values()).map(at -> new PairEntry(at.getLabel().toUpperCase(), at.getLabel(), StringUtils.EMPTY)).toList(), StringUtils.EMPTY);
            final StandardJsonResponse<List<PairEntry>> brokerEntries = new StandardJsonResponse<>(true, Arrays.stream(Broker.values()).map(b -> new PairEntry(b.getCode(), b.getName(), StringUtils.EMPTY)).toList(), StringUtils.EMPTY);
            final StandardJsonResponse<List<PairEntry>> tradePlatformEntries = new StandardJsonResponse<>(true, Arrays.stream(TradePlatform.values()).map(tp -> new PairEntry(tp.getCode(), tp.getLabel(), StringUtils.EMPTY)).toList(), StringUtils.EMPTY);
            final StandardJsonResponse<AccountDetails> accountDetails = new StandardJsonResponse<>(true, null, StringUtils.EMPTY);
            final StandardJsonResponse<AccountDTO> accountDTO = new StandardJsonResponse<>(true, null, StringUtils.EMPTY);
            final StandardJsonResponse<List<AnalysisResult>> successfulAnalysis = new StandardJsonResponse<>(true, null, StringUtils.EMPTY);
            final StandardJsonResponse<List<ApexChartCandleStick>> apexChartCandleSticks = new StandardJsonResponse<>(true, null, StringUtils.EMPTY);
            final StandardJsonResponse<List<MarketNews>> emptyNews = new StandardJsonResponse<>(true, null, "No news for the given date <date>");
            final StandardJsonResponse<MarketNews> news = new StandardJsonResponse<>(true, null, StringUtils.EMPTY);
            final StandardJsonResponse<List<MarketNews>> newsList = new StandardJsonResponse<>(true, List.of(), StringUtils.EMPTY);
            final StandardJsonResponse<Portfolio> portfolio = new StandardJsonResponse<>(true, null, StringUtils.EMPTY);
            final StandardJsonResponse<TreeSet<String>> countryCodes = new StandardJsonResponse<>(true, Arrays.stream(Country.values()).map(Country::getPhoneCode).collect(Collectors.toCollection(TreeSet::new)), StringUtils.EMPTY);
            final StandardJsonResponse<PhoneType[]> phoneTypes = new StandardJsonResponse<>(true, PhoneType.values(), StringUtils.EMPTY);
            final StandardJsonResponse<TreeSet<String>> currencyCodes = new StandardJsonResponse<>(true, Arrays.stream(Currency.values()).map(Currency::getIsoCode).collect(Collectors.toCollection(TreeSet::new)), StringUtils.EMPTY);
            final StandardJsonResponse<Country[]> countries = new StandardJsonResponse<>(true, Country.values(), StringUtils.EMPTY);
            final StandardJsonResponse<Language[]> languages = new StandardJsonResponse<>(true, Language.values(), StringUtils.EMPTY);
            final StandardJsonResponse<List<TransactionDTO>> transactionDTOs = new StandardJsonResponse<>(true, null, StringUtils.EMPTY);
            final StandardJsonResponse<UserDTO> userDTO = new StandardJsonResponse<>(true, null, StringUtils.EMPTY);
            final StandardJsonResponse<List<TradeDTO>> tradesDTO = new StandardJsonResponse<>(true, null, StringUtils.EMPTY);
            final StandardJsonResponse<TradeDTO> tradeDTO = new StandardJsonResponse<>(true, null, StringUtils.EMPTY);
            final StandardJsonResponse<TradeRecordReport> tradeRecords = new StandardJsonResponse<>(true, null, StringUtils.EMPTY);
            final StandardJsonResponse<TradeRecord> tradeRecord = new StandardJsonResponse<>(true, null, StringUtils.EMPTY);
            final StandardJsonResponse<PaginatedTradesDTO> paginatedTrades = new StandardJsonResponse<>(true, null, StringUtils.EMPTY);
            final StandardJsonResponse<TradeRecordControls> tradeRecordControls = new StandardJsonResponse<>(true, null, StringUtils.EMPTY);
            final StandardJsonResponse<TradeLog> tradeLog = new StandardJsonResponse<>(true, null, StringUtils.EMPTY);

            TypeSpec accountApiExamples =
                    TypeSpec
                            .classBuilder("AccountApiExamples")
                            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                            .addField(generateGlobalStaticField("CURRENCY_EXAMPLE", currencyEntries))
                            .addField(generateGlobalStaticField("ACCOUNT_TYPE_EXAMPLE", accountTypeEntries))
                            .addField(generateGlobalStaticField("BROKER_EXAMPLE", brokerEntries))
                            .addField(generateGlobalStaticField("TRADE_PLATFORM_EXAMPLE", tradePlatformEntries))
                            .addField(generateGlobalStaticField("ACCOUNT_DETAILS_EXAMPLE", accountDetails))
                            .addField(generateGlobalStaticField("ACCOUNT_DTO_EXAMPLE", accountDTO))
                            .addField(generateGlobalStaticField("ANALYSIS_EXAMPLE", successfulAnalysis))
                            .addField(generateGlobalStaticField("APEX_EXAMPLE", apexChartCandleSticks))
                            .addField(generateGlobalStaticField("NO_NEWS_EXAMPLE", emptyNews))
                            .addField(generateGlobalStaticField("NEWS_EXAMPLE", news))
                            .addField(generateGlobalStaticField("NEWS_LIST_EXAMPLE", newsList))
                            .addField(generateGlobalStaticField("PORTFOLIO_EXAMPLE", portfolio))
                            .addField(generateGlobalStaticField("COUNTRY_CODES_EXAMPLE", countryCodes))
                            .addField(generateGlobalStaticField("PHONE_TYPES_EXAMPLE", phoneTypes))
                            .addField(generateGlobalStaticField("CURRENCY_CODES_EXAMPLE", currencyCodes))
                            .addField(generateGlobalStaticField("COUNTRIES_EXAMPLE", countries))
                            .addField(generateGlobalStaticField("LANGUAGES_EXAMPLE", languages))
                            .addField(generateGlobalStaticField("TRANSACTIONS_EXAMPLE", transactionDTOs))
                            .addField(generateGlobalStaticField("USER_DTO_EXAMPLE", userDTO))
                            .addField(generateGlobalStaticField("TRADES_DTO_EXAMPLE", tradesDTO))
                            .addField(generateGlobalStaticField("TRADE_DTO_EXAMPLE", tradeDTO))
                            .addField(generateGlobalStaticField("TRADE_RECORDS_EXAMPLE", tradeRecords))
                            .addField(generateGlobalStaticField("TRADE_RECORD_EXAMPLE", tradeRecord))
                            .addField(generateGlobalStaticField("PAGINATED_TRADES_DTO_EXAMPLE", paginatedTrades))
                            .addField(generateGlobalStaticField("TRADE_RECORD_CONTROLS_EXAMPLE", tradeRecordControls))
                            .addField(generateGlobalStaticField("TRADE_LOG_EXAMPLE", tradeLog))
                            .build();

            // Generate the file
            JavaFile javaFile =
                    JavaFile
                            .builder(BASE_API_PACKAGE, accountApiExamples)
                            .build();

            javaFile.writeTo(Path.of(getGeneratedSourcePath()));
            LOGGER.info("Generated Account Api Examples.");
        } catch (IOException e) {
            if (!(e instanceof FilerException)) {
                LOGGER.error("Code generation failed for Account API Examples : {}", e.getMessage());
                LOGGER.error(e.getMessage(), e);
            }
        }
    }
}
