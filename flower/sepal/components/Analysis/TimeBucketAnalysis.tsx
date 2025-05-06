'use client'

import AnalysisBarChart from "@/components/Chart/Analysis/AnalysisBarChart";
import React from "react";
import {FilterSelector} from "@/types/apiTypes";
import {Loader2} from "lucide-react";
import {logErrors} from "@/lib/functions/util-functions";
import {useTimeBucketsAnalysisQuery} from "@/lib/hooks/query/queries";

/**
 * Renders the time bucket analysis content with chart
 *
 * @param accountNumber account number
 * @param filter filter
 * @param opened trades opened or closed
 * @author Stephen Prizio
 * @version 0.2.0
 */
export default function TimeBucketAnalysis(
  {
    accountNumber,
    filter = 'PROFIT',
    opened = false
  }
  : Readonly<{
    accountNumber: number,
    filter: FilterSelector
    opened?: boolean
  }>
) {

  const {
    data: timeBucketsAnalysisData,
    isLoading: isTimeBucketsAnalysisLoading,
    isError: isTimeBucketsAnalysisError,
  } = useTimeBucketsAnalysisQuery(accountNumber, opened, filter)


  //  RENDER

  if (isTimeBucketsAnalysisError) {
    logErrors(isTimeBucketsAnalysisError)
    return <p>Data could not be displayed.</p>
  }

  return (
    <div className={''}>
      {
        isTimeBucketsAnalysisLoading ?
          <div className={'h-[100px] flex items-center justify-center'}>
            <div className={'grid grid-cols-1 justify-items-center gap-8'}>
              <div>
                <Loader2 className="animate-spin text-secondary" size={50}/>
              </div>
            </div>
          </div>
          :
          <AnalysisBarChart data={timeBucketsAnalysisData ?? []} filter={filter} />
      }
    </div>
  )
}