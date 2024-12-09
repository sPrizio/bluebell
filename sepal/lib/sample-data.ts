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
    transactions: []
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
    transactions: []
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
    transactions: []
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
