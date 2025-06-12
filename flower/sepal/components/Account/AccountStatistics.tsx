import React from "react";
import {
  formatNumberForDisplay,
  formatTimeElapsed,
} from "@/lib/functions/util-functions";
import { AccountStatisticsType } from "@/types/apiTypes";

/**
 * Renders the account's statistics
 *
 * @param statistics account statistics
 * @author Stephen Prizio
 * @version 0.2.4
 */
export default function AccountStatistics({
  statistics,
}: Readonly<{
  statistics: AccountStatisticsType;
}>) {
  /**
   * Renders a table cell
   *
   * @param title title
   * @param val cell value
   */
  const cell = (title: string, val: string) => {
    return (
      <div className={""}>
        <div className={"text-sm font-bold text-primary"}>{title}</div>
        <div>{val}</div>
      </div>
    );
  };

  //  RENDER

  return (
    <div className={"mt-2 pb-3 grid grid-cols-2 gap-1"}>
      <div className={"bg-primary bg-opacity-5 py-3 px-2"}>
        {cell("Balance", "$ " + formatNumberForDisplay(statistics.balance))}
      </div>
      <div className={"py-3 px-2"}>
        {cell("Trades", statistics.numberOfTrades.toString())}
      </div>
      <div className={"bg-primary bg-opacity-5 py-3 px-2"}>
        {cell(
          "Average Profit",
          "$ " + formatNumberForDisplay(statistics.averageProfit),
        )}
      </div>
      <div className={"py-3 px-2"}>
        {cell("Profit Factor", formatNumberForDisplay(statistics.profitFactor))}
      </div>
      <div className={"bg-primary bg-opacity-5 py-3 px-2"}>
        {cell(
          "Average Loss",
          "$ " + formatNumberForDisplay(statistics.averageLoss),
        )}
      </div>
      <div className={"py-3 px-2"}>
        {cell("RRR", formatNumberForDisplay(statistics.rrr))}
      </div>
      <div className={"bg-primary bg-opacity-5 py-3 px-2"}>
        {cell(
          "Expectancy",
          "$ " + formatNumberForDisplay(statistics.expectancy),
        )}
      </div>
      <div className={"py-3 px-2"}>
        {cell("Win Rate", statistics.winPercentage + "%")}
      </div>
      <div className={"bg-primary bg-opacity-5 py-3 px-2"}>
        {cell("Sharpe Ratio", formatNumberForDisplay(statistics.sharpeRatio))}
      </div>
      <div className={"py-3 px-2"}>
        {cell("Retention", statistics.retention + "%")}
      </div>
      <div className={"bg-primary bg-opacity-5 py-3 px-2"}>
        {cell(
          "Assumed Drawdown",
          "$ " + formatNumberForDisplay(statistics.assumedDrawdown),
        )}
      </div>
      <div className={"py-3 px-2"}>
        {cell(
          "Average Trade Duration",
          formatTimeElapsed(statistics.tradeDuration),
        )}
      </div>
      <div className={"bg-primary bg-opacity-5 py-3 px-2"}>
        {cell(
          "Average Win Duration",
          formatTimeElapsed(statistics.winDuration),
        )}
      </div>
      <div className={"py-3 px-2"}>
        {cell(
          "Average Loss Duration",
          formatTimeElapsed(statistics.lossDuration),
        )}
      </div>
    </div>
  );
}
