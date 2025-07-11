"use client";

import React, { useMemo } from "react";
import {
  Area,
  ComposedChart,
  ResponsiveContainer,
  Tooltip,
  TooltipProps,
  YAxis,
} from "recharts";
import {
  NameType,
  ValueType,
} from "recharts/types/component/DefaultTooltipContent";
import { Css, DateTime } from "@/lib/constants";
import { BaseCard } from "@/components/Card/BaseCard";
import moment from "moment";
import { formatNumberForDisplay } from "@/lib/functions/util-functions";
import { AccountEquityPoint } from "@/types/apiTypes";

/**
 * Renders a chart to display an Account's growth over time
 *
 * @param data Account equity data points
 * @param showPoints show balance or points
 * @author Stephen Prizio
 * @version 1.0.0
 */
export default function AccountEquityChart({
  data = [],
  showPoints = false,
}: Readonly<{
  data: Array<AccountEquityPoint>;
  showPoints?: boolean;
}>) {
  const chartData = useMemo(() => {
    return data.map((item) => ({
      date: item.date,
      value: showPoints ? item.cumPoints : item.cumAmount,
    }));
  }, [data, showPoints]);

  //  GENERAL FUNCTIONS

  /**
   * Gets the min value
   */
  function getMin() {
    const min = data.map((d) => d.amount);
    return Math.min(...min) * 0.9975;
  }

  /**
   * Gets the max value
   */
  function getMax() {
    const max = data.map((d) => d.amount);
    return Math.max(...max) * 1.0025;
  }

  //  COMPONENTS

  /**
   * Renders a custom tooltip for the chart
   *
   * @param active is active
   * @param payload data
   * @param label label
   */
  const tooltip = ({
    active,
    payload,
    label,
  }: TooltipProps<ValueType, NameType>) => {
    if (active && (payload?.length ?? -1 > 0)) {
      const date = payload?.[0].payload.date ?? "";
      return (
        <BaseCard
          cardContent={
            <div className="grid grid-cols-2 gap-2 max-w-64 text-sm pt-4 pb-2">
              {payload?.map((item: any, itx: number) => {
                return (
                  <>
                    <div className="font-bold text-primary">Date</div>
                    <div className="text-right">
                      {moment(date).format(DateTime.ISOLongMonthDayYearFormat)}
                    </div>
                    <div className="font-bold text-primary">Time</div>
                    <div className="text-right">
                      {moment(date).format(DateTime.ISOShortTimeFormat)}
                    </div>
                    <div className="font-bold text-primary">Balance</div>
                    <div className="text-right">
                      {showPoints
                        ? formatNumberForDisplay(item.value)
                        : "$ " + formatNumberForDisplay(item.value)}
                    </div>
                  </>
                );
              })}
            </div>
          }
        />
      );
    }

    return null;
  };

  //  RENDER

  return (
    <div className={"flex items-center justify-center py-4"}>
      {data && data.length > 1 && (
        <div className={"w-[100%]"} key={showPoints.toString()}>
          <ResponsiveContainer width="100%" minHeight={400}>
            <ComposedChart data={chartData}>
              <defs>
                <linearGradient id="color" x1="0" y1="0" x2="0" y2="1">
                  <stop
                    offset="5%"
                    stopColor={`${Css.ColorPrimary}`}
                    stopOpacity={0.8}
                  />
                  <stop
                    offset="95%"
                    stopColor={`${Css.ColorPrimary}`}
                    stopOpacity={0}
                  />
                </linearGradient>
              </defs>
              <YAxis
                orientation={"right"}
                hide={true}
                dominantBaseline={getMin()}
                domain={["dataMin", "dataMax"]}
              />
              <Tooltip content={tooltip} />
              <Area
                type="monotone"
                dot={false}
                dataKey="value"
                stroke={`${Css.ColorPrimary}`}
                strokeWidth={4}
                fill="url(#color)"
              />
            </ComposedChart>
          </ResponsiveContainer>
        </div>
      )}
    </div>
  );
}
