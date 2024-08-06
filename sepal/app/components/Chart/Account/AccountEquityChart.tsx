import React from "react";
import styles from './AccountEquityChart.module.scss'
import {Area, AreaChart, CartesianGrid, ResponsiveContainer, Tooltip, XAxis, YAxis} from "recharts";
import {CoreConstants} from "@/app/constants";
import moment from "moment";
import {formatNumberForDisplay} from "@/app/services/data/dataIntegrityService";
import {HiOutlineTrendingDown, HiOutlineTrendingUp} from "react-icons/hi";
import {FaArrowsAltH} from "react-icons/fa";

/**
 * Renders the account equity graph
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
function AccountEquityChart() {

  const baseClass = "account-equity-chart"
  const data = [
    {
      "date": "2024-01-01",
      "balance": 10000.00,
      "delta": 0
    },
    {
      "date": "2024-02-01",
      "balance": 10248.98,
      "delta": 2.49
    },
    {
      "date": "2024-03-01",
      "balance": 10365.32,
      "delta": 1.14
    },
    {
      "date": "2024-04-01",
      "balance": 10194.21,
      "delta": -1.65
    },
    {
      "date": "2024-05-01",
      "balance": 10300.89,
      "delta": 1.05
    },
    {
      "date": "2024-06-01",
      "balance": 10578.41,
      "delta": 2.69
    }
  ]


  //  CONTENT

  /**
   * Custom tooltip for the chart
   *
   * @param active is it active
   * @param payload data
   * @param label tooltip label (x-axis value)
   */
  const CustomTooltip = ({ active, payload, label }: {active?: boolean, payload?: any, label?: string}) => {
    if (active && payload && payload.length) {
      const balance = payload[0].payload.balance
      const delta = payload[0].payload.delta

      let deltaContent
      if (delta === 0) {
        deltaContent =
          <div
            className={styles[`${baseClass}__tooltip-delta`] + ' ' + styles[`${baseClass}__tooltip-delta--neutral`]}>
            <div className={styles[`${baseClass}__tooltip-delta-symbol`]}><FaArrowsAltH /></div>
            {formatNumberForDisplay(delta)}%
          </div>
      } else if (delta > 0) {
        deltaContent =
          <div
            className={styles[`${baseClass}__tooltip-delta`] + ' ' + styles[`${baseClass}__tooltip-delta--positive`]}>
            <div className={styles[`${baseClass}__tooltip-delta-symbol`]}><HiOutlineTrendingUp/></div>
            {formatNumberForDisplay(delta)}%
          </div>
      } else {
        deltaContent =
          <div
            className={styles[`${baseClass}__tooltip-delta`] + ' ' + styles[`${baseClass}__tooltip-delta--negative`]}>
            <div className={styles[`${baseClass}__tooltip-delta-symbol`]}><HiOutlineTrendingDown/></div>
            {formatNumberForDisplay(delta)}%
          </div>
      }

      return (
        <div className={styles[`${baseClass}__tooltip`]}>
          <div className={styles[`${baseClass}__tooltip-label`]}>
            {moment(label, CoreConstants.DateTime.ISODateFormat).format(CoreConstants.DateTime.ISOMonthYearFormat)}
          </div>
          <div className={styles[`${baseClass}__tooltip-content`]}>
            ${formatNumberForDisplay(balance)}
            {deltaContent}
          </div>
        </div>
      );
    }

    return null;
  };


  //  GENERAL FUNCTIONS

  /**
   * Formats the x-axis data points
   *
   * @param val axis value
   */
  function computeXAxis(val: string) {
    return moment(val, CoreConstants.DateTime.ISODateFormat).format(CoreConstants.DateTime.ISOShortMonthFormat)
  }

  /**
   * Computes the bounds of the y-axis
   */
  function computeYAxisBounds() {
    const temp = data
    const min = Math.min(...temp.map(i => i.balance))
    const max = Math.max(...temp.map(i => i.balance))

    return [Math.floor(min * 0.995), Math.ceil(max * 1.001)]
  }


  //  RENDER

  return (
    <div className={styles[baseClass]}>
      <ResponsiveContainer width={'100%'} height={175}>
        <AreaChart data={data}>
          <defs>
            <linearGradient id="base" x1="0" y1="0" x2="0" y2="1">
              <stop offset="5%" stopColor={CoreConstants.CssConstants.ColorPrimary} stopOpacity={0.5}/>
              <stop offset="95%" stopColor={CoreConstants.CssConstants.ColorPrimary} stopOpacity={0}/>
            </linearGradient>
          </defs>
          <CartesianGrid
            vertical={false}
            horizontal={false}
          />
          <XAxis
            type={"category"}
            dataKey={"date"}
            tickFormatter={computeXAxis}
            ticks={[]}
            interval={0}
            includeHidden={true}
            tickLine={false}
            padding={{left: 12, right: 12}}
            axisLine={{ stroke: '#EAF0F4' }}
            tick={{ fill: CoreConstants.CssConstants.ColorDisplayGrey }}
          />
          <Area
            type="monotone"
            dataKey="balance"
            strokeWidth={3}
            stroke={CoreConstants.CssConstants.ColorPrimary}
            fill={"url(#base)"}
          />
          <YAxis
            domain={computeYAxisBounds()}
            hide={true}
            allowDecimals={false}
            includeHidden={true}
            interval={"equidistantPreserveStart"}
          />
          <Tooltip content={<CustomTooltip />} />
        </AreaChart>
      </ResponsiveContainer>
    </div>
  )
}

export default AccountEquityChart;