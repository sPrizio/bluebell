'use client'

import {notFound, useSearchParams} from "next/navigation";
import React, {useEffect, useState} from "react";
import {AggregateInterval, Icons} from "@/lib/enums";
import {useSepalPageInfoContext} from "@/lib/context/SepalContext";
import {delay, getAccount, getAccountNumber} from "@/lib/functions";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select";
import {dailyTradeRecords, monthlyTradeRecords, tradeRecordControls, yearlyTradeRecords} from "@/lib/sample-data";
import {Loader2} from "lucide-react";
import {Button} from "@/components/ui/button";
import {
  Drawer,
  DrawerClose,
  DrawerContent,
  DrawerDescription,
  DrawerFooter,
  DrawerHeader,
  DrawerTitle,
  DrawerTrigger,
} from "@/components/ui/drawer"
import {Label} from "@/components/ui/label";
import TradeRecordCard from "@/components/Card/Trade/TradeRecordCard";

/**
 * The page that shows an account's performance over time
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function PerformancePage() {

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
  const [accNumber, setAccNumber] = useState(getAccountNumber(searchParams, user.accounts))
  const [account, setAccount] = useState<Account | null>()
  const [aggInterval, setAggInterval] = useState<AggregateInterval>(AggregateInterval.DAILY)
  const [userSelection, setUserSelection] = useState<UserTradeRecordControlSelection>({
    aggInterval: aggInterval,
    month: 'January',
    year: '2024'
  })
  const [aggMonth, setAggMonth] = useState(userSelection.month)
  const [aggYear, setAggYear] = useState(userSelection.year)
  const [tradeRecords, setTradeRecords] = useState<Array<TradeRecord>>([])
  const [controls, setControls] = useState<TradeRecordControls>()

  const acc = getAccount(accNumber, user.accounts)
  if (!acc) {
    return notFound()
  }

  useEffect(() => {
    setPageTitle('Performance')
    setPageSubtitle(`A look at trading account ${accNumber} performance over time`)
    setPageIconCode(Icons.Performance)
    setBreadcrumbs([
      {label: 'Dashboard', href: '/dashboard', active: false},
      {label: 'Accounts', href: '/accounts', active: false},
      {label: accNumber.toString(), href: '/accounts/' + accNumber, active: false},
      {label: 'Performance', href: '/performance?account=default', active: true},
    ])

    getTradeRecords()
  }, [])

  useEffect(() => {
    setPageTitle('Performance')
    setPageSubtitle(`A look at trading account ${accNumber} performance over time`)
    setPageIconCode(Icons.Performance)
    setBreadcrumbs([
      {label: 'Dashboard', href: '/dashboard', active: false},
      {label: 'Accounts', href: '/accounts', active: false},
      {label: accNumber.toString(), href: '/accounts/' + accNumber, active: false},
      {label: 'Performance', href: '/performance?account=default', active: true},
    ])

    setAccNumber(accNumber)
    setAccount(getAccount(accNumber, user.accounts))
    getTradeRecords()
  }, [accNumber]);

  useEffect(() => {
    getTradeRecords()
  }, [aggInterval, aggMonth, aggYear]);


  //  GENERAL FUNCTIONS

  /**
   * Fetches the trade records
   */
  async function getTradeRecords() {

    setIsLoading(true)

    await delay(2000);
    await getTradeRecordControls()

    //TODO: TEMP
    if (userSelection.aggInterval === AggregateInterval.DAILY) {
      setTradeRecords(dailyTradeRecords)
    } else if (userSelection.aggInterval === AggregateInterval.MONTHLY) {
      setTradeRecords(monthlyTradeRecords)
    } else {
      setTradeRecords(yearlyTradeRecords)
    }

    setIsLoading(false)
  }

  //TODO: temp
  /**
   * Obtains the controls data for trade record filters
   */
  async function getTradeRecordControls() {
    setControls(tradeRecordControls)
  }

  /**
   * Fetches the month entries for the correct year
   */
  function getMatchingYear() : TradeRecordControlsYearEntry | null {
    return controls?.yearEntries.find(ye => ye.year === userSelection.year) ?? null
  }


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
              <div className={'text-lg'}>Loading Performance</div>
            </div>
          </div>
          :
          <>
            <div className={'grid grid-cols-1 gap-8'}>
              <div className={'flex items-center justify-end gap-4'}>
                <div>
                  <Select value={accNumber.toString()} onValueChange={(val) => setAccNumber(parseInt(val))}>
                    <SelectTrigger className="w-[180px] bg-white">
                      <SelectValue placeholder="Account"/>
                    </SelectTrigger>
                    <SelectContent>
                      {
                        user.accounts.map((item : Account) => {
                          return (
                            <SelectItem key={item.uid} value={item.accountNumber.toString()}>{item.name}</SelectItem>
                          )
                        })
                      }
                    </SelectContent>
                  </Select>
                </div>
                <div>
                  <Drawer>
                    <DrawerTrigger asChild>
                      <Button variant="primary">Filters</Button>
                    </DrawerTrigger>
                    <DrawerContent>
                      <div className="mx-auto w-full max-w-md">
                        <DrawerHeader>
                          <DrawerTitle>Filter Performance</DrawerTitle>
                          <DrawerDescription>Look at your performance at specific points in time.</DrawerDescription>
                        </DrawerHeader>
                        <div className="grid grid-cols-3 items-center w-full gap-1.5 p-4">
                          <div>
                            <Label>Interval</Label>
                            <Select value={userSelection.aggInterval.code} onValueChange={(val) => setUserSelection({...userSelection, aggInterval: AggregateInterval.get(val)})}>
                              <SelectTrigger className="bg-white">
                                <SelectValue placeholder="Account"/>
                              </SelectTrigger>
                              <SelectContent>
                                <SelectItem value={AggregateInterval.DAILY.code}>{AggregateInterval.DAILY.label}</SelectItem>
                                <SelectItem value={AggregateInterval.MONTHLY.code}>{AggregateInterval.MONTHLY.label}</SelectItem>
                                <SelectItem value={AggregateInterval.YEARLY.code}>{AggregateInterval.YEARLY.label}</SelectItem>
                              </SelectContent>
                            </Select>
                          </div>
                          <div>
                            <Label>Month</Label>
                            <Select value={userSelection.month} disabled={userSelection.aggInterval.code !== AggregateInterval.DAILY.code} onValueChange={(val) => setUserSelection({...userSelection, month: val})}>
                              <SelectTrigger className="bg-white">
                                <SelectValue placeholder="Account"/>
                              </SelectTrigger>
                              <SelectContent>
                                {
                                  getMatchingYear()?.monthEntries.map(item => {
                                    return (
                                      <SelectItem key={item.uid} value={item.month} disabled={item.value === 0}>{item.month}</SelectItem>
                                    )
                                  }) ?? <SelectItem value={'NA'}>N/A</SelectItem>
                                }
                              </SelectContent>
                            </Select>
                          </div>
                          <div>
                            <Label>Year</Label>
                            <Select value={userSelection.year} onValueChange={(val) => setUserSelection({...userSelection, year: val})}>
                              <SelectTrigger className="bg-white">
                                <SelectValue placeholder="Account"/>
                              </SelectTrigger>
                              <SelectContent>
                                {
                                  tradeRecordControls.yearEntries?.map(item => {
                                    return (
                                      <SelectItem key={item.uid} value={item.year} disabled={item.monthEntries.length === 0}>{item.year}</SelectItem>
                                    )
                                  }) ?? <SelectItem value={'NA'}>N/A</SelectItem>
                                }
                              </SelectContent>
                            </Select>
                          </div>
                        </div>
                        <DrawerFooter className={''}>
                          <Button variant={'primary'} onClick={() => {
                            setAggInterval(userSelection.aggInterval)
                            setAggMonth(userSelection.month)
                            setAggYear(userSelection.year)
                          }}>Submit</Button>
                          <DrawerClose asChild>
                            <Button variant="outline" onClick={() => setUserSelection({aggInterval: aggInterval, month: aggMonth, year: aggYear})}>Cancel</Button>
                          </DrawerClose>
                        </DrawerFooter>
                      </div>
                    </DrawerContent>
                  </Drawer>
                </div>
              </div>
            </div>
            <div className={'grid grid-cols-1 gap-8 mt-8'}>
              {
                tradeRecords?.map(item => {
                  return (
                    <TradeRecordCard key={item.uid} tradeRecord={item} aggInterval={aggInterval} />
                  )
                })
              }
            </div>
          </>
      }
    </div>
  )
}