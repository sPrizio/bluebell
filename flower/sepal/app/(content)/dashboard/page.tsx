'use client'

import React from "react";
import {Icons} from "@/lib/enums";
import {BaseCard} from "@/components/Card/BaseCard";
import DashboardContent from "@/components/Card/Content/DashboardContent";
import {logErrors, resolveIcon} from "@/lib/functions/util-functions";
import AccountsTable from "@/components/Table/Account/AccountsTable";
import TradeLogTable from "@/components/Table/Trade/TradeLogTable";
import AccountTransactionsTable from "@/components/Table/Account/AccountTransactionsTable";
import PortfolioGrowthChart from "@/components/Chart/Account/PortfolioGrowthChart";
import {usePortfolioQuery, useRecentTransactionsQuery, useTradeLogQuery, useUserQuery} from "@/lib/hooks/queries";
import Error from "@/app/error";
import LoadingPage from "@/app/loading";
import {User} from "@/types/apiTypes";

/**
 * The page that shows an overview of a user's portfolio
 *
 * @author Stephen Prizio
 * @version 0.2.0
 */
export default function DashboardPage() {

  const {data: user, isError: isUserError, error: userError, isLoading: isUserLoading} = useUserQuery();

  const activePortfolioUid : string  = getActivePortfolioUid(user) ?? ''
  const {
    data: portfolio,
    isError: isPortfolioError,
    error: portfolioError,
    isLoading: isPortfolioLoading
  } = usePortfolioQuery(activePortfolioUid)
  const {
    data: recentTransactions,
    isError: isRecentTransactionsError,
    error: recentTransactionsError,
    isLoading: isRecentTransactionsLoading
  } = useRecentTransactionsQuery()
  const {
    data: tradeLog,
    isError: isTradeLogError,
    error: tradeLogError,
    isLoading: isTradeLogLoading
  } = useTradeLogQuery()

  const isError = isUserError || isPortfolioError || isRecentTransactionsError || isTradeLogError
  const isLoading = isUserLoading || isPortfolioLoading || isRecentTransactionsLoading || isTradeLogLoading


  //  GENERAL FUNCTIONS

  /**
   * Obtains the active portfolio uid, if it exists
   *
   * @param user user
   */
  function getActivePortfolioUid(user: User | null | undefined) : string | null {
    if (user?.portfolios ?? false) {
      const defPort = user?.portfolios?.filter(p => p.defaultPortfolio) ?? null
      if ((defPort?.length ?? 0) > 0) {
        return defPort?.[0].uid ?? null
      }
    }

    return null
  }


  //  RENDER

  if (isLoading) {
    return <LoadingPage/>
  }

  if (isError) {
    logErrors(userError, portfolioError, recentTransactionsError, tradeLogError)
    return <Error/>
  }

  return (
    <div>
      <div className={'grid grid-cols-1 gap-8 w-full'}>
        {/* TODO: BB-54 Implement Portfolio UI */}
        {/*<div className={"grid grid-cols-1 md:grid-cols-2 xl:grid-cols-4 gap-8"}>
          <div className={""}>
            <BaseCard
              title={'Net Worth'}
              cardContent={<DashboardContent prefix={'$ '} value={portfolio?.netWorth ?? 0}
                                             delta={portfolio?.deltaNetWorth ?? 0}/>}
              icon={resolveIcon(Icons.ChartDoughnut, '', 30)}
            />
          </div>
          <div className={""}>
            <BaseCard
              title={'Trades'}
              cardContent={<DashboardContent value={portfolio?.trades ?? 0}
                                             delta={portfolio?.deltaTrades ?? 0}/>}
              icon={resolveIcon(Icons.Replace, '', 30)}
            />
          </div>
          <div>
            <BaseCard
              title={'Deposits'}
              cardContent={<DashboardContent value={portfolio?.deposits ?? 0}
                                             delta={portfolio?.deltaDeposits ?? 0}/>}
              icon={resolveIcon(Icons.ArrowBarDown, '', 30)}
            />
          </div>
          <div>
            <BaseCard
              title={'Withdrawals'}
              cardContent={<DashboardContent value={portfolio?.withdrawals ?? 0}
                                             delta={portfolio?.deltaWithdrawals ?? 0}/>}
              icon={resolveIcon(Icons.ArrowBarUp, '', 30)}
            />
          </div>
        </div>
        <div className={"grid grid-cols-1 xl:grid-cols-3 gap-8"}>
          <div className={"col-span-1 xl:col-span-2"}>
            <BaseCard
              title={'Portfolio Growth'}
              subtitle={'A look back at your portfolio\'s performance over the last 6 months.'}
              cardContent={<PortfolioGrowthChart key={portfolio?.equity.length} isNew={portfolio?.isNew ?? false}
                                                 data={portfolio?.equity ?? []}/>}
            />
          </div>
          <div className={""}>
            <BaseCard
              title={'Accounts'}
              subtitle={'Only active accounts will be shown.'}
              cardContent={
                <AccountsTable
                  accounts={portfolio?.accounts ?? []}
                  showAllLink={true}
                />
              }
            />
          </div>
        </div>*/}
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
                  account={portfolio?.accounts?.[0] ?? null}
                  transactions={recentTransactions ?? []}
                />
              }
            />
          </div>
        </div>
      </div>
    </div>
  );
}