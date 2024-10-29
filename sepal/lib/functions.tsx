import {
  IconArrowBarToDown,
  IconArrowBarUp,
  IconArrowNarrowDown,
  IconArrowNarrowLeft,
  IconArrowNarrowRight,
  IconArrowNarrowUp,
  IconArrowsRightLeft,
  IconChartDonutFilled,
  IconChartPie,
  IconChartScatter,
  IconLayoutDashboard,
  IconLogout,
  IconMountain,
  IconNews,
  IconReplaceFilled,
  IconUserCircle
} from "@tabler/icons-react";
import Image from "next/image";
import {Icons} from "@/lib/enums";
import React from "react";
import can from '@/app/assets/icons/locales/flags/canada.png'
import usa from '@/app/assets/icons/locales/flags/usa.png'
import eu from '@/app/assets/icons/locales/flags/eu.png'
import uk from '@/app/assets/icons/locales/flags/united-kingdom.png'
import jpy from '@/app/assets/icons/locales/flags/japan.png'
import cny from '@/app/assets/icons/locales/flags/china.png'

import aud from '@/app/assets/icons/locales/flags/australia.png'
import canRound from '@/app/assets/icons/locales/round/canada.png'
import usaRound from '@/app/assets/icons/locales/round/usa.png'
import euRound from '@/app/assets/icons/locales/round/eu.png'
import ukRound from '@/app/assets/icons/locales/round/united-kingdom.png'
import jpyRound from '@/app/assets/icons/locales/round/japan.png'
import cnyRound from '@/app/assets/icons/locales/round/china.png'
import audRound from '@/app/assets/icons/locales/round/australia.png'

import cmc from '@/app/assets/brokers/cmc.png'
import ftmo from '@/app/assets/brokers/ftmo.png'
import td365 from '@/app/assets/brokers/td365.png'
import td from '@/app/assets/brokers/td.png'
import {accounts} from "@/lib/sample-data";
import {ReadonlyURLSearchParams} from "next/navigation";

/**
 * Returns the correct icon based on the given enum value
 *
 * @param iconCode icon code
 * @param className optional css classes
 * @param iconSize optional icon size
 */
export function resolveIcon(iconCode: string, className = '', iconSize = 24) {
  switch (iconCode) {
    case Icons.Dashboard:
      return <IconLayoutDashboard className={className} size={iconSize}/>;
    case Icons.AccountOverview:
      return <IconChartPie className={className} size={iconSize}/>;
    case Icons.UserProfile:
      return <IconUserCircle className={className} size={iconSize}/>;
    case Icons.MarketNews:
      return <IconNews className={className} size={iconSize}/>;
    case Icons.Performance:
      return <IconChartScatter className={className} size={iconSize}/>;
    case Icons.Logout:
      return <IconLogout className={className} size={iconSize}/>;
    case Icons.ArrowUp:
      return <IconArrowNarrowUp className={className} size={iconSize}/>;
    case Icons.ArrowRight:
      return <IconArrowNarrowRight className={className} size={iconSize}/>;
    case Icons.ArrowDown:
      return <IconArrowNarrowDown className={className} size={iconSize}/>;
    case Icons.ArrowLeft:
      return <IconArrowNarrowLeft className={className} size={iconSize}/>;
    case Icons.ArrowLeftRight:
      return <IconArrowsRightLeft className={className} size={iconSize}/>;
    case Icons.ChartDoughnut:
      return <IconChartDonutFilled className={className} size={iconSize}/>;
    case Icons.Replace:
      return <IconReplaceFilled className={className} size={iconSize}/>;
    case Icons.ArrowBarDown:
      return <IconArrowBarToDown className={className} size={iconSize}/>;
    case Icons.ArrowBarUp:
      return <IconArrowBarUp className={className} size={iconSize}/>;
    case Icons.Mountain:
      return <IconMountain className={className} size={iconSize}/>;
    case Icons.Transactions:
      return <IconArrowsRightLeft className={className} size={iconSize}/>;
    case Icons.Trades:
      return <IconReplaceFilled className={className} size={iconSize}/>;
    default:
      return null;
  }
}

/**
 * Checks whether the given data is valid and exists
 *
 * @param data test data
 */
export function hasData(data: any) {
  return data !== null && data !== undefined
}

/**
 * Checks if the given object is null, undefined or empty
 *
 * @param object data
 */
export function emptyObject(object: any) {

  for (const prop in object) {
    if (Object.hasOwn(object, prop)) {
      return false
    }
  }

  return true
}

/**
 * Formats a number for pretty display
 *
 * @param val number to format
 */
export function formatNumberForDisplay(val: number | string) {

  if (typeof val === 'string') {
    try {
      return Number(val).toLocaleString()
    } catch (e) {
      return 'DATA ERROR!'
    }
  }

  return parseFloat(val.toFixed(2)).toLocaleString('en-US', {minimumFractionDigits: 2})
}

/**
 * Displays negative points with bracket instead of negative sign
 *
 * @param val number
 */
export function formatNegativePoints(val: number) {

  if (val < 0) {
    return '(' + formatNumberForDisplay(Math.abs(val)) + ')'
  }

  return formatNumberForDisplay(val)
}

/**
 * Returns an image for the given country code
 *
 * @param val iso code
 * @param height optional image height
 * @param width optional image width
 */
export function getFlagForCode(val: string, height = 25, width = 25) {
  switch (val) {
    case "CAD":
      return <Image src={can} height={height} width={width} alt={'Canada'}/>
    case "USD":
      return <Image src={usa} height={height} width={width} alt={'United States'}/>
    case "EUR":
      return <Image src={eu} height={height} width={width} alt={'European Union'}/>
    case "GBP":
      return <Image src={uk} height={height} width={width} alt={'United Kingdom'}/>
    case "JPY":
      return <Image src={jpy} height={height} width={width} alt={'Japan'}/>
    case "CNY":
      return <Image src={cny} height={height} width={width} alt={'China'}/>
    case "AUD":
      return <Image src={aud} height={height} width={width} alt={'Australia'}/>
    default:
      return null;
  }
}

/**
 * Returns an image for the given country code
 *
 * @param val iso code
 * @param height optional image height
 * @param width optional image width
 */
export function getRoundFlagForCode(val: string, height = 25, width = 25) {
  switch (val) {
    case "CAD":
      return <Image src={canRound} height={height} width={width} alt={'Canada'}/>
    case "USD":
      return <Image src={usaRound} height={height} width={width} alt={'United States'}/>
    case "EUR":
      return <Image src={euRound} height={height} width={width} alt={'European Union'}/>
    case "GBP":
      return <Image src={ukRound} height={height} width={width} alt={'United Kingdom'}/>
    case "JPY":
      return <Image src={jpyRound} height={height} width={width} alt={'Japan'}/>
    case "CNY":
      return <Image src={cnyRound} height={height} width={width} alt={'China'}/>
    case "AUD":
      return <Image src={audRound} height={height} width={width} alt={'Australia'}/>
    default:
      return null;
  }
}

/**
 * Returns an image for brokers
 *
 * @param val broker code
 * @param height optional image height
 * @param width optional image width
 */
export function getBrokerImageForCode(val: string, height = 25, width = 25) {
  switch (val) {
    case 'cmc':
      return <Image src={cmc} alt={'CMC Markets Logo'} height={height} width={width}/>
    case 'ftmo':
      return <Image src={ftmo} alt={'FTMO Logo'} height={height} width={width}/>
    case 'td365':
      return <Image src={td365} alt={'TD365 Logo'} height={height} width={width}/>
    case 'td':
      return <Image src={td} alt={'TD (Toronto-Dominion) Logo'} height={height} width={width}/>
    default:
      return null;
  }
}

/**
 * Delays the page by x amount of time
 *
 * @param ms time to delay in milliseconds
 */
export const delay = (ms: number) => new Promise(res => setTimeout(res, ms));

/**
 * Determines whether the given value is a number
 *
 * @param num value
 */
export const isNumeric = (num: any) => (typeof (num) === 'number' || typeof (num) === "string" && num.trim() !== '') && !isNaN(num as number);

/**
 * Converts string array in an enum for zos
 *
 * @param val array of strings
 */
export function safeConvertEnum(val: string[]): [string, ...string[]] {
  // @ts-ignore
  return val
}

/**
 * Returns the first account that has a default account flag set to true
 *
 * @param accounts array of accounts
 */
export function getDefaultAccount(accounts: Array<Account>): Account | null {
  return accounts.find(acc => acc.defaultAccount) ?? null
}

/**
 * Attempts to find an account matching the given account number
 *
 * @param val account number
 * @param accounts accounts list
 */
export function getAccount(val: number, accounts: Array<Account>): Account | null {
  if (val === -1) {
    return null
  } else {
    return accounts?.find(acc => acc.accountNumber === val) ?? null
  }
}

/**
 * Fetches the account number requested by the page
 *
 * @param params search params
 * @param accounts accounts list
 */
export function getAccountNumber(params: ReadonlyURLSearchParams, accounts: Array<Account>): number {

  let val = params.get('account') ?? -1

  if (val === 'default') {
    return getDefaultAccount(accounts)?.accountNumber ?? -1;
  }

  if (val !== -1 && isNumeric(val)) {
    return parseInt(val as string)
  }

  return -1
}