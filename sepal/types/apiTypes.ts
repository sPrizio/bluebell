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
interface AccountEquityPoint extends GenericApiType {
  date: string,
  portfolio: number
  [others: string]: any
}

interface Account extends GenericApiType {
  defaultAccount: boolean,
  accountOpenTime: string,
  accountCloseTime: string,
  balance: number,
  active: boolean,
  name: string,
  accountNumber: number,
  currency: string,
  broker: string,
  accountType: string,
  tradePlatform: string,
  lastTraded: string
}

interface Transaction extends GenericApiType {
  date: string,
  account: Account,
  amount: number,
  type: 'Deposit' | 'Withdrawal'
  status: 'Pending' | 'Complete' | 'Failed'
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
  account: Account
}

// TODO: implement on backend
interface TradeLog extends GenericApiType {
  start: string,
  end: string,
  records: Array<TradeRecord>
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