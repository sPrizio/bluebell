const accountTransactions: Array<Transaction> = [
  {
    uid: '1236',
    date: '2024-10-08',
    amount: 125.89,
    status: 'Pending',//TODO: remove Account from backend DTO
    type: 'Withdrawal',
    accountNumber: 123
  },
  {
    uid: '124',
    date: '2024-10-06',
    amount: 500.0,
    status: 'Complete',
    type: 'Deposit',
    accountNumber: 123
  },
  {
    uid: '125',
    date: '2024-10-01',
    amount: 375.0,
    status: 'Failed',
    type: 'Deposit',
    accountNumber: 123
  }
]

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
    lastTraded: '2024-10-11T12:22:45',
    transactions: accountTransactions
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
    lastTraded: '2024-10-01T16:09:45',
    transactions: accountTransactions
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
    lastTraded: '2024-07-30T16:01:45',
    transactions: accountTransactions
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

export const tradeRecordEquityPoints: Array<TradeRecordEquityPoint> = [
  //TODO: implement on backend, data always starts with zero
  {
    uid: 'trep-0',
    tradeId: 'trep-0',
    netProfit: 0,
    netPoints: 0
  },
  {
    uid: 'trep-1',
    tradeId: 'trep-1',
    netProfit: 156.36,
    netPoints: 31.58
  },
  {
    uid: 'trep-2',
    tradeId: 'trep-2',
    netProfit: -369.12,
    netPoints: -69.56
  },
  {
    uid: 'trep-3',
    tradeId: 'trep-3',
    netProfit: -45.21,
    netPoints: -10.25
  },
  {
    uid: 'trep-4',
    tradeId: 'trep-4',
    netProfit: 81.56,
    netPoints: 8.96
  },
  {
    uid: 'trep-5',
    tradeId: 'trep-5',
    netProfit: 201.84,
    netPoints: 56.78
  },
  {
    uid: 'trep-6',
    tradeId: 'trep-6',
    netProfit: 696.31,
    netPoints: 105.89
  },
  {
    uid: 'trep-7',
    tradeId: 'trep-7',
    netProfit: 401.25,
    netPoints: 78.54
  },
  {
    uid: 'trep-8',
    tradeId: 'trep-8',
    netProfit: 218.74,
    netPoints: 55.23
  },
  {
    uid: 'trep-9',
    tradeId: 'trep-9',
    netProfit: 900.56,
    netPoints: 158.25
  }
]

export const dailyTradeRecords: Array<TradeRecord> = [
  {
    uid: 'tr-123',
    start: '2024-10-08',
    end: '2024-10-09',
    largestLoss: -128.56,
    largestWin: 215.89,
    lossAverage: -96.36,
    losses: 4,
    lowestPoint: -100.25,
    netProfit: 321.47,
    points: 88.20,
    pointsGained: 215.23,
    pointsLost: -148.87,
    profitability: 1.65,
    retention: 56,
    trades: 9,
    winAverage: 117.55,
    winPercentage: 51,
    wins: 5,
    account: accounts[0],
    equityPoints: tradeRecordEquityPoints
  },
  {
    uid: 'tr-124',
    start: '2024-10-09',
    end: '2024-10-10',
    largestLoss: -128.56,
    largestWin: 215.89,
    lossAverage: -96.36,
    losses: 4,
    lowestPoint: -100.25,
    netProfit: 321.47,
    points: 88.20,
    pointsGained: 215.23,
    pointsLost: -148.87,
    profitability: 1.65,
    retention: 56,
    trades: 9,
    winAverage: 117.55,
    winPercentage: 51,
    wins: 5,
    account: accounts[0],
    equityPoints: tradeRecordEquityPoints
  },
  {
    uid: 'tr-125',
    start: '2024-10-10',
    end: '2024-10-11',
    largestLoss: -128.56,
    largestWin: 215.89,
    lossAverage: -96.36,
    losses: 4,
    lowestPoint: -100.25,
    netProfit: 321.47,
    points: 88.20,
    pointsGained: 215.23,
    pointsLost: -148.87,
    profitability: 1.65,
    retention: 56,
    trades: 9,
    winAverage: 117.55,
    winPercentage: 51,
    wins: 5,
    account: accounts[0],
    equityPoints: tradeRecordEquityPoints
  },
  {
    uid: 'tr-126',
    start: '2024-10-11',
    end: '2024-10-12',
    largestLoss: -128.56,
    largestWin: 215.89,
    lossAverage: -96.36,
    losses: 4,
    lowestPoint: -100.25,
    netProfit: 321.47,
    points: 88.20,
    pointsGained: 215.23,
    pointsLost: -148.87,
    profitability: 1.65,
    retention: 56,
    trades: 9,
    winAverage: 117.55,
    winPercentage: 51,
    wins: 5,
    account: accounts[0],
    equityPoints: tradeRecordEquityPoints
  },
  {
    uid: 'tr-127',
    start: '2024-10-14',
    end: '2024-10-15',
    largestLoss: -128.56,
    largestWin: 215.89,
    lossAverage: -96.36,
    losses: 4,
    lowestPoint: -100.25,
    netProfit: 321.47,
    points: 88.20,
    pointsGained: 215.23,
    pointsLost: -148.87,
    profitability: 1.65,
    retention: 56,
    trades: 9,
    winAverage: 117.55,
    winPercentage: 51,
    wins: 5,
    account: accounts[0],
    equityPoints: tradeRecordEquityPoints
  }
]

export const tradeRecordControls: TradeRecordControls = {
  uid: 'trc-123',
  yearEntries: [
    {
      uid: 'trc-y-2023',
      year: '2023',
      monthEntries: [],
    },
    {
      uid: 'trc-y-2024',
      year: '2024',
      monthEntries: [
        {
          uid: 'trc-m-jan',
          month: 'January',
          value: 2
        },
        {
          uid: 'trc-m-feb',
          month: 'February',
          value: 0
        },
        {
          uid: 'trc-m-mar',
          month: 'March',
          value: 0
        },
        {
          uid: 'trc-m-apr',
          month: 'April',
          value: 2
        },
        {
          uid: 'trc-m-may',
          month: 'May',
          value: 0
        },
        {
          uid: 'trc-m-jun',
          month: 'June',
          value: 2
        },
        {
          uid: 'trc-m-jul',
          month: 'July',
          value: 2
        },
        {
          uid: 'trc-m-aug',
          month: 'August',
          value: 2
        },
        {
          uid: 'trc-m-sep',
          month: 'September',
          value: 0
        },
        {
          uid: 'trc-m-oct',
          month: 'October',
          value: 0
        },
        {
          uid: 'trc-m-nov',
          month: 'November',
          value: 0
        },
        {
          uid: 'trc-m-dec',
          month: 'December',
          value: 0
        }
      ]
    }
  ],
}

export const tradeLog: Array<TradeLog> = [
  {
    uid: '1235',
    start: '2024-10-08',
    end: '2024-10-09',
    records: dailyTradeRecords.slice(0, 1)
  },
  {
    uid: '124',
    start: '2024-10-07',
    end: '2024-10-08',
    records: dailyTradeRecords.slice(0, 2)
  },
  {
    uid: '125',
    start: '2024-10-06',
    end: '2024-10-07',
    records: dailyTradeRecords.slice(0, 3)
  }
]
