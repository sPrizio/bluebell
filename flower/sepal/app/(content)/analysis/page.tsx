'use client'

import {useSepalPageInfoContext} from "@/lib/context/SepalContext";
import {getAccount, getAccountNumber} from "@/lib/functions/util-functions";
import {notFound, useSearchParams} from "next/navigation";
import React, {useEffect, useState} from "react";
import {Icons} from "@/lib/enums";
import {Loader2} from "lucide-react";
import {BaseCard} from "@/components/Card/BaseCard";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select";
import TimeBucketAnalysis from "@/components/Analysis/TimeBucketAnalysis";
import {Account, FilterSelector, TradeDurationFilterSelector, Weekday} from "@/types/apiTypes";
import WeekdayAnalysis from "@/components/Analysis/WeekdayAnalysis";
import WeekdayTimeBucketAnalysis from "@/components/Analysis/WeekdayTimeBucketAnalysis";
import TradeDurationAnalysis from "@/components/Analysis/TradeDurationAnalysis";

/**
 * The page that shows an analysis of an account's performance
 *
 * @author Stephen Prizio
 * @version 0.0.2
 */
export default function AnalysisPage() {

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
  const [accNumber, setAccNumber] = useState(getAccountNumber(searchParams, user?.accounts ?? []))
  const [openTbType, setOpenTbType] = useState<FilterSelector>('PROFIT')
  const [closedTbType, setClosedTbType] = useState<FilterSelector>('PROFIT')
  const [wdType, setWdType] = useState<FilterSelector>('PROFIT')
  const [tbWdType, setTbWdType] = useState<FilterSelector>('PROFIT')
  const [tdType, setTdType] = useState<FilterSelector>('PROFIT')
  const [weekday, setWeekday] = useState<Weekday>('MONDAY')
  const [tdFilter, setTdFilter] = useState<TradeDurationFilterSelector>('ALL')

  useEffect(() => {
    setPageTitle('Analysis')
    setPageSubtitle(`A more in-depth look at various aspects of trading account ${accNumber}'s performance.`)
    setPageIconCode(Icons.Analysis)
    setBreadcrumbs([
      {label: 'Dashboard', href: '/dashboard', active: false},
      {label: 'Accounts', href: '/accounts', active: false},
      {label: accNumber.toString(), href: '/accounts/' + accNumber, active: false},
      {label: 'Analysis', href: '/analysis?account=default', active: true},
    ])
  }, [])

  useEffect(() => {
    setPageTitle('Analysis')
    setPageSubtitle(`A more in-depth look at various aspects of trading account ${accNumber}'s performance.`)
    setPageIconCode(Icons.Analysis)
    setBreadcrumbs([
      {label: 'Dashboard', href: '/dashboard', active: false},
      {label: 'Accounts', href: '/accounts', active: false},
      {label: accNumber.toString(), href: '/accounts/' + accNumber, active: false},
      {label: 'Analysis', href: '/analysis?account=default', active: true},
    ])

    setAccNumber(accNumber)
  }, [accNumber]);

  const acc = getAccount(accNumber, user?.accounts ?? [])
  if (!acc) {
    return notFound()
  }


  //  COMPONENTS

  /**
   * Renders a re-usable select for selecting chart types
   *
   * @param value state variable
   * @param callBack setState function
   */
  const select = (value: string, callBack: Function) => {
    return (
      <div>
        <Select value={value} onValueChange={(val) => callBack(val)}>
          <SelectTrigger className="w-[120px] bg-white">
            <SelectValue placeholder={'Select a value...'}/>
          </SelectTrigger>
          <SelectContent>
            <SelectItem value={'PROFIT'}>Net Profit</SelectItem>
            <SelectItem value={'POINTS'}>Points</SelectItem>
            <SelectItem value={'PERCENTAGE'}>Win %</SelectItem>
          </SelectContent>
        </Select>
      </div>
    )
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
          <div className={'grid grid-cols-2 justify-center gap-8'}>
            <div className={'col-span-2'}>
              <div className={'flex items-center justify-end gap-4'}>
                <div>
                  <Select value={accNumber.toString()} onValueChange={(val) => setAccNumber(parseInt(val))}>
                    <SelectTrigger className="w-[180px] bg-white">
                      <SelectValue placeholder="Account"/>
                    </SelectTrigger>
                    <SelectContent>
                      {
                        user?.accounts?.map((item: Account) => {
                          return (
                            <SelectItem key={item.uid} value={item.accountNumber.toString()}>{item.name}</SelectItem>
                          )
                        }) ?? null
                      }
                    </SelectContent>
                  </Select>
                </div>
              </div>
            </div>
            <div>
              <BaseCard
                title={'Time Buckets (Opened)'}
                subtitle={'How your trades performed for each segment of the day when opened.'}
                headerControls={[select(openTbType, setOpenTbType)]}
                cardContent={<TimeBucketAnalysis accountNumber={accNumber} filter={openTbType} opened={true}/>}
              />
            </div>
            <div>
              <BaseCard
                title={'Time Buckets (Closed)'}
                subtitle={'How your trades performed for each segment of the day when closed.'}
                headerControls={[select(closedTbType, setClosedTbType)]}
                cardContent={<TimeBucketAnalysis accountNumber={accNumber} filter={closedTbType}/>}
              />
            </div>
            <div>
              <BaseCard
                title={'Weekday Performance'}
                subtitle={'How your trades performed for each day of the week.'}
                headerControls={[select(wdType, setWdType)]}
                cardContent={<WeekdayAnalysis accountNumber={accNumber} filter={wdType}/>}
              />
            </div>
            <div>
              <BaseCard
                title={'Weekday & Time Bucket Performance'}
                subtitle={'How your trades performed at different times on a specific weekday.'}
                headerControls={[
                  <div key={0}>
                    <Select value={weekday} onValueChange={(val) => setWeekday(val as Weekday)}>
                      <SelectTrigger className="w-[120px] bg-white">
                        <SelectValue placeholder={'Select a value...'}/>
                      </SelectTrigger>
                      <SelectContent>
                        <SelectItem value={'MONDAY'}>Monday</SelectItem>
                        <SelectItem value={'TUESDAY'}>Tuesday</SelectItem>
                        <SelectItem value={'WEDNESDAY'}>Wednesday</SelectItem>
                        <SelectItem value={'THURSDAY'}>Thursday</SelectItem>
                        <SelectItem value={'FRIDAY'}>Friday</SelectItem>
                      </SelectContent>
                    </Select>
                  </div>,
                  select(tbWdType, setTbWdType)
                ]}
                cardContent={<WeekdayTimeBucketAnalysis accountNumber={accNumber} weekday={weekday} filter={wdType}/>}
              />
            </div>
            <div>
              <BaseCard
                title={'Trade Duration Performance'}
                subtitle={'How your trades performed for various lengths of time.'}
                headerControls={[
                  <div key={0}>
                    <Select value={tdFilter} onValueChange={(val) => setTdFilter(val as TradeDurationFilterSelector)}>
                      <SelectTrigger className="w-[120px] bg-white">
                        <SelectValue placeholder={'Select a value...'}/>
                      </SelectTrigger>
                      <SelectContent>
                        <SelectItem value={'ALL'}>All Trades</SelectItem>
                        <SelectItem value={'WINS'}>Wins Only</SelectItem>
                        <SelectItem value={'LOSSES'}>Losses Only</SelectItem>
                      </SelectContent>
                    </Select>
                  </div>,
                  select(tdType, setTdType)
                ]}
                cardContent={<TradeDurationAnalysis accountNumber={accNumber} filter={tdType} tdFilter={tdFilter} />}
              />
            </div>
            <div>
              Add average count and change color of bar if the count is above average/std<br/>
            </div>
          </div>
      }
    </div>
  )
}