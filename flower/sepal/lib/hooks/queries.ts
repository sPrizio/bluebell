import {useQuery} from '@tanstack/react-query';
import {getPortfolio, getPortfolioRecord} from '../functions/portfolio-functions';
import {getRecentTransactions, getUser} from '../functions/account-functions';
import {getTradeLog} from '../functions/trade-functions';
import {DateTime} from '../constants';
import moment from 'moment';
import {Account, Portfolio, PortfolioRecord, TradeLog, Transaction, User} from '@/types/apiTypes';
import {isNumeric} from "@/lib/functions/util-functions";

export const useUserQuery = () => {
  return useQuery<User>({
    queryKey: ['user'],
    queryFn: () => getUser('s.test'), // TODO: change this to the auth module
  })
}

export const usePortfolioQuery = (portfolioUid: string, options = {}) => {
  return useQuery<Portfolio>({
    queryKey: ['portfolio'],
    queryFn: () => getPortfolio(portfolioUid),
    enabled: (portfolioUid?.length ?? 0) > 0,
  })
}

export const usePortfolioRecordQuery = (portfolioUid: string, options = {}) => {
  return useQuery<PortfolioRecord>({
    queryKey: ['portfolio-record'],
    queryFn: () => getPortfolioRecord(portfolioUid),
  })
}

export const useRecentTransactionsQuery = () => {
  return useQuery<Array<Transaction>>({
    queryKey: ['recent-transactions'],
    queryFn: getRecentTransactions,
  })
}

export const useTradeLogQuery = () => {
  /*const from = moment().subtract(5, 'days').format(DateTime.ISODateFormat);*/
  const from = moment().subtract(10, 'years').format(DateTime.ISODateFormat); // TODO: Temp
  const to = moment().format(DateTime.ISODateFormat);

  return useQuery<TradeLog>({
    queryKey: ['trade-log'],
    queryFn: () => getTradeLog(from, to, 'DAILY', 6)
  })
}

export const useAccountQuery = (id: string) => {
  const { data: user, isLoading: isUserLoading, isError: isUserError } = useUserQuery()
  const accountId = parseInt(id, 10)

  return useQuery<Account | null>({
    queryKey: ['account', id],
    enabled: isNumeric(id) && !!user,
    queryFn: () => user?.portfolios?.flatMap(p => p.accounts)?.find((acc) => acc.accountNumber === accountId) ?? null
  })
}
