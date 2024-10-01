//  CLIENT

export type StandardJsonResponse = {
    success?: boolean,
    data?: any,
    message?: string,
    internalMessage?: string,
}


//  PLATFORM

export type Sort = {
    empty: boolean,
    sorted: boolean,
    unsorted: boolean
}

export type Pageable = {
    offset: number,
    pageNumber: number,
    pageSize: number,
    paged: boolean,
    sort: Sort,
    unpaged: boolean,
}

export type PagedResponse<T> = {
    content: Array<T>,
    empty: boolean,
    first: boolean,
    last: boolean,
    number: number,
    numberOfElements: number
    pageable?: Pageable,
    size: number,
    sort?: Sort,
    totalElements: number,
    totalPages: number,
}


//  NEWS

export type NewsEntry = {
    content: string,
    country: string,
    empty: boolean,
    forecast: string,
    previous: string,
    severity: string,
    severityLevel: number,
    uid: string
}

export type NewsSlot = {
    active: boolean,
    empty: boolean,
    entries: Array<NewsEntry>,
    time: string,
    uid: string
}

export type News = {
    active: boolean,
    date: string,
    empty: boolean,
    future: boolean,
    past: boolean,
    slots: Array<NewsSlot>,
    uid: string
}


//  ACCOUNT

export type Currency = {
    isoCode: string,
    label: string
}

export type Account = {
    accountCloseTime: string | null | undefined,
    accountNumber: number,
    accountOpenTime: string,
    accountType: string,
    active: boolean,
    balance: number,
    broker: string,
    currency: Currency,
    defaultAccount: boolean,
    empty: boolean,
    lastTraded: string | null | undefined,
    name: string,
    tradePlatform: string,
    uid: string
}


//  TRADES

export type Trade = {
    account: Account,
    closePrice: number,
    empty: boolean,
    lotSize: number,
    netProfit: number,
    openPrice: number,
    points: number,
    product: string,
    stopLoss: number,
    takeProfit: number,
    tradeCloseTime: string,
    tradeId: string,
    tradeOpenTime: string,
    tradePlatform: string,
    tradeType: string
    uid: string
}

export type TradeRecord = {
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
    wins: number
}