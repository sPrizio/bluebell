import React from "react";
import {formatNumberForDisplay} from "@/lib/functions/util-functions";

/**
 * Renders the Account's statistics
 *
 * @param statistics Account statistics
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function AccountStatistics(
  {
    statistics,
  }
    : Readonly<{
    statistics: AccountStatistics
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
      <div className={''}>
        <div className={'text-sm font-bold text-primary'}>{title}</div>
        <div>{val}</div>
      </div>
    )
  }


  //  RENDER

  return (
    <div className={'mt-2 pb-3 grid grid-cols-2 gap-1'}>
      <div className={'bg-primary bg-opacity-5 py-3 px-2'}>{cell('Balance', '$ ' + formatNumberForDisplay(statistics.balance))}</div>
      <div className={'py-3 px-2'}>{cell('Trades', statistics.numberOfTrades.toString())}</div>
      <div className={'bg-primary bg-opacity-5 py-3 px-2'}>{cell('Average Profit', '$ ' + formatNumberForDisplay(statistics.averageProfit))}</div>
      <div className={'py-3 px-2'}>{cell('Profit Factor', formatNumberForDisplay(statistics.profitFactor))}</div>
      <div className={'bg-primary bg-opacity-5 py-3 px-2'}>{cell('Average Loss', '$ ' + formatNumberForDisplay(statistics.averageLoss))}</div>
      <div className={'py-3 px-2'}>{cell('RRR', formatNumberForDisplay(statistics.rrr))}</div>
      <div className={'bg-primary bg-opacity-5 py-3 px-2'}>{cell('Expectancy', '$ ' + formatNumberForDisplay(statistics.expectancy))}</div>
      <div className={'py-3 px-2'}>{cell('Win Rate', statistics.winPercentage + '%')}</div>
      <div className={'bg-primary bg-opacity-5 py-3 px-2'}>{cell('Sharpe Ratio', formatNumberForDisplay(statistics.sharpeRatio))}</div>
      <div className={'py-3 px-2'}>{cell('Retention', statistics.retention + '%')}</div>
    </div>
  )
}