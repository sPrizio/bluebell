import {
  formatNumberForDisplay,
  formatTimeElapsed,
} from "@/lib/functions/util-functions";
import { TradeInsightsType } from "@/types/apiTypes";

/**
 * Renders the trade's insights
 *
 * @param insights trade insights
 * @author Stephen Prizio
 * @version 0.2.4
 */
export default function TradeInsights({
  insights,
}: Readonly<{ insights: TradeInsightsType | null | undefined }>) {
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
        className={
          "[&:not(:nth-child(n+4))]:border-b [&:not(:nth-child(n+4))]:border-grey-200 max-xl:[&:not(:nth-child(n+5))]:border-b max-xl:[&:not(:nth-child(n+5))]:border-grey-200 max-md:[&:not(:nth-child(n+6))]:border-b max-md:[&:not(:nth-child(n+6))]:border-grey-200"
        }
      >
        <div className={"grid grid-cols-2 items-center"}>
          <div className={"flex justify-end text-left p-3"}>{title}</div>
          <div className={"text-right text-sm text-primary font-semibold"}>
            {val}
            {percent !== 0 && (
              <span className={"ml-2 text-xs"}>
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
    <div
      className={"grid grid-cols-1 lg:grid-cols-2 xl:grid-cols-3 gap-x-12 py-4"}
    >
      {cell("Day of Week", insights?.dayOfWeek ?? "", 0)}
      {cell(
        "Risk",
        formatNumberForDisplay(insights?.risk ?? 0) + " pts",
        insights?.riskEquityPercentage ?? 0,
      )}
      {cell("R:R", formatNumberForDisplay(insights?.rrr ?? 0), 0)}
      {cell("Duration", formatTimeElapsed(insights?.duration ?? 0), 0)}
      {cell(
        "Reward",
        formatNumberForDisplay(insights?.reward ?? 0) + " pts",
        insights?.rewardEquityPercentage ?? 0,
      )}
      {(insights?.drawdown ?? 0) === 0 && cell("Drawdown", "N/A", 0)}
      {(insights?.drawdown ?? 0) !== 0 &&
        cell(
          "Drawdown",
          "$ " + formatNumberForDisplay(insights?.drawdown ?? 0),
          insights?.drawdownPercentage ?? 0,
        )}
    </div>
  );
}
