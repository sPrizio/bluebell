import {keepPreviousData, useQuery} from '@tanstack/react-query';
import {ApiUrls, DateTime} from '../../constants';
import moment from 'moment';
import {
  Account,
  AccountCreationInfo,
  AccountDetails,
  AccountType, AnalysisResult,
  Broker,
  Currency,
  PagedTrades,
  Portfolio,
  PortfolioRecord,
  TradeLog,
  TradePlatform,
  TradeRecordReport,
  Transaction,
  User
} from '@/types/apiTypes';
import {isNumeric} from "@/lib/functions/util-functions";
import {get} from '../../functions/client';

export const useUserQuery = () => {
  return useQuery<User>({
    queryKey: ['user'],
    queryFn: () => get<User>(ApiUrls.User.GetUser, { username: 's.test' }) // TODO: change this to the auth module
  })
}

export const usePortfolioQuery = (portfolioNumber: number) => {
  return useQuery<Portfolio>({
    queryKey: ['portfolio', portfolioNumber],
    queryFn: () => get<Portfolio>(ApiUrls.Portfolio.GetPortfolio, { portfolioNumber: portfolioNumber })
  })
}

export const usePortfolioRecordQuery = (portfolioNumber: number) => {
  return useQuery<PortfolioRecord>({
    queryKey: ['portfolio-record', portfolioNumber],
    queryFn: () => get<PortfolioRecord>(ApiUrls.PortfolioRecord.GetPortfolioRecord, { portfolioNumber: portfolioNumber })
  })
}

export const useRecentTransactionsQuery = () => {
  return useQuery<Array<Transaction>>({
    queryKey: ['recent-transactions'],
    queryFn: () => get<Array<Transaction>>(ApiUrls.User.GetRecentTransactions, {}),
  })
}

export const useTradeLogQuery = () => {
  /*const from = moment().subtract(5, 'days').format(DateTime.ISODateFormat);*/
  const from = moment().subtract(10, 'years').format(DateTime.ISODateFormat); // TODO: Temp
  const to = moment().format(DateTime.ISODateFormat);

  const params = {
    start: from,
    end: to,
    interval: 'DAILY',
    count: "6",
  }

  return useQuery<TradeLog>({
    queryKey: ['trade-log', from, to, params.interval, params.count],
    queryFn: () => get<TradeLog>(ApiUrls.TradeRecord.GetTradeLog, params),
  })
}

export const useRecentTradeRecordsQuery = (id: string, interval: string, count: number) => {
  const { data: user } = useUserQuery()
  const accountId = parseInt(id, 10)

  return useQuery<TradeRecordReport | null>({
    queryKey: ['recent-trade-records', id],
    queryFn: () => get<TradeRecordReport>(ApiUrls.TradeRecord.GetRecentTradeRecords, { accountNumber: accountId.toString(), interval: interval, count: count }),
    enabled: isNumeric(id) && !!user
  })
}

export const useAccountQuery = (id: string) => {
  const { data: user } = useUserQuery()
  const accountId = parseInt(id, 10)

  return useQuery<Account | null>({
    queryKey: ['account', id],
    queryFn: () => user?.portfolios?.flatMap(p => p.accounts)?.find((acc) => acc.accountNumber === accountId) ?? null,
    enabled: isNumeric(id) && !!user
  })
}

export const useAccountDetailsQuery = (id: string) => {
  const { data: user } = useUserQuery()
  const accountId = parseInt(id, 10)

  return useQuery<AccountDetails | null>({
    queryKey: ['account-details', id],
    queryFn: () => get<AccountDetails>(ApiUrls.Account.GetDetails, { accountNumber: accountId.toString() }),
    enabled: isNumeric(id) && !!user
  })
}

export const useCurrenciesQuery = () => {
  return useQuery<Array<Currency>>({
    queryKey: ['currencies'],
    queryFn: () => get<Array<Currency>>(ApiUrls.Account.GetCurrencies, {}),
  })
}

export const useBrokersQuery = () => {
  return useQuery<Array<Broker>>({
    queryKey: ['brokers'],
    queryFn: () => get<Array<Broker>>(ApiUrls.Account.GetBrokers, {}),
  })
}

export const useTradePlatformsQuery = () => {
  return useQuery<Array<TradePlatform>>({
    queryKey: ['trade-platforms'],
    queryFn: () => get<Array<TradePlatform>>(ApiUrls.Account.GetTradePlatforms, {}),
  })
}

export const useAccountTypesQuery = () => {
  return useQuery<Array<AccountType>>({
    queryKey: ['account-types'],
    queryFn: () => get<Array<AccountType>>(ApiUrls.Account.GetAccountTypes, {}),
  })
}

export const useAccountCreationInfoQuery = () => {
  const {data: currencies, isLoading: isCurrenciesLoading} = useCurrenciesQuery()
  const {data: brokers, isLoading: isBrokersLoading} = useBrokersQuery()
  const {data: platforms, isLoading: isPlatformsLoading} = useTradePlatformsQuery()
  const {data: accountTypes, isLoading: isAccountTypesLoading} = useAccountTypesQuery()

  const allLoaded = !(
    isCurrenciesLoading ||
    isBrokersLoading ||
    isPlatformsLoading ||
    isAccountTypesLoading
  )

  return useQuery<AccountCreationInfo>({
    queryKey: ['account-creation-info'],
    queryFn: () => {
      if (!allLoaded) {
        throw new Error("Dependent queries not loaded yet")
      }
      return {
        currencies: currencies ?? [],
        brokers: brokers ?? [],
        platforms: platforms ?? [],
        accountTypes: accountTypes ?? [],
      }
    },
    enabled: allLoaded,
  })
}

export const usePagedTradesQuery = (accountNumber: number, start: string, end: string, page: number, pageSize: number) => {
  return useQuery<PagedTrades | null>({
    placeholderData: keepPreviousData,
    queryKey: ['paginated-trades', accountNumber, start, end, page, pageSize],
    queryFn: () => get<PagedTrades>(ApiUrls.Trade.GetPagedTrades, { accountNumber: accountNumber.toString(), start: start, end: end, page: page.toString(), pageSize: pageSize.toString() }),
    enabled: accountNumber > -1,
  })
}

export const useTimeBucketsAnalysisQuery = (accountNumber: number, isOpened: boolean, filter: string) => {
  return useQuery<Array<AnalysisResult>>({
    queryKey: ['time-buckets', accountNumber, isOpened, filter],
    queryFn: () => get<Array<AnalysisResult>>(ApiUrls.Analysis.TimeBuckets, { accountNumber: accountNumber.toString(), isOpened: isOpened.toString(), filter: filter })
  })
}

export const useWeekdaysAnalysisQuery = (accountNumber: number, filter: string) => {
  return useQuery<Array<AnalysisResult>>({
    queryKey: ['weekdays', accountNumber, filter],
    queryFn: () => get<Array<AnalysisResult>>(ApiUrls.Analysis.Weekdays, { accountNumber: accountNumber.toString(), filter: filter })
  })
}

export const useWeekdaysTimeBucketsAnalysisQuery = (accountNumber: number, weekday: string, filter: string) => {
  return useQuery<Array<AnalysisResult>>({
    queryKey: ['weekdays-time-buckets', accountNumber, weekday, filter],
    queryFn: () => get<Array<AnalysisResult>>(ApiUrls.Analysis.WeekdaysTimeBuckets, { accountNumber: accountNumber.toString(), weekday: weekday, filter: filter })
  })
}

export const useTradeDurationAnalysisQuery = (accountNumber: number, tradeDurationFilter: string, filter: string) => {
  return useQuery<Array<AnalysisResult>>({
    queryKey: ['time-buckets', accountNumber, tradeDurationFilter, filter],
    queryFn: () => get<Array<AnalysisResult>>(ApiUrls.Analysis.TradeDuration, { accountNumber: accountNumber.toString(), tradeDurationFilter: tradeDurationFilter.toString(), filter: filter })
  })
}