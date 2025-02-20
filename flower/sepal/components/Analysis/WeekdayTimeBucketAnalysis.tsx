import {AnalysisResult, FilterSelector} from "@/types/apiTypes";
import React, {useEffect, useState} from "react";
import {getWeekdaysTimeBucketsAnalysis} from "@/lib/functions/analysis-functions";
import {Loader2} from "lucide-react";
import AnalysisBarChart from "@/components/Chart/Analysis/AnalysisBarChart";

/**
 * Renders the time bucket weekday analysis content with chart
 *
 * @param accountNumber account number
 * @param weekday weekday
 * @param filter filter
 * @author Stephen Prizio
 * @version 0.0.2
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

  useEffect(() => {
    getAccWeekdayTimeBucketAnalysis()
  }, [filter, accountNumber, weekday]);

  const [isLoading, setIsLoading] = useState(false)
  const [data, setData] = useState<Array<AnalysisResult>>([])


  //  GENERAL FUNCTIONS

  /**
   * Fetches the analysis data
   */
  async function getAccWeekdayTimeBucketAnalysis() {

    setIsLoading(true)

    const data = await getWeekdaysTimeBucketsAnalysis(accountNumber, weekday, filter)
    setData(data ?? [])

    setIsLoading(false)
  }


  //  RENDER

  return (
    <div className={''}>
      {
        isLoading ?
          <div className={'h-[100px] flex items-center justify-center'}>
            <div className={'grid grid-cols-1 justify-items-center gap-8'}>
              <div>
                <Loader2 className="animate-spin text-secondary" size={50}/>
              </div>
            </div>
          </div>
          :
          <AnalysisBarChart data={data} filter={filter} />
      }
    </div>
  )
}