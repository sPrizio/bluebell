import {Area, AreaChart, ReferenceLine, ResponsiveContainer} from "recharts";
import React, {useEffect, useState} from "react";
import {Css} from "@/lib/constants";

interface ChartPoint {
  id: string,
  value: number
}


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
      setChartData(data.map(item => ({id: item.tradeId, value: item.netPoints})))
    } else {
      setChartData(data.map(item => ({id: item.tradeId, value: item.netProfit})))
    }
  }, []);

  useEffect(() => {
    if (showAsPoints) {
      setChartData(data.map(item => ({id: item.tradeId, value: item.netPoints})))
    } else {
      setChartData(data.map(item => ({id: item.tradeId, value: item.netProfit})))
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
            <linearGradient id={"split_"} x1="0" y1="0" x2="0" y2="1">
              <stop offset="0%" stopColor={`${Css.ColorGreen}`} stopOpacity={0.5}/>
              <stop offset={computeGradientOffset()} stopColor={'#FFFFFF'} stopOpacity={0.5}/>
              <stop offset="100%" stopColor={`${Css.ColorRed}`} stopOpacity={0.5}/>
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
            strokeWidth={2}
            stroke={`${Css.ColorPrimary}`}
            fillOpacity={1}
            fill="url(#split_)"
          />
        </AreaChart>
      </ResponsiveContainer>
    </div>
  )
}