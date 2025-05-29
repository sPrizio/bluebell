"use client";

import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import moment from "moment";
import { DateTime } from "@/lib/constants";
import { MarketNews } from "@/types/apiTypes";
import {
  getFlagForCode,
  resolveIcon,
} from "@/lib/functions/util-component-functions";
import { Icons } from "@/lib/enums";
import { useState } from "react";
import { Button } from "@/components/ui/button";
import {
  Tooltip,
  TooltipContent,
  TooltipProvider,
  TooltipTrigger,
} from "@/components/ui/tooltip";
import { TooltipArrow } from "@radix-ui/react-tooltip";

/**
 * Renders the market news table
 *
 * @param news market news
 * @author Stephen Prizio
 * @version 0.2.3
 */
export default function NewsTable({
  news = [],
}: Readonly<{
  news?: Array<MarketNews>;
}>) {
  const headerBackground = (active: boolean) =>
    active ? "text-white bg-primary hover:bg-primary " : "text-primary";
  const pastStyle = " opacity-25 ";

  const [hiddenRows, setHiddenRows] =
    useState<Array<number>>(computePastRows());

  //  GENERAL FUNCTIONS

  /**
   * Computes the row indices for past news
   */
  function computePastRows(): Array<number> {
    const arr: Array<number> = [];
    if (!news || news.length === 0) return arr;
    for (let i = 0; i < news?.length; i++) {
      if (news[i].past) {
        arr.push(i);
      }
    }

    return arr;
  }

  /**
   * Computes the color for the severity level
   *
   * @param val severity level
   */
  function computeSeverity(val: number) {
    switch (val) {
      case 1:
        return " text-threatLow ";
      case 2:
        return " text-threatModerate ";
      default:
        return " text-threatSevere ";
    }
  }

  /**
   * Toggles the collapsed state for a news row
   *
   * @param idx row index
   */
  function handleCollapse(idx: number) {
    const newHiddenRows = [...hiddenRows];
    if (newHiddenRows.includes(idx)) {
      newHiddenRows.splice(newHiddenRows.indexOf(idx), 1);
    } else {
      newHiddenRows.push(idx);
    }

    setHiddenRows(newHiddenRows);
  }

  //  RENDER

  return (
    <div className={""}>
      <Table>
        <TableHeader>
          <TableRow className={"hover:bg-transparent"}>
            <TableHead>Date</TableHead>
            <TableHead className={"w-[90px]"}>Time</TableHead>
            <TableHead>Country</TableHead>
            <TableHead>Impact</TableHead>
            <TableHead className={"w-[295px]"}>News</TableHead>
            <TableHead>Forecast</TableHead>
            <TableHead>Previous</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {news?.map((news, idx) => (
            <>
              <TableRow
                key={news.uid + news.date}
                className={`border-t-1 border-slate-200
                  hover:bg-transparent 
                  ${headerBackground(news.active)}`}
              >
                <TableCell
                  className={"font-semibold rounded-l-2xl"}
                  colSpan={6}
                >
                  {moment(news.date).format(DateTime.ISOLongMonthDayYearFormat)}
                  {news.active && (
                    <>
                      <span className={"ml-2"}>-</span>
                      <span className={"ml-2"}>Today</span>
                    </>
                  )}
                </TableCell>
                <TableCell className={"text-right rounded-r-2xl"}>
                  <div
                    className={
                      "flex items-center justify-end w-full font-semibold"
                    }
                  >
                    <Button
                      variant={"plain"}
                      className={"ml-2 hover:cursor-pointer"}
                      onClick={() => handleCollapse(idx)}
                    >
                      {hiddenRows.includes(idx)
                        ? resolveIcon(Icons.SquareRoundedChevronUpFilled)
                        : resolveIcon(Icons.SquareRoundedChevronDownFilled)}
                    </Button>
                  </div>
                </TableCell>
              </TableRow>
              {(news.slots?.length ?? 0) === 0 ? (
                <TableRow
                  key={news.uid + news.date}
                  className={
                    "hover:bg-transparent " +
                    `${headerBackground(news.active)}` +
                    (news.past ? `${pastStyle}` : "")
                  }
                >
                  <TableCell
                    className={"text-slate-400 text-center"}
                    colSpan={7}
                  >
                    No news today.
                  </TableCell>
                </TableRow>
              ) : null}
              {!hiddenRows.includes(idx) &&
                news.slots?.map((slot) => {
                  const iconSize = 30;
                  return (slot.entries?.length ?? 0) > 0
                    ? slot.entries.map((entry, index) => {
                        return (
                          <TableRow
                            key={slot.uid + news.date + slot.time + index}
                            className={"hover:bg-transparent border-0 "}
                          >
                            <TableCell />
                            {index === 0 ? (
                              <TableCell className={""}>{slot.time}</TableCell>
                            ) : (
                              <TableCell />
                            )}
                            <TableCell className={""}>
                              {getFlagForCode(entry.country ?? "")}
                            </TableCell>
                            <TableCell>
                              <div className={"flex items-center"}>
                                <TooltipProvider>
                                  <Tooltip delayDuration={250}>
                                    <TooltipTrigger asChild>
                                      <span
                                        className={
                                          "inline-block hover:cursor-pointer"
                                        }
                                      >
                                        {entry.severityLevel === 1 &&
                                          resolveIcon(
                                            Icons.AntennaBars3,
                                            computeSeverity(
                                              entry.severityLevel,
                                            ),
                                            iconSize,
                                          )}
                                        {entry.severityLevel === 2 &&
                                          resolveIcon(
                                            Icons.AntennaBars4,
                                            computeSeverity(
                                              entry.severityLevel,
                                            ),
                                            iconSize,
                                          )}
                                        {entry.severityLevel === 3 &&
                                          resolveIcon(
                                            Icons.AntennaBars5,
                                            computeSeverity(
                                              entry.severityLevel,
                                            ),
                                            iconSize,
                                          )}
                                      </span>
                                    </TooltipTrigger>
                                    <TooltipContent side={"left"}>
                                      <div className={"max-w-[250px]"}>
                                        Impact/Severity in this case refers to
                                        the likelihood of this news impacting
                                        the market as well as the magnitude of
                                        that implied volatility. More bars means
                                        more dangerous news.
                                      </div>
                                    </TooltipContent>
                                  </Tooltip>
                                </TooltipProvider>
                              </div>
                            </TableCell>
                            <TableCell className={""}>
                              {entry.content}
                            </TableCell>
                            <TableCell className={""}>
                              {entry.forecast}
                            </TableCell>
                            <TableCell className={""}>
                              {entry.previous}
                            </TableCell>
                          </TableRow>
                        );
                      })
                    : null;
                })}
            </>
          )) ?? null}
        </TableBody>
      </Table>
    </div>
  );
}
