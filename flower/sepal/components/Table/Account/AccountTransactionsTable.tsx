'use client'

import {Table, TableBody, TableCaption, TableCell, TableHead, TableHeader, TableRow} from "@/components/ui/table";
import Link from "next/link";
import {IconEdit, IconExternalLink, IconPointFilled, IconTrash} from "@tabler/icons-react";
import moment from "moment/moment";
import {DateTime} from "@/lib/constants";
import {formatNumberForDisplay} from "@/lib/functions/util-functions";
import React, {useEffect, useState} from "react";

import {Button} from "@/components/ui/button"
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuGroup,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"
import TransactionForm from "@/components/Form/Transaction/TransactionForm";
import BaseModal from "@/components/Modal/BaseModal";
import DeleteTransactionForm from "@/components/Form/Transaction/DeleteTransactionForm";
import {Account, Transaction} from "@/types/apiTypes";


/**
 * Renders the account transactions as a table
 *
 * @param account Account
 * @param transactions list of Account transactions
 * @param showActions shows the modification actions
 * @param showBottomLink show table caption
 * @author Stephen Prizio
 * @version 0.2.0
 */
export default function AccountTransactionsTable(
  {
    account,
    transactions = [],
    showActions = false,
    showBottomLink = true
  }
    : Readonly<{
    account: Account | null,
    transactions: Array<Transaction>,
    showActions?: boolean,
    showBottomLink?: boolean
  }>
) {

  const [modalActive, setModalActive] = useState(false)
  const [showModal, setShowModal] = useState<'edit' | 'delete' | 'none'>('none')
  const [transaction, setTransaction] = useState<Transaction>()

  useEffect(() => {
    if (showModal === 'edit') {
      setModalActive(true)
    } else if (showModal === 'none') {
      setModalActive(false)
    } else {
      setModalActive(true)
    }
  }, [showModal]);


  //  GENERAL FUNCTIONS

  /**
   * Computes the appropriate colors depending on the value of the status
   *
   * @param val status
   */
  function computeColors(val: string) {
    if (val === 'COMPLETED') {
      return ' text-primaryGreen '
    } else if (val === 'FAILED') {
      return ' text-primaryRed '
    }

    return ' text-primaryYellow '
  }


  //  RENDER

  return (
    <>
      {(!transactions || transactions.length === 0) && <div className="text-center text-slate-500 mt-2 mb-6 text-sm">No recent transactions.</div>}
      {
        transactions.length > 0 &&
        <>
            <Table>
              {
                showBottomLink ?
                  <TableCaption>
                    <div className={"flex items-center justify-center gap-1 pb-2 mt-4 text-sm"}>
                      <div className={""}>
                        <Link href={'/transactions?account=default'}>View All Transactions</Link>
                      </div>
                      <div className={""}>
                        <Link href={'#'}><IconExternalLink size={18}/></Link>
                      </div>
                    </div>
                  </TableCaption> : null
              }
                <TableHeader>
                    <TableRow className={'hover:bg-transparent'}>
                        <TableHead>Date</TableHead>
                        <TableHead>Account</TableHead>
                        <TableHead className={'text-center'}>Type</TableHead>
                        <TableHead className={'text-center'}>Value</TableHead>
                        <TableHead className={'text-right'}>Status</TableHead>
                      {
                        showActions ?
                          <TableHead className={'text-right'}/>
                          : null
                      }
                    </TableRow>
                </TableHeader>
                <TableBody>
                  {
                    transactions?.map((item) => {
                      return (
                        <TableRow key={item.uid} className={'hover:bg-transparent'}>
                          <TableCell>{moment(item.transactionDate).format(DateTime.ISOShortMonthFullDayFormat)}</TableCell>
                          <TableCell>{item.accountName}</TableCell>
                          <TableCell className={'text-center'}>{item.transactionType.label}</TableCell>
                          <TableCell className={'text-center'}>$&nbsp;{formatNumberForDisplay(item.amount)}</TableCell>
                          <TableCell className={'text-right h-full'}>
                            <div className={'flex items-center justify-end'}>
                              {item.transactionStatus.label}&nbsp;<span className={'inline-block ' + computeColors(item.transactionStatus.code)}><IconPointFilled
                              size={15}/></span>
                            </div>
                          </TableCell>
                          {
                            showActions ?
                              <TableCell className={'text-right flex items-center justify-end'}>
                                <DropdownMenu>
                                  <DropdownMenuTrigger asChild>
                                    <Button variant="outline">Actions</Button>
                                  </DropdownMenuTrigger>
                                  <DropdownMenuContent className="w-48">
                                    <DropdownMenuLabel>Transaction Actions</DropdownMenuLabel>
                                    <DropdownMenuSeparator/>
                                    <DropdownMenuGroup>
                                      <DropdownMenuItem className={'hover:cursor-pointer'} onClick={() => {
                                        setTransaction(item);
                                        setShowModal('edit');
                                      }}>
                                        <IconEdit/>
                                        <span>Edit</span>
                                      </DropdownMenuItem>
                                      <DropdownMenuItem className={'hover:cursor-pointer'} onClick={() => {
                                        setTransaction(item)
                                        setShowModal('delete')
                                      }}>
                                        <IconTrash/>
                                        <span>Delete</span>
                                      </DropdownMenuItem>
                                    </DropdownMenuGroup>
                                  </DropdownMenuContent>
                                </DropdownMenu>
                              </TableCell>
                              : null
                          }
                        </TableRow>
                      )
                    })
                  }
                </TableBody>
            </Table>
          {
            account && (transaction?.transactionDate ?? false) ?
              <BaseModal
                isOpen={modalActive && showModal === 'edit'}
                title={'Edit Transaction'}
                description={'Keep track of your Account\'s transactions by adding withdrawals & deposits.'}
                content={<TransactionForm account={account} mode={'edit'} transaction={transaction}/>}
                closeHandler={() => {
                  if (showModal !== 'none' && modalActive) {
                    setShowModal('none');
                  }
                }
                }
              /> : null
          }
          {
            account && (transaction?.transactionDate ?? false) ?
              <BaseModal
                isOpen={modalActive && showModal === 'delete'}
                title={'Edit Transaction'}
                description={'Keep track of your Account\'s transactions by adding withdrawals & deposits.'}
                content={<DeleteTransactionForm account={account} transaction={transaction} />}
                closeHandler={() => {
                  if (showModal !== 'none' && modalActive) {
                    setShowModal('none');
                  }
                }
                }
              /> : null
          }
        </>
      }
    </>
  )
}
