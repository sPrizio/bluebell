type ApiResponse<T> = {
  success: boolean
  message?: string
  data: T
}

interface GenericApiType {
  uid: string
}

interface EnumDisplay {
  code: string,
  label: string
}

interface User extends GenericApiType {
  apiToken: string,
  firstName: string,
  lastName: string,
  username: string,
  email: string,
  dateRegistered: string,
  phones: Array<PhoneNumber>,
  portfolios: Array<Portfolio>,
  roles: Array<string>,
}

interface PhoneNumber extends GenericApiType {
  phoneType: string,
  countryCode: string,
  telephoneNumber: number,
  display: string
}

interface AccountCreationInfo {
  currencies: Array<Currency>,
  brokers: Array<Broker>,
  platforms: Array<TradePlatform>,
  accountTypes: Array<AccountType>
}

interface PortfolioAccountEquityPoints {
  [others: string]: number
}

interface PortfolioEquityPoint extends GenericApiType {
  date: string,
  portfolio: number
  normalized: number,
  accounts: PortfolioAccountEquityPoints
}

interface Portfolio extends GenericApiType {
  portfolioNumber: number,
  name: string,
  active: boolean,
  created: string,
  defaultPortfolio: boolean,
  accounts: Array<Account>,
  user: User,
}

interface PortfolioRecord {
  newPortfolio: boolean,
  netWorth: number,
  trades: number,
  deposits: number,
  withdrawals: number,
  statistics: PortfolioStatistics,
  equity: Array<PortfolioEquityPoint>,
}

interface PortfolioStatistics {
  deltaNetWorth: number,
  deltaTrades: number,
  deltaDeposits: number,
  deltaWithdrawals: number,
}

interface AccountOption extends GenericApiType {
  code: string,
  label: string
}

interface Currency extends AccountOption {}

interface AccountType extends AccountOption {}

interface TradePlatform extends AccountOption {}

interface Broker extends AccountOption {}

interface Account extends GenericApiType {
  defaultAccount: boolean,
  accountOpenTime: string,
  portfolioNumber: number,
  accountCloseTime: string,
  balance: number,
  active: boolean,
  name: string,
  accountNumber: number,
  currency: EnumDisplay,
  broker: EnumDisplay,
  accountType: EnumDisplay,
  tradePlatform: EnumDisplay,
  lastTraded: string,
  transactions: Array<Transaction>
}

interface AccountEquityPoint {
  date: string,
  amount: number,
  points: number,
  cumAmount: number,
  cumPoints: number
}

interface AccountInsightsType {
  maxProfit: number,
  tradingDays: number,
  biggestLoss: number,
  largestGain: number,
  currentPL: number,
  drawdown: number,
  biggestLossDelta: number,
  largestGainDelta: number,
  currentPLDelta: number,
  drawdownDelta: number,
  maxProfitDelta: number
}

interface AccountStatisticsType {
  balance: number,
  averageProfit: number,
  averageLoss: number,
  numberOfTrades: number,
  rrr: number,
  lots: number,
  expectancy: number,
  winPercentage: number,
  profitFactor: number,
  retention: number,
  sharpeRatio: number,
  tradeDuration: number,
  winDuration: number,
  lossDuration: number,
  assumedDrawdown: number
}

interface AccountDetails {
  account: Account,
  consistency: number,
  equity: Array<AccountEquityPoint>,
  insights: AccountInsightsType,
  statistics: AccountStatisticsType
}

interface Transaction extends GenericApiType {
  transactionType: EnumDisplay
  transactionDate: string,
  name: string
  transactionStatus: EnumDisplay
  amount: number,
  accountNumber: number,
  accountName: string
}

interface Trade extends GenericApiType {
  tradeId: string,
  product: string,
  tradePlatform: string,
  tradeType: string,
  tradeOpenTime: string,
  tradeCloseTime: string,
  lotSize: number,
  openPrice: number,
  closePrice: number,
  netProfit: number,
  points: number,
  stopLoss: number,
  takeProfit: number,
  account: Account
}

interface TradeRecordEquityPoint {
  count: number,
  amount: number,
  points: number,
  cumAmount: number,
  cumPoints: number
}

interface TradeRecordTotals {
  count: number,
  trades: number,
  winPercentage: number,
  netProfit: number,
  netPoints: number
}

interface TradeRecordReport {
  tradeRecords: Array<TradeRecord>,
  tradeRecordTotals: TradeRecordTotals
}

interface TradeRecord extends GenericApiType {
  end: string,
  largestLoss: number,
  largestWin: number,
  lossAverage: number,
  losses: number,
  lowestPoint: number,
  netProfit: number,
  points: number,
  pointsGained: number,
  pointsLost: number,
  profitability: number,
  retention: number,
  start: string,
  trades: number,
  winAverage: number,
  winPercentage: number,
  wins: number,
  equityPoints: Array<TradeRecordEquityPoint>
  account: Account,
}

interface TradeLog {
  entries: Array<TradeLogEntry>
}

interface TradeLogEntry {
  start: string,
  end: string,
  records: Array<TradeLogEntryRecord>,
  totals: TradeLogEntryRecordTotals
}

interface TradeLogEntryRecord {
  accountNumber: number,
  accountName: string,
  report: TradeRecordReport
}

interface TradeLogEntryRecordTotals {
  accountsTraded: number,
  netProfit: number,
  netPoints: number,
  winPercentage: number,
  trades: number,
}

interface PagedTrades {
  currentPage: number,
  pageSize: number,
  totalPages: number,
  totalTrades: number,
  trades: Array<Trade>
}

interface TradeRecordControlMonthEntry extends GenericApiType {
  month: string,
  value: number
}

interface TradeRecordControlsYearEntry extends GenericApiType {
  year: string,
  monthEntries: Array<TradeRecordControlMonthEntry>
}

interface TradeRecordControls extends GenericApiType {
  yearEntries: Array<TradeRecordControlsYearEntry>
}

interface AnalysisResult {
  label: string,
  value: number,
  count: number
}

interface MarketNews extends GenericApiType {
  date: string,
  slots: Array<MarketNewsSlot>,
  active: boolean,
  past: boolean,
  future: boolean
}

interface MarketNewsSlot extends GenericApiType {
  time: string,
  entries: Array<MarketNewsEntry>,
  active: boolean
}

interface MarketNewsEntry extends GenericApiType {
  content: string,
  severity: string,
  severityLevel: number
  country: string,
  forecast: string,
  previous: string
}

export type FilterSelector = 'POINTS' | 'PROFIT' | 'PERCENTAGE'

export type TradeDurationFilterSelector = 'ALL' | 'WINS' | 'LOSSES'

export type Weekday = 'MONDAY' | 'TUESDAY' | 'WEDNESDAY' | 'THURSDAY' | 'FRIDAY'