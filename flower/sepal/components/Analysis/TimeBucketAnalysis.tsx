"use client";

import AnalysisBarChart from "@/components/Chart/Analysis/AnalysisBarChart";
import React from "react";
import { FilterSelector } from "@/types/apiTypes";
import { logErrors } from "@/lib/functions/util-functions";
import { useTimeBucketsAnalysisQuery } from "@/lib/hooks/query/queries";
import SepalLoader from "@/components/Svg/SepalLoader";
import AnalysisChartTooltipCard from "@/components/Card/Analysis/AnalysisChartTooltipCard";

/**
 * Renders the time bucket analysis content with chart
 *
 * @param accountNumber account number
 * @param filter filter
 * @param opened trades opened or closed
 * @author Stephen Prizio
 * @version 0.2.4
 */
export default function TimeBucketAnalysis({
  accountNumber,
  filter = "PROFIT",
  opened = false,
}: Readonly<{
  accountNumber: number;
  filter: FilterSelector;
  opened?: boolean;
}>) {
  const { data, isLoading, isError, error } = useTimeBucketsAnalysisQuery(
    accountNumber,
    opened,
    filter,
  );

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
        <AnalysisBarChart
          data={data ?? []}
          filter={filter}
          tooltip={
            <AnalysisChartTooltipCard filter={filter} headerLabel={"Bucket"} />
          }
        />
      )}
    </div>
  );
}
