import {Table, TableBody, TableCell, TableHead, TableHeader, TableRow} from "@/components/ui/table";
import moment from "moment";
import {DateTime} from "@/lib/constants";
import React from "react";

/**
 * Renders a table of Trade records
 *
 * @param records Trade records
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function TradeRecordTable(
  {
    records = [],
  }
    : Readonly<{
    records: Array<TradeRecord>,
  }>
) {


  //  RENDER

  return (
    <Table>
      <TableHeader className={'border-b-2 border-primaryLight'}>
        <TableRow>
          <TableHead className={'text-primary font-bold'}>Date</TableHead>
          <TableHead className={'text-center text-primary font-bold'}>Trades</TableHead>
          <TableHead className={'text-center text-primary font-bold'}>Win Rate</TableHead>
          <TableHead className={'text-right text-primary font-bold'}>P & L</TableHead>
        </TableRow>
      </TableHeader>
      <TableBody>
        {
          records?.map(item => {
            return (
              <TableRow key={item.uid} className={'hover:bg-transparent'}>
                <TableCell>{moment(item.end).format(DateTime.ISOMonthWeekDayFormat)}</TableCell>
                <TableCell className={'text-center'}>{item.trades}</TableCell>
                <TableCell className={'text-center'}>{item.winPercentage}%</TableCell>
                <TableCell className={'text-right font-bold ' + (item.netProfit >= 0 ? 'text-primaryGreen' : 'text-primaryRed')}>$&nbsp;{item.netProfit}</TableCell>
              </TableRow>
            )
          })
        }
      </TableBody>
    </Table>
  )
}