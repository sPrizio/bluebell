import {getAuthHeader} from "@/lib/functions/security-functions";
import {ApiUrls} from "@/lib/constants";
import { AnalysisResult } from "@/types/apiTypes";

/**
 * Obtains the analysis for time buckets
 *
 * @param accNumber account number
 * @param filter filter (points or profit or win %)
 * @param isOpened show opened or closed trades (opened if true)
 */
export async function getTimeBucketsAnalysis(accNumber: number, filter: string, isOpened: boolean): Promise<Array<AnalysisResult> | null> {

  const headers = getAuthHeader()
  headers['Content-Type'] = 'application/json'

  try {
    const res =
      await fetch(
        ApiUrls.Analysis.TimeBuckets
          .replace('{accountNumber}', accNumber.toString())
          .replace('{isOpened}', isOpened.toString())
          .replace('{filter}', filter), {
          method: 'GET',
          headers: headers,
        }
      )

    if (res.ok) {
      const data = await res.json()
      if (data.success) {
        return data.data
      }
    }
  } catch (e) {
    console.log(e)
  }

  return null;
}

/**
 * Obtains the analysis for weekdays
 *
 * @param accNumber account number
 * @param filter filter (points or profit or win %)
 */
export async function getWeekdaysAnalysis(accNumber: number, filter: string): Promise<Array<AnalysisResult> | null> {

  const headers = getAuthHeader()
  headers['Content-Type'] = 'application/json'

  try {
    const res =
      await fetch(
        ApiUrls.Analysis.Weekdays
          .replace('{accountNumber}', accNumber.toString())
          .replace('{filter}', filter), {
          method: 'GET',
          headers: headers,
        }
      )

    if (res.ok) {
      const data = await res.json()
      if (data.success) {
        return data.data
      }
    }
  } catch (e) {
    console.log(e)
  }

  return null;
}

/**
 * Obtains the analysis for time buckets and weekdays
 *
 * @param accNumber account number
 * @param weekday day of week
 * @param filter filter (points or profit or win %)
 */
export async function getWeekdaysTimeBucketsAnalysis(accNumber: number, weekday: string, filter: string): Promise<Array<AnalysisResult> | null> {

  const headers = getAuthHeader()
  headers['Content-Type'] = 'application/json'

  try {
    const res =
      await fetch(
        ApiUrls.Analysis.WeekdaysTimeBuckets
          .replace('{accountNumber}', accNumber.toString())
          .replace('{weekday}', weekday)
          .replace('{filter}', filter), {
      method: 'GET',
      headers: headers,
    }
  )

    if (res.ok) {
      const data = await res.json()
      if (data.success) {
        return data.data
      }
    }
  } catch (e) {
    console.log(e)
  }

  return null;
}

/**
 * Obtains the analysis for trade duration
 * @param accNumber account number
 * @param tradeDurationFilter opened or closed
 * @param filter filter (points or profit or win %)
 */
export async function getTradeDurationAnalysis(accNumber: number, tradeDurationFilter: string, filter: string): Promise<Array<AnalysisResult> | null> {

  const headers = getAuthHeader()
  headers['Content-Type'] = 'application/json'

  try {
    const res =
      await fetch(
        ApiUrls.Analysis.TradeDuration
          .replace('{accountNumber}', accNumber.toString())
          .replace('{tradeDurationFilter}', tradeDurationFilter)
          .replace('{filter}', filter), {
          method: 'GET',
          headers: headers,
        }
      )

    if (res.ok) {
      const data = await res.json()
      if (data.success) {
        return data.data
      }
    }
  } catch (e) {
    console.log(e)
  }

  return null;
}
