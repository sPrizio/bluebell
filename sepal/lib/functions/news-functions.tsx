import {ApiUrls} from "@/lib/constants";
import {getAuthHeader} from "@/lib/functions/security-functions";

/**
 * Triggers the fetch news endpoint
 */
export async function fetchNews(): Promise<boolean | null> {

  let headers = getAuthHeader()
  headers['Content-Type'] = 'application/json'

  try {
    const res =
      await fetch(ApiUrls.News.FetchNews, {
        method: 'POST',
        headers: headers,
      })

    if (res.ok) {
      const data = await res.json()
      return !!data.success;
    }
  } catch (e) {
    console.log(e)
  }

  return null;
}

/**
 * Obtains the market news for the given time span
 */
export async function getNews(start: string, end: string): Promise<Array<MarketNews> | null> {

  let headers = getAuthHeader()
  headers['Content-Type'] = 'application/json'

  try {
    const res =
      await fetch(ApiUrls.News.GetNews.replace('{start}', start).replace('{end}', end), {
        method: 'GET',
        headers: headers,
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