import {
  Table,
  TableBody,
  TableCaption,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table"
import {IconExternalLink, IconSquareRoundedCheckFilled} from "@tabler/icons-react";
import Link from "next/link";
import {formatNumberForDisplay} from "@/lib/services";
import moment from "moment";
import {DateTime} from "@/lib/constants";

/**
 * Renders a table containing the user's active accounts
 *
 * @param accounts active accounts
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function AccountsTable(
  {
    accounts = [],
    showAllLink = false,
    allowAccountSelection = false,
    showCompactTable = true
  }
    : Readonly<{
    accounts: Array<Account>,
    showAllLink?: boolean
    allowAccountSelection?: boolean
    showCompactTable?: boolean
  }>
) {


  //  RENDER

  return (
    <div>
      {
        accounts && accounts.length > 0 ?
          <Table>
            {
              showAllLink ?
                <TableCaption>
                  <div className={"flex items-center justify-center gap-1"}>
                    <div className={""}>
                      <Link href={'/accounts'}>View All Accounts</Link>
                    </div>
                    <div className={""}>
                      <Link href={'#'}><IconExternalLink size={18} /></Link>
                    </div>
                  </div>
                </TableCaption> : null
            }
            <TableHeader>
              {
                showCompactTable ?
                  <TableRow className={'hover:bg-transparent'}>
                    <TableHead>Name</TableHead>
                    <TableHead className={'text-center'}>Type</TableHead>
                    <TableHead className={'text-center'}>Broker</TableHead>
                    <TableHead className="text-right">Balance</TableHead>
                  </TableRow>
                  :
                  <TableRow className={'hover:bg-transparent'}>
                    <TableHead>Name</TableHead>
                    <TableHead className={'text-center'} />
                    <TableHead className={''}>Date Opened</TableHead>
                    <TableHead className={''}>Date Closed</TableHead>
                    <TableHead className={'text-center'}>Account Number</TableHead>
                    <TableHead className={'text-center'}>Currency</TableHead>
                    <TableHead className={'text-center'}>Trading Platform</TableHead>
                    <TableHead className={'text-center'}>Type</TableHead>
                    <TableHead className={'text-center'}>Broker</TableHead>
                    <TableHead className={''}>Last Traded</TableHead>
                    <TableHead className="text-right">Balance</TableHead>
                  </TableRow>
              }
            </TableHeader>
            {
              showCompactTable ?
                <TableBody>
                  {
                    accounts?.map((item, itx) => {
                      return (
                        <TableRow key={item.uid} className={allowAccountSelection ? 'hover:cursor-pointer' : 'hover:bg-transparent'}>
                          <TableCell>{item.name}</TableCell>
                          <TableCell className={'text-center'}>{item.accountType}</TableCell>
                          <TableCell className={'text-center'}>{item.broker}</TableCell>
                          <TableCell className="text-right">${formatNumberForDisplay(item.balance)}</TableCell>
                        </TableRow>
                      )
                    }) ?? null
                  }
                </TableBody>
                :
                <TableBody>
                  {
                    accounts?.map((item, itx) => {
                      return (
                        <TableRow key={item.uid} className={allowAccountSelection ? 'hover:cursor-pointer' : 'hover:bg-transparent'}>
                          <TableCell>{item.name}</TableCell>
                          <TableCell className={'text-center'}>
                            {
                              item.defaultAccount ? <IconSquareRoundedCheckFilled className={'text-primary'} /> : null
                            }
                          </TableCell>
                          <TableCell className={''}>{moment(item.accountOpenTime).format(DateTime.ISOShortMonthDayYearWithTimeFormat)}</TableCell>
                          <TableCell className={''}>
                            {
                              (item.accountCloseTime === '-1' || !item.accountCloseTime || item.accountCloseTime.length === 0) ?
                                <p>Active</p> : moment(item.accountCloseTime).format(DateTime.ISOShortMonthDayYearWithTimeFormat)
                            }
                          </TableCell>
                          <TableCell className={'text-center'}>{item.accountNumber}</TableCell>
                          <TableCell className={'text-center'}>{item.currency}</TableCell>
                          <TableCell className={'text-center'}>{item.tradePlatform}</TableCell>
                          <TableCell className={'text-center'}>{item.accountType}</TableCell>
                          <TableCell className={'text-center'}>{item.broker}</TableCell>
                          <TableCell className={''}>{moment(item.lastTraded).format(DateTime.ISOShortMonthDayYearWithTimeFormat)}</TableCell>
                          <TableCell className="text-right">${formatNumberForDisplay(item.balance)}</TableCell>
                        </TableRow>
                      )
                    }) ?? null
                  }
                </TableBody>
            }
          </Table>
          :
          <div className={'text-center pb-2 text-slate-600'}>
            No accounts found.
          </div>
      }
    </div>
  )
}