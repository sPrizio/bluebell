import { FilterSelector, TradeDurationFilterSelector } from "@/types/apiTypes";
import React from "react";
import AnalysisBarChart from "@/components/Chart/Analysis/AnalysisBarChart";
import { useTradeDurationAnalysisQuery } from "@/lib/hooks/query/queries";
import { logErrors } from "@/lib/functions/util-functions";
import SepalLoader from "@/components/Svg/SepalLoader";

/**
 * Renders the trade duration analysis chart
 *
 * @param accountNumber account number
 * @param filter analysis filter
 * @param tdFilter trade duration filter
 * @author Stephen Prizio
 * @version 0.2.2
 */
export default function TradeDurationAnalysis({
  accountNumber,
  filter = "PROFIT",
  tdFilter = "ALL",
}: Readonly<{
  accountNumber: number;
  filter: FilterSelector;
  tdFilter: TradeDurationFilterSelector;
}>) {
  const {
    data: tradeDurationAnalysisData,
    isLoading: isTradeDurationAnalysisLoading,
    isError: isTradeDurationAnalysisError,
  } = useTradeDurationAnalysisQuery(accountNumber, tdFilter, filter);

  //  RENDER

  if (isTradeDurationAnalysisError) {
    logErrors(isTradeDurationAnalysisError);
    return <div className={"text-center"}>Data could not be displayed.</div>;
  }

  return (
    <div className={""}>
      {isTradeDurationAnalysisLoading ? (
        <div className={"h-[100px] flex items-center justify-center"}>
          <div className={"grid grid-cols-1 justify-items-center gap-8"}>
            <div>
              <SepalLoader />
            </div>
          </div>
        </div>
      ) : (
        <AnalysisBarChart
          data={tradeDurationAnalysisData ?? []}
          filter={filter}
        />
      )}
    </div>
  );
}
