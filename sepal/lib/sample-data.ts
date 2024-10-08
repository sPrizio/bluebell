export const accounts: Array<Account> = [
  {
    uid: '123',
    defaultAccount: true,
    accountOpenTime: '-1',
    accountCloseTime: '-1',
    balance: 30987.65,
    active: false,
    name: 'Test Account 1',
    accountNumber: 123,
    currency: 'CAD',
    broker: 'CMC',
    accountType: 'CFD',
    tradePlatform: 'MT4',
    lastTraded: '-1'
  },
  {
    uid: '123',
    defaultAccount: true,
    accountOpenTime: '-1',
    accountCloseTime: '-1',
    balance: 30987.65,
    active: false,
    name: 'Test Account 1',
    accountNumber: 123,
    currency: 'CAD',
    broker: 'CMC',
    accountType: 'CFD',
    tradePlatform: 'MT4',
    lastTraded: '-1'
  },
  {
    uid: '123',
    defaultAccount: true,
    accountOpenTime: '-1',
    accountCloseTime: '-1',
    balance: 30987.65,
    active: false,
    name: 'Test Account 1',
    accountNumber: 123,
    currency: 'CAD',
    broker: 'CMC',
    accountType: 'CFD',
    tradePlatform: 'MT4',
    lastTraded: '-1'
  }
]

export const tradeRecords: Array<TradeRecord> = [
  {
    uid: '123',
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
    uid: '123',
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
    uid: '123',
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

export const chartData: Array<AccountEquityPoint> = [
  {
    uid: '123',
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
