'use client'

import {Table, TableBody, TableCaption, TableCell, TableHead, TableHeader, TableRow} from "@/components/ui/table";
import Link from "next/link";
import {IconEdit, IconExternalLink, IconPointFilled, IconTrash} from "@tabler/icons-react";
import moment from "moment/moment";
import {DateTime} from "@/lib/constants";
import {formatNumberForDisplay} from "@/lib/functions";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select";
import React, {useState} from "react";
import {
  Cloud,
  CreditCard,
  Github,
  Keyboard,
  LifeBuoy,
  LogOut,
  Mail,
  MessageSquare,
  Plus,
  PlusCircle,
  Settings,
  User,
  UserPlus,
  Users,
} from "lucide-react"

import { Button } from "@/components/ui/button"
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuGroup,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuPortal,
  DropdownMenuSeparator,
  DropdownMenuShortcut,
  DropdownMenuSub,
  DropdownMenuSubContent,
  DropdownMenuSubTrigger,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"

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
    showActions = false
  }
    : Readonly<{
    transactions: Array<Transaction>,
    showActions?: boolean
  }>
) {

  const [action, setAction] = useState<string>()
  const [transactionId, setTransactionId] = useState<string>()


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
      <TableHeader>
        <TableRow className={'hover:bg-transparent'}>
          <TableHead>Date</TableHead>
          <TableHead>Account</TableHead>
          <TableHead className={'text-center'}>Type</TableHead>
          <TableHead className={'text-center'}>Value</TableHead>
          <TableHead className={'text-right'}>Status</TableHead>
          {
            showActions ?
              <TableHead className={'text-right'} />
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
                <TableCell>{item.account.name}</TableCell>
                <TableCell className={'text-center'}>{item.type}</TableCell>
                <TableCell className={'text-center'}>${formatNumberForDisplay(item.amount)}</TableCell>
                <TableCell className={'text-right h-full'}>
                  <div className={'flex items-center justify-end'}>
                    {item.status}&nbsp;<span className={'inline-block ' + computeColors(item.status)}><IconPointFilled size={15} /></span>
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
                          <DropdownMenuSeparator />
                          <DropdownMenuGroup>
                            <DropdownMenuItem className={'hover:cursor-pointer'}>
                              <IconEdit/>
                              <span>Edit</span>
                            </DropdownMenuItem>
                            <DropdownMenuItem className={'hover:cursor-pointer'}>
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
  )
}
