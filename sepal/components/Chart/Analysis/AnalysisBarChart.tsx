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
  YAxis
} from "recharts";
import {Css} from "@/lib/constants";
import {BaseCard} from "@/components/Card/BaseCard";
import {NameType, ValueType,} from 'recharts/types/component/DefaultTooltipContent';
import {AnalysisResult, FilterSelector} from "@/types/apiTypes";
import {formatNegativePoints, formatNumberForDisplay} from "@/lib/functions/util-functions";

/**
 * Renders a bar chart based on the analysis data
 *
 * @param data data
 * @param filter filter
 * @author Stephen Prizio
 * @version 0.0.2
 */
export default function AnalysisBarChart(
  {
    data,
    filter = "PROFIT",
  }
  : Readonly<{
    data: Array<AnalysisResult>,
    filter: FilterSelector
  }>
) {


  //  COMPONENTS

  /**
   * Renders a custom tooltip for the chart
   *
   * @param active is active
   * @param payload data
   * @param label label
   */
  const tooltip = ({active, payload, label}: TooltipProps<ValueType, NameType>) => {

    if (active && (payload?.length ?? -1 > 0)) {
      return (
        <BaseCard
          title={'Overview'}
          cardContent={
            <div className={'mb-4 grid grid-cols-2 gap-4 w-[200px]'}>
              <div>Time </div>
              <div className={'text-right'}>{payload?.[0].payload.label}</div>
              <div>Value</div>
              <div className={'text-right'}>
                {filter === "PROFIT" && <span>$&nbsp;{formatNumberForDisplay(payload?.[0].payload.value)}</span>}
                {filter === "POINTS" && <span>{formatNegativePoints(payload?.[0].payload.value)}</span>}
                {filter === "PERCENTAGE" && <span>{payload?.[0].payload.value}%</span>}
              </div>
              <div>Count</div>
              <div className={'text-right'}>{payload?.[0].payload.count}</div>
            </div>
          }
        />
      );
    }

    return null;
  }


  //  RENDER

  return (
    <div className={''}>
      <ResponsiveContainer width="100%" minHeight={350}>
        <BarChart data={data}>
          <CartesianGrid strokeDasharray="3 3" vertical={false}/>
          <XAxis dataKey="label"/>
          <YAxis/>
          <Tooltip content={tooltip}/>
          <ReferenceLine y={0} stroke="#000"/>
          <Bar dataKey="value" barSize={(data?.length ?? 0) > 15 ? 20 : 50} fill={Css.ColorPrimary}/>
          {/*<Bar dataKey="uv" fill="#82ca9d" />*/}
        </BarChart>
      </ResponsiveContainer>
    </div>
  )
}