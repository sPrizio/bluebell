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
    uid: '123',
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
    uid: '123',
    start: '2024-10-07',
    end: '2024-10-08',
    records: tradeRecords.slice(0, 2)
  },
  {
    uid: '123',
    start: '2024-10-06',
    end: '2024-10-07',
    records: tradeRecords.slice(0, 3)
  }
]