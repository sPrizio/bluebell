"use client";

import React from "react";
import {
  Area,
  ComposedChart,
  Legend,
  ResponsiveContainer,
  Tooltip,
  TooltipProps,
  YAxis,
} from "recharts";
import {
  NameType,
  ValueType,
} from "recharts/types/component/DefaultTooltipContent";
import { BASE_COLORS, Css, DateTime } from "@/lib/constants";
// @ts-expect-error: error in import due to 3rd party library
import Please from "pleasejs/dist/Please";
import { BaseCard } from "@/components/Card/BaseCard";
import moment from "moment";
import { formatNumberForDisplay } from "@/lib/functions/util-functions";
import { PortfolioEquityPoint } from "@/types/apiTypes";
import { resolveIcon } from "@/lib/functions/util-component-functions";
import { Icons } from "@/lib/enums";

/**
 * Renders a chart to display an account's growth over time
 *
 * @param data account equity data points
 * @author Stephen Prizio
 * @version 1.0.0
 */
export default function PortfolioGrowthChart({
  data = [],
}: Readonly<{
  data: Array<PortfolioEquityPoint>;
}>) {
  const colors: Array<string> = [];
  for (let i = 0; i < data.length; i++) {
    if (i >= 0 && i < data.length) {
      colors.push(Please.make_color({ base_color: BASE_COLORS[i] }));
    } else {
      colors.push(Please.make_color({ base_color: BASE_COLORS[0] }));
    }
  }

  const accChartData = generateAccData();

  //  GENERAL FUNCTIONS

  /**
   * Computes the gradient
   */
  function computeGradientLimit() {
    const tempPoints = data;
    const min = Math.min(...tempPoints.map((i) => i.normalized));
    const max = Math.max(...tempPoints.map((i) => i.normalized));

    return Math.round(((max - min) / max) * 100.0) + "%";
  }

  /**
   * Gets all the chart keys
   */
  function getAccountKeys() {
    if (data.length > 0) {
      const keys = [];

      for (const point of data) {
        const accounts = point.accounts;
        // @ts-expect-error : ignore typing
        for (const acc of accounts) {
          if (keys.indexOf(acc.name) === -1) {
            keys.push(acc.name);
          }
        }
      }

      return keys;
    }

    return [];
  }

  /**
   * Resolves data
   */
  function resolveChartDataPointValue(
    point: PortfolioEquityPoint,
    key: string,
  ) {
    // @ts-expect-error : ignore typing
    for (const acc of point.accounts) {
      if (acc.name === key) {
        return acc.value;
      }
    }

    return 0;
  }

  /**
   * Generates the chart data compatible with recharts
   */
  function generateAccData() {
    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
    const res: any[] = [];

    for (const point of data) {
      const pointData = {
        date: point.date,
        portfolio: point.portfolio,
      };

      for (const key of getAccountKeys()) {
        const obj: Record<string, number> = {
          [key]: resolveChartDataPointValue(point, key),
        };

        // @ts-expect-error : ignore typing
        pointData[key] = obj[key];
      }

      res.push(pointData);
    }

    return res;
  }

  /**
   * Determines if the chart should show more than 1 account
   */
  function hasMultipleAccounts() {
    if (data.length > 0) {
      return getAccountKeys().length > 1;
    }

    return false;
  }

  //  COMPONENTS

  /**
   * Renders a custom legend for the chart
   *
   * @param props payload
   */
  // eslint-disable-next-line @typescript-eslint/ban-ts-comment
  const legend = (props: any) => {
    const { payload } = props;

    return (
      <div className={"flex flex-row items-center justify-end gap-4 text-sm "}>
        {
          // eslint-disable-next-line @typescript-eslint/ban-ts-comment
          payload
            .filter((item: any) => item.dataKey !== "portfolio")
            .map((item: any, itx: number) => {
              return (
                <div
                  key={item.dataKey}
                  className={"flex items-center justify-end "}
                  style={{ color: colors[itx] }}
                >
                  <span className={"inline-block"}>
                    {resolveIcon(Icons.PointFilled, "", 15)}
                  </span>
                  &nbsp;{item.value}
                </div>
              );
            })
        }
        <div className={"flex items-center justify-end text-primary"}>
          <span className={"inline-block"}>
            {resolveIcon(Icons.PointFilled, "", 15)}
          </span>
          &nbsp;Portfolio
        </div>
      </div>
    );
  };

  /**
   * Renders a custom tooltip for the chart
   *
   * @param active is active
   * @param payload data
   * @param label label
   */
  const tooltip = ({ active, payload }: TooltipProps<ValueType, NameType>) => {
    if (active && (payload?.length ?? -1 > 0)) {
      const date = payload?.[0].payload.date ?? "";
      return (
        <BaseCard
          cardContent={
            <div className="grid grid-cols-2 gap-2 max-w-64 text-sm pt-4 pb-2 items-center">
              <div className="font-bold">Period</div>
              <div className="text-right">
                {moment(date).format(DateTime.ISOMonthYearFormat)}
              </div>
              {payload?.map((item: any, itx: number) => {
                return (
                  <>
                    <div
                      className="font-bold capitalize"
                      style={{ color: item.color }}
                    >
                      {item.dataKey}
                    </div>
                    <div className="text-right">
                      {"$"}&nbsp;{formatNumberForDisplay(item.value)}
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
    <>
      {(data?.length ?? 0) > 0 && (
        <div className={"flex items-center justify-center pb-2"}>
          <div className={"w-[100%]"}>
            <ResponsiveContainer width="100%" minHeight={350}>
              <ComposedChart data={accChartData}>
                <defs>
                  <linearGradient id="color" x1="0" y1="0" x2="0" y2="1">
                    <stop
                      offset="5%"
                      stopColor={`${Css.ColorPrimary}`}
                      stopOpacity={0.8}
                    />
                    <stop
                      offset={computeGradientLimit()}
                      stopColor={`${Css.ColorPrimary}`}
                      stopOpacity={0}
                    />
                  </linearGradient>
                </defs>
                <Legend content={legend} verticalAlign={"top"} height={40} />
                {hasMultipleAccounts() ? (
                  <Legend content={legend} verticalAlign={"top"} height={40} />
                ) : null}
                <Tooltip content={tooltip} />
                {hasMultipleAccounts()
                  ? getAccountKeys().map((item: string, itx: number) => {
                      return (
                        <Area
                          key={itx + 1}
                          type="monotone"
                          dot={false}
                          dataKey={item}
                          strokeWidth={4}
                          stroke={colors[itx]}
                          fill={colors[itx]}
                          stackId="1"
                        />
                      );
                    })
                  : null}
                <YAxis type="number" hide={true} />
                <Area
                  type="monotone"
                  dot={false}
                  dataKey="portfolio"
                  stackId="1"
                  stroke={`${Css.ColorPrimary}`}
                  strokeWidth={4}
                  fill="url(#color)"
                />
              </ComposedChart>
            </ResponsiveContainer>
          </div>
        </div>
      )}
      {data?.length === 0 && (
        <div className="text-center text-slate-500 mt-2 mb-6 text-sm">
          You haven&apos;t taken any trades or made any deposits yet. Once you
          do, this chart will update.
        </div>
      )}
    </>
  );
}
