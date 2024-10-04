'use client'

import React, {useEffect} from "react";
import {Icons} from "@/lib/enums";
import {useSepalPageInfoContext} from "@/lib/SepalContext";
import {BaseCard} from "@/components/Card/BaseCard";
import {Check} from "lucide-react";
import {Button} from "@/components/ui/button";

/**
 * The page that shows all of a user's accounts
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function AccountsPage() {

  const {
    pageTitle,
    pageSubtitle,
    pageIconCode,
    setPageTitle,
    setPageSubtitle,
    setPageIconCode
  } = useSepalPageInfoContext()

  useEffect(() => {
    setPageTitle('Trading Accounts')
    setPageSubtitle('An overview of your trading accounts.')
    setPageIconCode(Icons.TradingAccounts)
  }, [])


  //  RENDER

  return (
    <div className={'grid grid-cols-1 gap-16 w-full'}>
      <div className={"grid grid-cols-1 md:grid-cols-2 xl:grid-cols-4 gap-8"}>
        <div className={""}>
          <BaseCard
            title={'Notifications'}
            subtitle={'You have 3 unread notifications.'}
            cardContent={<p>Hello World!</p>}
            headerControl={
              <Button className="w-full text-white">Create</Button>
            }
            footerControls={[
              <Button className="w-full text-white">Submit</Button>,
              <Button className="w-full" variant={"outline"}>Cancel</Button>
            ]}
          />
        </div>
        <div className={""}>
          <BaseCard
            title={'Notifications'}
            subtitle={'You have 3 unread notifications.'}
            cardContent={<p>Hello World!</p>}
            headerControl={
              <Button className="w-full text-white">Create</Button>
            }
            footerControls={[
              <Button className="w-full text-white">Submit</Button>,
              <Button className="w-full" variant={"outline"}>Cancel</Button>
            ]}
          />
        </div>
        <BaseCard
          title={'Notifications'}
          subtitle={'You have 3 unread notifications.'}
          cardContent={<p>Hello World!</p>}
          headerControl={
            <Button className="w-full text-white">Create</Button>
          }
          footerControls={[
            <Button className="w-full text-white">Submit</Button>,
            <Button className="w-full" variant={"outline"}>Cancel</Button>
          ]}
        />
        <BaseCard
          title={'Notifications'}
          subtitle={'You have 3 unread notifications.'}
          cardContent={<p>Hello World!</p>}
          headerControl={
            <Button className="w-full text-white">Create</Button>
          }
          footerControls={[
            <Button className="w-full text-white">Submit</Button>,
            <Button className="w-full" variant={"outline"}>Cancel</Button>
          ]}
        />
        {/*Account Profit in last month, trades in last month, deposits & withdrawals*/}
      </div>
      <div className={"grid grid-cols-1 xl:grid-cols-3 gap-8"}>
        {/*Graph and Accounts list row*/}

        <div className={"col-span-1 xl:col-span-2"}>
          <BaseCard
            title={'Notifications'}
            subtitle={'You have 3 unread notifications.'}
            cardContent={<p>Hello World!</p>}
            headerControl={
              <Button className="w-full text-white">Create</Button>
            }
            footerControls={[
              <Button className="w-full text-white">Submit</Button>,
              <Button className="w-full" variant={"outline"}>Cancel</Button>
            ]}
          />
        </div>
        <div className={""}>
          <BaseCard
            title={'Notifications'}
            subtitle={'You have 3 unread notifications.'}
            cardContent={<p>Hello World!</p>}
            headerControl={
              <Button className="w-full text-white">Create</Button>
            }
            footerControls={[
              <Button className="w-full text-white">Submit</Button>,
              <Button className="w-full" variant={"outline"}>Cancel</Button>
            ]}
          />
        </div>
      </div>
      <div className={"grid grid-cols-1 xl:grid-cols-3 gap-8"}>
        {/*Trade Log & Account Transactions row*/}
        <div className={"col-span-1 xl:col-span-2"}>
          <BaseCard
            title={'Notifications'}
            subtitle={'You have 3 unread notifications.'}
            cardContent={<p>Hello World!</p>}
            headerControl={
              <Button className="w-full text-white">Create</Button>
            }
            footerControls={[
              <Button className="w-full text-white">Submit</Button>,
              <Button className="w-full" variant={"outline"}>Cancel</Button>
            ]}
          />
        </div>
        <div className={""}>
          <BaseCard
            title={'Notifications'}
            subtitle={'You have 3 unread notifications.'}
            cardContent={<p>Hello World!</p>}
            headerControl={
              <Button className="w-full text-white">Create</Button>
            }
            footerControls={[
              <Button className="w-full text-white">Submit</Button>,
              <Button className="w-full" variant={"outline"}>Cancel</Button>
            ]}
          />
        </div>
      </div>
    </div>
  );
}