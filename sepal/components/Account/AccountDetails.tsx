'use client'

import React, {useEffect, useState} from "react";
import BaseModal from "@/components/Modal/BaseModal";
import {Button} from "@/components/ui/button";
import {IconEdit, IconTrash} from "@tabler/icons-react";
import AccountForm from "@/components/Form/account/AccountForm";
import DeleteAccountForm from "@/components/Form/account/DeleteAccountForm";
import {BaseCard} from "@/components/Card/BaseCard";
import AccountInformation from "@/components/Account/AccountInformation";
import {Switch} from "@/components/ui/switch";
import {Label} from "@/components/ui/label";
import SimpleBanner from "@/components/Banner/SimpleBanner";
import {delay} from "@/lib/functions";
import {accountDetails} from "@/lib/sample-data";
import AccountEquityChart from "@/components/Chart/AccountEquityChart";

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
    account?: Account
  }>
) {

  const [isLoading, setIsLoading] = useState(false)
  const [accDetails, setAccDetails] = useState<AccountDetails>()
  const [showPoints, setShowPoints] = useState(true)

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


  //  RENDER

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
              content={<DeleteAccountForm/>}
            />
          </div>
        </div>
      </div>
      <div className={'xl:col-span-3'}>
        <div className={'col-span-1 lg:col-span-3'}>
          <BaseCard
            loading={isLoading}
            title={'Account Equity'}
            subtitle={'A look at the evolution of your account since inception.'}
            cardContent={<AccountEquityChart data={accDetails?.equity ?? []} showPoints={showPoints} />}
            headerControls={[
              <div key={0} className="flex items-center space-x-2">
                <Label htmlFor="airplane-mode">Show as Points</Label>
                <Switch id="airplane-mode" checked={showPoints} onCheckedChange={setShowPoints} />
              </div>
            ]}
          />
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
        <BaseCard
          loading={isLoading}
          title={'Insights'}
          subtitle={'A quick look at some of the key markers of this account\'s performance.'}
          cardContent={<p>Trading days, max daily loss, max total loss, max daily profit, max profit</p>}
        />
      </div>
      <div className={'xl:col-span-2'}>
        <BaseCard
          loading={isLoading}
          title={'Statistics'}
          subtitle={'A look some of this account\'s key statistical measures for performance.'}
          cardContent={<p>Statistics: equity, average profit, average loss, balance, no of trades, average RRR, lots,
            expectancy, win
            rate, profit factor, retention, sharpe ratio</p>}
        />
      </div>
      <div className={'xl:col-span-2 flex justify-end'}>
        <BaseCard
          loading={isLoading}
          title={'Performance'}
          subtitle={'A look at this account\'s daily performance.'}
          cardContent={<p>trade record summary (daily)</p>}
        />
      </div>
      <div className={'sm:col-span-1 lg:col-span-2 xl:col-span-4'}>
        <BaseCard
          loading={isLoading}
          title={'Trades'}
          subtitle={'A view of each trade taken in this account'}
          cardContent={<p>Trades log</p>}
        />
      </div>
    </div>
  )
}