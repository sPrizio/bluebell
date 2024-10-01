import {CoreConstants} from "@/app/constants";

/**
 * Returns the auth header for api calls
 */
export function getAuthHeader(): any {
    const obj : any = {}
    obj[CoreConstants.ApiCredentials.AuthHeader] = CoreConstants.ApiCredentials.TestUserToken
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
 * Gets the news url
 *
 * @returns {string} url
 */
export function getNewsDomain(): string {
    return getDomain('/news')
}

/**
 * Gets the trade url
 *
 * @returns {string} url
 */
export function getTradeDomain(): string {
    return getDomain('/trade')
}

/**
 * Gets the trade record url
 *
 * @returns {string} url
 */
export function getTradeRecordDomain(): string {
    return getDomain('/trade-record')
}