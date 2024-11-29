'use client'

import {useSepalPageInfoContext} from "@/lib/context/SepalContext";
import React, {useEffect, useState} from "react";
import {Icons} from "@/lib/enums";
import {notFound, useSearchParams} from 'next/navigation'
import {getAccount, getAccountNumber} from "@/lib/functions/util-functions";
import {BaseCard} from "@/components/Card/BaseCard";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select";
import AccountTransactionsTable from "@/components/Table/Account/AccountTransactionsTable";
import {Button} from "@/components/ui/button";
import {IconCirclePlus} from "@tabler/icons-react";
import BaseModal from "@/components/Modal/BaseModal";
import TransactionForm from "@/components/Form/Transaction/TransactionForm";
import {Loader2} from "lucide-react";

/**
 * The page that shows all of a user's Account's transactions. Accounts can be cycled
 *
 * @author Stephen Prizio
 * @version 0.0.2
 */
export default function TransactionsPage() {

  const {
    pageTitle,
    pageSubtitle,
    pageIconCode,
    breadcrumbs,
    user,
    setPageTitle,
    setPageSubtitle,
    setPageIconCode,
    setBreadcrumbs,
    setUser
  } = useSepalPageInfoContext()

  const searchParams = useSearchParams()
  const [isLoading, setIsLoading] = useState(false)
  const [accNumber, setAccNumber] = useState(getAccountNumber(searchParams, user?.accounts))
  const [account, setAccount] = useState<Account | null>()

  const acc = getAccount(accNumber, user?.accounts)
  if (!acc) {
    return notFound()
  }

  useEffect(() => {
    setPageTitle('Transactions')
    setPageSubtitle('A list of transactions for trading Account ' + accNumber)
    setPageIconCode(Icons.Transactions)
    setBreadcrumbs([
      {label: 'Dashboard', href: '/dashboard', active: false},
      {label: 'Accounts', href: '/accounts', active: false},
      {label: accNumber.toString(), href: '/accounts/' + accNumber, active: false},
      {label: 'Transactions', href: '/transactions', active: true},
    ])
  }, [])

  useEffect(() => {
    setPageTitle('Transactions')
    setPageSubtitle('A list of transactions for trading Account ' + accNumber)
    setPageIconCode(Icons.Transactions)
    setBreadcrumbs([
      {label: 'Dashboard', href: '/dashboard', active: false},
      {label: 'Accounts', href: '/accounts', active: false},
      {label: accNumber.toString(), href: '/accounts/' + accNumber, active: false},
      {label: 'Transactions', href: '/transactions', active: true},
    ])

    setAccNumber(accNumber)
    setAccount(getAccount(accNumber, user?.accounts))
  }, [accNumber]);


  //  RENDER

  return (
    <div>
      {
        isLoading ?
          <div className={'h-[72vh] flex items-center justify-center'}>
            <div className={'grid grid-cols-1 justify-items-center gap-8'}>
              <div>
                <Loader2 className="animate-spin text-primary" size={50}/>
              </div>
              <div className={'text-lg'}>Loading account activity</div>
            </div>
          </div>
          :
          <div className={'grid grid-cols-1 gap-8'}>
            <div className={'flex items-center justify-end gap-4'}>
              <Select value={accNumber.toString()} onValueChange={(val) => setAccNumber(parseInt(val))}>
                <SelectTrigger className="w-[180px] bg-white">
                  <SelectValue placeholder="Account"/>
                </SelectTrigger>
                <SelectContent>
                  {
                    user?.accounts.map((item: Account) => {
                      return (
                        <SelectItem key={item.uid} value={item.accountNumber.toString()}>{item.name}</SelectItem>
                      )
                    })
                  }
                </SelectContent>
              </Select>
            </div>
            <div>
              {(!account || (account?.transactions?.length ?? 0) === 0) && <div className="text-center text-slate-500">No account activity.</div>}
              {
                account?.accountNumber && (account?.transactions?.length ?? 0) > 0 ?
                  <BaseCard
                    loading={isLoading}
                    title={'Transactions'}
                    subtitle={'A look at all of your transactions for the given Account.'}
                    cardContent={<AccountTransactionsTable account={account} transactions={account.transactions}
                                                           showActions={true} showBottomLink={false}/>}
                    headerControls={[
                      <BaseModal
                        key={0}
                        title={'Add a new Transaction'}
                        description={'Keep track of your Account\'s transactions by adding withdrawals & deposits.'}
                        trigger={<Button className="w-full text-white"><IconCirclePlus/>&nbsp;Add Transaction</Button>}
                        content={<TransactionForm account={account} mode={'create'}/>}
                      />
                    ]}
                  /> : null
              }
            </div>
          </div>
      }
    </div>
  )
}