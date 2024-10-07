import {Table, TableBody, TableCaption, TableCell, TableHead, TableHeader, TableRow} from "@/components/ui/table";
import Link from "next/link";
import {IconExternalLink, IconPointFilled} from "@tabler/icons-react";
import moment from "moment/moment";
import {DateTime} from "@/lib/constants";
import {formatNumberForDisplay} from "@/lib/services";

/**
 * Renders the account transactions as a table
 *
 * @param transactions list of account transactions
 * @author Stephen Prizip
 * @version 0.0.1
 */
export default function AccountTransactionsTable(
  {
    transactions = [],
  }
    : Readonly<{
    transactions: Array<Transaction>,
  }>
) {


  //  GENERAL FUNCTIONS

  /**
   * Computes the appropriate colors depending on the value of the status
   *
   * @param val status
   */
  function computeColors(val: 'Complete' | 'Failed' | 'Pending') {
    if (val === 'Complete') {
      return ' text-primaryGreen '
    } else if (val === 'Failed') {
      return ' text-primaryRed '
    }

    return ' text-primaryYellow '
  }


  //  RENDER

  return (
    <Table>
      <TableCaption>
        <div className={"flex items-center justify-center gap-1"}>
          <div className={""}>
            <Link href={'#'}>View All Transactions</Link>
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
          <TableHead className={'text-center'}>Type</TableHead>
          <TableHead className={'text-center'}>Value</TableHead>
          <TableHead className={'text-right'}>Status</TableHead>
        </TableRow>
      </TableHeader>
      <TableBody>
        {
          transactions?.map((item) => {
            return (
              <TableRow key={item.uid} className={'hover:bg-transparent'}>
                <TableCell>{moment(item.date).format(DateTime.ISOMonthWeekDayFormat)}</TableCell>
                <TableCell>{item.account.name}</TableCell>
                <TableCell className={'text-center'}>{item.type}</TableCell>
                <TableCell className={'text-center'}>${formatNumberForDisplay(item.amount)}</TableCell>
                <TableCell className={'text-right flex items-center justify-end'}>
                  {item.status}&nbsp;<span className={'inline-block ' + computeColors(item.status)}><IconPointFilled size={15} /></span>
                </TableCell>
              </TableRow>
            )
          })
        }
      </TableBody>
    </Table>
  )
}
