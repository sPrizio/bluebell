'use client'

import React, {useEffect, useState} from "react";
import {Area, ComposedChart, Legend, Line, ResponsiveContainer, Tooltip, TooltipProps} from "recharts";
import {NameType, ValueType,} from 'recharts/types/component/DefaultTooltipContent';
import {chartData} from "@/lib/sample-data";
import {BASE_COLORS, Css, DateTime} from "@/lib/constants";
import {IconPointFilled} from "@tabler/icons-react";
// @ts-ignore
import Please from 'pleasejs/dist/Please';
import {BaseCard} from "@/components/Card/BaseCard";
import moment from "moment";
import {formatNumberForDisplay} from "@/lib/functions/util-functions";

/**
 * Renders a chart to display an Account's growth over time
 *
 * @param data Account equity data points
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function PortfolioGrowthChart(
  {
    data = [],
  }
    : Readonly<{
    data: Array<PortfolioEquityPoint>,
  }>
) {

  const [colors, setColors] = useState<Array<string>>([])
  useEffect(() => {
    const colors = []
    for (let i = 0; i < data.length; i++) {
      if (i >= 0 && i < data.length) {
        colors.push(Please.make_color({base_color: BASE_COLORS[i]}))
      } else {
        colors.push(Please.make_color({base_color: BASE_COLORS[0]}))
      }
    }

    setColors(colors)
  }, [])


  //  GENERAL FUNCTIONS

  /**
   * Gets all the chart keys
   */
  function getChartKeys() {

    if (data.length > 0) {
      return Object.keys(data[0]).filter(key => key !== 'date' && key !== 'portfolio' && key !== 'uid');
    }

    return [];
  }

  /**
   * Determines if the chart should show more than 1 Account
   */
  function hasMultipleAccounts() {

    if (data.length > 0) {
      return getChartKeys().length > 1;
    }

    return false;
  }


  //  COMPONENTS

  /**
   * Renders a custom legend for the chart
   *
   * @param props payload
   */
  const legend = (props: any) => {
    const {payload} = props;

    return (
      <div className={'flex flex-row items-center justify-end gap-4 text-sm '}>
        {
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
  const tooltip = ({active, payload, label}: TooltipProps<ValueType, NameType>) => {

    if (active && (payload?.length ?? -1 > 0)) {

      const date = payload?.[0].payload.date ?? ''
      return (
        <BaseCard
          title={moment(date).format(DateTime.ISOMonthYearFormat)}
          cardContent={
            <div className={'flex flex-col items-center'}>
              {
                payload?.map((item: any, itx: number) => {
                  return (
                    <div key={itx} className={"flex flex-row items-center w-[250px] gap-6"} style={{color: item.color}}>
                      <div className={'justify-start'}>
                        {item.dataKey}
                      </div>
                      <div className={'grow justify-end text-right'}>
                        ${formatNumberForDisplay(item.value)}
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
          <ComposedChart data={chartData}>
            <defs>
              <linearGradient id="color" x1="0" y1="0" x2="0" y2="1">
                <stop offset="5%" stopColor={`${Css.ColorPrimary}`} stopOpacity={0.8}/>
                <stop offset="95%" stopColor={`${Css.ColorPrimary}`} stopOpacity={0}/>
              </linearGradient>
            </defs>
            <Legend content={legend} verticalAlign={'top'} height={40}/>
            {hasMultipleAccounts() ? <Legend content={legend} verticalAlign={'top'} height={40}/> : null}
            <Tooltip content={tooltip}/>
            {
              hasMultipleAccounts() ?
                getChartKeys().map((item: string, itx: number) => {
                  return (
                    <Line key={itx} type="monotone" dot={false} dataKey={item} strokeWidth={3} stroke={colors[itx]}/>
                  )
                }) : null
            }
            <Area type="monotone" dot={false} dataKey="portfolio" stackId="1" stroke={`${Css.ColorPrimary}`}
                  strokeWidth={4} fill="url(#color)"/>
          </ComposedChart>
        </ResponsiveContainer>
      </div>
    </div>
  )
}