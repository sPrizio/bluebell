import {getAuthHeader} from "@/lib/functions/security-functions";
import {ApiUrls} from "@/lib/constants";
import {PagedTrades, TradeLog, TradeRecordControls, TradeRecordReport} from "@/types/apiTypes";

/**
 * Fetches a list of trade records for the given time period
 *
 * @param accNumber account number
 * @param start start of time period
 * @param end end of time period
 * @param interval aggregate interval
 * @param count limit results
 */
export async function getTradeRecords(accNumber: number, start: string, end: string, interval: string, count: number): Promise<TradeRecordReport | null> {

  const headers = getAuthHeader()
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

/**
 * Fetches a list of recent trade records
 *
 * @param accNumber account number
 * @param interval aggregate interval
 * @param count limit results
 */
export async function getRecentTradeRecords(accNumber: number, interval: string, count: number): Promise<TradeRecordReport | null> {

  const headers = getAuthHeader()
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

/**
 * Fetches filters for viewing trade records
 *
 * @param accNumber account number
 * @param interval aggregate interval
 */
export async function getTradeRecordControls(accNumber: number, interval: string): Promise<TradeRecordControls | null> {

  const headers = getAuthHeader()
  headers['Content-Type'] = 'application/json'

  try {
    const res =
      await fetch(
        ApiUrls.TradeRecord.GetTradeRecordControls
          .replace('{accountNumber}', accNumber.toString())
          .replace('{interval}', interval), {
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
 * Fetches the trade log
 *
 * @param start start of time period
 * @param end end of time period
 * @param interval aggregate interval
 * @param count limit results
 */
export async function getTradeLog(start: string, end: string, interval: string, count: number): Promise<TradeLog | null> {

  const headers = getAuthHeader()
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

/**
 * Fetches paginated trades
 *
 * @param accNumber account number
 * @param start start of time period
 * @param end end of time period
 * @param page current page
 * @param pageSize page size
 */
export async function getPagedTrades(accNumber: number, start: string, end: string, page: number, pageSize: number): Promise<PagedTrades | null> {

  const headers = getAuthHeader()
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

  const headers = getAuthHeader()
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