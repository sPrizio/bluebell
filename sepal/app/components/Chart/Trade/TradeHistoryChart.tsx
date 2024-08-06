import React from "react";
import styles from './TradeHistoryChart.module.scss'
import {Area, AreaChart, CartesianGrid, ResponsiveContainer} from "recharts";
import {CoreConstants} from "@/app/constants";

function TradeHistoryChart() {

  const baseClass = "trade-history-chart"

  const data = [
    {
      "tradeCount": 0,
      "points": 0,
      "profit": 0,
      "cumulativePoints": 0,
      "cumulativeProfit": 0
    },
    {
      "tradeCount": 1,
      "points": 8.12,
      "profit": 47.36,
      "cumulativePoints": 8.12,
      "cumulativeProfit": 47.36
    },
    {
      "tradeCount": 2,
      "points": -25.36,
      "profit": -98.56,
      "cumulativePoints": -17.24,
      "cumulativeProfit": 47.36
    },
    {
      "tradeCount": 3,
      "points": 10.45,
      "profit": 56.89,
      "cumulativePoints": -6.79,
      "cumulativeProfit": 47.36
    },
    {
      "tradeCount": 4,
      "points": 5.9,
      "profit": 28.52,
      "cumulativePoints": -0.89,
      "cumulativeProfit": 47.36
    },
    {
      "tradeCount": 5,
      "points": 16.98,
      "profit": 104.78,
      "cumulativePoints": 16.09,
      "cumulativeProfit": 47.36
    },
    {
      "tradeCount": 6,
      "points": -3.2,
      "profit": -18.33,
      "cumulativePoints": 12.89,
      "cumulativeProfit": 47.36
    },
    {
      "tradeCount": 7,
      "points": 9.63,
      "profit": 61.89,
      "cumulativePoints": 22.52,
      "cumulativeProfit": 47.36
    }
  ]


  //  GENERAL FUNCTIONS

  /**
   * Utility function to compute the dynamic gradient depending on the values being below or above 0
   */
  function computeGradientOffset() {
    const tempPoints = data
    const min = Math.min(...tempPoints.map(i => i.cumulativePoints))
    const max = Math.max(...tempPoints.map(i => i.cumulativePoints))
    const ratio = Math.round((max / (Math.abs(min) + max)) * 100.0)

    return ratio + '%'
  }


  //  RENDER

  return (
    <div className={styles[baseClass]}>
      <ResponsiveContainer width={'100%'} height={200}>
        <AreaChart data={data}>
          <defs>
            <linearGradient id={"split_"} x1="0" y1="0" x2="0" y2="1">
              <stop offset="0%" stopColor={CoreConstants.CssConstants.ColorSuccess} stopOpacity={0.5}/>
              <stop offset={computeGradientOffset()} stopColor={CoreConstants.CssConstants.White} stopOpacity={0.5}/>
              <stop offset="100%" stopColor={CoreConstants.CssConstants.ColorDanger} stopOpacity={0.5}/>
            </linearGradient>
          </defs>

          <CartesianGrid vertical={false} horizontal={false} />
          {/*<ReferenceLine
            y={0}
            stroke="#DDE1E8"
          />*/}
          <Area
            type="monotone"
            dataKey="cumulativePoints"
            strokeWidth={2}
            stroke={CoreConstants.CssConstants.ColorPrimary}
            fillOpacity={1}
            fill="url(#split_)"
          />
        </AreaChart>
      </ResponsiveContainer>
    </div>
  )
}

export default TradeHistoryChart;