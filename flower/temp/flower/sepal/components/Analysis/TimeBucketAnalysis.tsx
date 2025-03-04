'use client'

import AnalysisBarChart from "@/components/Chart/Analysis/AnalysisBarChart";
import React, {useEffect, useState} from "react";
import {getTimeBucketsAnalysis} from "@/lib/functions/analysis-functions";
import {AnalysisResult, FilterSelector} from "@/types/apiTypes";
import {Loader2} from "lucide-react";

/**
 * Renders the time bucket analysis content with chart
 *
 * @param accountNumber account number
 * @param filter filter
 * @param opened trades opened or closed
 * @author Stephen Prizio
 * @version 0.0.2
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

  useEffect(() => {
    getAccTimeBucketsAnalysis()
  }, [filter, accountNumber]);

  const [isLoading, setIsLoading] = useState(false)
  const [data, setData] = useState<Array<AnalysisResult>>([])


  //  GENERAL FUNCTIONS

  /**
   * Fetches the analysis data
   */
  async function getAccTimeBucketsAnalysis() {

    setIsLoading(true)

    const data = await getTimeBucketsAnalysis(accountNumber, filter, opened)
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