import { ApiUrls } from "./constants";

export const ApiUrlMappings: Record<string, string> = {
  //  Account
  [ApiUrls.Internal.Account.Create]: ApiUrls.External.Account.CreateAccount,
  [ApiUrls.Internal.Account.Update]: ApiUrls.External.Account.UpdateAccount,
  [ApiUrls.Internal.Account.Delete]: ApiUrls.External.Account.DeleteAccount,
  [ApiUrls.Internal.Account.Currencies]: ApiUrls.External.Account.GetCurrencies,
  [ApiUrls.Internal.Account.AccountTypes]:
    ApiUrls.External.Account.GetAccountTypes,
  [ApiUrls.Internal.Account.Brokers]: ApiUrls.External.Account.GetBrokers,
  [ApiUrls.Internal.Account.TradePlatforms]:
    ApiUrls.External.Account.GetTradePlatforms,
  [ApiUrls.Internal.Account.Details]: ApiUrls.External.Account.GetDetails,

  //  Analysis
  [ApiUrls.Internal.Analysis.TimeBuckets]:
    ApiUrls.External.Analysis.TimeBuckets,
  [ApiUrls.Internal.Analysis.TradeDuration]:
    ApiUrls.External.Analysis.TradeDuration,
  [ApiUrls.Internal.Analysis.Weekdays]: ApiUrls.External.Analysis.Weekdays,
  [ApiUrls.Internal.Analysis.WeekdaysTimeBuckets]:
    ApiUrls.External.Analysis.WeekdaysTimeBuckets,

  //  Charting
  [ApiUrls.Internal.Charting.Apex]: ApiUrls.External.Charting.Get,

  //  Job
  [ApiUrls.Internal.Job.ById]: ApiUrls.External.Job.GetJobById,
  [ApiUrls.Internal.Job.ByStatusAndTypePaged]:
    ApiUrls.External.Job.GetJobsByStatusAndTypePaged,
  [ApiUrls.Internal.Job.Types]: ApiUrls.External.Job.GetJobTypes,

  //  MarketPrice
  [ApiUrls.Internal.MarketPrice.TimeIntervals]:
    ApiUrls.External.MarketPrice.GetTimeIntervals,

  //  News
  [ApiUrls.Internal.News.GetByInterval]: ApiUrls.External.News.GetNews,
  [ApiUrls.Internal.News.FetchUpdate]: ApiUrls.External.News.FetchNews,

  //  Portfolio
  [ApiUrls.Internal.Portfolio.Get]: ApiUrls.External.Portfolio.GetPortfolio,
  [ApiUrls.Internal.Portfolio.Create]:
    ApiUrls.External.Portfolio.CreatePortfolio,
  [ApiUrls.Internal.Portfolio.Delete]:
    ApiUrls.External.Portfolio.DeletePortfolio,
  [ApiUrls.Internal.Portfolio.Update]:
    ApiUrls.External.Portfolio.UpdatePortfolio,

  //  PortfolioRecord
  [ApiUrls.Internal.PortfolioRecord.Get]:
    ApiUrls.External.PortfolioRecord.GetPortfolioRecord,

  //  Security
  /*[ApiUrls.Internal.Security.Login]: ApiUrls.External.Security.Login,
  [ApiUrls.Internal.Security.Logout]: ApiUrls.External.Security.Login, // NOTE: no Logout in External
  [ApiUrls.Internal.Security.Me]: ApiUrls.External.Security.Login, // NOTE: no Me in External*/

  //  Symbol
  [ApiUrls.Internal.Symbol.GetTraded]: ApiUrls.External.Symbol.GetTradedSymbols,

  //  System
  [ApiUrls.Internal.System.HealthCheck]: ApiUrls.External.System.HealthCheck,

  //  Trade
  [ApiUrls.Internal.Trade.Create]: ApiUrls.External.Trade.CreateTrade,
  [ApiUrls.Internal.Trade.Update]: ApiUrls.External.Trade.UpdateTrade,
  [ApiUrls.Internal.Trade.Delete]: ApiUrls.External.Trade.DeleteTrade,
  [ApiUrls.Internal.Trade.GetForTradeId]:
    ApiUrls.External.Trade.GetTradeForTradeId,
  [ApiUrls.Internal.Trade.GetForIntervalPaged]:
    ApiUrls.External.Trade.GetPagedTrades,
  [ApiUrls.Internal.Trade.GetInsights]: ApiUrls.External.Trade.GetInsights,
  [ApiUrls.Internal.Trade.Import]: ApiUrls.External.Trade.ImportTrades,

  //  TradeRecord
  [ApiUrls.Internal.TradeRecord.GetForInterval]:
    ApiUrls.External.TradeRecord.GetTradeRecords,
  [ApiUrls.Internal.TradeRecord.GetRecent]:
    ApiUrls.External.TradeRecord.GetRecentTradeRecords,
  [ApiUrls.Internal.TradeRecord.GetTradeLog]:
    ApiUrls.External.TradeRecord.GetTradeLog,
  [ApiUrls.Internal.TradeRecord.GetControls]:
    ApiUrls.External.TradeRecord.GetTradeRecordControls,

  //  Transaction
  [ApiUrls.Internal.Transaction.GetForIntervalPaged]:
    ApiUrls.External.Transaction.GetPaged,
  [ApiUrls.Internal.Transaction.Create]: ApiUrls.External.Transaction.Create,
  [ApiUrls.Internal.Transaction.Update]: ApiUrls.External.Transaction.Update,
  [ApiUrls.Internal.Transaction.Delete]: ApiUrls.External.Transaction.Delete,

  //  User
  [ApiUrls.Internal.User.GetRecentTransactions]:
    ApiUrls.External.User.GetRecentTransactions,
  [ApiUrls.Internal.User.Get]: ApiUrls.External.User.GetUser,
  [ApiUrls.Internal.User.Update]: ApiUrls.External.User.UpdateUser,
  [ApiUrls.Internal.User.Create]: ApiUrls.External.User.RegisterUser,
};

/**
 * Matches the given url and pattern
 *
 * @param pattern pattern
 * @param url url
 */
export function matchUrlPattern(pattern: string, url: string): boolean {
  const regex = new RegExp(
    pattern
      .replace(/[.*+?^${}()|[\]\\]/g, "\\$&")
      .replace(/\{[^}]+\}/g, "[^&=?/]+"),
  );
  return regex.test(url);
}

/**
 * Extract query params as key value pairs from a search query
 *
 * @param search search query
 */
export function extractParamsFromPattern(
  search: string,
): Record<string, string> {
  const result: Record<string, string> = {};
  if (search?.startsWith("?") ?? false) {
    const pairs = search.substring(1).split("&");

    for (const pair of pairs) {
      const [key, value] = pair.split("=");
      if (key) {
        result[key] = decodeURIComponent(value || "");
      }
    }
  }

  return result;
}

/**
 * Populate a url with its query params
 *
 * @param url url
 * @param params query params
 */
export function populateUrl(
  url: string,
  params: Record<string, string>,
): string {
  console.log(url);
  let populatedUrl = url;

  for (const [key, value] of Object.entries(params)) {
    const encodedValue = encodeURIComponent(value);
    populatedUrl = populatedUrl.replaceAll(`{${key}}`, encodedValue);
  }

  return populatedUrl;
}
