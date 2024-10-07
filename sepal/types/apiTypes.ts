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