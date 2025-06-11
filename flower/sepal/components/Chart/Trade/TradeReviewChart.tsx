import { Trade } from "@/types/apiTypes";
import { useApexChartQuery } from "@/lib/hooks/query/queries";
import LoadingPage from "@/app/loading";
import { logErrors } from "@/lib/functions/util-functions";
import Error from "@/app/error";
import { useMemo } from "react";
import Chart from "react-apexcharts";
import { CandleStickChartConfig } from "@/lib/apex-chart-constants";

/**
 * Renders a candlestick chart for reviewing trades
 *
 * @param trade trade
 * @param interval time interval
 * @author Stephen Prizio
 * @version 0.2.4
 */
export default function TradeReviewChart({
  trade,
  interval,
}: Readonly<{
  trade: Trade | null | undefined;
  interval: string;
}>) {
  const { data, isLoading, isError, error, isSuccess } = useApexChartQuery(
    trade?.tradeId ?? "-1",
    trade?.account.accountNumber ?? -1,
    interval,
  );

  const formattedData = useMemo(() => {
    if (!isSuccess || !data) return [];

    return [
      {
        data: data.map((item) => ({
          x: new Date(item.x),
          y: [item.y[0], item.y[1], item.y[2], item.y[3]],
        })),
      },
    ];
  }, [isSuccess, data]);

  if (isLoading) {
    return <LoadingPage />;
  }

  if (isError) {
    logErrors(error);
    return <Error />;
  }

  //  RENDER

  return (
    <div className={"py-4"}>
      <Chart
        options={CandleStickChartConfig.Options}
        series={formattedData}
        type="candlestick"
        height="350"
      />
    </div>
  );
}
