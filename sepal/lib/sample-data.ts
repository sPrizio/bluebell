export const accounts: Array<Account> = [
  {
    uid: '1231',
    defaultAccount: true,
    accountOpenTime: '2024-10-10T20:00:15',
    accountCloseTime: '-1',
    balance: 30987.65,
    active: true,
    name: 'Test Account 1',
    accountNumber: 123,
    currency: {uid: 'CAD', code: 'CAD', label: 'CAD'},
    broker: {uid: 'cmc_1', code: 'cmc', label: 'CMC Markets'},
    accountType: {uid: 'cfd', code: 'cfd', label: 'CFD'},
    tradePlatform: {uid: 'MT4', code: 'MT4', label: 'MT4'},
    lastTraded: '2024-10-11T12:22:45'
  },
  {
    uid: '1232',
    defaultAccount: false,
    accountOpenTime: '2023-08-01T15:34:15',
    accountCloseTime: '-1',
    balance: 30987.65,
    active: true,
    name: 'Test Account 2',
    accountNumber: 1234,
    currency: {uid: 'CAD', code: 'CAD', label: 'CAD'},
    broker: {uid: 'ftmo_2', code: 'ftmo', label: 'FTMO'},
    accountType: {uid: 'cfd', code: 'cfd', label: 'CFD'},
    tradePlatform: {uid: 'MT4', code: 'MT4', label: 'MT4'},
    lastTraded: '2024-10-01T16:09:45'
  },
  {
    uid: '1233',
    defaultAccount: false,
    accountOpenTime: '2023-06-24T09:45:15',
    accountCloseTime: '2024-07-30T23:54:05',
    balance: 30987.65,
    active: false,
    name: 'Test Account 3',
    accountNumber: 12345,
    currency: {uid: 'cad', code: 'cad', label: 'CAD'},
    broker: {uid: 'cmc_3', code: 'cmc', label: 'CMC Markets'},
    accountType: {uid: 'cfd', code: 'cfd', label: 'CFD'},
    tradePlatform: {uid: 'MT4', code: 'MT4', label: 'MT4'},
    lastTraded: '2024-07-30T16:01:45'
  }
]

export const tradeRecords: Array<TradeRecord> = [
  {
    uid: '1234',
    end: '2024-10-07',
    largestLoss: -126.36,
    largestWin: 214.56,
    lossAverage: -89.63,
    losses: 10,
    lowestPoint: -89.63,
    netProfit: 974.25,
    points: 214.87,
    pointsGained: 1293.63,
    pointsLost: 855.11,
    profitability: 1.36,
    retention: 0.65,
    start: '2024-10-06',
    trades: 15,
    winAverage: 202.33,
    winPercentage: 51,
    wins: 11,
    account: accounts[0]
  },
  {
    uid: '124',
    end: '2024-10-07',
    largestLoss: -126.36,
    largestWin: 214.56,
    lossAverage: -89.63,
    losses: 10,
    lowestPoint: -89.63,
    netProfit: -974.25,
    points: -214.87,
    pointsGained: 1293.63,
    pointsLost: 855.11,
    profitability: 1.36,
    retention: 0.65,
    start: '2024-10-06',
    trades: 15,
    winAverage: 202.33,
    winPercentage: 51,
    wins: 11,
    account: accounts[0]
  },
  {
    uid: '125',
    end: '2024-10-07',
    largestLoss: -126.36,
    largestWin: 214.56,
    lossAverage: -89.63,
    losses: 10,
    lowestPoint: -89.63,
    netProfit: 974.25,
    points: 214.87,
    pointsGained: 1293.63,
    pointsLost: 855.11,
    profitability: 1.36,
    retention: 0.65,
    start: '2024-10-06',
    trades: 15,
    winAverage: 202.33,
    winPercentage: 51,
    wins: 11,
    account: accounts[0]
  },
  {
    uid: '126',
    end: '2024-10-07',
    largestLoss: -126.36,
    largestWin: 214.56,
    lossAverage: -89.63,
    losses: 10,
    lowestPoint: -89.63,
    netProfit: 974.25,
    points: 214.87,
    pointsGained: 1293.63,
    pointsLost: 855.11,
    profitability: 1.36,
    retention: 0.65,
    start: '2024-10-06',
    trades: 15,
    winAverage: 202.33,
    winPercentage: 51,
    wins: 11,
    account: accounts[0]
  },
  {
    uid: '127',
    end: '2024-10-07',
    largestLoss: -126.36,
    largestWin: 214.56,
    lossAverage: -89.63,
    losses: 10,
    lowestPoint: -89.63,
    netProfit: 974.25,
    points: 214.87,
    pointsGained: 1293.63,
    pointsLost: 855.11,
    profitability: 1.36,
    retention: 0.65,
    start: '2024-10-06',
    trades: 15,
    winAverage: 202.33,
    winPercentage: 51,
    wins: 11,
    account: accounts[0]
  }
]

export const tradeLog: Array<TradeLog> = [
  {
    uid: '1235',
    start: '2024-10-08',
    end: '2024-10-09',
    records: tradeRecords.slice(0, 1)
  },
  {
    uid: '124',
    start: '2024-10-07',
    end: '2024-10-08',
    records: tradeRecords.slice(0, 2)
  },
  {
    uid: '125',
    start: '2024-10-06',
    end: '2024-10-07',
    records: tradeRecords.slice(0, 3)
  }
]

export const accountTransactions: Array<Transaction> = [
  {
    uid: '1236',
    date: '2024-10-08',
    amount: 125.89,
    status: 'Pending',
    account: accounts[0],
    type: 'Withdrawal'
  },
  {
    uid: '124',
    date: '2024-10-06',
    amount: 500.0,
    status: 'Complete',
    account: accounts[0],
    type: 'Deposit'
  },
  {
    uid: '125',
    date: '2024-10-01',
    amount: 375.0,
    status: 'Failed',
    account: accounts[0],
    type: 'Deposit'
  }
]

export const chartData: Array<PortfolioEquityPoint> = [
  {
    uid: '1237',
    date: '2024-04-01',
    portfolio: 4000,
    testAccount1: 2400,
    testAccount2: 3000,
    testAccount3: 4000,
  },
  {
    uid: '124',
    date: '2024-05-01',
    portfolio: 3000,
    testAccount1: 1398,
    testAccount2: 2210,
    testAccount3: 1589,
  },
  {
    uid: '125',
    date: '2024-06-01',
    portfolio: 2000,
    testAccount1: 5890,
    testAccount2: 2290,
    testAccount3: 3698,
  },
  {
    uid: '126',
    date: '2024-07-01',
    portfolio: 2780,
    testAccount1: 3908,
    testAccount2: 2000,
    testAccount3: 789,
  },
  {
    uid: '127',
    date: '2024-08-01',
    portfolio: 1890,
    testAccount1: 4800,
    testAccount2: 2181,
    testAccount3: 2693,
  },
  {
    uid: '128',
    date: '2024-09-01',
    portfolio: 2390,
    testAccount1: 3800,
    testAccount2: 2500,
    testAccount3: 3687,
  },
  {
    uid: '129',
    date: '2024-10-01',
    portfolio: 3490,
    testAccount1: 4300,
    testAccount2: 2100,
    testAccount3: 3896,
  },
];

export const accountCreationInfo: AccountCreationInfo = {
  uid: '1238',
  currencies: [
    {uid: 'CAD', code: 'CAD', label: 'CAD'},
    {uid: 'USD', code: 'USD', label: 'USD'},
    {uid: 'EUR', code: 'EUR', label: 'EUR'},
  ],
  platforms: [
    {uid: 'MT4', code: 'MT4', label: 'MetaTrader 4'},
    {uid: 'MT5', code: 'MT5', label: 'MetaTrader 5'},
    {uid: 'cTrader', code: 'cTrader', label: 'cTrader'},
  ],
  brokers: [
    {uid: 'FTMO', code: 'ftmo', label: 'FTMO'},
    {uid: 'CMC', code: 'cmc', label: 'CMC Markets'},
    {uid: 'td365', code: 'td365', label: 'td365'},
    {uid: 'TD', code: 'td', label: 'TD'},
  ],
  accountTypes: [
    {uid: 'forex', code: 'forex', label: 'Forex'},
    {uid: 'cfd', code: 'cfd', label: 'CFD'},
    {uid: 'options', code: 'options', label: 'Options'},
    {uid: 'shares', code: 'shares', label: 'Shares'},
  ]
}

export const accountDetails: AccountDetails = {
  uid: '1239',
  account: accounts[0],
  consistency: 51,
  insights: {
    uid: '3344',
    tradingDays: 31,
    trades: 189,
    maxDailyLoss: -489.33,
    maxTotalLoss: -987.21,
    maxDailyProfit: 783.56,
    maxProfit: 3894.12,
  },
  equity: [
    {
      uid: '1233',
      date: '2024-04-01',
      amount: 30086.65,
      points: 15.23,
    },
    {
      uid: '1244',
      date: '2024-05-01',
      amount: 30191.55,
      points: 46.89,
    },
    {
      uid: '1256',
      date: '2024-06-01',
      amount: 30611.55,
      points: 123.88,
    },
    {
      uid: '1266',
      date: '2024-07-01',
      amount: 30631.80,
      points: 121.02,
    },
    {
      uid: '1277',
      date: '2024-08-01',
      amount: 30301.80,
      points: 85.21,
    },
    {
      uid: '1288',
      date: '2024-09-01',
      amount: 30721.80,
      points: 144.17,
    },
    {
      uid: '1299',
      date: '2024-10-01',
      amount: 30391.80,
      points: 91.56,
    },
  ]
};