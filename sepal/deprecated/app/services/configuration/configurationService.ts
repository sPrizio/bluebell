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
 * Gets the News url
 *
 * @returns {string} url
 */
export function getNewsDomain(): string {
    return getDomain('/News')
}

/**
 * Gets the Trade url
 *
 * @returns {string} url
 */
export function getTradeDomain(): string {
    return getDomain('/Trade')
}

/**
 * Gets the Trade record url
 *
 * @returns {string} url
 */
export function getTradeRecordDomain(): string {
    return getDomain('/Trade-record')
}