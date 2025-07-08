import React from "react";
import { TableCell, TableHead, TableRow } from "@/components/ui/table";
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
 * @version 1.0.0
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

    return arr?.toSorted((a, b) => b.date.localeCompare(a.date)) ?? [];
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
          headerContent={
            <TableRow className="hover:bg-transparent">
              <TableHead className="w-[180px]">Date</TableHead>
              <TableHead className="w-[160px]">Account</TableHead>
              <TableHead className="text-center w-[100px] pr-[24px]">
                Trades
              </TableHead>
              <TableHead className="text-center w-[100px] pr-[16px]">
                Win %
              </TableHead>
              <TableHead className="text-center w-[100px] pr-[32px]">
                Points
              </TableHead>
              <TableHead className="text-right w-[120px] pr-[16px]">
                P & L
              </TableHead>
            </TableRow>
          }
          bodyContent={log?.entries?.map((item, idx) => {
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
                            <TableCell className="w-[180px] invisible">
                              {moment(rec.tradeRecord.end).format(
                                DateTime.ISOMonthWeekDayFormat,
                              )}
                            </TableCell>
                            <TableCell className="w-[160px]">
                              {rec.accountName}
                            </TableCell>
                            <TableCell className="text-center w-[100px]">
                              {rec.tradeRecord.trades}
                            </TableCell>
                            <TableCell className="text-center w-[100px]">
                              {rec.tradeRecord.winPercentage}
                            </TableCell>
                            <TableCell className="text-center w-[100px]">
                              {formatNegativePoints(rec.tradeRecord.points)}
                            </TableCell>
                            <TableCell className="text-right w-[120px]">
                              $&nbsp;
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
              </React.Fragment>
            );
          })}
          footerContent={
            (showTotals &&
              log?.entries?.map((item, idx) => {
                return (
                  <TableRow
                    key={idx + "_totals"}
                    className={
                      "hover:bg-transparent !border-t-2 !border-primaryLight text-primary"
                    }
                  >
                    <TableCell className="w-[180px] font-bold">
                      Totals
                    </TableCell>
                    <TableCell className="w-[160px] font-bold">
                      {item.totals.accountsTraded ?? 0}&nbsp;&nbsp;
                      {(item.totals.accountsTraded ?? 0) === 1
                        ? "account"
                        : "accounts"}
                    </TableCell>
                    <TableCell className="text-center w-[100px] font-bold pr-[24px]">
                      {item.totals.trades ?? 0}
                    </TableCell>
                    <TableCell className="text-center w-[100px] font-bold pr-[16px]">
                      {item.totals.winPercentage ?? 0}%
                    </TableCell>
                    <TableCell className="text-center w-[100px] font-bold pr-[32px]">
                      {formatNegativePoints(item.totals.netPoints ?? 0)}
                    </TableCell>
                    <TableCell className="text-right w-[120px] font-bold pr-[16px]">
                      $&nbsp;{formatNumberForDisplay(item.totals.netProfit)}
                    </TableCell>
                  </TableRow>
                );
              })) ??
            null
          }
          caption={
            <div
              className={
                "flex items-center justify-center gap-1 pb-2 mt-4 text-sm text-muted-foreground"
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
          }
        />
      )}
    </div>
  );
}
