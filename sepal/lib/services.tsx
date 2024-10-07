import {
  IconArrowBarToDown,
  IconArrowBarUp,
  IconArrowNarrowDown,
  IconArrowNarrowLeft,
  IconArrowNarrowRight,
  IconArrowNarrowUp,
  IconArrowsRightLeft,
  IconChartDonutFilled,
  IconChartLine,
  IconChartPie,
  IconLayoutDashboard,
  IconLogout2,
  IconNews,
  IconReplaceFilled,
  IconUserCircle
} from "@tabler/icons-react";
import {Icons} from "@/lib/enums";
import React from "react";

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
      return <IconLayoutDashboard className={className} size={iconSize} />;
    case Icons.AccountOverview:
      return <IconChartPie className={className} size={iconSize} />;
    case Icons.UserProfile:
      return <IconUserCircle className={className} size={iconSize} />;
    case Icons.MarketNews:
      return <IconNews className={className} size={iconSize} />;
    case Icons.Performance:
      return <IconChartLine className={className} size={iconSize} />;
    case Icons.Logout:
      return <IconLogout2 className={className} size={iconSize} />;
    case Icons.ArrowUp:
      return <IconArrowNarrowUp className={className} size={iconSize} />;
    case Icons.ArrowRight:
      return <IconArrowNarrowRight className={className} size={iconSize} />;
    case Icons.ArrowDown:
      return <IconArrowNarrowDown className={className} size={iconSize} />;
    case Icons.ArrowLeft:
      return <IconArrowNarrowLeft className={className} size={iconSize} />;
    case Icons.ArrowLeftRight:
      return <IconArrowsRightLeft className={className} size={iconSize} />;
    case Icons.ChartDoughnut:
      return <IconChartDonutFilled className={className} size={iconSize} />;
    case Icons.Replace:
      return <IconReplaceFilled className={className} size={iconSize} />;
    case Icons.ArrowBarDown:
      return <IconArrowBarToDown className={className} size={iconSize} />;
    case Icons.ArrowBarUp:
      return <IconArrowBarUp className={className} size={iconSize} />;
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

  return val
}