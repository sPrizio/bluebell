'use client'

import {Table, TableBody, TableCaption, TableCell, TableHead, TableHeader, TableRow,} from "@/components/ui/table"
import {IconExternalLink, IconSquareRoundedCheckFilled} from "@tabler/icons-react";
import Link from "next/link";
import {formatNumberForDisplay, getBrokerImageForCode, getFlagForCode, getRoundFlagForCode} from "@/lib/functions";
import moment from "moment";
import {DateTime} from "@/lib/constants";
import {useRouter} from "next/navigation";
import Badge from "@/components/Badge/Badge";

/**
 * Renders a table containing the user's active accounts
 *
 * @param accounts active accounts
 * @param showAllLink show link below table to direct to accounts page
 * @param allowAccountSelection allow clicking on rows
 * @param showCompactTable minimal table flag
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

  const router = useRouter();


  //  GENERAL FUNCTIONS

  /**
   * Obtains the image representing the given broker
   *
   * @param val incoming key
   */
  function getBrokerImage(val: string): React.ReactNode {
    return getBrokerImageForCode(val.toLowerCase(), 25, 25)
  }

  function redirectToAccount(val: number) {
    if (allowAccountSelection) {
      router.push(`/accounts/${val}`)
    }
  }


  //  RENDER

  return (
    <div className={'pb-2'}>
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
                    <TableHead className={'w-[50px] text-center'} />
                    <TableHead className={'w-[150px]'}>Number</TableHead>
                    <TableHead className={'w-[175px]'}>Name</TableHead>
                    <TableHead className={'w-[175px]'}>Opened</TableHead>
                    <TableHead className={'w-[175px]'}>Closed</TableHead>
                    <TableHead className={'w-[60px] text-center'}>Currency</TableHead>
                    <TableHead className={'w-[85px] text-center'}>Platform</TableHead>
                    <TableHead className={'w-[85px] text-center'}>Type</TableHead>
                    <TableHead className={'w-[60px] text-center'}>Broker</TableHead>
                    <TableHead className={'w-[175px]'}>Last Traded</TableHead>
                    <TableHead className="text-right">Balance</TableHead>
                  </TableRow>
              }
            </TableHeader>
            {
              showCompactTable ?
                <TableBody>
                  {
                    accounts?.map((item) => {
                      return (
                        <TableRow key={item.uid} className={allowAccountSelection ? 'hover:cursor-pointer' : 'hover:bg-transparent'}>
                          <TableCell>{item.name}</TableCell>
                          <TableCell className={'text-center'}>{item.accountType.label}</TableCell>
                          <TableCell className={'text-center'}>{item.broker.label}</TableCell>
                          <TableCell className="text-right">${formatNumberForDisplay(item.balance)}</TableCell>
                        </TableRow>
                      )
                    }) ?? null
                  }
                </TableBody>
                :
                <TableBody>
                  {
                    accounts?.map((item) => {
                      return (
                        <TableRow key={item.uid} className={allowAccountSelection ? 'hover:cursor-pointer' : 'hover:bg-transparent'} onClick={() => redirectToAccount(item.accountNumber)}>
                          <TableCell className={'text-center'}>
                            {item.defaultAccount ? <IconSquareRoundedCheckFilled className={'text-primary'} /> : null}
                          </TableCell>
                          <TableCell className={''}>{item.accountNumber}</TableCell>
                          <TableCell>{item.name}</TableCell>
                          <TableCell className={''}>{moment(item.accountOpenTime).format(DateTime.ISOShortMonthDayYearWithTimeFormat)}</TableCell>
                          <TableCell className={''}>
                            {
                              (item.accountCloseTime === '-1' || !item.accountCloseTime || item.accountCloseTime.length === 0) ?
                                <Badge text={'Open'} variant={'success'} /> : moment(item.accountCloseTime).format(DateTime.ISOShortMonthDayYearWithTimeFormat)
                            }
                          </TableCell>
                          <TableCell className={'text-center flex items-center justify-center h-full'}>
                            <div className={'mt-2'}>
                              {getFlagForCode(item.currency.label)}
                            </div>
                          </TableCell>
                          <TableCell className={'text-center'}>{item.tradePlatform.label}</TableCell>
                          <TableCell className={'text-center'}>{item.accountType.label}</TableCell>
                          <TableCell className={'text-center flex items-center justify-center'}>
                            <div className={'mt-2'}>
                              {getBrokerImage(item.broker.code)}
                            </div>
                          </TableCell>
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