import {AnalysisResult, FilterSelector, TradeDurationFilterSelector} from "@/types/apiTypes";
import React, {useEffect, useState} from "react";
import {getTradeDurationAnalysis} from "@/lib/functions/analysis-functions";
import {Loader2} from "lucide-react";
import AnalysisBarChart from "@/components/Chart/Analysis/AnalysisBarChart";

/**
 * Renders the trade duration analysis chart
 *
 * @param accountNumber account number
 * @param filter analysis filter
 * @param tdFilter trade duration filter
 * @author Stephen Prizio
 * @version 0.0.2
 */
export default function TradeDurationAnalysis(
  {
    accountNumber,
    filter = 'PROFIT',
    tdFilter = 'ALL'
  }
  : Readonly<{
    accountNumber: number,
    filter: FilterSelector,
    tdFilter: TradeDurationFilterSelector
  }>
) {

  useEffect(() => {
    getAccTradeDurationAnalysis()
  }, [filter, accountNumber]);

  const [isLoading, setIsLoading] = useState(false)
  const [data, setData] = useState<Array<AnalysisResult>>([])


  //  GENERAL FUNCTIONS

  /**
   * Fetches the analysis data
   */
  async function getAccTradeDurationAnalysis() {

    setIsLoading(true)

    const data = await getTradeDurationAnalysis(accountNumber, tdFilter, filter)
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
          <AnalysisBarChart data={data} filter={filter}/>
      }
    </div>
  )
}
