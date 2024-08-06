import React from "react";
import styles from './AccountActivity.module.scss'
import {Bar, BarChart, CartesianGrid, Legend, ResponsiveContainer, Tooltip, XAxis, YAxis} from "recharts";
import {CoreConstants} from "@/app/constants";
import {formatNumberForDisplay} from "@/app/services/data/dataIntegrityService";

/**
 * Component that shows an account's activity
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function AccountActivity() {

  const baseClass = "account-activity";

  const data = [
    {
      "name": "P&L",
      "previous": 4000,
      "current": 2400
    },
    {
      "name": "Points",
      "previous": 3000,
      "current": 1398
    },
    {
      "name": "Deposits",
      "previous": 2000,
      "current": 5800
    },
    {
      "name": "Withdrawals",
      "previous": 2780,
      "current": 3908
    },
  ]


  //  GENERAL FUNCTIONS

  /**
   * Returns the border radius for the bar display
   */
  function computeBorderRadius() : number | [number, number, number, number] {
    return [
      CoreConstants.CssConstants.BarRadius,
      CoreConstants.CssConstants.BarRadius,
      CoreConstants.CssConstants.BarRadius,
      CoreConstants.CssConstants.BarRadius
    ]
  }


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
      const previous = payload[0].payload.previous
      const current = payload[0].payload.current

      return (
        <div className={styles[`${baseClass}__tooltip`]}>
          <div className={styles[`${baseClass}__tooltip-label`]}>
            {payload[0].payload.name}
          </div>
          <div className={styles[`${baseClass}__tooltip-content`] + ' ' + styles[`${baseClass}__tooltip-content--current`]}>
            Current: ${formatNumberForDisplay(current)}
          </div>
          <div className={styles[`${baseClass}__tooltip-content`] + ' ' + styles[`${baseClass}__tooltip-content--previous`]}>
            Previous: ${formatNumberForDisplay(previous)}
          </div>
        </div>
      );
    }

    return null;
  };


  //  RENDER

  return (
    <div className={styles[baseClass]}>
      <ResponsiveContainer width={'100%'} height={250}>
        <BarChart data={data} barSize={25}>
          <CartesianGrid vertical={false} stroke={CoreConstants.CssConstants.ColorLightGrey} strokeWidth={2} />
          <XAxis
            dataKey="name"
            type={"category"}
            ticks={[]}
            interval={0}
            includeHidden={true}
            tickLine={false}
            padding={{left: 12, right: 12}}
            axisLine={{ stroke: '#EAF0F4', opacity: 0 }}
            tick={{ fill: CoreConstants.CssConstants.ColorDisplayGrey }}
          />
          <YAxis
            includeHidden={true}
            tickLine={false}
            axisLine={{ stroke: '#EAF0F4', opacity: 0 }}
            tick={{ fill: CoreConstants.CssConstants.ColorDisplayGrey }}
          />
          <Tooltip content={<CustomTooltip />} />
          <Legend
            verticalAlign="bottom"
            height={20}
            align={"center"}
            iconType={'circle'}
          />
          <Bar dataKey="previous" fill={CoreConstants.CssConstants.ColorDisplayGrey} radius={computeBorderRadius()} />
          <Bar dataKey="current" fill={CoreConstants.CssConstants.ColorPrimary} radius={computeBorderRadius()} />
        </BarChart>
      </ResponsiveContainer>
    </div>
  )
}