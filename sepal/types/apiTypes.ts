interface GenericApiType {
  uid: string
}

interface User extends GenericApiType {
  apiToken: string,
  firstName: string,
  lastName: string,
  username: string,
  email: string,
  dateRegistered: string,
  phones: Array<PhoneNumber>,
  accounts: Array<Account>,
  roles: Array<string>,
}

interface PhoneNumber extends GenericApiType {
  phoneType: string,
  countryCode: string,
  telephoneNumber: number,
  display: string
}

// TODO: implement on backend
interface AccountCreationInfo extends GenericApiType {
  currencies: Array<Currency>,
  brokers: Array<Broker>,
  platforms: Array<TradePlatform>,
  accountTypes: Array<AccountType>
}

// TODO: implement on backend
interface PortfolioEquityPoint extends GenericApiType {
  date: string,
  portfolio: number

  [others: string]: any
}

// TODO: implement on backend
interface AccountOption extends GenericApiType {
  code: string,
  label: string
}

// TODO: implement on backend
interface Currency extends AccountOption {}

// TODO: implement on backend
interface AccountType extends AccountOption {}

// TODO: implement on backend
interface TradePlatform extends AccountOption {}

// TODO: implement on backend
interface Broker extends AccountOption {}

interface Account extends GenericApiType {
  defaultAccount: boolean,
  accountOpenTime: string,
  accountCloseTime: string,
  balance: number,
  active: boolean,
  name: string,
  accountNumber: number,
  currency: Currency,
  broker: Broker,
  accountType: AccountType,
  tradePlatform: TradePlatform,
  lastTraded: string,
  //TODO: implement on backend
  transactions: Array<Transaction>
}

// TODO: implement on backend
interface AccountEquityPoint extends GenericApiType {
  date: string,
  amount: number,
  points: number
}

// TODO: implement on backend
interface AccountInsights extends GenericApiType {
  tradingDays: number,
  trades: number,
  maxDailyLoss: number,
  maxTotalLoss: number,
  maxDailyProfit: number,
  maxProfit: number,
}

// TODO: implement on backend
interface AccountStatistics extends GenericApiType {
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
  sharpeRatio: number;
}

// TODO: implement on backend
interface AccountDetails extends GenericApiType {
  account: Account,
  consistency: number,
  equity: Array<AccountEquityPoint>,
  insights: AccountInsights,
  statistics: AccountStatistics
}

interface Transaction extends GenericApiType {
  date: string,
  amount: number,
  type: 'Deposit' | 'Withdrawal'
  status: 'Pending' | 'Complete' | 'Failed',
  accountNumber: number //TODO: implement this on backend
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
  account: Account,
}

// TODO: implement on backend
interface TradeLog extends GenericApiType {
  start: string,
  end: string,
  records: Array<TradeRecord>
}

// TODO: implement on backend
interface TradeRecordControlMonthEntry extends GenericApiType {
  month: string,
  value: number
}

// TODO: implement on backend
interface TradeRecordControlsYearEntry extends GenericApiType {
  year: string,
  monthEntries: Array<TradeRecordControlMonthEntry>
}

// TODO: implement on backend
interface TradeRecordControls extends GenericApiType {
  yearEntries: Array<TradeRecordControlsYearEntry>
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