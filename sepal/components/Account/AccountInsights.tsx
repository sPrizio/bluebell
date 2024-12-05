import React from "react";
import {formatNumberForDisplay} from "@/lib/functions/util-functions";

/**
 * Renders an Account's insights
 *
 * @param insights Account insights
 * @author Stephen Prizio
 * @version 0.0.2
 */
export default function AccountInsights(
  {
    insights,
  }
    : Readonly<{
    insights: AccountInsights
  }>
) {

  /**
   * Renders a table cell
   *
   * @param title title
   * @param val cell value
   * @param percent percentage value
   */
  const cell = (title: string, val: string, percent: number) => {
    return (
      <div
        className={'[&:not(:nth-child(n+4))]:border-b [&:not(:nth-child(n+4))]:border-grey-200 max-xl:[&:not(:nth-child(n+5))]:border-b max-xl:[&:not(:nth-child(n+5))]:border-grey-200 max-md:[&:not(:nth-child(n+6))]:border-b max-md:[&:not(:nth-child(n+6))]:border-grey-200'}>
        <div className={'grid grid-cols-2 items-center'}>
          <div className={'text-right p-3'}>{title}</div>
          <div className={'flex items-center max-lg:justify-start justify-end p-3'}>
            {val}{percent > 0 && <span className={'ml-2 text-sm'}>({formatNumberForDisplay(percent)}%)</span> }
          </div>
        </div>
      </div>
    )
  }


  //  RENDER

  //  TODO: add percentages

  return (
    <div className={'grid grid-cols-1 lg:grid-cols-2 xl:grid-cols-3 gap-x-12'}>
      {cell('Trading Days', insights.tradingDays.toString(), 0)}
      {cell('Max Daily Loss', '$ ' + formatNumberForDisplay(insights.maxDailyLoss), 12.0)}
      {cell('Max Daily Profit', '$ ' + formatNumberForDisplay(insights.maxDailyProfit), 12.0)}
      {cell('Trades', insights.trades.toString(), 0)}
      {cell('Max Loss', '$ ' + formatNumberForDisplay(insights.maxTotalLoss), 15.35)}
      {cell('Max Profit', '$ ' + formatNumberForDisplay(insights.maxProfit), 17.45)}
    </div>
  )
}