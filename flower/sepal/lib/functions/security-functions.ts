import { ApiCredentials } from "@/lib/constants";

/**
 * Checks for a valid password as per the guidelines defined by greenhouse
 *
 * @param val test input
 */
export function isValidPassword(val: string): boolean {
  const allowedSymbols = [
    "!",
    "@",
    "#",
    "$",
    "%",
    "&",
    "*",
    "-",
    "_",
    "+",
    " ",
  ];

  const hasAlphanumeric = (/^[a-z0-9]+$/i.exec(val)?.length ?? -1) > 0;
  let hasSymbols = false;

  for (const str of allowedSymbols) {
    if (val.includes(str)) {
      hasSymbols = true;
      break;
    }
  }

  return !hasAlphanumeric && hasSymbols;
}

/**
 * Returns the auth header for api calls
 */
export function getAuthHeader(): any {
  const obj: any = {};
  obj[ApiCredentials.AuthHeader] = ApiCredentials.TestUserToken;
  return obj;
}

/**
 * Gets the base api url
 *
 * @returns {string} url
 */
export function baseUrl(): string {
  return process.env.NEXT_PUBLIC_BASE_API_DOMAIN ?? "";
}

/**
 * Gets the domain url
 *
 * @returns {string} url
 */
export function getDomain(appendVal: string): string {
  return baseUrl() + process.env.NEXT_PUBLIC_BASE_API_VERSION + appendVal;
}

/**
 * Gets the chart domain
 *
 * @returns {string} url
 */
export function getChartDomain(): string {
  return getDomain("/chart");
}

/**
 * Gets the News url
 *
 * @returns {string} url
 */
export function getNewsDomain(): string {
  return getDomain("/news");
}

/**
 * Gets the Trade url
 *
 * @returns {string} url
 */
export function getTradeDomain(): string {
  return getDomain("/trade");
}

/**
 * Gets the Trade record url
 *
 * @returns {string} url
 */
export function getTradeRecordDomain(): string {
  return getDomain("/trade-record");
}

/**
 * Gets the user url
 *
 * @returns {string} url
 */
export function getUserDomain(): string {
  return getDomain("/user");
}

/**
 * Gets the analysis url
 *
 * @returns {string} url
 */
export function getAnalysisDomain(): string {
  return getDomain("/analysis");
}

/**
 * Gets the account url
 *
 * @returns {string} url
 */
export function getAccountDomain(): string {
  return getDomain("/account");
}

/**
 * Gets the portfolio url
 *
 * @returns {string} url
 */
export function getPortfolioDomain(): string {
  return getDomain("/portfolio");
}

/**
 * Gets the portfolio record url
 *
 * @returns {string} url
 */
export function getPortfolioRecordDomain(): string {
  return getDomain("/portfolio-record");
}

/**
 * Gets the symbol url
 *
 * @returns {string} url
 */
export function getSymbolDomain(): string {
  return getDomain("/symbol");
}

/**
 * Gets the job url
 *
 * @returns {string} url
 */
export function getJobDomain(): string {
  return getDomain("/job");
}
