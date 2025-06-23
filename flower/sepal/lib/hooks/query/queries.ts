import { keepPreviousData, useQuery } from "@tanstack/react-query";
import { ApiUrls, DateTime } from "../../constants";
import { useRouter } from "next/navigation";
import moment from "moment";
import {
  Account,
  AccountCreationInfo,
  AccountDetails,
  AccountType,
  AnalysisResult,
  ApexChartCandleStick,
  Broker,
  Currency,
  EnumDisplay,
  HealthCheck,
  Job,
  MarketNews,
  PagedJobs,
  PagedTrades,
  PagedTransactions,
  Portfolio,
  PortfolioRecord,
  Trade,
  TradeInsightsType,
  TradeLog,
  TradePlatform,
  TradeRecordControls,
  TradeRecordReport,
  Transaction,
  User,
} from "@/types/apiTypes";
import { isNumeric } from "@/lib/functions/util-functions";
import { get } from "../../functions/client";
import { SessionData } from "@/lib/auth/session";

export const useSessionQuery = () => {
  const router = useRouter();

  return useQuery<SessionData>({
    queryKey: ["session"],
    queryFn: () =>
      fetch(ApiUrls.Internal.Security.Me).then((res) => {
        if (res.status === 401) {
          router.push("/login");
        } else if (!res.ok) {
          throw new Error("Not authenticated");
        }

        return res.json();
      }),
    retry: false,
    refetchOnWindowFocus: false,
  });
};

export const useUserQuery = (username: string) => {
  return useQuery<User>({
    queryKey: ["user", username],
    queryFn: () =>
      //get<User>(ApiUrls.User.GetUser, { username: ApiCredentials.TestUser }),
      get<User>(ApiUrls.Internal.User.Get, { username: username }),
    enabled: !!username,
  });
};

export const usePortfolioQuery = (portfolioNumber: number) => {
  return useQuery<Portfolio>({
    queryKey: ["portfolio", portfolioNumber],
    queryFn: () =>
      get<Portfolio>(ApiUrls.Internal.Portfolio.Get, {
        portfolioNumber: portfolioNumber,
      }),
    enabled: portfolioNumber !== -1,
  });
};

export const usePortfolioRecordQuery = (portfolioNumber: number) => {
  return useQuery<PortfolioRecord>({
    queryKey: ["portfolio-record", portfolioNumber],
    queryFn: () =>
      get<PortfolioRecord>(ApiUrls.Internal.PortfolioRecord.Get, {
        portfolioNumber: portfolioNumber,
      }),
    enabled: portfolioNumber !== -1,
  });
};

export const useRecentTransactionsQuery = () => {
  return useQuery<Array<Transaction>>({
    queryKey: ["recent-transactions"],
    queryFn: () =>
      get<Array<Transaction>>(ApiUrls.Internal.User.GetRecentTransactions, {}),
  });
};

export const useTradeLogQuery = () => {
  /*const from = moment().subtract(5, 'days').format(DateTime.ISODateFormat);*/
  const from = moment().subtract(10, "years").format(DateTime.ISODateFormat); // TODO: Temp
  const to = moment().format(DateTime.ISODateFormat);

  const params = {
    start: from,
    end: to,
    interval: "DAILY",
    count: "6",
  };

  return useQuery<TradeLog>({
    queryKey: ["trade-log", from, to, params.interval, params.count],
    queryFn: () =>
      get<TradeLog>(ApiUrls.Internal.TradeRecord.GetTradeLog, params),
  });
};

export const useRecentTradeRecordsQuery = (
  username: string,
  id: string,
  interval: string,
  count: number,
) => {
  const { data: user } = useUserQuery(username);
  const accountId = parseInt(id, 10);

  return useQuery<TradeRecordReport | null>({
    queryKey: ["recent-trade-records", id],
    queryFn: () =>
      get<TradeRecordReport>(ApiUrls.Internal.TradeRecord.GetRecent, {
        accountNumber: accountId.toString(),
        interval: interval,
        count: count,
      }),
    enabled: isNumeric(id) && !!user,
  });
};

export const useTradeQuery = (accId: string, id: string) => {
  return useQuery<Trade | null>({
    queryKey: ["trade", accId, id],
    queryFn: () =>
      get<Trade>(ApiUrls.Internal.Trade.GetForTradeId, {
        accountNumber: accId,
        tradeId: id,
      }),
    enabled: isNumeric(accId) && Number(accId) > -1,
  });
};

export const useAccountQuery = (id: string, username: string) => {
  const { data: user } = useUserQuery(username);
  const accountId = parseInt(id, 10);

  return useQuery<Account | null>({
    queryKey: ["account", id],
    queryFn: () =>
      user?.portfolios
        ?.flatMap((p) => p.accounts)
        ?.find((acc) => acc.accountNumber === accountId) ?? null,
    enabled: isNumeric(id) && !!user,
  });
};

export const useAccountDetailsQuery = (id: string, username: string) => {
  const { data: user } = useUserQuery(username);
  const accountId = parseInt(id, 10);

  return useQuery<AccountDetails | null>({
    queryKey: ["account-details", id],
    queryFn: () =>
      get<AccountDetails>(ApiUrls.Internal.Account.Details, {
        accountNumber: accountId.toString(),
      }),
    enabled: isNumeric(id) && !!user,
  });
};

export const useCurrenciesQuery = () => {
  return useQuery<Array<Currency>>({
    queryKey: ["currencies"],
    queryFn: () =>
      get<Array<Currency>>(ApiUrls.Internal.Account.Currencies, {}),
  });
};

export const useBrokersQuery = () => {
  return useQuery<Array<Broker>>({
    queryKey: ["brokers"],
    queryFn: () => get<Array<Broker>>(ApiUrls.Internal.Account.Brokers, {}),
  });
};

export const useTradePlatformsQuery = () => {
  return useQuery<Array<TradePlatform>>({
    queryKey: ["trade-platforms"],
    queryFn: () =>
      get<Array<TradePlatform>>(ApiUrls.Internal.Account.TradePlatforms, {}),
  });
};

export const useAccountTypesQuery = () => {
  return useQuery<Array<AccountType>>({
    queryKey: ["account-types"],
    queryFn: () =>
      get<Array<AccountType>>(ApiUrls.Internal.Account.AccountTypes, {}),
  });
};

export const useAccountCreationInfoQuery = () => {
  const { data: currencies, isLoading: isCurrenciesLoading } =
    useCurrenciesQuery();
  const { data: brokers, isLoading: isBrokersLoading } = useBrokersQuery();
  const { data: platforms, isLoading: isPlatformsLoading } =
    useTradePlatformsQuery();
  const { data: accountTypes, isLoading: isAccountTypesLoading } =
    useAccountTypesQuery();

  const allLoaded = !(
    isCurrenciesLoading ||
    isBrokersLoading ||
    isPlatformsLoading ||
    isAccountTypesLoading
  );

  return useQuery<AccountCreationInfo>({
    queryKey: ["account-creation-info"],
    queryFn: () => {
      if (!allLoaded) {
        throw new Error("Dependent queries not loaded yet");
      }
      return {
        currencies: currencies ?? [],
        brokers: brokers ?? [],
        platforms: platforms ?? [],
        accountTypes: accountTypes ?? [],
      };
    },
    enabled: allLoaded,
  });
};

export const usePagedTransactionsQuery = (
  accountNumber: number,
  start: string,
  end: string,
  page: number,
  pageSize: number,
  transactionType: string,
  transactionStatus: string,
  sort: "asc" | "desc" = "asc",
) => {
  return useQuery<PagedTransactions>({
    placeholderData: keepPreviousData,
    queryKey: [
      "paged-transactions",
      accountNumber,
      start,
      end,
      page,
      pageSize,
      transactionType,
      transactionStatus,
      sort,
    ],
    queryFn: () =>
      get<PagedTransactions>(ApiUrls.Internal.Transaction.GetForIntervalPaged, {
        accountNumber: accountNumber.toString(),
        start: start,
        end: end,
        page: page.toString(),
        pageSize: pageSize.toString(),
        transactionType: transactionType,
        transactionStatus: transactionStatus,
        sort: sort,
      }),
    enabled: accountNumber !== -1,
  });
};

export const usePagedTradesQuery = (
  accountNumber: number,
  start: string,
  end: string,
  page: number,
  pageSize: number,
  tradeType: string,
  symbol: string,
  sort: "asc" | "desc" = "asc",
) => {
  return useQuery<PagedTrades | null>({
    placeholderData: keepPreviousData,
    queryKey: [
      "paginated-trades",
      accountNumber,
      start,
      end,
      page,
      pageSize,
      tradeType,
      symbol,
      sort,
    ],
    queryFn: () =>
      get<PagedTrades>(ApiUrls.Internal.Trade.GetForIntervalPaged, {
        accountNumber: accountNumber.toString(),
        start: start,
        end: end,
        page: page.toString(),
        pageSize: pageSize.toString(),
        tradeType: tradeType,
        symbol: symbol,
        sort: sort,
      }),
    enabled: accountNumber !== -1,
  });
};

export const useTimeBucketsAnalysisQuery = (
  accountNumber: number,
  isOpened: boolean,
  filter: string,
) => {
  return useQuery<Array<AnalysisResult>>({
    queryKey: ["time-buckets", accountNumber, isOpened, filter],
    queryFn: () =>
      get<Array<AnalysisResult>>(ApiUrls.Internal.Analysis.TimeBuckets, {
        accountNumber: accountNumber.toString(),
        isOpened: isOpened.toString(),
        filter: filter,
      }),
  });
};

export const useWeekdaysAnalysisQuery = (
  accountNumber: number,
  filter: string,
) => {
  return useQuery<Array<AnalysisResult>>({
    queryKey: ["weekdays", accountNumber, filter],
    queryFn: () =>
      get<Array<AnalysisResult>>(ApiUrls.Internal.Analysis.Weekdays, {
        accountNumber: accountNumber.toString(),
        filter: filter,
      }),
  });
};

export const useWeekdaysTimeBucketsAnalysisQuery = (
  accountNumber: number,
  weekday: string,
  filter: string,
) => {
  return useQuery<Array<AnalysisResult>>({
    queryKey: ["weekdays-time-buckets", accountNumber, weekday, filter],
    queryFn: () =>
      get<Array<AnalysisResult>>(
        ApiUrls.Internal.Analysis.WeekdaysTimeBuckets,
        {
          accountNumber: accountNumber.toString(),
          weekday: weekday,
          filter: filter,
        },
      ),
  });
};

export const useTradeDurationAnalysisQuery = (
  accountNumber: number,
  tradeDurationFilter: string,
  filter: string,
) => {
  return useQuery<Array<AnalysisResult>>({
    queryKey: ["time-buckets", accountNumber, tradeDurationFilter, filter],
    queryFn: () =>
      get<Array<AnalysisResult>>(ApiUrls.Internal.Analysis.TradeDuration, {
        accountNumber: accountNumber.toString(),
        tradeDurationFilter: tradeDurationFilter.toString(),
        filter: filter,
      }),
  });
};

export const useMarketNewsQuery = (start: string, end: string) => {
  return useQuery<Array<MarketNews>>({
    queryKey: ["market-news", start, end],
    queryFn: () =>
      get<Array<MarketNews>>(ApiUrls.Internal.News.GetByInterval, {
        start: start,
        end: end,
      }),
  });
};

export const useTradeRecordControlsQuery = (
  accountNumber: number,
  interval: string,
  options: { enabled: boolean },
) => {
  return useQuery<TradeRecordControls>({
    queryKey: ["trade-record-controls", accountNumber, interval],
    queryFn: () =>
      get<TradeRecordControls>(ApiUrls.Internal.TradeRecord.GetControls, {
        accountNumber: accountNumber.toString(),
        interval: interval,
      }),
    enabled: accountNumber > -1,
  });
};

export const useTradeRecordsQuery = (
  accountNumber: number,
  start: string,
  end: string,
  interval: string,
  count: number,
  options: { enabled: boolean },
) => {
  return useQuery<TradeRecordReport>({
    queryKey: ["trade-records", accountNumber, start, end, interval, count],
    queryFn: () =>
      get<TradeRecordReport>(ApiUrls.Internal.TradeRecord.GetForInterval, {
        accountNumber: accountNumber.toString(),
        start: start,
        end: end,
        interval: interval,
        count: count,
      }),
    enabled: accountNumber > -1,
  });
};

export const useTradedSymbolsQuery = (accountNumber: number) => {
  return useQuery<Array<string>>({
    queryKey: ["traded-symbols", accountNumber],
    queryFn: () =>
      get<Array<string>>(ApiUrls.Internal.Symbol.GetTraded, {
        accountNumber: accountNumber.toString(),
      }),
    enabled: accountNumber > -1,
  });
};

export const usePagedJobsQuery = (
  start: string,
  end: string,
  page: number,
  pageSize: number,
  jobType: string,
  jobStatus: string,
  sort: "asc" | "desc" = "asc",
) => {
  return useQuery<PagedJobs | null>({
    placeholderData: keepPreviousData,
    queryKey: [
      "paginated-jobs",
      start,
      end,
      page,
      pageSize,
      jobType,
      jobStatus,
      sort,
    ],
    queryFn: () =>
      get<PagedJobs>(ApiUrls.Internal.Job.ByStatusAndTypePaged, {
        start: start,
        end: end,
        page: page.toString(),
        pageSize: pageSize.toString(),
        jobStatus: jobStatus,
        jobType: jobType,
        sort: sort,
      }),
  });
};

export const useTradeInsightsQuery = (
  accountNumber: string,
  tradeId: string,
) => {
  return useQuery<TradeInsightsType>({
    queryKey: ["trade-insights", accountNumber, tradeId],
    queryFn: () =>
      get<TradeInsightsType>(ApiUrls.Internal.Trade.GetInsights, {
        accountNumber: accountNumber,
        tradeId: tradeId,
      }),
  });
};

export const useJobTypesQuery = () => {
  return useQuery<Array<EnumDisplay>>({
    queryKey: ["job-types"],
    queryFn: () => get<Array<EnumDisplay>>(ApiUrls.Internal.Job.Types, {}),
  });
};

export const useJobQuery = (jobId: string) => {
  return useQuery<Job>({
    queryKey: ["job", jobId],
    queryFn: () => get<Job>(ApiUrls.Internal.Job.ById, { jobId: jobId }),
  });
};

//  at present this query is used for display purposes in the side nav, if this were to change, the enabled flag would need to be refactored
//  this api call is also used in the middleware, but not with react query
export const useHealthCheckQuery = () => {
  return useQuery<HealthCheck>({
    queryKey: ["health-check"],
    queryFn: () => get<HealthCheck>(ApiUrls.Internal.System.HealthCheck, {}),
    enabled: process.env.ENABLE_BUILD_VERSION === "true",
  });
};

export const useApexChartQuery = (
  tradeId: string,
  accountNumber: number,
  interval: string,
) => {
  return useQuery<Array<ApexChartCandleStick>>({
    queryKey: ["apexChart", tradeId, interval],
    queryFn: () =>
      get<Array<ApexChartCandleStick>>(ApiUrls.Internal.Charting.Apex, {
        tradeId: tradeId,
        accountNumber: accountNumber.toString(),
        interval: interval,
      }),
  });
};

export const useMarketPriceTimerIntervalQuery = () => {
  return useQuery<Array<EnumDisplay>>({
    queryKey: ["market-price-time-intervals"],
    queryFn: () =>
      get<Array<EnumDisplay>>(ApiUrls.Internal.MarketPrice.TimeIntervals, {}),
  });
};
