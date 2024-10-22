'use client'

import {useSepalPageInfoContext} from "@/lib/context/SepalContext";
import React, {useEffect, useState} from "react";
import {Icons} from "@/lib/enums";
import {useSearchParams} from 'next/navigation'
import {delay, isNumeric} from "@/lib/functions";
import {accounts, accountTransactions} from "@/lib/sample-data";
import {useToast} from "@/hooks/use-toast";
import {BaseCard} from "@/components/Card/BaseCard";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select";
import AccountTransactionsTable from "@/components/Table/account/AccountTransactionsTable";
import {Button} from "@/components/ui/button";
import {IconCirclePlus} from "@tabler/icons-react";
import AccountForm from "@/components/Form/account/AccountForm";
import BaseModal from "@/components/Modal/BaseModal";
import TransactionForm from "@/components/Form/transaction/TransactionForm";

/**
 * THe page that shows all of a user's account's transactions. Accounts can be cycled
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function TransactionsPage() {

  const {toast} = useToast();
  const searchParams = useSearchParams()
  const [isLoading, setIsLoading] = useState(false)
  const [accNumber, setAccNumber] = useState(getAccountNumber())
  const [account, setAccount] = useState<Account>()

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

  useEffect(() => {
    setPageTitle('Transactions')
    setPageSubtitle('A list of transactions for trading account ' + accNumber)
    setPageIconCode(Icons.Transactions)
    setBreadcrumbs([
      {label: 'Dashboard', href: '/dashboard', active: false},
      {label: 'Accounts', href: '/accounts', active: false},
      {label: accNumber.toString(), href: '/accounts/' + accNumber, active: false},
      {label: 'Transactions', href: '/transactions', active: true},
    ])

    getAccount()
  }, [accNumber])


  //  GENERAL FUNCTIONS

  /**
   * Fetches the account number requested by the page
   */
  function getAccountNumber() {
    let val = searchParams.get('account') ?? -1
    if (val !== -1 && isNumeric(val)) {
      return parseInt(val as string)
    }

    return -1
  }

  /**
   * Fetches the associated account information
   */
  async function getAccount() {

    setIsLoading(true)

    //TODO: temp
    await delay(2000)
    if (accNumber === -1) {
      await getDefaultAccount()
      if (searchParams.get('account') !== 'default') {
        toast({
          title: 'Invalid Account Number',
          description: `The account number ${accNumber} is not valid. Returning the default account.`,
          variant: 'danger'
        })
      }
    } else {
      for (let acc of accounts) {
        if (acc.accountNumber === accNumber) {
          setAccount(acc)
          setAccNumber(acc.accountNumber)
          setIsLoading(false)
          return
        }
      }

      // no account found
      await getDefaultAccount()
      toast({
        title: 'Account Not Found',
        description: `No account was found with the account number ${accNumber}. Returning the default account.`,
        variant: 'warning'
      })
    }

    setIsLoading(false)
  }

  /**
   * Returns the default account of the portfolio
   */
  async function getDefaultAccount() {
    setIsLoading(true)

    //TODO: temp
    await delay(2000)
    setAccount(accounts[0])
    setAccNumber(accounts[0].accountNumber)

    setIsLoading(false)
  }


  //  RENDER

  return (
    <div className={'grid grid-cols-1 gap-8'}>
      <div className={'flex items-center justify-end gap-4'}>
        <Select value={accNumber.toString()} onValueChange={(val) => setAccNumber(parseInt(val))}>
          <SelectTrigger className="w-[180px] bg-white">
            <SelectValue placeholder="Account"/>
          </SelectTrigger>
          <SelectContent>
            {
              user.accounts.map(item => {
                return (
                  <SelectItem key={item.uid} value={item.accountNumber.toString()}>{item.name}</SelectItem>
                )
              })
            }
          </SelectContent>
        </Select>
      </div>
      <div>
        {
          account && account.accountNumber ?
            <BaseCard
              loading={isLoading}
              title={'Transactions'}
              subtitle={'A look at all of your transactions for the given account.'}
              cardContent={<AccountTransactionsTable account={account} transactions={accountTransactions} showActions={true} showBottomLink={false} />}
              headerControls={[
                <BaseModal
                  key={0}
                  title={'Add a new Transaction'}
                  description={'Keep track of your account\'s transactions by adding withdrawals & deposits.'}
                  trigger={<Button className="w-full text-white"><IconCirclePlus/>&nbsp;Add Transaction</Button>}
                  content={<TransactionForm account={account} mode={'create'}/>}
                />
              ]}
            /> : null
        }
      </div>
    </div>
  )
}