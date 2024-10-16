import React from "react";
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


  //  RENDER

  return (
    <div className={'grid sm:grid-cols-1 lg:grid-cols-2 xl:grid-cols-4 gap-6'}>
      <div className={'sm:col-span-1 lg:col-span-2 xl:col-span-4'}>
        <SimpleBanner
          text={(account?.active ?? false) ? 'This account is active.' : 'This account is marked currently inactive.'}
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
            title={'Account Equity'}
            subtitle={'A look at the evolution of your account since inception.'}
            cardContent={<p>Include chart & consistency score below chart</p>}
            headerControls={[
              <div key={0} className="flex items-center space-x-2">
                <Switch id="airplane-mode"/>
                <Label htmlFor="airplane-mode">Points only</Label>
              </div>
            ]}
          />
        </div>
      </div>
      <div className={''}>
        <BaseCard
          title={'Account Information'}
          cardContent={<AccountInformation account={account}/>}
        />
      </div>
      <div className={'sm:col-span-1 lg:col-span-2 xl:col-span-4'}>
        <BaseCard
          title={'Insights'}
          subtitle={'A quick look at some of the key markers of this account\'s performance.'}
          cardContent={<p>Trading days, max daily loss, max total loss, max daily profit, max profit</p>}
        />
      </div>
      <div className={'xl:col-span-2'}>
        <BaseCard
          title={'Statistics'}
          subtitle={'A look some of this account\'s key statistical measures for performance.'}
          cardContent={<p>Statistics: equity, average profit, average loss, balance, no of trades, average RRR, lots,
            expectancy, win
            rate, profit factor, retention, sharpe ratio</p>}
        />
      </div>
      <div className={'xl:col-span-2'}>
        <BaseCard
          title={'Performance'}
          subtitle={'A look at this account\'s daily performance.'}
          cardContent={<p>trade record summary (daily)</p>}
        />
      </div>
      <div className={'sm:col-span-1 lg:col-span-2 xl:col-span-4'}>
        <BaseCard
          title={'Trades'}
          subtitle={'A view of each trade taken in this account'}
          cardContent={<p>Trades log</p>}
        />
      </div>
    </div>
  )
}