import { FilterSelector } from "@/types/apiTypes";
import React from "react";
import AnalysisBarChart from "@/components/Chart/Analysis/AnalysisBarChart";
import { useWeekdaysAnalysisQuery } from "@/lib/hooks/query/queries";
import { logErrors } from "@/lib/functions/util-functions";
import SepalLoader from "@/components/Svg/SepalLoader";

/**
 * Renders the weekday analysis content with chart
 *
 * @param accountNumber account number
 * @param filter filter
 * @author Stephen Prizio
 * @version 0.2.2
 */
export default function WeekdayAnalysis({
  accountNumber,
  filter = "PROFIT",
}: Readonly<{
  accountNumber: number;
  filter: FilterSelector;
}>) {
  const {
    data: weekdaysAnalysisData,
    isLoading: isWeekdaysAnalysisLoading,
    isError: isWeekdaysAnalysisError,
  } = useWeekdaysAnalysisQuery(accountNumber, filter);

  //  RENDER

  if (isWeekdaysAnalysisError) {
    logErrors(isWeekdaysAnalysisError);
    return <p>Data could not be displayed.</p>;
  }

  return (
    <div className={""}>
      {isWeekdaysAnalysisLoading ? (
        <div className={"h-[100px] flex items-center justify-center"}>
          <div className={"grid grid-cols-1 justify-items-center gap-8"}>
            <div>
              <SepalLoader />
            </div>
          </div>
        </div>
      ) : (
        <AnalysisBarChart data={weekdaysAnalysisData ?? []} filter={filter} />
      )}
    </div>
  );
}
