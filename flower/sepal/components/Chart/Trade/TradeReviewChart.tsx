import { ApexChartCandleStick } from "@/types/apiTypes";
import { useMemo } from "react";
import Chart from "react-apexcharts";
import { CandleStickChartConfig } from "@/lib/apex-chart-constants";

/**
 * Renders a candlestick chart for reviewing trades
 *
 * @param data chart data
 * @author Stephen Prizio
 * @version 0.2.4
 */
export default function TradeReviewChart({
  data,
}: Readonly<{
  data: Array<ApexChartCandleStick>;
}>) {
  const formattedData = useMemo(() => {
    return [
      {
        data: data.map((item) => ({
          x: new Date(item.x),
          y: [item.y[0], item.y[1], item.y[2], item.y[3]],
        })),
      },
    ];
  }, [data]);

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
