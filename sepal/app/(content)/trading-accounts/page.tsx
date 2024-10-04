'use client'

import React, {useEffect} from "react";
import {Icons} from "@/lib/enums";
import {useSepalPageInfoContext} from "@/lib/SepalContext";
import {BaseCard} from "@/components/Card/BaseCard";

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
      <div className={"grid grid-cols-2 xl:grid-cols-4 gap-8"}>
        <div className={""}><BaseCard/></div>
        <div className={""}><BaseCard/></div>
        <div className={""}><BaseCard/></div>
        <div className={""}><BaseCard/></div>
        {/*Account Profit in last month, trades in last month, deposits & withdrawals*/}
      </div>
      <div className={"grid grid-cols-1 xl:grid-cols-3 gap-8"}>
        {/*Graph and Accounts list row*/}
        <div className={"col-span-2"}><BaseCard/></div>
        <div className={""}><BaseCard/></div>
      </div>
      <div className={"grid grid-cols-1 xl:grid-cols-3 gap-8"}>
        {/*Trade Log & Account Transactions row*/}
        <div className={"col-span-2"}><BaseCard/></div>
        <div className={""}><BaseCard/></div>
      </div>
    </div>
  );
}