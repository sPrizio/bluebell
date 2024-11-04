'use client'

import {Table, TableBody, TableCaption, TableCell, TableHead, TableHeader, TableRow} from "@/components/ui/table";
import Link from "next/link";
import {IconEdit, IconExternalLink, IconPointFilled, IconTrash} from "@tabler/icons-react";
import moment from "moment/moment";
import {DateTime} from "@/lib/constants";
import {formatNumberForDisplay} from "@/lib/functions";
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
import TransactionForm from "@/components/Form/transaction/TransactionForm";
import BaseModal from "@/components/Modal/BaseModal";
import DeleteTransactionForm from "@/components/Form/transaction/DeleteTransactionForm";


/**
 * Renders the Account transactions as a table
 *
 * @param account Account
 * @param transactions list of Account transactions
 * @param showActions shows the modification actions
 * @param showBottomLink show table caption
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function AccountTransactionsTable(
  {
    account,
    transactions = [],
    showActions = false,
    showBottomLink = true
  }
    : Readonly<{
    account: Account
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
    <>
      <Table>
        {
          showBottomLink ?
            <TableCaption>
              <div className={"flex items-center justify-center gap-1 pb-2"}>
                <div className={""}>
                  <Link href={'/transactions?Account=default'}>View All Transactions</Link>
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
                  <TableCell>{moment(item.date).format(DateTime.ISOShortMonthFullDayFormat)}</TableCell>
                  <TableCell>{item.accountNumber}</TableCell>
                  <TableCell className={'text-center'}>{item.type}</TableCell>
                  <TableCell className={'text-center'}>${formatNumberForDisplay(item.amount)}</TableCell>
                  <TableCell className={'text-right h-full'}>
                    <div className={'flex items-center justify-end'}>
                      {item.status}&nbsp;<span className={'inline-block ' + computeColors(item.status)}><IconPointFilled
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
        transaction && transaction.date ?
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
        transaction && transaction.date ?
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
  )
}
