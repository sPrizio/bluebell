import { Css, DateTime } from "@/lib/constants";
import moment from "moment";
import { formatNumberForDisplay } from "@/lib/functions/util-functions";
import { ApexOptions } from "apexcharts";

const CandleStickChartConfigMarketPriceTooltip = (
  date: string,
  open: number,
  high: number,
  low: number,
  close: number,
) => {
  return `<div class="grid grid-cols-2 gap-2 max-w-64 p-4">
        <div class="font-bold text-primary">Time</div>
        <div class="text-right">${date}</div>
        <div class="font-bold text-primary">Open</div>
        <div class="text-right">${formatNumberForDisplay(open)}</div>
        <div class="font-bold text-primary">High</div>
        <div class="text-right">${formatNumberForDisplay(high)}</div>
        <div class="font-bold text-primary">Low</div>
        <div class="text-right">${formatNumberForDisplay(low)}</div>
        <div class="font-bold text-primary">Close</div>
        <div class="text-right">${formatNumberForDisplay(close)}</div>
      </div>`;
};

const CandleStickChartConfigTradeTooltip = (
  date: string,
  open: number,
  high: number,
  low: number,
  close: number,
) => {
  return `<div class="grid grid-cols-2 gap-2 max-w-64 p-4">
        <div class="font-bold text-primary">Open Price</div>
        <div class="text-right">${date}</div>
        <div class="font-bold text-primary">Stop Loss</div>
        <div class="text-right">${formatNumberForDisplay(open)}</div>
        <div class="font-bold text-primary">Take Profit</div>
        <div class="text-right">${formatNumberForDisplay(high)}</div>
      </div>`;
};

const CandleStickChartConfigAxisStyle = {
  colors: Css.ColorFontPrimary,
  fontWeight: "700",
};

export const CandleStickChartConfig: { Options: ApexOptions } = {
  Options: {
    chart: {
      type: "candlestick",
    },
    grid: {
      show: false,
    },
    legend: {
      show: false,
    },
    plotOptions: {
      candlestick: {
        colors: {
          upward: Css.ColorGreen,
          downward: Css.ColorRed,
        },
      },
    },
    stroke: {
      width: [2, 1],
      colors: [Css.ColorPrimary, "rgb(0, 0, 0)"],
      curve: "smooth",
    },
    tooltip: {
      custom: function ({
        seriesIndex,
        dataPointIndex,
        w,
      }: {
        seriesIndex: number;
        dataPointIndex: number;
        w: any;
      }) {
        const date = w.config.series[seriesIndex].data[dataPointIndex].x;
        const data = w.config.series[seriesIndex].data[dataPointIndex].y;

        if (w.config.series[seriesIndex].name === "Trade") {
          return CandleStickChartConfigTradeTooltip(
            moment(date).format(DateTime.ISOShortTimeFormat),
            data[0],
            data[1],
            data[2],
            data[3],
          );
        } else {
          return CandleStickChartConfigMarketPriceTooltip(
            moment(date).format(DateTime.ISOShortTimeFormat),
            data[0],
            data[1],
            data[2],
            data[3],
          );
        }
      },
    },
    xaxis: {
      type: "datetime",
      labels: {
        formatter: function (val: string) {
          return moment(val).format(DateTime.ISOShortTimeFormat);
        },
        style: CandleStickChartConfigAxisStyle,
      },
      tooltip: {
        enabled: false,
      },
    },
    yaxis: {
      axisBorder: {
        show: true,
      },
      tooltip: {
        enabled: false,
      },
      labels: {
        formatter: function (val: number) {
          return formatNumberForDisplay(val);
        },
        style: CandleStickChartConfigAxisStyle,
      },
    },
    annotations: {
      yaxis: [],
    },
  },
};
