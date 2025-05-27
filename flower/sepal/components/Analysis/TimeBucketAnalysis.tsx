"use client";

import AnalysisBarChart from "@/components/Chart/Analysis/AnalysisBarChart";
import React from "react";
import { FilterSelector } from "@/types/apiTypes";
import { logErrors } from "@/lib/functions/util-functions";
import { useTimeBucketsAnalysisQuery } from "@/lib/hooks/query/queries";
import SepalLoader from "@/components/Svg/SepalLoader";

/**
 * Renders the time bucket analysis content with chart
 *
 * @param accountNumber account number
 * @param filter filter
 * @param opened trades opened or closed
 * @author Stephen Prizio
 * @version 0.2.2
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
    return <p>Data could not be displayed.</p>;
  }

  return (
    <div className={""}>
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
