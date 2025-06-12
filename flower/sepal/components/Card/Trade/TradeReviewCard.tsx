"use client";

import TradeReviewChart from "@/components/Chart/Trade/TradeReviewChart";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { BaseCard } from "@/components/Card/BaseCard";
import React, { useEffect, useState } from "react";
import {
  useApexChartQuery,
  useMarketPriceTimerIntervalQuery,
} from "@/lib/hooks/query/queries";
import { logErrors } from "@/lib/functions/util-functions";
import Error from "@/app/error";
import { Trade } from "@/types/apiTypes";

/**
 * Renders the card that shows a trade review chart
 *
 * @param trade trade
 * @author Stephen Prizio
 * @version 0.2.4
 */
export default function TradeReviewCard({
  trade,
}: Readonly<{ trade: Trade | undefined | null }>) {
  const [chartInterval, setChartInterval] = useState("default");

  const {
    data: intervals,
    isError: isIntervalError,
    isLoading: isIntervalLoading,
    error: intervalError,
    isSuccess: isIntervalSuccess,
  } = useMarketPriceTimerIntervalQuery();

  const {
    data: apexData,
    isLoading: isApexLoading,
    isError: isApexError,
    error: apexError,
  } = useApexChartQuery(
    trade?.tradeId ?? "-1",
    trade?.account.accountNumber ?? -1,
    chartInterval,
  );

  useEffect(() => {
    if (isIntervalSuccess) {
      setChartInterval(intervals[1].code);
    }
  }, [isIntervalSuccess]);

  if (isIntervalError || isApexError) {
    logErrors(intervalError, apexError);
    return <Error />;
  }

  //  RENDER

  return (
    <BaseCard
      title={"Trade Review"}
      loading={isIntervalLoading || isApexLoading}
      includeSkeleton={false}
      subtitle={"Review the trade as it was taken."}
      cardContent={
        (apexData?.length ?? 0) > 0 ? (
          <TradeReviewChart trade={trade} data={apexData ?? []} />
        ) : null
      }
      headerControls={[
        <div key={0}>
          <Select
            value={chartInterval}
            onValueChange={(val) => setChartInterval(val)}
          >
            <SelectTrigger className="w-[120px] bg-white">
              <SelectValue placeholder={"Select a value..."} />
            </SelectTrigger>
            <SelectContent>
              {intervals?.map((inter) => {
                return (
                  <SelectItem key={inter.code} value={inter.code}>
                    {inter.label}
                  </SelectItem>
                );
              }) ?? null}
            </SelectContent>
          </Select>
        </div>,
      ]}
      emptyText={"Trade cannot be reviewed at this time. Come back later."}
    />
  );
}
