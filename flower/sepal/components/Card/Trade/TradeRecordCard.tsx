"use client";

import TradeRecordChart from "@/components/Chart/Trade/TradeRecordChart";
import { BaseCard } from "@/components/Card/BaseCard";
import moment from "moment";
import { DateTime } from "@/lib/constants";
import { Label } from "@/components/ui/label";
import { Switch } from "@/components/ui/switch";
import { useState } from "react";
import {
  formatNegativePoints,
  formatNumberForDisplay,
} from "@/lib/functions/util-functions";
import { TradeRecord } from "@/types/apiTypes";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";

/**
 * Renders a card for displaying Trade records
 *
 * @param tradeRecord Trade record
 * @param aggInterval aggregated interval
 * @author Stephen Prizio
 * @version 0.2.3
 */
export default function TradeRecordCard({
  tradeRecord,
  aggInterval,
}: Readonly<{
  tradeRecord: TradeRecord;
  aggInterval: string;
}>) {
  const [showPoints, setShowPoints] = useState(false);

  //  GENERAL FUNCTIONS

  /**
   * Formats the card date based on the aggregate interval
   *
   * @param val input date
   */
  function formatDate(val: string) {
    switch (aggInterval) {
      case "DAILY":
        return moment(val).format(DateTime.ISOMonthWeekDayFormat);
      case "MONTHLY":
        return moment(val).format(DateTime.ISOMonthYearFormat);
      case "YEARLY":
        return moment(val).format(DateTime.ISOYearFormat);
      default:
        return val;
    }
  }

  //  RENDER

  /**
   * Returns a simple cell for data with no bg
   *
   * @param val data to render
   * @param className custom css classes
   */
  const simpleCellNoBg = (val: string, className = "") => {
    return <div className={`py-1 px-2 text-sm ${className}`}>{val}</div>;
  };

  /**
   * Returns a simple cell for data
   *
   * @param val data to render
   * @param className custom css classes
   */
  const simpleCellWithBg = (val: string, className = "") => {
    return (
      <div className={`py-1 px-2 bg-primary bg-opacity-10 ${className}`}>
        {val}
      </div>
    );
  };

  return (
    <BaseCard
      title={formatDate(tradeRecord.start)}
      cardContent={
        <div
          className={"grid grid-cols-1 lg:grid-cols-5 gap-8 items-start mb-4"}
        >
          <div className={"lg:col-span-2"}>
            <TradeRecordChart
              data={tradeRecord.equityPoints}
              showAsPoints={showPoints}
              height={250}
            />
          </div>
          <div className={"lg:col-span-3"}>
            <div>
              <Table>
                <TableHeader>
                  <TableRow className={"hover:bg-transparent"}>
                    <TableHead
                      colSpan={3}
                      className={"text-primary font-semibold"}
                    >
                      Trading
                    </TableHead>
                    <TableHead
                      colSpan={3}
                      className={"text-primary font-semibold"}
                    >
                      Statistics
                    </TableHead>
                    <TableHead
                      colSpan={3}
                      className={"text-primary font-semibold"}
                    >
                      Results
                    </TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  <TableRow
                    className={"hover:bg-transparent border-transparent"}
                  >
                    <TableCell rowSpan={2}>
                      <div>
                        {tradeRecord.trades}
                        <small>
                          &nbsp;&nbsp;
                          {tradeRecord.trades === 1 ? "trade" : "trades"}
                        </small>
                      </div>
                    </TableCell>
                    <TableCell className={"text-right"}>
                      {tradeRecord.wins}
                      <small>
                        &nbsp;&nbsp;{tradeRecord.wins === 1 ? "win" : "wins"}
                      </small>
                    </TableCell>
                    <TableCell />
                    <TableCell rowSpan={2}>Average</TableCell>
                    <TableCell className={"text-right"}>
                      $&nbsp;{formatNumberForDisplay(tradeRecord.winAverage)}
                      <small>&nbsp;win</small>
                    </TableCell>
                    <TableCell />
                    <TableCell>P & L</TableCell>
                    <TableCell className={"text-right"}>
                      $&nbsp;{formatNumberForDisplay(tradeRecord.netProfit)}
                    </TableCell>
                  </TableRow>
                  <TableRow className={"hover:bg-transparent"}>
                    <TableCell className={"text-right"}>
                      {tradeRecord.losses}
                      <small>
                        &nbsp;&nbsp;
                        {tradeRecord.losses === 1 ? "loss" : "losses"}
                      </small>
                    </TableCell>
                    <TableCell />
                    <TableCell className={"text-right"}>
                      $&nbsp;{formatNumberForDisplay(tradeRecord.lossAverage)}
                      <small>&nbsp;loss</small>
                    </TableCell>
                    <TableCell />
                    <TableCell>Win %</TableCell>
                    <TableCell className={"text-right"}>
                      {tradeRecord.winPercentage}%
                    </TableCell>
                  </TableRow>
                  <TableRow
                    className={"hover:bg-transparent border-transparent"}
                  >
                    <TableCell rowSpan={2}>
                      <div>
                        {formatNegativePoints(tradeRecord.points)}
                        <small>&nbsp;points</small>
                      </div>
                    </TableCell>
                    <TableCell className={"text-right"}>
                      {tradeRecord.pointsGained}
                      <small>&nbsp;gained</small>
                    </TableCell>
                    <TableCell />
                    <TableCell rowSpan={2}>Largest</TableCell>
                    <TableCell className={"text-right"}>
                      $&nbsp;{formatNumberForDisplay(tradeRecord.largestWin)}
                      <small>&nbsp;win</small>
                    </TableCell>
                    <TableCell />
                    <TableCell>Profitability</TableCell>
                    <TableCell className={"text-right"}>
                      {tradeRecord.profitability}
                    </TableCell>
                  </TableRow>
                  <TableRow className={"hover:bg-transparent"}>
                    <TableCell className={"text-right"}>
                      {tradeRecord.pointsLost}
                      <small>&nbsp;lost</small>
                    </TableCell>
                    <TableCell />
                    <TableCell className={"text-right"}>
                      $&nbsp;{formatNumberForDisplay(tradeRecord.largestLoss)}
                      <small>&nbsp;loss</small>
                    </TableCell>
                    <TableCell />
                    <TableCell>Retention</TableCell>
                    <TableCell className={"text-right"}>
                      {tradeRecord.retention}%
                    </TableCell>
                  </TableRow>
                  <TableRow className={"hover:bg-transparent"}>
                    <TableCell />
                    <TableCell />
                    <TableCell />
                    <TableCell>Drawdown</TableCell>
                    <TableCell className={"text-right"}>
                      $&nbsp;{formatNumberForDisplay(tradeRecord.lowestPoint)}
                    </TableCell>
                    <TableCell />
                    <TableCell />
                    <TableCell />
                  </TableRow>
                </TableBody>
              </Table>
            </div>
          </div>
        </div>
      }
      headerControls={[
        <div key={0} className="flex items-center space-x-2">
          <Label htmlFor="airplane-mode">Show as Points</Label>
          <Switch
            id="airplane-mode"
            checked={showPoints}
            onCheckedChange={setShowPoints}
          />
        </div>,
      ]}
    />
  );
}
