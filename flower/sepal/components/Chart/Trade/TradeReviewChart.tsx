import { ApexChartCandleStick, Trade } from "@/types/apiTypes";
import { useMemo } from "react";
import Chart from "react-apexcharts";
import { CandleStickChartConfig } from "@/lib/apex-chart-constants";
import moment from "moment";
import { Css } from "@/lib/constants";

/**
 * Renders a candlestick chart for reviewing trades
 *
 * @param trade trade
 * @param data chart data
 * @author Stephen Prizio
 * @version 0.2.4
 */
export default function TradeReviewChart({
  trade,
  data,
}: Readonly<{
  trade: Trade | null | undefined;
  data: Array<ApexChartCandleStick>;
}>) {
  const formattedData = useMemo(() => {
    return [
      ...(trade?.tradeCloseTime
        ? [
            {
              name: "Trade",
              type: "line",
              data: [
                {
                  x: moment(trade.tradeOpenTime).toDate(),
                  y: trade.openPrice,
                  meta: { id: "tradeOpen" },
                },
                {
                  x: moment(trade.tradeCloseTime).toDate(),
                  y: trade.closePrice,
                  meta: { id: "tradeClose" },
                },
              ],
            },
          ]
        : []),
      {
        name: "Market Prices",
        type: "candlestick",
        data: data.map((item) => ({
          x: new Date(item.x),
          y: [item.y[0], item.y[1], item.y[2], item.y[3]],
        })),
      },
    ];
  }, [
    data,
    trade?.tradeCloseTime,
    trade?.tradeOpenTime,
    trade?.openPrice,
    trade?.closePrice,
  ]);

  const additionalOptions = useMemo(() => {
    const options = CandleStickChartConfig.Options;

    if ((trade?.stopLoss ?? 0) !== 0) {
      options.annotations?.yaxis?.push({
        y: trade?.stopLoss,
        borderColor: Css.ColorRed,
        label: {
          borderColor: Css.ColorRed,
          offsetY: 25,
          style: {
            color: Css.ColorWhite,
            background: Css.ColorRed,
            padding: {
              right: 15,
              bottom: 4,
            },
          },
          text: "Stop Loss",
        },
      });
    }

    if ((trade?.takeProfit ?? 0) !== 0) {
      options.annotations?.yaxis?.push({
        y: trade?.takeProfit,
        borderColor: Css.ColorGreen,
        label: {
          borderColor: Css.ColorGreen,
          offsetY: -10,
          style: {
            color: Css.ColorWhite,
            background: Css.ColorGreen,
            padding: {
              right: 15,
              bottom: 4,
            },
          },
          text: "Take Profit",
        },
      });
    }

    return options;
  }, [trade?.stopLoss, trade?.takeProfit]);

  //  RENDER

  return (
    <div className={"py-4"}>
      <Chart
        options={additionalOptions}
        series={formattedData}
        type="candlestick"
        height="350"
      />
    </div>
  );
}
