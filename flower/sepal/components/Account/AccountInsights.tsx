import React from "react";
import { formatNumberForDisplay } from "@/lib/functions/util-functions";
import {
  Tooltip,
  TooltipContent,
  TooltipProvider,
  TooltipTrigger,
} from "@/components/ui/tooltip";
import { IconHelpSquareRounded } from "@tabler/icons-react";
import { AccountInsightsType } from "@/types/apiTypes";
import { resolveIcon } from "@/lib/functions/util-component-functions";
import { Icons } from "@/lib/enums";

/**
 * Renders an Account's insights
 *
 * @param insights Account insights
 * @author Stephen Prizio
 * @version 0.0.2
 */
export default function AccountInsights({
  insights,
}: Readonly<{
  insights: AccountInsightsType;
}>) {
  /**
   * Renders a table cell
   *
   * @param title title
   * @param val cell value
   * @param percent percentage value
   * @param hasToolTip show a tool tip
   */
  const cell = (
    title: string,
    val: string,
    percent: number,
    hasToolTip: boolean,
  ) => {
    return (
      <div
        className={
          "[&:not(:nth-child(n+4))]:border-b [&:not(:nth-child(n+4))]:border-grey-200 max-xl:[&:not(:nth-child(n+5))]:border-b max-xl:[&:not(:nth-child(n+5))]:border-grey-200 max-md:[&:not(:nth-child(n+6))]:border-b max-md:[&:not(:nth-child(n+6))]:border-grey-200"
        }
      >
        <div className={"grid grid-cols-2 items-center"}>
          <div className={"flex justify-end text-left p-3"}>
            {hasToolTip && (
              <TooltipProvider>
                <Tooltip>
                  <TooltipTrigger className={"flex justify-end"}>
                    <span className={"inline-block"}>
                      {resolveIcon(Icons.HelpSquareRounded, "text-primary")}
                    </span>
                    &nbsp;&nbsp;{title}
                  </TooltipTrigger>
                  <TooltipContent
                    className={
                      "bg-white border-1 border-gray-200 text-slate-700 shadow-sm"
                    }
                  >
                    <p>
                      The drawdown is calculated using closed trades. Active
                      trades are not considered <br />
                      during the calculations.
                    </p>
                  </TooltipContent>
                </Tooltip>
              </TooltipProvider>
            )}
            {!hasToolTip && title}
          </div>
          <div
            className={"flex items-center max-lg:justify-start justify-end p-3"}
          >
            {val}
            {percent !== 0 && (
              <span className={"ml-2 text-sm"}>
                ({formatNumberForDisplay(percent)}%)
              </span>
            )}
          </div>
        </div>
      </div>
    );
  };

  //  RENDER

  return (
    <div className={"grid grid-cols-1 lg:grid-cols-2 xl:grid-cols-3 gap-x-12"}>
      {cell("Trading Days", insights.tradingDays.toString(), 0, false)}
      {cell(
        "Biggest Loss",
        "$ " + formatNumberForDisplay(insights.biggestLoss),
        insights.biggestLossDelta,
        false,
      )}
      {cell(
        "Largest Gain",
        "$ " + formatNumberForDisplay(insights.largestGain),
        insights.largestGainDelta,
        false,
      )}
      {cell(
        "Current P & L",
        "$ " + formatNumberForDisplay(insights.currentPL),
        insights.currentPLDelta,
        false,
      )}
      {cell(
        "Drawdown",
        "$ " + formatNumberForDisplay(insights.drawdown),
        insights.drawdownDelta,
        true,
      )}
      {cell(
        "Max Profit",
        "$ " + formatNumberForDisplay(insights.maxProfit),
        insights.maxProfitDelta,
        false,
      )}
    </div>
  );
}
