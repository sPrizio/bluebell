import {ApiCredentials} from "@/lib/constants";

/**
 * Checks for a valid password as per the guidelines defined by bluebell
 *
 * @param val test input
 */
export function isValidPassword(val: string): boolean {

  const allowedSymbols = ['!', '@', '#', '$', '%', '&', '*', '-', '_', '+', ' ']

  let hasAlphanumeric = (/^[a-z0-9]+$/i.exec(val)?.length ?? -1) > 0;
  let hasSymbols = false;

  for (let str of allowedSymbols) {
    if (val.includes(str)) {
      hasSymbols = true;
      break
    }
  }

  return !hasAlphanumeric && hasSymbols;
}

/**
 * Returns the auth header for api calls
 */
export function getAuthHeader(): any {
  const obj : any = {}
  obj[ApiCredentials.AuthHeader] = ApiCredentials.TestUserToken
  return obj
}

/**
 * Gets the base api url
 *
 * @returns {string} url
 */
export function baseUrl(): string {
  return 'http://localhost:8080'
}

/**
 * Gets the domain url
 *
 * @returns {string} url
 */
export function getDomain(appendVal: string): string {
  return baseUrl() + '/api/v1' + appendVal
}

/**
 * Gets the chart domain
 *
 * @returns {string} url
 */
export function getChartDomain(): string {
  return getDomain('/chart')
}

/**
 * Gets the News url
 *
 * @returns {string} url
 */
export function getNewsDomain(): string {
  return getDomain('/news')
}

/**
 * Gets the Trade url
 *
 * @returns {string} url
 */
export function getTradeDomain(): string {
  return getDomain('/trade')
}

/**
 * Gets the Trade record url
 *
 * @returns {string} url
 */
export function getTradeRecordDomain(): string {
  return getDomain('/trade-record')
}

/**
 * Gets the user url
 *
 * @returns {string} url
 */
export function getUserDomain(): string {
  return getDomain('/user')
}

/**
 * Gets the analysis url
 *
 * @returns {string} url
 */
export function getAnalysisDomain(): string {
  return getDomain('/analysis')
}

/**
 * Gets the account url
 *
 * @returns {string} url
 */
export function getAccountDomain(): string {
  return getDomain('/account')
}

/**
 * Gets the account url
 *
 * @returns {string} url
 */
export function getPortfolioDomain(): string {
  return getDomain('/portfolio')
}