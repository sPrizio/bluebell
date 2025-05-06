import {formatNumberForDisplay} from "@/lib/functions/util-functions";
import {Icons} from "@/lib/enums";
import React from "react";
import {resolveIcon} from "@/lib/functions/util-component-functions";

/**
 * Component that renders small cards meant to act as overview Account summaries
 *
 * @param prefix text to inject before value
 * @param value value
 * @param delta change
 * @param icon icon to render
 * @author Stephen Prizio
 * @version 0.2.0
 */
export default function DashboardContent(
  {
    prefix = '',
    value = 0.0,
    delta = 0.0,
    icon = null
  }
    : Readonly<{
    prefix?: string,
    value: number | string,
    delta?: number,
    icon?: React.ReactNode,
  }>
) {


  //  GENERAL FUNCTIONS

  /**
   * Computes the font color based on the delta value
   */
  function computeDeltaColor() {
    if (delta > 0) {
      return ' text-primaryGreen '
    } else if (delta < 0) {
      return ' text-primaryRed '
    }

    return ' text-slate-500 '
  }

  /**
   * Computes the type of icon to render based on the delta value
   */
  function computeDeltaDirection() {
    if (delta > 0) {
      return resolveIcon(Icons.ArrowUp, '', 18)
    } else if (delta < 0) {
      return resolveIcon(Icons.ArrowDown, '', 18)
    }

    return resolveIcon(Icons.ArrowLeftRight, '', 18)
  }


  //  RENDER

  return (
    <div className={"flex gap-2 items-start w-full"}>
      <div className={"grow"}>
        <span className={'text-xl'}>{prefix}{typeof value === "string" ? value : formatNumberForDisplay(value)}</span>
        <br/>
        <div className={'flex items-center text-sm gap-1 pt-1'}>
          <span className={'inline-block font-bold' + computeDeltaColor()}>{computeDeltaDirection()}</span><span className={computeDeltaColor() + 'font-bold'}>{formatNumberForDisplay(Math.abs(delta))}%</span>
          <span className={''}>Since last month</span>
        </div>
        &nbsp;
      </div>
      {
        icon ?
          <div className={"flex items-center justify-center w-1/4"}>
            <span className={'text-primary'}>{icon}</span>
          </div>
          : null
      }
    </div>
  )
}