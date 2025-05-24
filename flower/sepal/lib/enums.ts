/**
 * App icons represented as icons
 */
export enum Icons {
  Dashboard = "dashboard",
  AccountOverview = "account-overview",
  UserProfile = "user-profile",
  MarketNews = "market-news",
  Performance = "performance",
  Transactions = "transactions",
  Logout = "logout",
  ArrowUp = "arrow-up",
  ArrowLeft = "arrow-left",
  ArrowRight = "arrow-right",
  ArrowDown = "arrow-down",
  ArrowLeftRight = "arrow-left-right",
  ChartDoughnut = "chart-doughnut",
  Replace = "replace",
  ArrowBarDown = "arrow-bar-down",
  ArrowBarUp = "arrow-bar-up",
  Mountain = "mountain",
  Trades = "trades",
  Analysis = "analysis",
  Portfolios = "portfolios",
  JobsOverview = "jobs-overview",
  Job = "job",
  CirclePlus = "circle-plus",
}

/**
 * Enum representing Trade record intervals for aggregating data
 */
export class AggregateInterval {
  static readonly DAILY = new AggregateInterval("DAILY", "Daily");
  static readonly MONTHLY = new AggregateInterval("MONTHLY", "Monthly");
  static readonly YEARLY = new AggregateInterval("YEARLY", "Yearly");

  // private to disallow creating other instances of this type
  private constructor(
    public readonly code: string,
    public readonly label: string,
  ) {}

  public static get(val: string): AggregateInterval {
    switch (val.toUpperCase()) {
      case "DAILY":
        return this.DAILY;
      case "MONTHLY":
        return this.MONTHLY;
      default:
        return this.YEARLY;
    }
  }
}
