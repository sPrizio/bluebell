import {
  IconArrowBarToDown,
  IconArrowBarUp,
  IconArrowNarrowDown,
  IconArrowNarrowLeft,
  IconArrowNarrowRight,
  IconArrowNarrowUp,
  IconArrowsRightLeft,
  IconBrain,
  IconChartDonutFilled,
  IconChartPie,
  IconChartScatter,
  IconFolders,
  IconLayoutDashboard,
  IconLogout,
  IconMountain,
  IconNews,
  IconReplaceFilled,
  IconSearch,
  IconUserCircle,
} from "@tabler/icons-react";
import Image from "next/image";
import { Icons } from "@/lib/enums";
import React from "react";
import can from "@/app/assets/icons/locales/flags/canada.png";
import usa from "@/app/assets/icons/locales/flags/usa.png";
import eu from "@/app/assets/icons/locales/flags/eu.png";
import uk from "@/app/assets/icons/locales/flags/united-kingdom.png";
import jpy from "@/app/assets/icons/locales/flags/japan.png";
import cny from "@/app/assets/icons/locales/flags/china.png";

import aud from "@/app/assets/icons/locales/flags/australia.png";
import canRound from "@/app/assets/icons/locales/round/canada.png";
import usaRound from "@/app/assets/icons/locales/round/usa.png";
import euRound from "@/app/assets/icons/locales/round/eu.png";
import ukRound from "@/app/assets/icons/locales/round/united-kingdom.png";
import jpyRound from "@/app/assets/icons/locales/round/japan.png";
import cnyRound from "@/app/assets/icons/locales/round/china.png";
import audRound from "@/app/assets/icons/locales/round/australia.png";

import cmc from "@/app/assets/brokers/cmc.png";
import ftmo from "@/app/assets/brokers/ftmo.png";
import td365 from "@/app/assets/brokers/td365.png";
import td from "@/app/assets/brokers/td.png";
import LoadingPage from "@/app/loading";
import { redirect } from "next/navigation";
import { logErrors } from "@/lib/functions/util-functions";
import Error from "@/app/error";
import { Portfolio } from "@/types/apiTypes";

/**
 * Returns the correct icon based on the given enum value
 *
 * @param iconCode icon code
 * @param className optional css classes
 * @param iconSize optional icon size
 */
export function resolveIcon(iconCode: string, className = "", iconSize = 24) {
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
      return <IconChartScatter className={className} size={iconSize} />;
    case Icons.Logout:
      return <IconLogout className={className} size={iconSize} />;
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
    case Icons.Mountain:
      return <IconMountain className={className} size={iconSize} />;
    case Icons.Transactions:
      return <IconArrowsRightLeft className={className} size={iconSize} />;
    case Icons.Trades:
      return <IconReplaceFilled className={className} size={iconSize} />;
    case Icons.Analysis:
      return <IconSearch className={className} size={iconSize} />;
    case Icons.Portfolios:
      return <IconFolders className={className} size={iconSize} />;
    case Icons.JobsOverview:
      return <IconBrain className={className} size={iconSize} />;
    default:
      return <span>-</span>;
  }
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
      return <Image src={can} height={height} width={width} alt={"Canada"} />;
    case "USD":
      return (
        <Image src={usa} height={height} width={width} alt={"United States"} />
      );
    case "EUR":
      return (
        <Image src={eu} height={height} width={width} alt={"European Union"} />
      );
    case "GBP":
      return (
        <Image src={uk} height={height} width={width} alt={"United Kingdom"} />
      );
    case "JPY":
      return <Image src={jpy} height={height} width={width} alt={"Japan"} />;
    case "CNY":
      return <Image src={cny} height={height} width={width} alt={"China"} />;
    case "AUD":
      return (
        <Image src={aud} height={height} width={width} alt={"Australia"} />
      );
    default:
      return <span>-</span>;
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
      return (
        <Image src={canRound} height={height} width={width} alt={"Canada"} />
      );
    case "USD":
      return (
        <Image
          src={usaRound}
          height={height}
          width={width}
          alt={"United States"}
        />
      );
    case "EUR":
      return (
        <Image
          src={euRound}
          height={height}
          width={width}
          alt={"European Union"}
        />
      );
    case "GBP":
      return (
        <Image
          src={ukRound}
          height={height}
          width={width}
          alt={"United Kingdom"}
        />
      );
    case "JPY":
      return (
        <Image src={jpyRound} height={height} width={width} alt={"Japan"} />
      );
    case "CNY":
      return (
        <Image src={cnyRound} height={height} width={width} alt={"China"} />
      );
    case "AUD":
      return (
        <Image src={audRound} height={height} width={width} alt={"Australia"} />
      );
    default:
      return <span>-</span>;
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
    case "CMC_MARKETS":
      return (
        <Image
          src={cmc}
          alt={"CMC Markets Logo"}
          height={height}
          width={width}
        />
      );
    case "FTMO":
      return (
        <Image src={ftmo} alt={"FTMO Logo"} height={height} width={width} />
      );
    case "td365":
      return (
        <Image src={td365} alt={"TD365 Logo"} height={height} width={width} />
      );
    case "td":
      return (
        <Image
          src={td}
          alt={"TD (Toronto-Dominion) Logo"}
          height={height}
          width={width}
        />
      );
    case "":
    default:
      return <span>-</span>;
  }
}

/**
 * Validates the page flow for certain, repeated, use queries
 *
 * @param isLoading is loading
 * @param isError has an error
 * @param activePortfolio the active portfolio
 * @param hasMismatch mismatch between account and portfolio
 * @param error possible error
 */
export function validatePageQueryFlow(
  isLoading: boolean,
  isError: boolean,
  activePortfolio: Portfolio | null,
  hasMismatch: boolean,
  error: Error | null,
) {
  if (isLoading) {
    return <LoadingPage />;
  }

  if (isError || (!isError && !activePortfolio)) {
    redirect("/portfolios");
  }

  if ((activePortfolio?.accounts?.length ?? 0) === 0) {
    redirect("/accounts");
  }

  if (hasMismatch || isError) {
    logErrors("User and portfolio mismatch!", error);
    return <Error />;
  }
}
