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
  return {};
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
 * Gets the domain api url (base url + version)
 *
 * @returns {string} url
 */
export function getBaseDomain(): string {
  return baseUrl() + process.env.NEXT_PUBLIC_BASE_API_VERSION;
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
export function getChartDomain(internal = false): string {
  if (internal) {
    return getInternalApiPrefix("/chart");
  }

  return getDomain("/chart");
}

/**
 * Gets the News url
 *
 * @returns {string} url
 */
export function getNewsDomain(internal = false): string {
  if (internal) {
    return getInternalApiPrefix("/news");
  }

  return getDomain("/news");
}

/**
 * Gets the Trade url
 *
 * @returns {string} url
 */
export function getTradeDomain(internal = false): string {
  if (internal) {
    return getInternalApiPrefix("/trade");
  }

  return getDomain("/trade");
}

/**
 * Gets the Trade record url
 *
 * @returns {string} url
 */
export function getTradeRecordDomain(internal = false): string {
  if (internal) {
    return getInternalApiPrefix("/trade-record");
  }

  return getDomain("/trade-record");
}

/**
 * Gets the user url
 *
 * @returns {string} url
 */
export function getUserDomain(internal = false): string {
  if (internal) {
    return getInternalApiPrefix("/user");
  }

  return getDomain("/user");
}

/**
 * Gets the analysis url
 *
 * @returns {string} url
 */
export function getAnalysisDomain(internal = false): string {
  if (internal) {
    return getInternalApiPrefix("/analysis");
  }

  return getDomain("/analysis");
}

/**
 * Gets the account url
 *
 * @returns {string} url
 */
export function getAccountDomain(internal = false): string {
  if (internal) {
    return getInternalApiPrefix("/account");
  }

  return getDomain("/account");
}

/**
 * Gets the portfolio url
 *
 * @returns {string} url
 */
export function getPortfolioDomain(internal = false): string {
  if (internal) {
    return getInternalApiPrefix("/portfolio");
  }

  return getDomain("/portfolio");
}

/**
 * Gets the portfolio record url
 *
 * @returns {string} url
 */
export function getPortfolioRecordDomain(internal = false): string {
  if (internal) {
    return getInternalApiPrefix("/portfolio-record");
  }

  return getDomain("/portfolio-record");
}

/**
 * Gets the symbol url
 *
 * @returns {string} url
 */
export function getSymbolDomain(internal = false): string {
  if (internal) {
    return getInternalApiPrefix("/symbol");
  }

  return getDomain("/symbol");
}

/**
 * Gets the system url
 *
 * @returns {string} url
 */
export function getSystemDomain(internal = false): string {
  if (internal) {
    return getInternalApiPrefix("/system");
  }

  return getDomain("/system");
}

/**
 * Gets the job url
 *
 * @returns {string} url
 */
export function getJobDomain(internal = false): string {
  if (internal) {
    return getInternalApiPrefix("/job");
  }

  return getDomain("/job");
}

/**
 * Gets the market price domain
 *
 * @returns {string} url
 */
export function getMarketPriceDomain(internal = false): string {
  if (internal) {
    return getInternalApiPrefix("/market-price");
  }

  return getDomain("/market-price");
}

/**
 * Gets the transaction domain
 *
 * @returns {string} url
 */
export function getTransactionDomain(internal = false): string {
  if (internal) {
    return getInternalApiPrefix("/transaction");
  }

  return getDomain("/transaction");
}

/**
 * Gets the security domain
 *
 * @param internal if internal api call, render a different path
 * @returns {string} url
 */
export function getSecurityDomain(internal = false): string {
  if (internal) {
    return "/api/security";
  }

  return getDomain("/security");
}

/**
 * Gets the api prefix for internal calls
 *
 * @param val url
 * @returns {string} url
 */
export function getInternalApiPrefix(val: string): string {
  return "/api/proxy" + val;
}
