import { formatNumberForDisplay } from "@/lib/functions/util-functions";
import { Icons } from "@/lib/enums";
import React from "react";
import { resolveIcon } from "@/lib/functions/util-component-functions";

/**
 * Component that renders small cards meant to act as overview Account summaries
 *
 * @param prefix text to inject before value
 * @param value value
 * @param delta change
 * @param deltaPercentage change percentage
 * @param icon icon to render
 * @author Stephen Prizio
 * @version 0.2.2
 */
export default function DashboardContent({
  prefix = "",
  value = 0.0,
  delta = 0.0,
  deltaPercentage = 0.0,
  icon = null,
}: Readonly<{
  prefix?: string;
  value: number | string;
  delta?: number;
  deltaPercentage?: number;
  icon?: React.ReactNode;
}>) {
  //  GENERAL FUNCTIONS

  /**
   * Computes the font color based on the deltaPercentage value
   */
  function computeDeltaColor() {
    if (deltaPercentage > 0) {
      return " text-primaryGreen ";
    } else if (deltaPercentage < 0) {
      return " text-primaryRed ";
    }

    return " text-slate-500 ";
  }

  /**
   * Computes the type of icon to render based on the deltaPercentage value
   */
  function computeDeltaDirection() {
    if (delta > 0) {
      return resolveIcon(Icons.CirclePlus, "", 18);
    } else if (deltaPercentage < 0) {
      return resolveIcon(Icons.ArrowDown, "", 18);
    }

    return resolveIcon(Icons.ArrowLeftRight, "", 18);
  }

  /**
   * Computes the type of icon to render based on the deltaPercentage value
   */
  function computeDeltaPercentageDirection() {
    if (deltaPercentage > 0) {
      return resolveIcon(Icons.ArrowUp, "", 18);
    } else if (deltaPercentage < 0) {
      return resolveIcon(Icons.ArrowDown, "", 18);
    }

    return resolveIcon(Icons.ArrowLeftRight, "", 18);
  }

  //  RENDER

  return (
    <div className="flex flex-col w-full gap-1">
      <div className="flex w-full items-stretch gap-4">
        <div className="flex items-center text-xl">
          {prefix}
          {typeof value === "string" ? value : formatNumberForDisplay(value)}
        </div>

        <div className="flex flex-1 items-center">
          <div className="flex items-center w-full text-sm">
            <span className={"font-bold " + computeDeltaColor()}>
              {deltaPercentage === 0
                ? "-"
                : formatNumberForDisplay(deltaPercentage) + "%"}
            </span>
          </div>
        </div>

        {icon && (
          <div className="flex items-center justify-center w-1/4">
            <span className="text-primary">{icon}</span>
          </div>
        )}
      </div>

      <div className="flex items-center text-sm gap-1 pb-2">
        {delta != 0 && (
          <span className={"font-bold " + computeDeltaColor()}>
            {computeDeltaPercentageDirection()}
          </span>
        )}
        {delta != 0 && (
          <span className={computeDeltaColor() + " font-bold mr-2"}>
            {prefix}
            {typeof value === "string"
              ? delta
              : formatNumberForDisplay(Math.abs(delta))}
          </span>
        )}
        <span>{delta === 0 ? "No Change" : "Since last month"}</span>
      </div>
    </div>
  );
}
