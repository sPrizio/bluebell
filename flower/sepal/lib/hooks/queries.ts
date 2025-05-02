import {useQuery} from '@tanstack/react-query';
import {ApiUrls, DateTime} from '../constants';
import moment from 'moment';
import {
  Account, AccountCreationInfo, AccountType,
  Broker,
  Currency,
  Portfolio,
  PortfolioRecord,
  TradeLog,
  TradePlatform,
  Transaction,
  User
} from '@/types/apiTypes';
import {isNumeric} from "@/lib/functions/util-functions";
import {get} from '../functions/client';

export const useUserQuery = () => {
  return useQuery<User>({
    queryKey: ['user'],
    queryFn: () => get<User>(ApiUrls.User.GetUser, { username: 's.test' }) // TODO: change this to the auth module
  })
}

export const usePortfolioQuery = (portfolioUid: string) => {
  return useQuery<Portfolio>({
    queryKey: ['portfolio'],
    queryFn: () => get<Portfolio>(ApiUrls.Portfolio.GetPortfolio, { uid: portfolioUid }),
    enabled: (portfolioUid?.length ?? 0) > 0,
  })
}

export const usePortfolioRecordQuery = (portfolioUid: string) => {
  return useQuery<PortfolioRecord>({
    queryKey: ['portfolio-record'],
    queryFn: () => get<PortfolioRecord>(ApiUrls.PortfolioRecord.GetPortfolioRecord, { uid: portfolioUid }),
    enabled: (portfolioUid?.length ?? 0) > 0,
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
    queryKey: ['trade-log'],
    queryFn: () => get<TradeLog>(ApiUrls.TradeRecord.GetTradeLog, params),
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
  const {data: currencies} = useCurrenciesQuery()
  const {data: brokers} = useBrokersQuery()
  const {data: platforms} = useTradePlatformsQuery()
  const {data: accountTypes} = useAccountTypesQuery()

  return useQuery<AccountCreationInfo>({
    queryKey: ['account-creation-info'],
    queryFn: () => ({
      currencies: currencies ?? [],
      brokers: brokers ?? [],
      platforms: platforms ?? [],
      accountTypes: accountTypes ?? [],
    })
  })
}
