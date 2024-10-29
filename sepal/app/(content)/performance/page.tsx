'use client'

import {notFound, useSearchParams} from "next/navigation";
import React, {useEffect, useState} from "react";
import {AggregateInterval, Icons} from "@/lib/enums";
import {useSepalPageInfoContext} from "@/lib/context/SepalContext";
import {delay, getAccount, getAccountNumber, getDefaultAccount, isNumeric} from "@/lib/functions";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select";
import {dailyTradeRecords, monthlyTradeRecords, yearlyTradeRecords} from "@/lib/sample-data";
import {BaseCard} from "@/components/Card/BaseCard";
import moment from "moment";
import {DateTime} from "@/lib/constants";
import TradeRecordContent from "@/components/Content/Trade/TradeRecordContent";
import {Loader2} from "lucide-react";

/**
 * The page that shows an account's performance over time
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function PerformancePage() {

  const {
    pageTitle,
    pageSubtitle,
    pageIconCode,
    breadcrumbs,
    user,
    setPageTitle,
    setPageSubtitle,
    setPageIconCode,
    setBreadcrumbs,
    setUser
  } = useSepalPageInfoContext()

  const searchParams = useSearchParams()
  const [isLoading, setIsLoading] = useState(false)
  const [accNumber, setAccNumber] = useState(getAccountNumber(searchParams, user.accounts))
  const [account, setAccount] = useState<Account | null>()
  const [aggInterval, setAggInterval] = useState<AggregateInterval>(AggregateInterval.DAILY)
  const [tradeRecords, setTradeRecords] = useState<Array<TradeRecord>>([])

  const acc = getAccount(accNumber, user.accounts)
  if (!acc) {
    return notFound()
  }

  useEffect(() => {
    setPageTitle('Performance')
    setPageSubtitle('A look a trading account\'s performance over time ' + accNumber)
    setPageIconCode(Icons.Performance)
    setBreadcrumbs([
      {label: 'Dashboard', href: '/dashboard', active: false},
      {label: 'Accounts', href: '/accounts', active: false},
      {label: accNumber.toString(), href: '/accounts/' + accNumber, active: false},
      {label: 'Performance', href: '/performance', active: true},
    ])

    getTradeRecords()
  }, [])

  useEffect(() => {
    setPageTitle('Performance')
    setPageSubtitle('A look a trading account\'s performance over time ' + accNumber)
    setPageIconCode(Icons.Performance)
    setBreadcrumbs([
      {label: 'Dashboard', href: '/dashboard', active: false},
      {label: 'Accounts', href: '/accounts', active: false},
      {label: accNumber.toString(), href: '/accounts/' + accNumber, active: false},
      {label: 'Performance', href: '/performance', active: true},
    ])

    setAccNumber(accNumber)
    setAccount(getAccount(accNumber, user.accounts))
    getTradeRecords()
  }, [accNumber]);

  useEffect(() => {
    getTradeRecords()
  }, [aggInterval]);


  //  GENERAL FUNCTIONS

  /**
   * Fetches the trade records
   */
  async function getTradeRecords() {

    setIsLoading(true)

    await delay(2000);

    //TODO: TEMP
    if (aggInterval === AggregateInterval.DAILY) {
      setTradeRecords(dailyTradeRecords)
    } else if (aggInterval === AggregateInterval.MONTHLY) {
      setTradeRecords(monthlyTradeRecords)
    } else {
      setTradeRecords(yearlyTradeRecords)
    }

    setIsLoading(false)
  }

  /**
   * Formats the card date based on the aggregate interval
   *
   * @param val input date
   */
  function formatDate(val: string) {
    switch (aggInterval) {
      case AggregateInterval.DAILY:
        return moment(val).format(DateTime.ISOMonthWeekDayFormat)
      case AggregateInterval.MONTHLY:
        return moment(val).format(DateTime.ISOMonthYearFormat)
      default:
        return moment(val).format(DateTime.ISOYearFormat)
    }
  }


  //  RENDER

  return (
    <div className={'grid grid-cols-1 gap-8'}>
      {
        isLoading ?
          <div className={'h-[72vh] flex items-center justify-center'}>
            <div className={'grid grid-cols-1 justify-items-center gap-4'}>
              <div>
                <Loader2 className="animate-spin text-primary" size={50}/>
              </div>
              <div className={'text-lg'}>Loading Performance</div>
            </div>
          </div>
          :
          <>
            <p>NEEDS A LOADING STATE</p>
            <p>NEEDS DYNAMIC DATE CONTROLS</p>
            <div className={'flex items-center justify-end gap-4'}>
              <div>
                <Select value={aggInterval.code} onValueChange={(val) => setAggInterval(AggregateInterval.get(val))}>
                  <SelectTrigger className="w-[180px] bg-white">
                    <SelectValue placeholder="Account"/>
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value={AggregateInterval.DAILY.code}>{AggregateInterval.DAILY.label}</SelectItem>
                    <SelectItem value={AggregateInterval.MONTHLY.code}>{AggregateInterval.MONTHLY.label}</SelectItem>
                    <SelectItem value={AggregateInterval.YEARLY.code}>{AggregateInterval.YEARLY.label}</SelectItem>
                  </SelectContent>
                </Select>
              </div>
              <div>
                <Select value={accNumber.toString()} onValueChange={(val) => setAccNumber(parseInt(val))}>
                  <SelectTrigger className="w-[180px] bg-white">
                    <SelectValue placeholder="Account"/>
                  </SelectTrigger>
                  <SelectContent>
                    {
                      user.accounts.map(item => {
                        return (
                          <SelectItem key={item.uid} value={item.accountNumber.toString()}>{item.name}</SelectItem>
                        )
                      })
                    }
                  </SelectContent>
                </Select>
              </div>
            </div>
            {
              tradeRecords?.map(item => {
                return (
                  <BaseCard
                    key={item.uid}
                    title={formatDate(item.start)}
                    cardContent={<TradeRecordContent tradeRecord={item} aggInterval={aggInterval}/>}
                  />
                )
              })
            }
          </>
      }
    </div>
  )
}