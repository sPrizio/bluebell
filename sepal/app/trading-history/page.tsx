'use client'

import styles from './layout.module.scss'
import React, {useEffect, useState} from "react";
import {Interval, SimpleOption} from "@/app/types/appTypes";
import SimpleSelect from "@/app/components/Select/SimpleSelect";
import BluebellDatePicker from "@/app/components/DatePicker/BluebellDatePicker";
import moment from "moment";
import BaseCard from "@/app/components/Card/BaseCard";
import TradeHistory from "@/app/components/Trade/History/TradeHistory";
import {CoreConstants} from "@/app/constants";
import {getAuthHeader} from "@/app/services/configuration/configurationService";
import {StandardJsonResponse, TradeRecord} from "@/app/types/apiTypes";

/**
 * Page that displays trade history for different time periods, aggregated by different intervals
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function TradingHistory() {

  const baseClass = "trading-history-page"
  const quickPicks: SimpleOption[] =
    [
      {label: 'Today', value: 'today', unit: 'days', count: 0, interval: Interval.DAILY},
      {label: 'Yesterday', value: 'yesterday', unit: 'days', count: 1, interval: Interval.DAILY},
      {label: 'This Week', value: 'this-week', unit: 'weeks', count: 1, interval: Interval.DAILY},
      {label: 'This Month', value: 'this-month', unit: 'months', count: 1, interval: Interval.DAILY},
      {label: 'Last 3 Months', value: 'last-3-months', unit: 'months', count: 3, interval: Interval.DAILY},
      {label: 'Last 6 Months', value: 'last-6-months', unit: 'months', count: 6, interval: Interval.MONTHLY},
      {label: 'All-time', value: 'all-time', unit: 'years', count: 25, interval: Interval.MONTHLY},
      {label: 'Custom', value: 'custom', unit: 'days', count: -1, interval: Interval.DAILY}
    ]

  const [isLoading, setIsLoading] = useState<boolean>(false)
  const [startDate, setStartDate] = useState<Date | null>(moment().startOf('month').toDate())
  const [endDate, setEndDate] = useState<Date | null>(new Date())
  const [quickPick, setQuickPick] = useState(getQuickPickForValue('this-month').value)
  const [tradeRecords, setTradeRecords] = useState<Array<TradeRecord>>([])

  useEffect(() => {
    getTradeRecords()
  }, [endDate])


  //  GENERAL FUNCTIONS

  /**
   * When the end date is changed, mark the time span as custom
   *
   * @param val custom date
   */
  function handleEndDateChange(val: Date) {
    setEndDate(val)
    setQuickPick('custom')
  }

  /**
   * Find the quick pick for associated value
   *
   * @param val test value
   */
  function getQuickPickForValue(val: string) {
    return quickPicks.find(qp => qp.value === val) ?? quickPicks[0]
  }

  /**
   * Handler for selecting a new option
   *
   * @param val selected option
   */
  function handleSelectChange(val: string) {
    setQuickPick(val)
    const pick = getQuickPickForValue(val)
    setStartDate(resolveQuickPick(pick, true))
    setEndDate(resolveQuickPick(pick, false))
  }

  /**
   * Resolves the date bounds depending on the selected option
   *
   * @param option simple option
   * @param start if true, this is the start of the period otherwise it is the end
   */
  function resolveQuickPick(option: SimpleOption, start: boolean) {

    if (start) {
      switch (option.value) {
        case 'yesterday':
          return moment().subtract(1, 'days').startOf('day').toDate()
        case 'this-week':
          return moment().startOf('week').add(1, 'days').toDate()
        case 'this-month':
          return moment().startOf('month').toDate()
        case 'last-3-months':
        case 'last-6-months':
          return moment().subtract(option.count, option.unit).toDate()
        case 'all-time':
          return moment().subtract(25, 'years').toDate()
        default:
          return moment().startOf('day').toDate()
      }
    } else {
      switch (option.value) {
        case 'yesterday':
          return moment().subtract(1, 'days').startOf('day').toDate()
        default:
          return moment().startOf('day').toDate()
      }
    }
  }

  /**
   * Determines the date format to display based on the given interval
   *
   * @param interval time interval
   * @param date date string to display
   */
  function resolveDate(interval: Interval, date: string) {

    if (interval == Interval.MONTHLY) {
      return moment(date).format(CoreConstants.DateTime.ISOMonthYearFormat)
    }

    return moment(date).format(CoreConstants.DateTime.ISOLongMonthDayYearFormat)
  }

  /**
   * Obtains the most recent trade records
   */
  async function getTradeRecords() {

    setIsLoading(true)

    try {
      const res = await fetch(
        CoreConstants.ApiUrls.TradeRecord.Get
          .replace('{start}', startDate?.toISOString().split('T')[0] ?? '')
          .replace('{end}', endDate?.toISOString().split('T')[0] ?? '')
          .replace('{accountNumber}', CoreConstants.ApiCredentials.TestAccountNumber)
          .replace('{interval}', getQuickPickForValue(quickPick).interval.toString())
          .replace('{count}', '-1')
        + '&locales=CAN&locales=USD',
        {headers: getAuthHeader(), method: 'GET'}
      )

      if (res.ok) {
        const data: StandardJsonResponse = await res.json()
        if (data.success) {
          setTradeRecords(data.data)
        }
      }
    } catch (e) {
      console.log(e)
    }

    setIsLoading(false)
  }

  //TODO: trade history chart

  //  RENDER

  return (
    <div className={styles[baseClass]}>
      <div className={styles[`${baseClass}__page-row`] + ' ' + styles[`${baseClass}__controls`]}>
        <div className={styles[`${baseClass}__control`] + ' ' + styles[`${baseClass}__control--datepicker`]}>
          <BluebellDatePicker
            label={'Start Date'}
            val={startDate}
            handler={setStartDate}
            isRounded={true}
            disableWeekends={true}
          />
        </div>
        <div className={styles[`${baseClass}__control`] + ' ' + styles[`${baseClass}__control--datepicker`]}>
          <BluebellDatePicker
            label={'End Date'}
            val={endDate}
            handler={handleEndDateChange}
            isRounded={true}
            disableWeekends={true}
          />
        </div>
        <div className={styles[`${baseClass}__control`]}>
          <SimpleSelect options={quickPicks} val={quickPick} handler={handleSelectChange}/>
        </div>
      </div>
      {
        tradeRecords && tradeRecords.length > 0 ?
          <>
            {
              tradeRecords.map((item, key) => {
                return (
                  <div className={styles[`${baseClass}__page-row`]} key={key}>
                    <div className={styles[`${baseClass}__history-wrapper`]}>
                      <BaseCard
                        loading={isLoading}
                        subtitle={getQuickPickForValue(quickPick).interval == Interval.MONTHLY ? '' : moment(item.start).format(CoreConstants.DateTime.ISOWeekdayFormat)}
                        hasBorder={false}
                        hasOverflow={true}
                        title={resolveDate(getQuickPickForValue(quickPick).interval, item.start)}
                        content={[<TradeHistory key={0} tradeRecord={item} />]}
                      />
                    </div>
                  </div>
                )
              })
            }
          </>
          : <p>No data available.</p>
      }
    </div>
  )
}