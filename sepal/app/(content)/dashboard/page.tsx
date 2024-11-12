'use client'

import React, {useEffect} from "react";
import {Icons} from "@/lib/enums";
import {useSepalPageInfoContext} from "@/lib/context/SepalContext";
import {BaseCard} from "@/components/Card/BaseCard";
import {Button} from "@/components/ui/button";
import DashboardContent from "@/components/Card/Content/DashboardContent";
import {resolveIcon} from "@/lib/functions/util-functions";
import AccountsTable from "@/components/Table/Account/AccountsTable";
import {IconCirclePlus} from "@tabler/icons-react";
import TradeLogTable from "@/components/Table/Trade/TradeLogTable";
import {accounts, chartData, tradeLog} from "@/lib/sample-data";
import AccountTransactionsTable from "@/components/Table/Account/AccountTransactionsTable";
import PortfolioGrowthChart from "@/components/Chart/Account/PortfolioGrowthChart";
import BaseModal from "@/components/Modal/BaseModal";
import AccountForm from "@/components/Form/Account/AccountForm";

/**
 * The page that shows an overview of a user's portfolio
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function DashboardPage() {

  const {
    pageTitle,
    pageSubtitle,
    pageIconCode,
    breadcrumbs,
    setPageTitle,
    setPageSubtitle,
    setPageIconCode,
    setBreadcrumbs
  } = useSepalPageInfoContext()

  useEffect(() => {
    setPageTitle('Dashboard')
    setPageSubtitle('An overview of your trading portfolio.')
    setPageIconCode(Icons.Dashboard)
    setBreadcrumbs([
      {label: 'Dashboard', href: '/dashboard', active: true},
    ])

  }, [])


  //  RENDER

  return (
    <div className={'grid grid-cols-1 gap-8 w-full'}>
      <div className={"grid grid-cols-1 md:grid-cols-2 xl:grid-cols-4 gap-8"}>
        <div className={""}>
          <BaseCard
            title={'Net Worth'}
            cardContent={<DashboardContent prefix={'$'} value={40000} delta={3.48}/>}
            icon={resolveIcon(Icons.ChartDoughnut, '', 30)}
          />
        </div>
        <div className={""}>
          <BaseCard
            title={'Trades'}
            cardContent={<DashboardContent value={650} delta={12}/>}
            icon={resolveIcon(Icons.Replace, '', 30)}
          />
        </div>
        <div>
          <BaseCard
            title={'Deposits'}
            cardContent={<DashboardContent value={336} delta={20.4}/>}
            icon={resolveIcon(Icons.ArrowBarDown, '', 30)}
          />
        </div>
        <div>
          <BaseCard
            title={'Withdrawals'}
            cardContent={<DashboardContent value={18} delta={-1.10}/>}
            icon={resolveIcon(Icons.ArrowBarUp, '', 30)}
          />
        </div>
      </div>
      <div className={"grid grid-cols-1 xl:grid-cols-3 gap-8"}>
        <div className={"col-span-1 xl:col-span-2"}>
          <BaseCard
            title={'Portfolio Growth'}
            subtitle={'A look back at your portfolio\'s performance over the last 6 months.'}
            cardContent={<PortfolioGrowthChart data={chartData}/>}
          />
        </div>
        <div className={""}>
          <BaseCard
            title={'Accounts'}
            subtitle={'Only active accounts will be shown.'}
            cardContent={
              <AccountsTable
                accounts={accounts}
                showAllLink={true}
              />
            }
          />
        </div>
      </div>
      <div className={"grid grid-cols-1 xl:grid-cols-3 gap-8"}>
        <div className={"col-span-1 xl:col-span-2"}>
          <BaseCard
            title={'Trade Log'}
            subtitle={'Your performance over the last few days.'}
            cardContent={<TradeLogTable log={tradeLog}/>}
          />
        </div>
        <div className={""}>
          <BaseCard
            title={'Transaction Activity'}
            subtitle={'Your most recent Account transactions.'}
            cardContent={
              <AccountTransactionsTable
                account={accounts[0]}
                transactions={accounts[0].transactions}
              />
            }
          />
        </div>
      </div>
    </div>
  );
}