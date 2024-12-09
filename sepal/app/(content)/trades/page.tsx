'use client'

import {useSepalPageInfoContext} from "@/lib/context/SepalContext";
import {notFound, useSearchParams} from "next/navigation";
import React, {useEffect, useState} from "react";
import {getAccount, getAccountNumber} from "@/lib/functions/util-functions";
import {Icons} from "@/lib/enums";
import {Loader2} from "lucide-react";
import TradeTable from "@/components/Table/Trade/TradeTable";
import {BaseCard} from "@/components/Card/BaseCard";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select";

/**
 * Renders the Trade history page
 *
 * @author Stephen Prizio
 * @version 0.0.2
 */
export default function TradesPage() {

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
  const [aggTrades, setAggTrades] = useState<Array<Trade>>([])

  const acc = getAccount(accNumber, user?.accounts)
  if (!acc) {
    return notFound()
  }

  useEffect(() => {
    setPageTitle('Trades')
    setPageSubtitle(`A look at the trades inside trading account ${accNumber}`)
    setPageIconCode(Icons.Performance)
    setBreadcrumbs([
      {label: 'Dashboard', href: '/dashboard', active: false},
      {label: 'Accounts', href: '/accounts', active: false},
      {label: accNumber.toString(), href: '/accounts/' + accNumber, active: false},
      {label: 'Trades', href: '/trades?account=default', active: true},
    ])
  }, [])

  useEffect(() => {
    setPageTitle('Trades')
    setPageSubtitle(`A look at the trades inside trading account ${accNumber}`)
    setPageIconCode(Icons.Performance)
    setBreadcrumbs([
      {label: 'Dashboard', href: '/dashboard', active: false},
      {label: 'Accounts', href: '/accounts', active: false},
      {label: accNumber.toString(), href: '/accounts/' + accNumber, active: false},
      {label: 'Trades', href: '/trades?account=default', active: true},
    ])

    setAccNumber(accNumber)
  }, [accNumber]);


  //  GENERAL FUNCTIONS


  //  RENDER

  return (
    <div className={''}>
      {
        isLoading ?
          <div className={'h-[72vh] flex items-center justify-center'}>
            <div className={'grid grid-cols-1 justify-items-center gap-8'}>
              <div>
                <Loader2 className="animate-spin text-primary" size={50}/>
              </div>
              <div className={'text-lg'}>Loading Trades</div>
            </div>
          </div>
          :
          <div className={'grid grid-cols-1 gap-8'}>
            <div className={'flex items-center justify-end gap-4'}>
              <div>
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
            </div>
            <div>
              <BaseCard
                loading={isLoading}
                title={'Trades'}
                subtitle={'A view of each Trade taken in this account.'}
                cardContent={
                  <TradeTable
                    account={acc}
                  />
                }
              />
            </div>
          </div>
      }
    </div>
  )
}