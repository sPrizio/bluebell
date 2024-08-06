'use client'

import styles from './layout.module.scss'
import BaseCard from "@/app/components/Card/BaseCard";
import React, {useEffect, useState} from "react";
import TradeList from "@/app/components/Trade/List/TradeList";
import SimpleSelect from "@/app/components/Select/SimpleSelect";
import {Interval, SimpleOption} from "@/app/types/appTypes";
import moment from "moment";
import {CoreConstants} from "@/app/constants";
import {getAuthHeader} from "@/app/services/configuration/configurationService";
import {PagedResponse, StandardJsonResponse, Trade} from "@/app/types/apiTypes";

/**
 * The trades page, showing all trades and allowing the user to upload new ones into the system
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function Trades() {

  const baseClass = "trades-page"
  const defaultQuickPick = 'today'
  const quickPicks: SimpleOption[] =
    [
      {label: 'Today', value: 'today', unit: 'days', count: 0, interval: Interval.DAILY},
      {label: 'Yesterday', value: 'yesterday', unit: 'days', count: 1, interval: Interval.DAILY},
      {label: 'This Week', value: 'this-week', unit: 'weeks', count: 1, interval: Interval.DAILY},
      {label: 'This Month', value: 'this-month', unit: 'months', count: 1, interval: Interval.DAILY},
      {label: 'Last 3 Months', value: 'last-3-months', unit: 'months', count: 3, interval: Interval.WEEKLY},
      {label: 'Last 6 Months', value: 'last-6-months', unit: 'months', count: 6, interval: Interval.MONTHLY},
      {label: 'All-time', value: 'all-time', unit: 'years', count: 25, interval: Interval.YEARLY},
    ]

  const [isLoading, setIsLoading] = useState<boolean>(false)
  const [quickPick, setQuickPick] = useState(defaultQuickPick)
  const [start, setStart] = useState(computeStartDate(defaultQuickPick));
  const [end, setEnd] = useState(computeEndDate(defaultQuickPick))
  const [trades, setTrades] = useState<PagedResponse<Trade>>()

  useEffect(() => {
    getTrades('0')
  }, [quickPick, start, end])


  //  GENERAL FUNCTIONS

  /**
   * Computes the start date
   * @param val
   */
  function computeStartDate(val: string) {
    return moment().startOf('day').subtract(getQuickPick(val)?.count ?? 1, getQuickPick(val)?.unit ?? 'days').format(CoreConstants.DateTime.ISODateTimeFormat)
  }

  /**
   * Computes the end date
   */
  function computeEndDate(val: string) {
    return moment().add(1, 'days').startOf('day').format(CoreConstants.DateTime.ISODateTimeFormat)
  }

  /**
   * Finds the quick pick matching the given selection
   */
  function getQuickPick(val: string) {
    return quickPicks.find(qp => qp.value === val)
  }

  /**
   * Handles updating quick pick selection and changing the interval
   */
  async function handleQuickPickChange(e: string) {
    setQuickPick(e)
    setStart(computeStartDate(e))
    setEnd(computeEndDate(e))
  }

  /**
   * Fetches trades for the given page and time span
   */
  async function getTrades(page: string) {

    setIsLoading(true)

    try {
      const res =
        await fetch(
          CoreConstants.ApiUrls.Trade.GetPaginated
            .replace('{accountNumber}', CoreConstants.ApiCredentials.TestAccountNumber)
            .replace('{start}', start)
            .replace('{end}', end)
            .replace('{page}', page),
          {
            headers: getAuthHeader(),
            method: 'GET',
          }
        )

      if (res.ok && res as StandardJsonResponse) {
        const data: StandardJsonResponse = await res.json()
        if (data.success && data.data as PagedResponse<Trade>) {
          const trades : PagedResponse<Trade> = data.data
          setTrades(trades)
        }
      }
    } catch (e) {
      console.log(e)
    }

    setIsLoading(false)
  }


  //  RENDER

  return (
    <>
      <div className={styles[baseClass]}>
        <BaseCard
          loading={isLoading}
          hasBorder={false}
          hasOverflow={true}
          title={'Log'}
          content={[<TradeList hasAdmin={false} key={0} trades={trades} paginationHandler={getTrades} />]}
          controls={[<SimpleSelect options={quickPicks} key={0} handler={handleQuickPickChange} val={quickPick}/>]}
        />
      </div>
    </>
  )
}

