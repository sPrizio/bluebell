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
import {formatNegativePoints, formatNumberForDisplay} from "@/lib/services";
import moment from "moment";
import {DateTime} from "@/lib/constants";

/**
 * Trade history table showing days and all accounts traded on that day
 *
 * @param log trade log
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
        <div className={"flex items-center justify-center gap-1"}>
          <div className={""}>
            <Link href={'#'}>View Full Performance</Link>
          </div>
          <div className={""}>
            <Link href={'#'}><IconExternalLink size={18}/></Link>
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
          log && log.length && log.map((item, itx) => {
            return (
              <>
                <TableRow className={'hover:bg-transparent'}>
                  <TableCell colSpan={6} className={'text-primary font-semibold'}>
                    {moment(item.end).format(DateTime.ISOMonthWeekDayFormat)}
                  </TableCell>
                </TableRow>
                {
                  item && item.records.length && item.records.map((rec, ritx) => {
                    return (
                      <TableRow key={ritx} className={'border-b-0 hover:bg-transparent'}>
                        <TableCell className={'invisible'}>{moment(rec.end).format(DateTime.ISOMonthWeekDayFormat)}</TableCell>
                        <TableCell>{rec.account.name}</TableCell>
                        <TableCell className={'text-center'}>{rec.trades}</TableCell>
                        <TableCell className={'text-center'}>{rec.winPercentage}%</TableCell>
                        <TableCell className={'text-center'}>{formatNegativePoints(rec.points)}</TableCell>
                        <TableCell className="text-right">${formatNumberForDisplay(rec.netProfit)}</TableCell>
                      </TableRow>
                    )
                  })
                }
              </>
            )
          })
        }


        {/*{
          log && log.records.length && log.records.map((rec, ritx) => {
            return (
              <TableRow key={ritx}>
                <TableCell>{moment(item.end).format(DateTime.ISOMonthWeekDayFormat)}</TableCell>
                <TableCell>{item.account.name}</TableCell>
                <TableCell className={'text-center'}>{item.trades}</TableCell>
                <TableCell className={'text-center'}>{item.winPercentage}%</TableCell>
                <TableCell className={'text-center'}>{formatNegativePoints(item.points)}</TableCell>
                <TableCell className="text-right">${formatNumberForDisplay(item.netProfit)}</TableCell>
              </TableRow>
            )
          })
        }*/}
      </TableBody>
    </Table>
  )
}