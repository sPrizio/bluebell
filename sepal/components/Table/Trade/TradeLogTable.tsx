import React from 'react';
import {
  Table,
  TableBody,
  TableCaption,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table"
import {IconExternalLink} from "@tabler/icons-react";
import Link from "next/link";
import {formatNegativePoints, formatNumberForDisplay} from "@/lib/functions";
import moment from "moment";
import {DateTime} from "@/lib/constants";

/**
 * Trade history table showing days and all accounts traded on that day
 *
 * @param log Trade log
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function TradeLogTable(
  {
    log = [],
  }
    : Readonly<{
    log: Array<TradeLog>,
  }>
) {


  //  RENDER

  return (
    <Table>
      <TableCaption>
        <div className={"flex items-center justify-center gap-1 pb-2"}>
          <div className={""}>
            <Link href={'/performance?Account=default'}>View Full Performance</Link>
          </div>
          <div className={""}>
            <Link href={'/performance?Account=default'}><IconExternalLink size={18}/></Link>
          </div>
        </div>
      </TableCaption>
      <TableHeader>
        <TableRow className={'hover:bg-transparent'}>
          <TableHead>Date</TableHead>
          <TableHead>Account</TableHead>
          <TableHead className={'text-center'}>Trades</TableHead>
          <TableHead className={'text-center'}>Win %</TableHead>
          <TableHead className={'text-center'}>Points</TableHead>
          <TableHead className="text-right">P & L</TableHead>
        </TableRow>
      </TableHeader>
      <TableBody>
        {
          log?.map((item) => {
            return (
              <React.Fragment key={item.uid}>
                <TableRow key={item.uid} className={'hover:bg-transparent'}>
                  <TableCell colSpan={6} className={'text-primary font-semibold'}>
                    {moment(item.end).format(DateTime.ISOMonthWeekDayFormat)}
                  </TableCell>
                </TableRow>
                {
                  item?.records?.map((rec) => {
                    return (
                      <TableRow key={rec.uid} className={'border-b-0 hover:bg-transparent'}>
                        <TableCell className={'invisible'}>{moment(rec.end).format(DateTime.ISOMonthWeekDayFormat)}</TableCell>
                        <TableCell>{rec.account.name}</TableCell>
                        <TableCell className={'text-center'}>{rec.trades}</TableCell>
                        <TableCell className={'text-center'}>{rec.winPercentage}%</TableCell>
                        <TableCell className={'text-center'}>{formatNegativePoints(rec.points)}</TableCell>
                        <TableCell className="text-right">${formatNumberForDisplay(rec.netProfit)}</TableCell>
                      </TableRow>
                    )
                  }) ?? null
                }
              </React.Fragment>
            )
          }) ?? null
        }
      </TableBody>
    </Table>
  )
}