package com.bluebell.planter.core.services.trade;

import com.bluebell.planter.core.constants.CoreConstants;
import com.bluebell.planter.core.enums.trade.info.TradeType;
import com.bluebell.planter.core.models.entities.account.Account;
import com.bluebell.planter.core.models.entities.trade.Trade;
import com.bluebell.planter.core.repositories.trade.TradeRepository;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.bluebell.planter.core.validation.GenericValidator.validateDatesAreNotMutuallyExclusive;
import static com.bluebell.planter.core.validation.GenericValidator.validateParameterIsNotNull;

/**
 * Service-layer for {@link Trade} entities
 *
 * @author Stephen Prizio
 * @version 0.0.9
 */
@Service
public class TradeService {

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

        validateParameterIsNotNull(tradeType, CoreConstants.Validation.Trade.TRADE_TYPE_CANNOT_BE_NULL);
        validateParameterIsNotNull(account, CoreConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);

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

        validateParameterIsNotNull(start, CoreConstants.Validation.DateTime.START_DATE_CANNOT_BE_NULL);
        validateParameterIsNotNull(end, CoreConstants.Validation.DateTime.END_DATE_CANNOT_BE_NULL);
        validateDatesAreNotMutuallyExclusive(start, end, CoreConstants.Validation.DateTime.MUTUALLY_EXCLUSIVE_DATES);

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
     * @return {@link List} of {@link Trade}s
     */
    public Page<Trade> findAllTradesWithinTimespan(final LocalDateTime start, final LocalDateTime end, final Account account, final int page, final int pageSize) {

        validateParameterIsNotNull(start, CoreConstants.Validation.DateTime.START_DATE_CANNOT_BE_NULL);
        validateParameterIsNotNull(end, CoreConstants.Validation.DateTime.END_DATE_CANNOT_BE_NULL);
        validateDatesAreNotMutuallyExclusive(start, end, CoreConstants.Validation.DateTime.MUTUALLY_EXCLUSIVE_DATES);
        validateParameterIsNotNull(account, CoreConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);

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
        validateParameterIsNotNull(tradeId, CoreConstants.Validation.Trade.TRADE_ID_CANNOT_BE_NULL);
        validateParameterIsNotNull(account, CoreConstants.Validation.Account.ACCOUNT_CANNOT_BE_NULL);

        return Optional.ofNullable(this.tradeRepository.findTradeByTradeIdAndAccount(tradeId, account));
    }
}
