import React from "react";
import {
  Bar,
  BarChart,
  CartesianGrid,
  ReferenceLine,
  ResponsiveContainer,
  Tooltip,
  TooltipProps,
  XAxis,
  YAxis,
} from "recharts";
import { Css } from "@/lib/constants";
import { BaseCard } from "@/components/Card/BaseCard";
import {
  NameType,
  ValueType,
} from "recharts/types/component/DefaultTooltipContent";
import { AnalysisResult, FilterSelector } from "@/types/apiTypes";
import {
  formatNegativePoints,
  formatNumberForDisplay,
} from "@/lib/functions/util-functions";

/**
 * Renders a bar chart based on the analysis data
 *
 * @param data data
 * @param filter filter
 * @param tooltip tooltip react component
 * @author Stephen Prizio
 * @version 0.2.4
 */
export default function AnalysisBarChart({
  data,
  filter = "PROFIT",
  tooltip,
}: Readonly<{
  data: Array<AnalysisResult>;
  filter: FilterSelector;
  tooltip: React.ReactNode;
}>) {
  //  COMPONENTS

  /**
   * Renders a custom tooltip for the chart
   *
   * @param active is active
   * @param payload data
   * @param label label
   */
  const tooltipRender = ({
    active,
    payload,
    label,
  }: TooltipProps<ValueType, NameType>) => {
    if (active && payload && payload.length > 0 && tooltip) {
      // @ts-expect-error skip type checking for recharts dependency
      return React.cloneElement(tooltip, { payload });
    }

    return null;
  };

  //  RENDER

  return (
    <div className={""}>
      <ResponsiveContainer width="100%" minHeight={350}>
        <BarChart data={data}>
          <CartesianGrid strokeDasharray="3 3" vertical={false} />
          <XAxis dataKey="label" />
          <YAxis />
          <Tooltip content={tooltipRender} />
          <ReferenceLine y={0} stroke="#000" />
          <Bar
            dataKey="value"
            barSize={(data?.length ?? 0) > 15 ? 20 : 50}
            fill={Css.ColorPrimary}
          />
          {/*<Bar dataKey="uv" fill="#82ca9d" />*/}
        </BarChart>
      </ResponsiveContainer>
    </div>
  );
}
