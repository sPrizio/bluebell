import { Account, User } from "@/types/apiTypes";
import { AppRouterInstance } from "next/dist/shared/lib/app-router-context.shared-runtime";
import { ReadonlyURLSearchParams } from "next/navigation";

/**
 * Checks whether the given data is valid and exists
 *
 * @param data test data
 */
export function hasData(data: any) {
  return data !== null && data !== undefined;
}

/**
 * Checks if the given object is null, undefined or empty
 *
 * @param object data
 */
export function emptyObject(object: any) {
  for (const prop in object) {
    if (Object.hasOwn(object, prop)) {
      return false;
    }
  }

  return true;
}

/**
 * Formats a number for pretty display
 *
 * @param val number to format
 */
export function formatNumberForDisplay(val: number | string): string {
  if (!val) {
    return "0";
  }

  if (typeof val === "string") {
    try {
      return Number(val).toLocaleString().replace(".00", "").replace(",", "");
    } catch (e) {
      console.log(e);
      return "DATA ERROR!";
    }
  }

  return parseFloat(val.toFixed(2))
    .toLocaleString("en-US", { minimumFractionDigits: 2 })
    .replace(".00", "");
}

/**
 * Displays negative points with bracket instead of negative sign
 *
 * @param val number
 */
export function formatNegativePoints(val: number) {
  if (val < 0) {
    return "(" + formatNumberForDisplay(Math.abs(val)) + ")";
  }

  return formatNumberForDisplay(val);
}

/**
 * Delays the page by x amount of time
 *
 * @param ms time to delay in milliseconds
 */
export const delay = (ms: number) => new Promise((res) => setTimeout(res, ms));

/**
 * Determines whether the given value is a number
 *
 * @param num value
 */
export const isNumeric = (num: any) =>
  (typeof num === "number" || (typeof num === "string" && num.trim() !== "")) &&
  !isNaN(num as number);

/**
 * Converts string array in an enum for zos
 *
 * @param val array of strings
 */
export function safeConvertEnum(val: string[]): [string, ...string[]] {
  // @ts-expect-error : will fail if bad enum type
  return val;
}

/**
 * Returns the first Account that has a default Account flag set to true
 *
 * @param accounts array of accounts
 */
export function getDefaultAccount(accounts: Array<Account>): Account | null {
  return accounts?.find((acc) => acc.defaultAccount) ?? null;
}

/**
 * Attempts to find an Account matching the given Account number
 *
 * @param val Account number
 * @param accounts accounts list
 */
export function getAccount(
  val: number,
  accounts: Array<Account>,
): Account | null {
  if (val === -1) {
    return null;
  } else {
    return accounts?.find((acc) => acc.accountNumber === val) ?? null;
  }
}

/**
 * Fetches the Account number requested by the page
 *
 * @param params search params
 * @param accounts accounts list
 */
export function getAccountNumber(
  params: ReadonlyURLSearchParams,
  accounts: Array<Account>,
): number {
  const val = params.get("account") ?? -1;

  if (val === "default") {
    return getDefaultAccount(accounts)?.accountNumber ?? -1;
  }

  if (val !== -1 && isNumeric(val)) {
    return parseInt(val as string);
  }

  return -1;
}

/**
 * Flatten a multidimensional object
 *
 * For example:
 *   flattenObject{ a: 1, b: { c: 2 } }
 * Returns:
 *   { a: 1, c: 2}
 */
export const flattenObject = (obj: any) => {
  const flattened: any = {};
  Object.keys(obj).forEach((key) => {
    const value = obj[key];

    if (typeof value === "object" && value !== null && !Array.isArray(value)) {
      Object.assign(flattened, flattenObject(value));
    } else {
      flattened[key] = value;
    }
  });

  return flattened;
};

/**
 * Capitalizes a string
 *
 * @param {string} str input string
 * @returns {string} capitalized string
 */
export const capitalize = (str: string): string => {
  return str
    .replace(/-/g, " ")
    .split(" ")
    .map((word) => word.charAt(0).toUpperCase() + word.slice(1).toLowerCase())
    .join(" ");
};

/**
 * Formats the given number of seconds into a pretty display string
 *
 * @param {number} seconds elapsed
 * @returns {string} formatted string
 */
export function formatTimeElapsed(seconds: number): string {
  if (seconds < 60) {
    return seconds + "s";
  } else if (seconds < 3600) {
    return Math.floor(seconds / 60) + "m " + (seconds % 60) + "s";
  } else {
    return (
      Math.floor(seconds / 3600) +
      "h " +
      Math.floor((seconds % 3600) / 60) +
      "m " +
      ((seconds % 3600) % 60) +
      "s"
    );
  }
}

/**
 * Logs errors to the logging console
 *
 * @param errors errors
 */
export function logErrors(...errors: any[]) {
  errors.forEach((error) => {
    if (error != null || error) {
      console.error(error);
    }
  });
}

/**
 * Obtains the active portfolio uid, if it exists
 *
 * @param user user
 */
export function getActivePortfolioNumber(
  user: User | null | undefined,
): number | null {
  if (user?.portfolios ?? false) {
    const defPort = user?.portfolios?.filter((p) => p.defaultPortfolio) ?? null;
    if ((defPort?.length ?? 0) > 0) {
      return defPort?.[0].portfolioNumber ?? null;
    }
  }

  return null;
}

/**
 * Selects the new account by updating the url
 *
 * @param router router
 * @param searchParams search params
 * @param accNumber account number
 */
export async function selectNewAccount(
  router: AppRouterInstance,
  searchParams: ReadonlyURLSearchParams,
  accNumber: number,
) {
  const params = new URLSearchParams(searchParams.toString());
  params.set("account", accNumber.toString());
  router.push(`?${params.toString()}`);
}
