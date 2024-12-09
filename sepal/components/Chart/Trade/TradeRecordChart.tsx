import {Area, AreaChart, ReferenceLine, ResponsiveContainer} from "recharts";
import React, {useEffect, useState} from "react";
import {Css} from "@/lib/constants";

interface ChartPoint {
  id: string | number,
  value: number
}

/**
 * Renders a chart showing a trade records equity over a count of trades
 *
 * @param data array of trade record equity points
 * @param showAsPoints show points instead of balance
 * @author Stephen Prizio
 * @version 0.0.2
 */
export default function TradeRecordChart(
  {
    data,
    showAsPoints = false
  }
    : Readonly<{
    data: Array<TradeRecordEquityPoint>,
    showAsPoints?: boolean
  }>
) {

  const [chartData, setChartData] = useState<Array<ChartPoint>>([])

  useEffect(() => {
    if (showAsPoints) {
      setChartData(data?.map(item => ({id: item.count, value: item.cumPoints})) ?? [])
    } else {
      setChartData(data?.map(item => ({id: item.count, value: item.cumAmount})) ?? [])
    }
  }, []);

  useEffect(() => {
    if (showAsPoints) {
      setChartData(data?.map(item => ({id: item.count, value: item.cumPoints})) ?? [])
    } else {
      setChartData(data?.map(item => ({id: item.count, value: item.cumAmount})) ?? [])
    }
  }, [showAsPoints]);


  //  GENERAL FUNCTIONS

  /**
   * Utility function to compute the dynamic gradient depending on the values being below or above 0
   */
  function computeGradientOffset() {
    const tempPoints = chartData
    const min = Math.min(...tempPoints.map(i => i.value))
    const max = Math.max(...tempPoints.map(i => i.value))
    const ratio = Math.round((max / (Math.abs(min) + max)) * 100.0)

    return ratio + '%'
  }


  //  RENDER

  return (
    <div className={'w-full'}>
      <ResponsiveContainer width={'100%'} height={300}>
        <AreaChart data={chartData}>
          <defs>
            <linearGradient id="color" x1="0" y1="0" x2="0" y2="1">
              <stop offset="5%" stopColor={`${Css.ColorPrimary}`} stopOpacity={0.65}/>
              <stop offset="95%" stopColor={`${Css.ColorPrimary}`} stopOpacity={0}/>
            </linearGradient>
          </defs>
          <ReferenceLine
            y={0}
            strokeWidth={2}
            stroke="#F3F3F3"
          />
          <Area
            type="monotone"
            dataKey={'value'}
            strokeWidth={4}
            stroke={`${Css.ColorPrimary}`}
            fillOpacity={1}
            fill="url(#color)"
          />
        </AreaChart>
      </ResponsiveContainer>
    </div>
  )
}