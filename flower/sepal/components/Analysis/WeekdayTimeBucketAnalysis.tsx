import { FilterSelector } from "@/types/apiTypes";
import React from "react";
import AnalysisBarChart from "@/components/Chart/Analysis/AnalysisBarChart";
import { useWeekdaysTimeBucketsAnalysisQuery } from "@/lib/hooks/query/queries";
import { logErrors } from "@/lib/functions/util-functions";
import SepalLoader from "@/components/Svg/SepalLoader";

/**
 * Renders the time bucket weekday analysis content with chart
 *
 * @param accountNumber account number
 * @param weekday weekday
 * @param filter filter
 * @author Stephen Prizio
 * @version 0.2.4
 */
export default function WeekdayTimeBucketAnalysis({
  weekday,
  accountNumber,
  filter = "PROFIT",
}: Readonly<{
  weekday: string;
  accountNumber: number;
  filter: FilterSelector;
}>) {
  const { data, isLoading, isError, error } =
    useWeekdaysTimeBucketsAnalysisQuery(accountNumber, weekday, filter);

  //  RENDER

  if (isError) {
    logErrors(error);
    return (
      <div className="text-center text-slate-500 my-4 text-sm">
        No data to display.
      </div>
    );
  }

  return (
    <div className={"pt-6 pb-4"}>
      {isLoading ? (
        <div className={"h-[100px] flex items-center justify-center"}>
          <div className={"grid grid-cols-1 justify-items-center gap-8"}>
            <div>
              <SepalLoader />
            </div>
          </div>
        </div>
      ) : (
        <AnalysisBarChart data={data ?? []} filter={filter} />
      )}
    </div>
  );
}
