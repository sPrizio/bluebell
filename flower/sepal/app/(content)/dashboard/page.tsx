'use client'

import React, {useState} from "react";
import {Icons} from "@/lib/enums";
import {BaseCard} from "@/components/Card/BaseCard";
import DashboardContent from "@/components/Card/Content/DashboardContent";
import {logErrors, resolveIcon} from "@/lib/functions/util-functions";
import AccountsTable from "@/components/Table/Account/AccountsTable";
import TradeLogTable from "@/components/Table/Trade/TradeLogTable";
import PortfolioGrowthChart from "@/components/Chart/Account/PortfolioGrowthChart";
import {
  usePortfolioQuery,
  usePortfolioRecordQuery,
  useRecentTransactionsQuery,
  useTradeLogQuery,
  useUserQuery
} from "@/lib/hooks/queries";
import Error from "@/app/error";
import LoadingPage from "@/app/loading";
import {User} from "@/types/apiTypes";
import TransactionsTable from "@/components/Table/Transaction/TransactionsTable";
import PageHeaderSection from "@/components/Section/PageHeaderSection";
import {PageInfoProvider} from "@/lib/context/PageInfoProvider";
import ReusableSelect from "@/components/Input/ReusableSelect";

/**
 * The page that shows an overview of a user's portfolio
 *
 * @author Stephen Prizio
 * @version 0.2.0
 */
export default function DashboardPage() {

  const {data: user, isError: isUserError, error: userError, isLoading: isUserLoading} = useUserQuery();
  const activePortfolio = getActivePortfolioNumber(user) ?? -1

  const {
    data: portfolio,
    isError: isPortfolioError,
    error: portfolioError,
    isLoading: isPortfolioLoading
  } = usePortfolioQuery(activePortfolio)
  const {
    data: portfolioRecord,
    isError: isPortfolioRecordError,
    error: portfolioRecordError,
    isLoading: isPortfolioRecordLoading
  } = usePortfolioRecordQuery(activePortfolio)
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

  const isError = isUserError || isPortfolioError || isPortfolioRecordError || isRecentTransactionsError || isTradeLogError
  const isLoading = isUserLoading || isPortfolioLoading || isPortfolioRecordLoading || isRecentTransactionsLoading || isTradeLogLoading


  //  GENERAL FUNCTIONS

  /**
   * Obtains the active portfolio uid, if it exists
   *
   * @param user user
   */
  function getActivePortfolioNumber(user: User | null | undefined) : number | null {
    if (user?.portfolios ?? false) {
      const defPort = user?.portfolios?.filter(p => p.defaultPortfolio) ?? null
      if ((defPort?.length ?? 0) > 0) {
        return defPort?.[0].portfolioNumber ?? null
      }
    }

    return null
  }


  //  RENDER

  const [selectedPortfolio, setSelectedPortfolio] = useState(activePortfolio)
  if (isLoading) {
    return <LoadingPage/>
  }

  if (isError) {
    logErrors(userError, portfolioError, portfolioRecordError, recentTransactionsError, tradeLogError)
    return <Error/>
  }

  const pageInfo = {
    title: "Portfolio Dashboard",
    subtitle: `An overview of portfolio ${portfolio?.name ?? ''}`,
    iconCode: Icons.Dashboard,
    breadcrumbs: [
      {label: 'Dashboard', href: '/dashboard', active: true},
    ]
  }

  return (
    <PageInfoProvider value={pageInfo}>
      <PageHeaderSection
        title={pageInfo.title}
        subtitle={pageInfo.subtitle}
        iconCode={pageInfo.iconCode}
        breadcrumbs={pageInfo.breadcrumbs}
      />
      <div>
        <div className={'grid grid-cols-1 gap-8 w-full'}>
          <div className={'flex flex-row items-end justify-end'}>
            <ReusableSelect
              title={'Portfolio'}
              initialValue={selectedPortfolio.toString()}
              options={user?.portfolios?.map(p => {
                return {label: p.name, value: p.portfolioNumber}
              }) ?? []}
              handler={(val : string) => setSelectedPortfolio(parseInt(val))}
            />
          </div>
          {/* TODO: BB-54 Implement Portfolio UI */}
          <div className={"grid grid-cols-1 md:grid-cols-2 xl:grid-cols-4 gap-8"}>
            <div className={""}>
              <BaseCard
                title={'Net Worth'}
                cardContent={<DashboardContent prefix={'$ '} value={portfolioRecord?.netWorth ?? 0}
                                               delta={portfolioRecord?.statistics?.deltaNetWorth ?? 0}/>}
                icon={resolveIcon(Icons.ChartDoughnut, '', 30)}
              />
            </div>
            <div className={""}>
              <BaseCard
                title={'Trades'}
                cardContent={<DashboardContent value={portfolioRecord?.trades ?? 0}
                                               delta={portfolioRecord?.statistics?.deltaTrades ?? 0}/>}
                icon={resolveIcon(Icons.Replace, '', 30)}
              />
            </div>
            <div>
              <BaseCard
                title={'Deposits'}
                cardContent={<DashboardContent value={portfolioRecord?.deposits ?? 0}
                                               delta={portfolioRecord?.statistics?.deltaDeposits ?? 0}/>}
                icon={resolveIcon(Icons.ArrowBarDown, '', 30)}
              />
            </div>
            <div>
              <BaseCard
                title={'Withdrawals'}
                cardContent={<DashboardContent value={portfolioRecord?.withdrawals ?? 0}
                                               delta={portfolioRecord?.statistics?.deltaWithdrawals ?? 0}/>}
                icon={resolveIcon(Icons.ArrowBarUp, '', 30)}
              />
            </div>
          </div>
          <div className={"grid grid-cols-1 xl:grid-cols-3 gap-8"}>
            <div className={"col-span-1 xl:col-span-2"}>
              <BaseCard
                title={'Portfolio Growth'}
                subtitle={'A look back at your portfolio\'s performance over the last 6 months.'}
                cardContent={<PortfolioGrowthChart key={portfolioRecord?.equity.length} isNew={portfolioRecord?.newPortfolio ?? false}
                                                   data={portfolioRecord?.equity ?? []}/>}
              />
            </div>
            <div className={""}>
              <BaseCard
                title={'Accounts'}
                subtitle={'Only active accounts will be shown.'}
                cardContent={
                  <AccountsTable
                    accounts={portfolio?.accounts.filter(acc => acc.active) ?? []}
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
                  <TransactionsTable transactions={recentTransactions ?? []} showBottomLink={true} />
                }
              />
            </div>
          </div>
        </div>
      </div>
    </PageInfoProvider>
  );
}