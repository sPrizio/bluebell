'use client'

import styles from './layout.module.scss'
import React, {useEffect, useState} from "react";
import BluebellDatePicker from "@/app/components/DatePicker/BluebellDatePicker";
import BaseCard from "@/app/components/Card/BaseCard";
import {CoreConstants} from "@/app/constants";
import {IntradayChart} from "@/app/components/Chart/Intraday/IntradayChart";
import SimpleSelect from "@/app/components/Select/SimpleSelect";
import {Interval, SimpleOption} from "@/app/types/appTypes";
import {formatNumberForDisplay} from "@/app/services/data/dataIntegrityService";
import {getAuthHeader} from "@/app/services/configuration/configurationService";
import {StandardJsonResponse} from "@/app/types/apiTypes";
import moment from "moment-timezone";

/**
 * The charting page
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function Chart() {

  const baseClass = "chart-page"

  const initialOptions = {
    chart: {
      type: 'candlestick',
      height: 350
    },
    title: {
      text: undefined,
      align: 'left'
    },
    xaxis: {
      type: 'datetime',
      labels: {
        datetimeUTC: false,
        formatter: function (value: any, timestamp: number, opts: any) {
          return moment(timestamp).format(CoreConstants.DateTime.ISOShortMonthFullDayFormatTimeFormat);
        }
      }
    },
    yaxis: {
      tooltip: {
        enabled: true
      },
      labels: {
        formatter: function (value: string | number) {
          return formatNumberForDisplay(value)
        }
      }
    }
  }
  const quickPicks: SimpleOption[] = [
    {label: '1 Minute', value: 'one-minute', unit: 'days', count: 0, interval: Interval.MINUTE},
    {label: '5 Minute', value: 'five-minute', unit: 'days', count: 0, interval: Interval.MINUTE},
    {label: '10 Minute', value: 'ten-minute', unit: 'days', count: 0, interval: Interval.MINUTE},
    {label: '15 Minute', value: 'fifteen-minute', unit: 'days', count: 0, interval: Interval.MINUTE},
    {label: '30 Minute', value: 'thirty-minute', unit: 'days', count: 0, interval: Interval.MINUTE},
    {label: '1 hour', value: 'one-hour', unit: 'days', count: 0, interval: Interval.MINUTE},
    {label: '1 day', value: 'one-day', unit: 'days', count: 1, interval: Interval.MINUTE},
  ]

  const [isLoading, setIsLoading] = useState<boolean>(false)
  const [seriesData, setSeriesData] = useState<any>([{data: []}])
  const [options, setOptions] = useState(initialOptions)
  const [quickPick, setQuickPick] = useState<string>(quickPicks[1].value)
  const [startDate, setStartDate] = useState<Date | null>(moment().subtract(1, 'months').startOf('month').toDate())
  const [endDate, setEndDate] = useState<Date | null>(moment().subtract(1, 'months').startOf('month').add(1, 'days').toDate())

  useEffect(() => {
    getMarketPrices()
    setOptions({...options})
  }, [endDate, quickPick]);


  //  GENERAL FUNCTIONS

  /**
   * Handles the changing of dates to fetch new data
   *
   * @param val date change
   */
  function handleChange(val: Date) {
    setStartDate(val)
    setEndDate(moment(val).add(1, 'days').toDate())
    setOptions({...options})
  }

  async function getMarketPrices() {

    setIsLoading(true)

    try {
      const res = await fetch(
        CoreConstants.ApiUrls.Chart.GetApex
          .replace('{start}', startDate?.toISOString().split('T')[0] ?? '')
          .replace('{end}', endDate?.toISOString().split('T')[0] ?? '')
          .replace('{interval}', quickPick)
        + '&locales=CAN&locales=USD',
        {headers: getAuthHeader(), method: 'GET'}
      )

      if (res.ok) {
        const data: StandardJsonResponse = await res.json()
        if (data.success) {
          const result = data.data
          setSeriesData([{data: result}])
        }
      }
    } catch (e) {
      console.log(e)
    }

    setIsLoading(false)
  }


  //  RENDER

  return (
    <div className={styles[baseClass]}>
      <div className={styles[`${baseClass}__page-row`] + ' ' + styles[`${baseClass}__controls`]}>
        <div className={styles[`${baseClass}__control`] + ' ' + styles[`${baseClass}__control--datepicker`]}>
          <BluebellDatePicker
            label={'View Day/Date'}
            val={startDate}
            handler={handleChange}
            isRounded={true}
            disableWeekends={true}
            monthDropdown={true}
            yearDropdown={true}
          />
        </div>
        <div className={styles[`${baseClass}__control`]}>
          <SimpleSelect options={quickPicks} val={quickPick} handler={setQuickPick}/>
        </div>
        <div className={styles[`${baseClass}__control`]}>
          {
            quickPick === 'one-hour' ?
              <small>* For intervals of 1-hour, a 3-day window will be shown around the selected date</small> : null
          }
          {
            quickPick === 'one-day' ?
              <small>* For intervals of 1-day, a 1-month window will be shown around the selected date</small> : null
          }
        </div>
      </div>
      <div className={styles[`${baseClass}__page-row`]}>
        <div className={styles[`${baseClass}__history-wrapper`]}>
          <BaseCard
            loading={isLoading}
            hasBorder={false}
            hasOverflow={true}
            title={moment(startDate).format(CoreConstants.DateTime.ISOLongMonthDayYearFormat)}
            content={[<IntradayChart series={seriesData} options={options} key={0} id={Math.random()}/>]}
            hasError={(seriesData?.[0].data?.length ?? 0) < 1}
          />
        </div>
      </div>
    </div>
  )
}