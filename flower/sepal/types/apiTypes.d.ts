type ApiResponse<T> = {
  success: boolean;
  message?: string;
  data: T;
};

interface GenericApiType {
  uid: string;
}

interface EnumDisplay {
  code: string;
  label: string;
}

interface User extends GenericApiType {
  userIdentifier: string;
  apiToken: string;
  firstName: string;
  lastName: string;
  username: string;
  email: string;
  dateRegistered: string;
  phones: Array<PhoneNumber>;
  portfolios: Array<Portfolio>;
  roles: Array<string>;
}

interface PhoneNumber extends GenericApiType {
  phoneType: string;
  countryCode: string;
  telephoneNumber: number;
  display: string;
}

interface AccountCreationInfo {
  currencies: Array<Currency>;
  brokers: Array<Broker>;
  platforms: Array<TradePlatform>;
  accountTypes: Array<AccountType>;
}

interface PortfolioAccountEquityPoints {
  [others: string]: number;
}

interface PortfolioEquityPoint extends GenericApiType {
  date: string;
  portfolio: number;
  normalized: number;
  accounts: PortfolioAccountEquityPoints;
}

interface Portfolio extends GenericApiType {
  portfolioNumber: number;
  name: string;
  active: boolean;
  created: string;
  defaultPortfolio: boolean;
  accounts: Array<Account>;
}

interface PortfolioRecord {
  newPortfolio: boolean;
  netWorth: number;
  trades: number;
  deposits: number;
  withdrawals: number;
  statistics: PortfolioStatistics;
  equity: Array<PortfolioEquityPoint>;
}

interface PortfolioStatistics {
  differenceNetWorth: number;
  differenceTrades: number;
  differenceDeposits: number;
  differenceWithdrawals: number;
  deltaNetWorth: number;
  deltaTrades: number;
  deltaDeposits: number;
  deltaWithdrawals: number;
}

interface GenericEnum extends GenericApiType {
  code: string;
  label: string;
}

type Language = GenericEnum;

type PhoneType = GenericEnum;

type Country = GenericEnum;

type Currency = GenericEnum;

type AccountType = GenericEnum;

type TradePlatform = GenericEnum;

type Broker = GenericEnum;

type TradeRecordTimeInterval = GenericEnum;

interface Account extends GenericApiType {
  portfolioNumber: number;
  defaultAccount: boolean;
  accountOpenTime: string;
  accountCloseTime: string;
  initialBalance: number;
  balance: number;
  active: boolean;
  name: string;
  accountNumber: number;
  currency: EnumDisplay;
  broker: EnumDisplay;
  accountType: EnumDisplay;
  tradePlatform: EnumDisplay;
  lastTraded: string;
  transactions: Array<Transaction>;
}

interface AccountEquityPoint {
  date: string;
  amount: number;
  points: number;
  cumAmount: number;
  cumPoints: number;
}

interface AccountInsightsType {
  tradingDays: number;
  currentPL: number;
  biggestLoss: number;
  largestGain: number;
  drawdown: number;
  maxProfit: number;
  currentPLDelta: number;
  biggestLossDelta: number;
  largestGainDelta: number;
  drawdownDelta: number;
  maxProfitDelta: number;
}

interface AccountStatisticsType {
  balance: number;
  averageProfit: number;
  averageLoss: number;
  numberOfTrades: number;
  rrr: number;
  lots: number;
  expectancy: number;
  winPercentage: number;
  profitFactor: number;
  retention: number;
  sharpeRatio: number;
  tradeDuration: number;
  winDuration: number;
  lossDuration: number;
  assumedDrawdown: number;
}

interface AccountDetails {
  consistency: number;
  equity: Array<AccountEquityPoint>;
  insights: AccountInsightsType;
  statistics: AccountStatisticsType;
  riskFreeRate: number;
}

interface Transaction extends GenericApiType {
  transactionType: EnumDisplay;
  transactionDate: string;
  name: string;
  transactionStatus: EnumDisplay;
  amount: number;
  accountNumber: number;
  accountName: string;
}

interface PagedTrades {
  page: number;
  pageSize: number;
  trades: Array<Trade>;
  totalElements: number;
  totalPages: number;
}

interface Trade extends GenericApiType {
  tradeId: string;
  product: string;
  tradePlatform: string;
  tradeType: string;
  tradeOpenTime: string;
  tradeCloseTime: string;
  lotSize: number;
  openPrice: number;
  closePrice: number;
  netProfit: number;
  points: number;
  stopLoss: number;
  takeProfit: number;
  account: Account;
}

interface TradeRecordEquityPoint {
  count: number;
  amount: number;
  points: number;
  cumAmount: number;
  cumPoints: number;
}

interface TradeRecordTotals {
  count: number;
  trades: number;
  tradesWon: number;
  tradesLost: number;
  winPercentage: number;
  netProfit: number;
  netPoints: number;
}

interface TradeRecordReport {
  tradeRecords: Array<TradeRecord>;
  tradeRecordTotals: TradeRecordTotals;
}

interface TradeRecord extends GenericApiType {
  start: string;
  end: string;
  netProfit: number;
  lowestPoint: number;
  pointsGained: number;
  pointsLost: number;
  points: number;
  largestWin: number;
  winAverage: number;
  largestLoss: number;
  lossAverage: number;
  winPercentage: number;
  wins: number;
  losses: number;
  trades: number;
  profitability: number;
  retention: number;
  interval: TradeRecordTimeInterval;
  equityPoints: Array<TradeRecordEquityPoint>;
  account: Account;
}

interface TradeLog {
  entries: Array<TradeLogEntry>;
}

interface TradeLogEntry {
  start: string;
  end: string;
  records: Array<TradeLogEntryRecord>;
  totals: TradeLogEntryRecordTotals;
}

interface TradeLogEntryRecord {
  accountNumber: number;
  accountName: string;
  report: TradeRecordReport;
}

interface TradeLogEntryRecordTotals {
  accountsTraded: number;
  netProfit: number;
  netPoints: number;
  trades: number;
  winPercentage: number;
}

interface TradeRecordControlMonthEntry extends GenericApiType {
  monthNumber: number;
  month: string;
  value: number;
}

interface TradeRecordControlsYearEntry extends GenericApiType {
  year: string;
  monthEntries: Array<TradeRecordControlMonthEntry>;
}

interface TradeRecordControls extends GenericApiType {
  yearEntries: Array<TradeRecordControlsYearEntry>;
}

interface AnalysisResult {
  label: string;
  value: number;
  count: number;
}

interface MarketNews extends GenericApiType {
  date: string;
  slots: Array<MarketNewsSlot>;
  active: boolean;
  past: boolean;
  future: boolean;
}

interface MarketNewsSlot extends GenericApiType {
  time: string;
  entries: Array<MarketNewsEntry>;
  active: boolean;
}

interface MarketNewsEntry extends GenericApiType {
  content: string;
  severity: string;
  severityLevel: number;
  country: string;
  forecast: string;
  previous: string;
}

interface PairEntry {
  code: any;
  label: any;
  symbol: string;
}

interface ApexChartCandleStick {
  x: number;
  y: Array<number>;
}

interface Action extends GenericApiType {
  actionId: string;
  priority: number;
  name: string;
  status: EnumDisplay;
  performableAction: string;
}

interface JobResultEntry extends GenericApiType {
  success: boolean;
  data: string;
  logs: string;
}

interface JobResult extends GenericApiType {
  jobId: string;
  entries: Array<JobResultEntry>;
}

interface Job extends GenericApiType {
  jobId: string;
  id: number;
  name: string;
  executionTime: string;
  completionTime: string;
  status: EnumDisplay;
  type: EnumDisplay;
  actions: Array<Action>;
  jobResult: JobResult;
}

interface PagedJobs {
  page: number;
  pageSize: number;
  job: Array<Job>;
  totalElements: number;
  totalPages: number;
}

interface HealthCheck {
  domain: string;
  baseApiDomain: string;
  version: string;
  apiVersion: string;
}

interface CreateUpdatePortfolioRequest {
  defaultPortfolio: boolean;
  active: boolean;
  name: string;
}

interface CreateUpdateAccountRequest {
  isDefault: boolean;
  balance: number;
  active: boolean;
  name: string;
  number: number;
  currency: string;
  broker: string;
  type: string;
  tradePlatform: string;
  isLegacy: boolean;
  accountOpenTime: string | undefined | null;
  accountCloseTime: string | undefined | null;
}

interface PagedJobs {
  page: number;
  pageSize: number;
  jobs: Array<Job>;
  totalElements: number;
  totalPages: number;
}

export type FilterSelector = "POINTS" | "PROFIT" | "PERCENTAGE";

export type TradeDurationFilterSelector = "ALL" | "WINS" | "LOSSES";

export type Weekday =
  | "MONDAY"
  | "TUESDAY"
  | "WEDNESDAY"
  | "THURSDAY"
  | "FRIDAY";
