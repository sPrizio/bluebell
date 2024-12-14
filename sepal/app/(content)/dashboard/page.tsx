'use client'

import React, {useEffect, useState} from "react";
import {Icons} from "@/lib/enums";
import {useSepalPageInfoContext} from "@/lib/context/SepalContext";
import {BaseCard} from "@/components/Card/BaseCard";
import DashboardContent from "@/components/Card/Content/DashboardContent";
import {resolveIcon} from "@/lib/functions/util-functions";
import AccountsTable from "@/components/Table/Account/AccountsTable";
import TradeLogTable from "@/components/Table/Trade/TradeLogTable";
import AccountTransactionsTable from "@/components/Table/Account/AccountTransactionsTable";
import PortfolioGrowthChart from "@/components/Chart/Account/PortfolioGrowthChart";
import {getPortfolio} from "@/lib/functions/portfolio-functions";
import {Loader2} from "lucide-react";
import {getRecentTransactions} from "@/lib/functions/account-functions";
import {getTradeLog} from "@/lib/functions/trade-functions";
import moment from "moment/moment";
import {DateTime} from "@/lib/constants";

/**
 * The page that shows an overview of a user's portfolio
 *
 * @author Stephen Prizio
 * @version 0.0.2
 */
export default function DashboardPage() {

  const {
    user,
    pageTitle,
    pageSubtitle,
    pageIconCode,
    breadcrumbs,
    setUser,
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

    getAccPortfolioInfo()
    getAccRecentTransactions()
    getAccTradeLog()
  }, [])

  const [isLoading, setIsLoading] = useState(false)
  const [portfolioInfo, setPortfolioInfo] = useState<Portfolio | null>()
  const [recentTransactions, setRecentTransactions] = useState<Array<Transaction> | null>()
  const [tradeLog, setTradeLog] = useState<TradeLog | null>()


  //  GENERAL FUNCTIONS

  /**
   * Fetches the portfolio information
   */
  async function getAccPortfolioInfo() {

    setIsLoading(true)

    const data = await getPortfolio();
    setPortfolioInfo(data)

    setIsLoading(false)
  }

  /**
   * Fetches the user's recent transactions
   */
  async function getAccRecentTransactions() {

    setIsLoading(true)

    const data = await getRecentTransactions();
    setRecentTransactions(data)

    setIsLoading(false)
  }

  /**
   * Fetches the trade log
   */
  async function getAccTradeLog() {

    setIsLoading(true)

    const data = await getTradeLog(moment().subtract(5, 'days').format(DateTime.ISODateFormat), moment().format(DateTime.ISODateFormat), 'DAILY', 6);
    setTradeLog(data)

    setIsLoading(false)
  }


  //  RENDER

  return (
    <div>
      {
        isLoading ?
          <div className={'h-[72vh] flex items-center justify-center'}>
            <div className={'grid grid-cols-1 justify-items-center gap-8'}>
              <div>
                <Loader2 className="animate-spin text-primary" size={50}/>
              </div>
              <div className={'text-lg'}>Loading Dashboard</div>
            </div>
          </div> :
          <div className={'grid grid-cols-1 gap-8 w-full'}>
            <div className={"grid grid-cols-1 md:grid-cols-2 xl:grid-cols-4 gap-8"}>
              <div className={""}>
                <BaseCard
                  title={'Net Worth'}
                  cardContent={<DashboardContent prefix={'$'} value={portfolioInfo?.netWorth ?? 0}
                                                 delta={portfolioInfo?.deltaNetWorth ?? 0}/>}
                  icon={resolveIcon(Icons.ChartDoughnut, '', 30)}
                />
              </div>
              <div className={""}>
                <BaseCard
                  title={'Trades'}
                  cardContent={<DashboardContent value={portfolioInfo?.trades ?? 0}
                                                 delta={portfolioInfo?.deltaTrades ?? 0}/>}
                  icon={resolveIcon(Icons.Replace, '', 30)}
                />
              </div>
              <div>
                <BaseCard
                  title={'Deposits'}
                  cardContent={<DashboardContent value={portfolioInfo?.deposits ?? 0}
                                                 delta={portfolioInfo?.deltaDeposits ?? 0}/>}
                  icon={resolveIcon(Icons.ArrowBarDown, '', 30)}
                />
              </div>
              <div>
                <BaseCard
                  title={'Withdrawals'}
                  cardContent={<DashboardContent value={portfolioInfo?.withdrawals ?? 0}
                                                 delta={portfolioInfo?.deltaWithdrawals ?? 0}/>}
                  icon={resolveIcon(Icons.ArrowBarUp, '', 30)}
                />
              </div>
            </div>
            <div className={"grid grid-cols-1 xl:grid-cols-3 gap-8"}>
              <div className={"col-span-1 xl:col-span-2"}>
                <BaseCard
                  title={'Portfolio Growth'}
                  subtitle={'A look back at your portfolio\'s performance over the last 6 months.'}
                  cardContent={<PortfolioGrowthChart key={portfolioInfo?.equity.length} isNew={portfolioInfo?.isNew ?? false} data={portfolioInfo?.equity ?? []}/>}
                />
              </div>
              <div className={""}>
                <BaseCard
                  title={'Accounts'}
                  subtitle={'Only active accounts will be shown.'}
                  cardContent={
                    <AccountsTable
                      accounts={user?.accounts ?? []}
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
                  cardContent={<TradeLogTable log={tradeLog} showTotals={true}/>}
                />
              </div>
              <div className={""}>
                <BaseCard
                  title={'Transaction Activity'}
                  subtitle={'Your most recent account transactions.'}
                  cardContent={
                    <AccountTransactionsTable
                      account={user?.accounts?.[0] ?? null}
                      transactions={recentTransactions ?? []}
                    />
                  }
                />
              </div>
            </div>
          </div>
      }
    </div>
  );
}