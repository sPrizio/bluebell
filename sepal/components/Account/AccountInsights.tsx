import React from "react";
import {formatNumberForDisplay} from "@/lib/functions";

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
   */
  const cell = (title: string, val: string) => {
    return (
      <div
        className={'[&:not(:nth-child(n+4))]:border-b [&:not(:nth-child(n+4))]:border-grey-200 max-xl:[&:not(:nth-child(n+5))]:border-b max-xl:[&:not(:nth-child(n+5))]:border-grey-200 max-md:[&:not(:nth-child(n+6))]:border-b max-md:[&:not(:nth-child(n+6))]:border-grey-200'}>
        <div className={'grid grid-cols-2 items-center'}>
          <div className={'text-right p-3'}>{title}</div>
          <div className={'flex items-center max-lg:justify-start justify-end p-3'}>{val}</div>
        </div>
      </div>
    )
  }


  //  RENDER

  return (
    <div className={'grid grid-cols-1 lg:grid-cols-2 xl:grid-cols-3 gap-x-12'}>
      {cell('Trading Days', insights.tradingDays.toString())}
      {cell('Max Daily Loss', '$ ' + formatNumberForDisplay(insights.maxDailyLoss))}
      {cell('Max Daily Profit', '$ ' + formatNumberForDisplay(insights.maxDailyProfit))}
      {cell('Trades', insights.trades.toString())}
      {cell('Max Loss', '$ ' + formatNumberForDisplay(insights.maxTotalLoss))}
      {cell('Max Profit', '$ ' + formatNumberForDisplay(insights.maxProfit))}
    </div>
  )
}