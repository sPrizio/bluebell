'use client'

import React, {useEffect, useState} from "react";
import BaseModal from "@/components/Modal/BaseModal";
import {Button} from "@/components/ui/button";
import {IconCirclePlus, IconEdit, IconExternalLink, IconPlus, IconTrash} from "@tabler/icons-react";
import AccountForm from "@/components/Form/account/AccountForm";
import DeleteAccountForm from "@/components/Form/account/DeleteAccountForm";
import {BaseCard} from "@/components/Card/BaseCard";
import AccountInformation from "@/components/Account/AccountInformation";
import {Switch} from "@/components/ui/switch";
import {Label} from "@/components/ui/label";
import SimpleBanner from "@/components/Banner/SimpleBanner";
import {delay} from "@/lib/functions";
import {accountDetails, dailyTradeRecords, trades} from "@/lib/sample-data";
import AccountEquityChart from "@/components/Chart/Account/AccountEquityChart";
import {Progress} from "@/components/ui/progress";
import AccountInsights from "@/components/Account/AccountInsights";
import AccountStatistics from "@/components/Account/AccountStatistics";
import TradeRecordTable from "@/components/Table/trade/TradeRecordTable";
import TradeTable from "@/components/Table/trade/TradeTable";
import Link from "next/link";

/**
 * Renders the account details layout
 *
 * @param account account info
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function AccountDetails(
  {
    account,
  }
    : Readonly<{
    account: Account
  }>
) {

  const [isLoading, setIsLoading] = useState(false)
  const [accDetails, setAccDetails] = useState<AccountDetails>()
  const [showPoints, setShowPoints] = useState(false)

  useEffect(() => {
    getAccountDetails()
  }, []);


  //  GENERAL FUNCTIONS

  /**
   * Fetches the account details
   */
  async function getAccountDetails() {
    setIsLoading(true)

    await delay(3000)
    setAccDetails(accountDetails)

    setIsLoading(false)
  }

  /**
   * Computes the consistency general color
   */
  function computeConsistencyColor() {
    const val = accDetails?.consistency ?? 0
    switch (true) {
      case (val < 35):
        return 'primaryRed'
      case (val < 75):
        return 'primaryYellow'
      case (val <= 100):
        return 'primaryGreen'
      default:
        return 'primary'
    }
  }

  /**
   * Computes the consistency general value
   */
  function computeConsistency() {
    const val = accDetails?.consistency ?? 0
    switch (true) {
      case (val < 35):
        return 'danger'
      case (val < 75):
        return 'warning'
      case (val <= 100):
        return 'success'
      default:
        return 'info'
    }
  }

  /**
   * Computes the consistency status text
   */
  function computeConsistencyStatus() {
    const val = accDetails?.consistency ?? 0
    switch (true) {
      case (val < 35):
        return 'Poor'
      case (val < 75):
        return 'Average'
      case (val <= 100):
        return 'Great!'
      default:
        return ''
    }
  }


  //  RENDER

  const intervalStyles = ' text-center font-bold text-sm bg-opacity-15 border-r-4 py-2 '

  return (
    <div className={'grid sm:grid-cols-1 lg:grid-cols-2 xl:grid-cols-4 gap-6'}>
      <div className={'sm:col-span-1 lg:col-span-2 xl:col-span-4'}>
        <SimpleBanner
          text={(account?.active ?? false) ? 'This account is currently active.' : 'This account is marked currently inactive.'}
          variant={(account?.active ?? false) ? 'info' : 'danger'}
        />
      </div>
      <div className={'sm:col-span-1 lg:col-span-2 xl:col-span-4'}>
        <div className={'flex gap-4 items-center justify-end'}>
          {
            (account?.active ?? false) ?
              <div className={''}>
                <BaseModal
                  key={0}
                  title={'Import Trades'}
                  description={'Here you may manually import trades into the account for tracking.'}
                  trigger={<Button className="" variant={"primary"}><IconCirclePlus/>&nbsp;Import Trades</Button>}
                  content={<AccountForm mode={'edit'} account={account}/>}
                />
              </div> : null
          }
          <div className={''}>
            <BaseModal
              key={0}
              title={'Update Trading Account Information'}
              description={'Here you can edit/update any account information. Note that some aspects of this account cannot be changed after account creation.'}
              trigger={<Button className="" variant={"outline"}><IconEdit/>&nbsp;Update</Button>}
              content={<AccountForm mode={'edit'} account={account}/>}
            />
          </div>
          <div className={''}>
            <BaseModal
              key={1}
              title={'Delete Trading Account'}
              trigger={<Button
                className="bg-primaryRed text-white hover:bg-primaryRedLight"><IconTrash/>&nbsp;Delete</Button>}
              content={<DeleteAccountForm account={account ?? null} />}
            />
          </div>
        </div>
      </div>
      <div className={'xl:col-span-3'}>
        <div className={'col-span-1 lg:col-span-3'}>
          <div className={'grid col-span-1 gap-6'}>
            <div>
              <BaseCard
                loading={isLoading}
                title={'Account Equity'}
                subtitle={'A look at the evolution of your account since inception.'}
                cardContent={<AccountEquityChart data={accDetails?.equity ?? []} showPoints={showPoints}/>}
                headerControls={[
                  <div key={0} className="flex items-center space-x-2">
                    <Label htmlFor="airplane-mode">Show as Points</Label>
                    <Switch id="airplane-mode" checked={showPoints} onCheckedChange={setShowPoints}/>
                  </div>,
                  <div key={1}>
                    <Link href={`/transactions?account=${account?.accountNumber}`}>
                      <Button variant={'outline'}><IconExternalLink size={18}/>&nbsp;Transactions</Button>
                    </Link>
                  </div>
                ]}
              />
            </div>
            <div>
              <BaseCard
                loading={isLoading}
                title={'Consistency'}
                subtitle={'This calculation includes both sizing, RRR and general performance. A greater score indicates higher consistency.'}
                cardContent={
                  <div className={'grid grid-cols-1 items-center justify-end gap-2'}>
                    <div className={'flex items-center justify-end gap-2'}>
                      Consistency Score: <span
                      className={'font-bold text-' + computeConsistencyColor()}>{computeConsistencyStatus()}&nbsp;&nbsp;({accDetails?.consistency ?? 0}%)</span>
                    </div>
                    <div>
                      <Progress
                        className={'h-6'}
                        value={accDetails?.consistency ?? 0}
                        variant={computeConsistency()}
                      />
                    </div>
                    <div className={'flex items-center justify-end w-full gap-1'}>
                      <div className={intervalStyles + ' basis-[34%] bg-primaryRed border-primaryRed text-primaryRed'}>
                        &lt;&nbsp;35%
                      </div>
                      <div
                        className={intervalStyles + ' basis-[40%] bg-primaryYellow border-primaryYellow text-primaryYellow'}>
                        35-75%
                      </div>
                      <div
                        className={intervalStyles + ' basis-[26%] bg-primaryGreen border-primaryGreen text-primaryGreen'}>
                        &gt;&nbsp;75%
                      </div>
                    </div>
                  </div>
                }
              />
            </div>
          </div>
        </div>
      </div>
      <div className={''}>
        <BaseCard
          loading={isLoading}
          title={'Account Information'}
          cardContent={<AccountInformation account={account}/>}
        />
      </div>
      <div className={'sm:col-span-1 lg:col-span-2 xl:col-span-4'}>
        {
          accDetails?.insights ?
            <BaseCard
              loading={isLoading}
              title={'Insights'}
              subtitle={'A quick look at some of the key markers of this account\'s performance.'}
              cardContent={<AccountInsights insights={accDetails.insights}/>}
            />
            :
            null
        }
      </div>
      <div className={'xl:col-span-2'}>
        {
          accDetails?.statistics ?
            <BaseCard
              loading={isLoading}
              title={'Statistics'}
              subtitle={'A look some of this account\'s key statistical measures for performance.'}
              cardContent={<AccountStatistics statistics={accDetails.statistics}/>}
            />
            :
            null
        }
      </div>
      <div className={'xl:col-span-2 flex justify-end'}>
        <BaseCard
          loading={isLoading}
          title={'Performance'}
          subtitle={'A look at this account\'s recent daily performance.'}
          cardContent={<TradeRecordTable records={dailyTradeRecords}/>}
          headerControls={[
            <Link key={0} href={`/performance?account=${account?.accountNumber}`}>
              <Button className="" variant={"outline"}><IconExternalLink size={18}/>&nbsp;View Full Performance</Button>
            </Link>
          ]}
        />
      </div>
      <div className={'sm:col-span-1 lg:col-span-2 xl:col-span-4'}>
        <BaseCard
          loading={isLoading}
          title={'Trades'}
          subtitle={'A view of each trade taken in this account.'}
          headerControls={[
            <Link key={0} href={`/trades?account=${account?.accountNumber}`}>
              <Button className="" variant={"outline"}><IconExternalLink size={18}/>&nbsp;View All Trades</Button>
            </Link>
          ]}
          cardContent={
            <TradeTable
              trades={trades}
              totalElements={trades.length}
              page={0}
              pageSize={10}
            />
          }
        />
      </div>
    </div>
  )
}