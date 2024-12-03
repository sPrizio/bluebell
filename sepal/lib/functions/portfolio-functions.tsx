import {getAuthHeader} from "@/lib/functions/security-functions";
import {ApiUrls} from "@/lib/constants";

export async function getPortfolio(): Promise<Portfolio | null> {

  let headers = getAuthHeader()
  headers['Content-Type'] = 'application/json'

  try {
    const res =
      await fetch(
        ApiUrls.Portfolio.GetPortfolio, {
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