import { TableCell, TableHead, TableRow } from "@/components/ui/table";
import moment from "moment";
import { DateTime } from "@/lib/constants";
import React from "react";
import {
  formatNegativePoints,
  formatNumberForDisplay,
} from "@/lib/functions/util-functions";
import { TradeRecordReport } from "@/types/apiTypes";
import BaseTableContainer from "@/components/Table/BaseTableContainer";

/**
 * Renders a table of Trade records
 *
 * @param records Trade records
 * @author Stephen Prizio
 * @version 1.0.0
 */
export default function TradeRecordTable({
  report,
  showTotals = false,
}: Readonly<{
  report: TradeRecordReport | null | undefined;
  showTotals?: boolean;
}>) {
  //  RENDER

  return (
    <div className="py-2">
      <BaseTableContainer
        headerContent={
          <TableRow className={"hover:bg-transparent"}>
            <TableHead className={"text-primary font-bold"}>Date</TableHead>
            <TableHead className={"text-center text-primary font-bold"}>
              Trades
            </TableHead>
            <TableHead className={"text-center text-primary font-bold"}>
              Win Rate
            </TableHead>
            <TableHead className={"text-center text-primary font-bold"}>
              Points
            </TableHead>
            <TableHead className={"text-right text-primary font-bold"}>
              P & L
            </TableHead>
          </TableRow>
        }
        bodyContent={
          <>
            {report?.tradeRecords?.map((item) => {
              return (
                <TableRow
                  key={item.uid + "_tr_" + item.start}
                  className={"hover:bg-transparent"}
                >
                  <TableCell>
                    {moment(item.start).format(DateTime.ISOWeekdayFormat)}
                    ,&nbsp;
                    {moment(item.start).format(DateTime.ISOMonthDayFormat)}
                  </TableCell>
                  <TableCell className={"text-center"}>{item.trades}</TableCell>
                  <TableCell className={"text-center"}>
                    {item.winPercentage}%
                  </TableCell>
                  <TableCell className={"text-center"}>
                    {formatNegativePoints(item.points)}
                  </TableCell>
                  <TableCell
                    className={
                      "text-right font-semibold " +
                      (item.netProfit >= 0
                        ? "text-primaryGreen"
                        : "text-primaryRed")
                    }
                  >
                    $&nbsp;{formatNumberForDisplay(item.netProfit)}
                  </TableCell>
                </TableRow>
              );
            })}
            {showTotals && (report?.tradeRecords?.length ?? 0) > 0 && (
              <TableRow
                className={
                  "hover:bg-transparent !border-t-2 !border-primaryLight text-primary"
                }
              >
                <TableCell className={"font-bold"}>
                  {report?.tradeRecordTotals?.count ?? 0}&nbsp;days
                </TableCell>
                <TableCell className={"text-center font-bold"}>
                  {report?.tradeRecordTotals?.trades ?? 0}
                </TableCell>
                <TableCell className={"text-center font-bold"}>
                  {report?.tradeRecordTotals?.winPercentage ?? 0}%
                </TableCell>
                <TableCell className={"text-center font-bold"}>
                  {formatNegativePoints(
                    report?.tradeRecordTotals?.netPoints ?? 0,
                  )}
                </TableCell>
                <TableCell className={"text-right font-bold "}>
                  $&nbsp;
                  {formatNumberForDisplay(
                    report?.tradeRecordTotals?.netProfit ?? 0,
                  )}
                </TableCell>
              </TableRow>
            )}
          </>
        }
      />
    </div>
  );
}
