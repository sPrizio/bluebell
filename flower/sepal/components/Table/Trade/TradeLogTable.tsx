import React from "react";
import {
  Table,
  TableBody,
  TableCaption,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import Link from "next/link";
import {
  formatNegativePoints,
  formatNumberForDisplay,
} from "@/lib/functions/util-functions";
import moment from "moment";
import { DateTime } from "@/lib/constants";
import { TradeLog, TradeRecord } from "@/types/apiTypes";
import { resolveIcon } from "@/lib/functions/util-component-functions";
import { Icons } from "@/lib/enums";
import BaseTableContainer from "@/components/Table/BaseTableContainer";

interface ReducedRecord {
  accountName: string;
  accountNumber: number;
  tradeRecord: TradeRecord;
}

interface GroupedEntry {
  date: string;
  records: Array<ReducedRecord>;
}

/**
 * Trade history table showing days and all accounts traded on that day
 *
 * @param log Trade log
 * @param showTotals show totals row
 * @author Stephen Prizio
 * @version 0.2.4
 */
export default function TradeLogTable({
  log,
  showTotals = false,
}: Readonly<{
  log: TradeLog | null | undefined;
  showTotals?: boolean;
}>) {
  const reducedRecords: Array<ReducedRecord> = generateReducedRecords();
  const dateList: Array<string> = generateDateList();
  const groupedEntries: Array<GroupedEntry> = groupTradeRecords();

  //  GENERAL FUNCTIONS

  /**
   * Generates a list of unique dates for sorting the trade log
   */
  function generateDateList(): Array<string> {
    return Array.from(
      new Set(reducedRecords?.map((rr) => rr.tradeRecord.end) ?? []),
    );
  }

  /**
   * Reduces the data model to list of trade records and their account info
   */
  function generateReducedRecords(): Array<ReducedRecord> {
    const arr: Array<ReducedRecord> = [];

    const groupedRecords: Array<{
      accountNumber: number;
      accountName: string;
      tradeRecords: Array<TradeRecord>;
    }> =
      log?.entries
        ?.flatMap((entry) => entry.records)
        ?.flatMap((record) => {
          return {
            accountName: record.accountName,
            accountNumber: record.accountNumber,
            tradeRecords: record.report.tradeRecords,
          };
        }) ?? [];

    for (const item of groupedRecords) {
      for (const rec of item.tradeRecords) {
        arr.push({
          accountName: item.accountName,
          accountNumber: item.accountNumber,
          tradeRecord: rec,
        });
      }
    }

    return arr;
  }

  /**
   * Groups the trade records into something consumable by the component renderer
   */
  function groupTradeRecords(): Array<GroupedEntry> {
    const arr: Array<GroupedEntry> = [];

    for (const date of dateList) {
      arr.push({
        date: date,
        records:
          reducedRecords?.filter((rr) => rr.tradeRecord.end === date) ?? [],
      });
    }

    return arr?.sort((a, b) => b.date.localeCompare(a.date)) ?? [];
  }

  //  RENDER

  return (
    <div className={"py-4"}>
      {(!log || (log?.entries?.length ?? 0) === 0) && (
        <div className={"text-center text-slate-500 pb-2 mb-4 text-sm"}>
          No recent trade records.
        </div>
      )}
      {(log?.entries?.length ?? 0) > 0 && (
        <BaseTableContainer
          height={500}
          table={
            <Table>
              <TableCaption>
                <div
                  className={
                    "flex items-center justify-center gap-1 pb-2 mt-4 text-sm"
                  }
                >
                  <div className={""}>
                    <Link href={"/performance?account=default"}>
                      View Full Performance
                    </Link>
                  </div>
                  <div className={""}>
                    <Link href={"/performance?account=default"}>
                      {resolveIcon(Icons.ExternalLink, "", 18)}
                    </Link>
                  </div>
                </div>
              </TableCaption>
              <TableHeader>
                <TableRow className={"hover:bg-transparent"}>
                  <TableHead>Date</TableHead>
                  <TableHead>Account</TableHead>
                  <TableHead className={"text-center"}>Trades</TableHead>
                  <TableHead className={"text-center"}>Win %</TableHead>
                  <TableHead className={"text-center"}>Points</TableHead>
                  <TableHead className="text-right">P & L</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {log?.entries?.map((item, idx) => {
                  return (
                    <React.Fragment key={"rf" + idx}>
                      {groupedEntries?.map((gr, grIdx) => {
                        return (
                          <React.Fragment key={"gr" + grIdx + "rf"}>
                            <TableRow
                              key={gr + "gr" + idx}
                              className={"hover:bg-transparent"}
                            >
                              <TableCell
                                colSpan={6}
                                className={"text-primary font-semibold"}
                              >
                                {moment(gr.date).format(
                                  DateTime.ISOMonthWeekDayFormat,
                                )}
                              </TableCell>
                            </TableRow>
                            {gr.records?.map((rec) => {
                              return (
                                <TableRow
                                  key={
                                    rec.tradeRecord.end +
                                    rec.tradeRecord.trades +
                                    rec.tradeRecord.netProfit +
                                    rec.tradeRecord.points
                                  }
                                  className={"hover:bg-transparent"}
                                >
                                  <TableCell className={"invisible"}>
                                    {moment(rec.tradeRecord.end).format(
                                      DateTime.ISOMonthWeekDayFormat,
                                    )}
                                  </TableCell>
                                  <TableCell>{rec.accountName}</TableCell>
                                  <TableCell className={"text-center"}>
                                    {rec.tradeRecord.trades}
                                  </TableCell>
                                  <TableCell className={"text-center"}>
                                    {rec.tradeRecord.winPercentage}
                                  </TableCell>
                                  <TableCell className={"text-center"}>
                                    {formatNegativePoints(
                                      rec.tradeRecord.points,
                                    )}
                                  </TableCell>
                                  <TableCell className="text-right">
                                    $&nbsp;{" "}
                                    {formatNumberForDisplay(
                                      rec.tradeRecord.netProfit,
                                    )}
                                  </TableCell>
                                </TableRow>
                              );
                            }) ?? null}
                          </React.Fragment>
                        );
                      }) ?? null}
                      {showTotals && (
                        <TableRow
                          className={
                            "hover:bg-transparent !border-t-2 !border-primaryLight text-primary"
                          }
                        >
                          <TableCell className={"font-bold"}>Totals</TableCell>
                          <TableCell className={"font-bold"}>
                            {item.totals.accountsTraded ?? 0}&nbsp;&nbsp;
                            {(item.totals.accountsTraded ?? 0) === 1
                              ? "account"
                              : "accounts"}
                          </TableCell>
                          <TableCell className={"text-center font-bold"}>
                            {item.totals.trades ?? 0}
                          </TableCell>
                          <TableCell className={"text-center font-bold"}>
                            {item.totals.winPercentage ?? 0}%
                          </TableCell>
                          <TableCell className={"text-center font-bold"}>
                            {formatNegativePoints(item.totals.netPoints ?? 0)}
                          </TableCell>
                          <TableCell className={"text-right font-bold "}>
                            $&nbsp;
                            {formatNumberForDisplay(item.totals.netProfit)}
                          </TableCell>
                        </TableRow>
                      )}
                    </React.Fragment>
                  );
                })}
              </TableBody>
            </Table>
          }
        />
      )}
    </div>
  );
}
