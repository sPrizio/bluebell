import {getAuthHeader} from "@/lib/functions/security-functions";
import {ApiUrls} from "@/lib/constants";
import { AnalysisResult } from "@/types/apiTypes";

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
