import {getAuthHeader} from "@/lib/functions/security-functions";
import {ApiUrls} from "@/lib/constants";

export async function getTradeRecords(accNumber: number, start: string, end: string, interval: string, count: number): Promise<TradeRecordReport | null> {

  let headers = getAuthHeader()
  headers['Content-Type'] = 'application/json'

  try {
    const res =
      await fetch(
        ApiUrls.TradeRecord.GetTradeRecords
          .replace('{accountNumber}', accNumber.toString())
          .replace('{start}', start)
          .replace('{end}', end)
          .replace('{interval}', interval)
          .replace('{count}', count.toString()), {
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

export async function getRecentTradeRecords(accNumber: number, interval: string, count: number): Promise<TradeRecordReport | null> {

  let headers = getAuthHeader()
  headers['Content-Type'] = 'application/json'

  try {
    const res =
      await fetch(
        ApiUrls.TradeRecord.GetRecentTradeRecords
          .replace('{accountNumber}', accNumber.toString())
          .replace('{interval}', interval)
          .replace('{count}', count.toString()), {
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

export async function getTradeLog(start: string, end: string, interval: string, count: number): Promise<Array<TradeLog> | null> {

  let headers = getAuthHeader()
  headers['Content-Type'] = 'application/json'

  try {
    const res =
      await fetch(
        ApiUrls.TradeRecord.GetTradeLog
          .replace('{start}', start)
          .replace('{end}', end)
          .replace('{interval}', interval)
          .replace('{count}', count.toString()), {
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

export async function getPagedTrades(accNumber: number, start: string, end: string, page: number, pageSize: number): Promise<Array<Trade> | null> {

  let headers = getAuthHeader()
  headers['Content-Type'] = 'application/json'

  try {
    const res =
      await fetch(
        ApiUrls.Trade.GetPagedTrades
          .replace('{accountNumber}', accNumber.toString())
          .replace('{start}', start)
          .replace('{end}', end)
          .replace('{page}', page.toString())
          .replace('{pageSize}', pageSize.toString()), {
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
 * Performs the user registration api call
 *
 * @param accNumber account number
 * @param file File
 * @param isStrategy flag
 */
export async function importTrades(accNumber: number, file: File | null, isStrategy: boolean): Promise<boolean | null> {

  let headers = getAuthHeader()
  if (!file) {
    return false;
  }

  try {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('fileName', file.name);

    const res =
      await fetch(
        ApiUrls.Trade.ImportTrades
          .replace('{accountNumber}', accNumber.toString())
          .replace('{isStrategy}', isStrategy.toString()
          ), {
          method: 'POST',
          headers: headers,
          body: formData
        })

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