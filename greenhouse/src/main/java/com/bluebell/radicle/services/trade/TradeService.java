package com.bluebell.radicle.services.trade;

import com.bluebell.platform.constants.CorePlatformConstants;
import com.bluebell.platform.enums.GenericEnum;
import com.bluebell.platform.enums.trade.TradePlatform;
import com.bluebell.platform.enums.trade.TradeType;
import com.bluebell.platform.models.api.dto.trade.CreateUpdateMultipleTradesDTO;
import com.bluebell.platform.models.core.entities.account.Account;
import com.bluebell.platform.models.core.entities.trade.Trade;
import com.bluebell.platform.services.MathService;
import com.bluebell.radicle.repositories.account.AccountRepository;
import com.bluebell.radicle.repositories.trade.TradeRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static com.bluebell.radicle.validation.GenericValidator.validateDatesAreNotMutuallyExclusive;
import static com.bluebell.radicle.validation.GenericValidator.validateParameterIsNotNull;

/**
 * Service-layer for {@link Trade} entities
 *
 * @author Stephen Prizio
 * @version 0.1.6
 */
@Slf4j
@Service
public class TradeService {

    private final MathService mathService = new MathService();

    @Resource(name = "accountRepository")
    private AccountRepository accountRepository;

    @Resource(name = "tradeRepository")
    private TradeRepository tradeRepository;


    //  METHODS

    /**
     * Returns a {@link List} of {@link Trade}s for the given {@link TradeType} and {@link Account}
     *
     * @param tradeType {@link TradeType}
     * @param account   {@link Account}
     * @return {@link List} of {@link Trade}s
     */
    public List<Trade> findAllByTradeType(final TradeType tradeType, final Account account) {

        validateParameterIsNotNull(tradeType, CorePlatformConstants.Validation.Trade.TRADE_TYPE_CANNOT_BE_NULL);
        validateParameterIsNotNull(account, CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);

        return this.tradeRepository.findAllByTradeTypeAndAccountOrderByTradeOpenTimeAsc(tradeType, account);
    }

    /**
     * Returns a {@link List} of {@link Trade}s that are within the given time span
     *
     * @param start   {@link LocalDateTime} start of interval (inclusive)
     * @param end     {@link LocalDateTime} end of interval (exclusive)
     * @param account {@link Account}
     * @return {@link List} of {@link Trade}s
     */
    public List<Trade> findAllTradesWithinTimespan(final LocalDateTime start, final LocalDateTime end, final Account account) {

        validateParameterIsNotNull(start, CorePlatformConstants.Validation.DateTime.START_DATE_CANNOT_BE_NULL);
        validateParameterIsNotNull(end, CorePlatformConstants.Validation.DateTime.END_DATE_CANNOT_BE_NULL);
        validateDatesAreNotMutuallyExclusive(start, end, CorePlatformConstants.Validation.DateTime.MUTUALLY_EXCLUSIVE_DATES);

        return this.tradeRepository.findAllTradesWithinDate(start, end, account);
    }

    /**
     * Returns a {@link List} of {@link Trade}s that are within the given time span
     *
     * @param start    {@link LocalDateTime} start of interval (inclusive)
     * @param end      {@link LocalDateTime} end of interval (exclusive)
     * @param account  {@link Account}
     * @param page     page number
     * @param pageSize page size
     * @return {@link Page} of {@link Trade}s
     */
    public Page<Trade> findAllTradesWithinTimespan(final LocalDateTime start, final LocalDateTime end, final Account account, final int page, final int pageSize) {

        validateParameterIsNotNull(start, CorePlatformConstants.Validation.DateTime.START_DATE_CANNOT_BE_NULL);
        validateParameterIsNotNull(end, CorePlatformConstants.Validation.DateTime.END_DATE_CANNOT_BE_NULL);
        validateDatesAreNotMutuallyExclusive(start, end, CorePlatformConstants.Validation.DateTime.MUTUALLY_EXCLUSIVE_DATES);
        validateParameterIsNotNull(account, CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);

        return this.tradeRepository.findAllTradesWithinDatePaged(start.toLocalDate().atStartOfDay(), end.toLocalDate().atStartOfDay(), account, PageRequest.of(page, pageSize));
    }

    /**
     * Returns an {@link Optional} containing a {@link Trade}
     *
     * @param tradeId trade id
     * @param account {@link Account}
     * @return {@link Optional} {@link Trade}
     */
    public Optional<Trade> findTradeByTradeId(final String tradeId, final Account account) {
        validateParameterIsNotNull(tradeId, CorePlatformConstants.Validation.Trade.TRADE_ID_CANNOT_BE_NULL);
        validateParameterIsNotNull(account, CorePlatformConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);

        return Optional.ofNullable(this.tradeRepository.findTradeByTradeIdAndAccount(tradeId, account));
    }

    /**
     * Creates new trades for the given payload
     *
     * @param tradesDTO {@link CreateUpdateMultipleTradesDTO}
     * @return true if trades are created
     */
    public boolean createTrades(final CreateUpdateMultipleTradesDTO tradesDTO) {
        validateParameterIsNotNull(tradesDTO, "trades dto cannot be null");
        validateParameterIsNotNull(tradesDTO.userIdentifier(), CorePlatformConstants.Validation.Security.User.USER_CANNOT_BE_NULL);

        if (CollectionUtils.isEmpty(tradesDTO.trades())) {
            LOGGER.error("No trades were given for creation");
            return false;
        }

        final Account account = this.accountRepository.findAccountByAccountNumber(tradesDTO.accountNumber());
        if (account == null) {
            LOGGER.error("Account not found for number {}", tradesDTO.accountNumber());
            return false;
        }

        try {
            final List<Trade> trades = tradesDTO.trades().stream().map(td ->
                    Trade
                            .builder()
                            .tradeId(td.tradeId())
                            .product(td.product())
                            .tradePlatform(GenericEnum.getByCode(TradePlatform.class, td.tradePlatform()))
                            .tradeType(GenericEnum.getByCode(TradeType.class, td.tradeType()))
                            .tradeOpenTime(LocalDateTime.parse(td.tradeOpenTime(), DateTimeFormatter.ofPattern(CorePlatformConstants.MT4_DATE_SHORT_TIME_FORMAT)))
                            .tradeCloseTime(LocalDateTime.parse(td.tradeCloseTime(), DateTimeFormatter.ofPattern(CorePlatformConstants.MT4_DATE_SHORT_TIME_FORMAT)))
                            .lotSize(this.mathService.getDouble(td.lotSize()))
                            .openPrice(this.mathService.getDouble(td.openPrice()))
                            .closePrice(this.mathService.getDouble(td.closePrice()))
                            .netProfit(this.mathService.getDouble(td.netProfit()))
                            .stopLoss(this.mathService.getDouble(td.stopLoss()))
                            .takeProfit(this.mathService.getDouble(td.takeProfit()))
                            .account(account)
                            .build()
            ).toList();

            saveAll(trades, account);
            return true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * Saves all {@link Trade}s within the given list to the database
     *
     * @param trades {@link List} of {@link Trade}s
     * @param account {@link Account}
     * @return count of insertions/updates
     */
    public int saveAll(final List<Trade> trades, final Account account) {

        if (CollectionUtils.isEmpty(trades) || account == null) {
            return -1;
        }

        int count = 0;
        for (final Trade trade : trades) {
            count += this.tradeRepository.upsertTrade(
                    trade.getTradeId(),
                    trade.getProduct(),
                    trade.getTradePlatform(),
                    trade.getTradeType(),
                    trade.getTradeOpenTime(),
                    trade.getTradeCloseTime(),
                    trade.getLotSize(),
                    trade.getOpenPrice(),
                    trade.getClosePrice(),
                    trade.getNetProfit(),
                    trade.getStopLoss(),
                    trade.getTakeProfit(),
                    account.getId()
            );
        }

        return count;
    }
}
