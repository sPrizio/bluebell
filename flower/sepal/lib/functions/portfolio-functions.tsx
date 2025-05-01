import {getAuthHeader} from "@/lib/functions/security-functions";
import {ApiUrls} from "@/lib/constants";
import {Portfolio} from "@/types/apiTypes";

/**
 * Fetches the User's portfolio information
 *
 * @param portfolioUid portfolio uid
 */
export async function getPortfolio(portfolioUid: string): Promise<Portfolio> {

  const headers = getAuthHeader()
  headers['Content-Type'] = 'application/json'

  const res = await fetch(ApiUrls.Portfolio.GetPortfolio.replace("{uid}", portfolioUid), {
    method: "GET",
    headers: headers,
  });

  if (!res.ok) {
    throw new Error(`Failed to get Portfolio with status: ${res.status}`);
  }

  const data = await res.json();
  if (!data.success) {
    throw new Error(`API returned with error: ${data.message} || ${JSON.stringify(data)}`);
  }

  return data.data;
}