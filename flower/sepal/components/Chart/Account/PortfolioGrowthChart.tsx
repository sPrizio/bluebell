'use client'

import React from "react";
import {Area, ComposedChart, Legend, Line, ResponsiveContainer, Tooltip, TooltipProps, YAxis} from "recharts";
import {NameType, ValueType,} from 'recharts/types/component/DefaultTooltipContent';
import {BASE_COLORS, Css, DateTime} from "@/lib/constants";
import {IconPointFilled} from "@tabler/icons-react";
// @ts-expect-error: error in import due to 3rd party library
import Please from 'pleasejs/dist/Please';
import {BaseCard} from "@/components/Card/BaseCard";
import moment from "moment";
import {formatNumberForDisplay} from "@/lib/functions/util-functions";
import {PortfolioEquityPoint} from "@/types/apiTypes";

type Entry = Record<string, number>;

/**
 * Renders a chart to display an account's growth over time
 *
 * @param data account equity data points
 * @author Stephen Prizio
 * @version 0.2.0
 */
export default function PortfolioGrowthChart(
  {
    data = [],
  }
  : Readonly<{
    data: Array<PortfolioEquityPoint>,
  }>
) {

  const colors: Array<string> = []
  for (let i = 0; i < data.length; i++) {
    if (i >= 0 && i < data.length) {
      colors.push(Please.make_color({base_color: BASE_COLORS[i]}))
    } else {
      colors.push(Please.make_color({base_color: BASE_COLORS[0]}))
    }
  }

  const accChartData = generateAccData()


  //  GENERAL FUNCTIONS

  /**
   * Computes the gradient
   */
  function computeGradientLimit() {
    const tempPoints = data
    const min = Math.min(...tempPoints.map(i => i.normalized))
    const max = Math.max(...tempPoints.map(i => i.normalized))


    return Math.round(((max - min) / max) * 100.0) + '%'
  }

  /**
   * Gets all the chart keys
   */
  function getAccountKeys() {

    if (data.length > 0) {
      const keys = []

      for (const point of data) {
        const accounts = point.accounts
        // @ts-expect-error : ignore typing
        for (const acc of accounts) {
          if (keys.indexOf(acc.name) === -1) {
            keys.push(acc.name)
          }
        }
      }

      return keys;
    }

    return [];
  }

  /**
   * Resolves data
   */
  function resolveChartDataPointValue(point: PortfolioEquityPoint, key: string) {
    // @ts-expect-error : ignore typing
    for (const acc of point.accounts) {
      if (acc.name === key) {
        return acc.normalized
      }
    }

    return 0;
  }

  /**
   * Calculates the sume of keys for the counter object
   */
  function aggregateSumByKey(data: Entry[], key: string): number {
    return data.reduce((sum, item) => {
      return sum + (item[key] || 0);
    }, 0);
  }

  /**
   * Generates the chart data compatible with recharts
   */
  function generateAccData() {

    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
    const res: any[] = []
    let counter = 0.0
    const counterObj: Entry[] = []

    for (const point of data) {
      counter += point.normalized

      const data = {
        date: point.date,
        portfolio: counter,
      }

      for (const key of getAccountKeys()) {
        const obj: Record<string, number> = {
          [key]: resolveChartDataPointValue(point, key),
        };

        counterObj.push(obj)
        // @ts-expect-error : ignore typing
        data[key] = aggregateSumByKey(counterObj, key)
      }

      res.push(data)
    }

    return res
  }

  /**
   * Determines if the chart should show more than 1 account
   */
  function hasMultipleAccounts() {

    if (data.length > 0) {
      return getAccountKeys().length > 1;
    }

    return false;
  }


  //  COMPONENTS

  /**
   * Renders a custom legend for the chart
   *
   * @param props payload
   */
  // eslint-disable-next-line @typescript-eslint/ban-ts-comment
  const legend = (props: any) => {
    const {payload} = props;

    return (
      <div className={'flex flex-row items-center justify-end gap-4 text-sm '}>
        {
          // eslint-disable-next-line @typescript-eslint/ban-ts-comment
          payload.filter((item: any) => item.dataKey !== 'portfolio').map((item: any, itx: number) => {
            return (
              <div key={item.dataKey} className={'flex items-center justify-end '} style={{color: colors[itx]}}>
                <span className={'inline-block'}><IconPointFilled size={15}/></span>&nbsp;{item.value}
              </div>
            )
          })
        }
        <div className={'flex items-center justify-end text-primary'}>
          <span className={'inline-block'}><IconPointFilled size={15}/></span>&nbsp;portfolio
        </div>
      </div>
    )
  }

  /**
   * Renders a custom tooltip for the chart
   *
   * @param active is active
   * @param payload data
   * @param label label
   */
  const tooltip = ({active, payload}: TooltipProps<ValueType, NameType>) => {

    if (active && (payload?.length ?? -1 > 0)) {

      const date = payload?.[0].payload.date ?? ''
      return (
        <BaseCard
          title={moment(date).format(DateTime.ISOMonthYearFormat)}
          cardContent={
            <div className={'flex flex-col items-center'}>
              {
                // eslint-disable-next-line @typescript-eslint/ban-ts-comment
                payload?.map((item: any, itx: number) => {
                  return (
                    <div key={itx} className={"flex flex-row items-center w-[250px] gap-6"} style={{color: item.color}}>
                      <div className={'justify-start'}>
                        {item.dataKey}
                      </div>
                      <div className={'grow justify-end text-right'}>
                        {formatNumberForDisplay(item.value)}&nbsp;%
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
      <div className={'w-[100%]'}>
        <ResponsiveContainer width='100%' minHeight={300}>
          <ComposedChart data={accChartData}>
            <defs>
              <linearGradient id="color" x1="0" y1="0" x2="0" y2="1">
                <stop offset="5%" stopColor={`${Css.ColorPrimary}`} stopOpacity={0.8}/>
                <stop offset={computeGradientLimit()} stopColor={`${Css.ColorPrimary}`} stopOpacity={0}/>
              </linearGradient>
            </defs>
            <Legend content={legend} verticalAlign={'top'} height={40}/>
            {hasMultipleAccounts() ? <Legend content={legend} verticalAlign={'top'} height={40}/> : null}
            <Tooltip content={tooltip}/>
            {
              hasMultipleAccounts() ?
                getAccountKeys().map((item: string, itx: number) => {
                  return (
                    <Line key={itx} type="monotone" dot={false} dataKey={item} strokeWidth={3} stroke={colors[itx]}/>
                  )
                }) : null
            }
            <YAxis dataKey={'portfolio'} type="number" domain={['dataMin', 'dataMax']} hide={true} />
            <Area type="monotone" dot={false} dataKey="portfolio" stackId="1" stroke={`${Css.ColorPrimary}`}
                  strokeWidth={4} fill="url(#color)"/>
          </ComposedChart>
        </ResponsiveContainer>
      </div>
    </div>
  )
}