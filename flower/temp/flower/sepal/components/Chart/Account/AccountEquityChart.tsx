'use client'

import React, {useEffect, useState} from "react";
import {Area, ComposedChart, ResponsiveContainer, Tooltip, TooltipProps, YAxis} from "recharts";
import {NameType, ValueType,} from 'recharts/types/component/DefaultTooltipContent';
import {Css, DateTime} from "@/lib/constants";
import {BaseCard} from "@/components/Card/BaseCard";
import moment from "moment";
import {formatNumberForDisplay} from "@/lib/functions/util-functions";
import {AccountEquityPoint} from "@/types/apiTypes";

interface InternalEquityPoint {
  date: string,
  value: number,
}

/**
 * Renders a chart to display an Account's growth over time
 *
 * @param data Account equity data points
 * @param showPoints show balance or points
 * @author Stephen Prizio
 * @version 0.0.2
 */
export default function AccountEquityChart(
  {
    data = [],
    showPoints = false,
  }
  : Readonly<{
    data: Array<AccountEquityPoint>,
    showPoints?: boolean
  }>
) {

  const [chartData, setChartData] = useState<Array<InternalEquityPoint>>()
  useEffect(() => {
    if (showPoints) {
      setChartData(data.map(item => ({date: item.date, value: item.cumPoints})))
    } else {
      setChartData(data.map(item => ({date: item.date, value: item.cumAmount})))
    }
  }, [showPoints]);


  //  GENERAL FUNCTIONS

  /**
   * Gets the min value
   */
  function getMin() {
    const min = data.map(d => d.amount)
    return Math.min(...min) * 0.9975
  }

  /**
   * Gets the max value
   */
  function getMax() {
    const max = data.map(d => d.amount)
    return Math.max(...max) * 1.0025
  }


  //  COMPONENTS

  /**
   * Renders a custom tooltip for the chart
   *
   * @param active is active
   * @param payload data
   * @param label label
   */
  const tooltip = ({active, payload, label}: TooltipProps<ValueType, NameType>) => {

    if (active && (payload?.length ?? -1 > 0)) {

      const date = payload?.[0].payload.date ?? ''
      return (
        <BaseCard
          title={moment(date).format(DateTime.ISOShortMonthDayYearWithTimeFormat)}
          cardContent={
            <div className={'flex flex-col items-center'}>
              {
                payload?.map((item: any, itx: number) => {
                  return (
                    <div key={itx} className={"flex flex-row items-center w-[200px] gap-6"} style={{color: item.color}}>
                      <div className={'justify-start capitalize'}>
                        {showPoints ? 'Points' : 'Balance'}
                      </div>
                      <div className={'grow justify-end text-right'}>
                        {showPoints ? formatNumberForDisplay(item.value) : '$ ' + formatNumberForDisplay(item.value)}
                      </div>
                    </div>
                  )
                }) ?? null
              }
            </div>
          }
        />
      );
    }

    return null;
  }


  //  RENDER

  return (
    <div className={'flex items-center justify-center pb-2'}>
      {
        (!data || data.length <= 1) &&
          <div className="mt-4 flex flex-col items-center">
              <p className={'text-slate-500'}>No trades to show. Come back after trading a bit!</p>
          </div>
      }
      {
        data && data.length > 1 &&
          <div className={'w-[100%]'} key={showPoints.toString()}>
              <ResponsiveContainer width='100%' minHeight={400}>
                  <ComposedChart data={chartData}>
                      <defs>
                          <linearGradient id="color" x1="0" y1="0" x2="0" y2="1">
                              <stop offset="5%" stopColor={`${Css.ColorPrimary}`} stopOpacity={0.8}/>
                              <stop offset="95%" stopColor={`${Css.ColorPrimary}`} stopOpacity={0}/>
                          </linearGradient>
                      </defs>
                      <YAxis orientation={'right'} hide={true} dominantBaseline={getMin()} domain={['dataMin', 'dataMax']}/>
                      <Tooltip content={tooltip}/>
                      <Area type="monotone" dot={false} dataKey="value" stroke={`${Css.ColorPrimary}`} strokeWidth={4} fill="url(#color)"/>
                  </ComposedChart>
              </ResponsiveContainer>
          </div>
      }
    </div>
  )
}