import {FilterSelector} from "@/types/apiTypes";
import React from "react";
import {Loader2} from "lucide-react";
import AnalysisBarChart from "@/components/Chart/Analysis/AnalysisBarChart";
import {useWeekdaysTimeBucketsAnalysisQuery} from "@/lib/hooks/query/queries";
import {logErrors} from "@/lib/functions/util-functions";

/**
 * Renders the time bucket weekday analysis content with chart
 *
 * @param accountNumber account number
 * @param weekday weekday
 * @param filter filter
 * @author Stephen Prizio
 * @version 0.2.0
 */
export default function WeekdayTimeBucketAnalysis(
  {
    weekday,
    accountNumber,
    filter = 'PROFIT',
  }
  : Readonly<{
    weekday: string,
    accountNumber: number,
    filter: FilterSelector
  }>
) {

  const {
    data: weekdaysTimeBucketsAnalysisData,
    isLoading: isWeekdaysTimeBucketsAnalysisLoading,
    isError: isWeekdaysTimeBucketsAnalysisError,
  } = useWeekdaysTimeBucketsAnalysisQuery(accountNumber, weekday, filter)


  //  RENDER

  if (isWeekdaysTimeBucketsAnalysisError) {
    logErrors(isWeekdaysTimeBucketsAnalysisError)
    return <p>Data could not be displayed.</p>
  }

  return (
    <div className={''}>
      {
        isWeekdaysTimeBucketsAnalysisLoading ?
          <div className={'h-[100px] flex items-center justify-center'}>
            <div className={'grid grid-cols-1 justify-items-center gap-8'}>
              <div>
                <Loader2 className="animate-spin text-secondary" size={50}/>
              </div>
            </div>
          </div>
          :
          <AnalysisBarChart data={weekdaysTimeBucketsAnalysisData ?? []} filter={filter} />
      }
    </div>
  )
}