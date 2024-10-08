'use client'

import React, {useEffect} from "react";
import {Icons} from "@/lib/enums";
import {useSepalPageInfoContext} from "@/lib/SepalContext";
import {BaseCard} from "@/components/Card/BaseCard";
import {Check} from "lucide-react";
import {Button} from "@/components/ui/button";
import DashboardContent from "@/components/Card/content/DashboardContent";
import {resolveIcon} from "@/lib/services";
import AccountsTable from "@/components/Table/AccountsTable";
import {IconCirclePlus, IconPlus} from "@tabler/icons-react";
import TradeLogTable from "@/components/Table/TradeLogTable";
import {record} from "zod";
import {accounts, accountTransactions, chartData, tradeLog, tradeRecords} from "@/lib/sample-data";
import AccountTransactionsTable from "@/components/Table/AccountTransactionsTable";
import PortfolioGrowthChart from "@/components/Chart/PortfolioGrowthChart";

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
    setPageTitle('Dashboard')
    setPageSubtitle('An overview of your trading portfolio.')
    setPageIconCode(Icons.Dashboard)
  }, [])


  //  RENDER

  return (
    <div className={'grid grid-cols-1 gap-16 w-full'}>
      <div className={"grid grid-cols-1 md:grid-cols-2 xl:grid-cols-4 gap-8"}>
        <div className={""}>
          <BaseCard
            title={'Net Worth'}
            cardContent={<DashboardContent prefix={'$'} value={40000} delta={3.48} icon={resolveIcon(Icons.ChartDoughnut, '', 40)} />}
          />
        </div>
        <div className={""}>
          <BaseCard
            title={'Trades'}
            cardContent={<DashboardContent value={650} delta={12} icon={resolveIcon(Icons.Replace, '', 40)} />}
          />
        </div>
        <div>
          <BaseCard
            title={'Deposits'}
            cardContent={<DashboardContent value={336} delta={20.4} icon={resolveIcon(Icons.ArrowBarDown, '', 40)} />}
          />
        </div>
        <div>
          <BaseCard
            title={'Withdrawals'}
            cardContent={<DashboardContent value={18} delta={-1.10} icon={resolveIcon(Icons.ArrowBarUp, '', 40)} />}
          />
        </div>
      </div>
      <div className={"grid grid-cols-1 xl:grid-cols-3 gap-8"}>
        <div className={"col-span-1 xl:col-span-2"}>
          <BaseCard
            title={'Portfolio Growth'}
            subtitle={'A look back at your portfolio\'s performance over the last 6 months.'}
            cardContent={<PortfolioGrowthChart data={chartData} />}
          />
        </div>
        <div className={""}>
          <BaseCard
            title={'Accounts'}
            subtitle={'Only active accounts will be shown.'}
            cardContent={<AccountsTable accounts={accounts} />}
            headerControl={
              <Button className="w-full text-white"><IconCirclePlus />&nbsp;Add</Button>
            }
          />
        </div>
      </div>
      <div className={"grid grid-cols-1 xl:grid-cols-3 gap-8"}>
        <div className={"col-span-1 xl:col-span-2"}>
          <BaseCard
            title={'Trade Log'}
            subtitle={'Your performance over the last few days.'}
            cardContent={<TradeLogTable log={tradeLog} />}
          />
        </div>
        <div className={""}>
          <BaseCard
            title={'Transaction Activity'}
            subtitle={'Your most recent account transactions.'}
            cardContent={<AccountTransactionsTable transactions={accountTransactions} />}
          />
        </div>
      </div>
    </div>
  );
}