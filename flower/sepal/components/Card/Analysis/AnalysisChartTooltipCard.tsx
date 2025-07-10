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
 * @version 1.0.0
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
      cardContent={
        <div className="grid grid-cols-2 gap-2 max-w-64 text-sm pt-4 pb-2">
          {payload?.map((item: any, itx: number) => {
            return (
              <>
                <div className="font-bold text-primary">{headerLabel}</div>
                <div className="text-right">{payload?.[0].payload.label}</div>
                <div className="font-bold text-primary">Value</div>
                <div className="text-right">
                  {filter === "PROFIT" && (
                    <span>
                      $&nbsp;
                      {formatNumberForDisplay(payload?.[0].payload.value)}
                    </span>
                  )}
                  {filter === "POINTS" && (
                    <span>
                      {formatNegativePoints(payload?.[0].payload.value)}
                    </span>
                  )}
                  {filter === "PERCENTAGE" && (
                    <span>{payload?.[0].payload.value}%</span>
                  )}
                </div>
                <div className="font-bold text-primary">Count</div>
                <div className="text-right">{payload?.[0].payload.count}</div>
              </>
            );
          })}
        </div>
      }
    />
  );
}
