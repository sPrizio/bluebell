import {
  formatNegativePoints,
  formatNumberForDisplay,
} from "@/lib/functions/util-functions";
import { BaseCard } from "@/components/Card/BaseCard";
import React from "react";
import { FilterSelector } from "@/types/apiTypes";
import {
  NameType,
  Payload,
  ValueType,
} from "recharts/types/component/DefaultTooltipContent";

/**
 * Renders a custom tooltip for the analysis charts
 *
 * @param filter filter selected
 * @param headerLabel label for x-axis
 * @param payload recharts payload
 * @author Stephen Prizio
 * @version 0.2.4
 */
export default function AnalysisChartTooltipCard({
  filter,
  headerLabel,
  payload,
}: Readonly<{
  filter: FilterSelector;
  headerLabel: string;
  payload?: Payload<ValueType, NameType>[] | undefined;
}>) {
  //  RENDER
  return (
    <BaseCard
      title={"Overview"}
      cardContent={
        <div className={"mb-4 grid grid-cols-2 gap-4 w-[250px]"}>
          <div>{headerLabel}</div>
          <div className={"text-right"}>{payload?.[0].payload.label}</div>
          <div>Value</div>
          <div className={"text-right"}>
            {filter === "PROFIT" && (
              <span>
                $&nbsp;{formatNumberForDisplay(payload?.[0].payload.value)}
              </span>
            )}
            {filter === "POINTS" && (
              <span>{formatNegativePoints(payload?.[0].payload.value)}</span>
            )}
            {filter === "PERCENTAGE" && (
              <span>{payload?.[0].payload.value}%</span>
            )}
          </div>
          <div>Count</div>
          <div className={"text-right"}>{payload?.[0].payload.count}</div>
        </div>
      }
    />
  );
}
