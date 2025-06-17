"use client";

import React, { useEffect, useState } from "react";
import { Icons } from "@/lib/enums";
import { BaseCard } from "@/components/Card/BaseCard";
import DashboardContent from "@/components/Card/Content/DashboardContent";
import {
  getActivePortfolioNumber,
  logErrors,
} from "@/lib/functions/util-functions";
import AccountsTable from "@/components/Table/Account/AccountsTable";
import TradeLogTable from "@/components/Table/Trade/TradeLogTable";
import PortfolioGrowthChart from "@/components/Chart/Account/PortfolioGrowthChart";
import {
  usePortfolioQuery,
  usePortfolioRecordQuery,
  useRecentTransactionsQuery,
  useTradeLogQuery,
  useUserQuery,
} from "@/lib/hooks/query/queries";
import Error from "@/app/error";
import LoadingPage from "@/app/loading";
import TransactionsTable from "@/components/Table/Transaction/TransactionsTable";
import { PageInfoProvider } from "@/lib/context/PageInfoProvider";
import ReusableSelect from "@/components/Input/ReusableSelect";
import { usePortfolioStore } from "@/lib/store/portfolioStore";
import { resolveIcon } from "@/lib/functions/util-component-functions";
import { redirect } from "next/navigation";

/**
 * The page that shows an overview of a user's portfolio
 *
 * @author Stephen Prizio
 * @version 0.2.2
 */
export default function DashboardPage() {
  const {
    data: user,
    isError: isUserError,
    error: userError,
    isLoading: isUserLoading,
    isSuccess: isUserSuccess,
  } = useUserQuery();

  const { setSelectedPortfolioId } = usePortfolioStore();
  const [activePortfolio, setActivePortfolio] = useState(-1);

  useEffect(() => {
    const accPId = getActivePortfolioNumber(user) ?? -1;
    if (isUserSuccess && accPId !== -1) {
      setActivePortfolio(accPId);
      setSelectedPortfolioId(accPId);
    } else if (isUserError) {
      redirect("/portfolios");
    }
  }, [user, setSelectedPortfolioId, isUserSuccess, isUserError]);

  const {
    data: portfolio,
    isError: isPortfolioError,
    error: portfolioError,
    isLoading: isPortfolioLoading,
  } = usePortfolioQuery(activePortfolio);
  const {
    data: portfolioRecord,
    isError: isPortfolioRecordError,
    error: portfolioRecordError,
    isLoading: isPortfolioRecordLoading,
  } = usePortfolioRecordQuery(activePortfolio);
  const {
    data: recentTransactions,
    isError: isRecentTransactionsError,
    error: recentTransactionsError,
    isLoading: isRecentTransactionsLoading,
  } = useRecentTransactionsQuery();
  const {
    data: tradeLog,
    isError: isTradeLogError,
    error: tradeLogError,
    isLoading: isTradeLogLoading,
  } = useTradeLogQuery();

  const isError =
    isUserError ||
    isPortfolioError ||
    isPortfolioRecordError ||
    isRecentTransactionsError ||
    isTradeLogError;
  const isLoading =
    isUserLoading ||
    isPortfolioLoading ||
    isPortfolioRecordLoading ||
    isRecentTransactionsLoading ||
    isTradeLogLoading;

  //  RENDER

  if (isLoading || activePortfolio === null) {
    return <LoadingPage />;
  }

  if (isError) {
    logErrors(
      userError,
      portfolioError,
      portfolioRecordError,
      recentTransactionsError,
      tradeLogError,
    );
    return <Error />;
  }

  const pageInfo = {
    title: "Portfolio Dashboard",
    subtitle: `An overview of portfolio ${portfolio?.name ?? ""}`,
    iconCode: Icons.LayoutDashboard,
    breadcrumbs: [{ label: "Dashboard", href: "/dashboard", active: true }],
  };

  return (
    <PageInfoProvider value={pageInfo}>
      <div>
        <div className={"grid grid-cols-1 gap-8 w-full"}>
          <div className={"flex flex-row items-end justify-end"}>
            {activePortfolio !== -1 && (
              <ReusableSelect
                title={"Portfolio"}
                initialValue={activePortfolio?.toString()}
                options={
                  user?.portfolios?.map((p) => {
                    return { label: p.name, value: p.portfolioNumber };
                  }) ?? []
                }
                handler={(val: string) => {
                  setSelectedPortfolioId(parseInt(val));
                }}
              />
            )}
          </div>
          <div
            className={"grid grid-cols-1 md:grid-cols-2 xl:grid-cols-4 gap-8"}
          >
            <div className={""}>
              <BaseCard
                title={"Net Worth"}
                cardContent={
                  <DashboardContent
                    prefix={"$ "}
                    value={portfolioRecord?.netWorth ?? 0}
                    delta={portfolioRecord?.statistics?.differenceNetWorth ?? 0}
                    deltaPercentage={
                      portfolioRecord?.statistics?.deltaNetWorth ?? 0
                    }
                  />
                }
                icon={resolveIcon(Icons.ChartDoughnutFilled, "", 30)}
              />
            </div>
            <div className={""}>
              <BaseCard
                title={"Trades"}
                cardContent={
                  <DashboardContent
                    value={portfolioRecord?.trades ?? 0}
                    delta={portfolioRecord?.statistics?.differenceTrades ?? 0}
                    deltaPercentage={
                      portfolioRecord?.statistics?.deltaTrades ?? 0
                    }
                  />
                }
                icon={resolveIcon(Icons.ReplaceFilled, "", 30)}
              />
            </div>
            <div>
              <BaseCard
                title={"Deposits"}
                cardContent={
                  <DashboardContent
                    value={portfolioRecord?.deposits ?? 0}
                    delta={portfolioRecord?.statistics?.differenceDeposits ?? 0}
                    deltaPercentage={
                      portfolioRecord?.statistics?.deltaDeposits ?? 0
                    }
                  />
                }
                icon={resolveIcon(Icons.ArrowBarDown, "", 30)}
              />
            </div>
            <div>
              <BaseCard
                title={"Withdrawals"}
                cardContent={
                  <DashboardContent
                    value={portfolioRecord?.withdrawals ?? 0}
                    delta={
                      portfolioRecord?.statistics?.differenceWithdrawals ?? 0
                    }
                    deltaPercentage={
                      portfolioRecord?.statistics?.deltaWithdrawals ?? 0
                    }
                  />
                }
                icon={resolveIcon(Icons.ArrowBarUp, "", 30)}
              />
            </div>
          </div>
          <div className={"grid grid-cols-1 xl:grid-cols-3 gap-8"}>
            <div className={"col-span-1 xl:col-span-2"}>
              <BaseCard
                title={"Portfolio Growth"}
                subtitle={
                  "A look back at your portfolio's performance over the last 6 months."
                }
                cardContent={
                  portfolioRecord?.equity?.length ? (
                    <PortfolioGrowthChart
                      key={portfolioRecord.equity.length}
                      data={portfolioRecord.equity}
                    />
                  ) : null
                }
                emptyText={
                  "You haven't taken any trades or made any deposits yet. Once you do, this chart will update."
                }
              />
            </div>
            <div className={""}>
              <BaseCard
                title={"Accounts"}
                subtitle={"Only active accounts will be shown."}
                cardContent={
                  portfolio?.accounts?.filter((acc) => acc.active).length ? (
                    <AccountsTable
                      accounts={portfolio.accounts.filter((acc) => acc.active)}
                      showAllLink={true}
                    />
                  ) : null
                }
                emptyText={
                  "This portfolio doesn't currently have any trading accounts."
                }
              />
            </div>
          </div>
          <div className={"grid grid-cols-1 xl:grid-cols-3 gap-8"}>
            <div className={"col-span-1 xl:col-span-2"}>
              <BaseCard
                title={"Trade Log"}
                subtitle={"Your performance over the last few days."}
                cardContent={
                  tradeLog?.entries?.length ? (
                    <TradeLogTable log={tradeLog} showTotals={true} />
                  ) : null
                }
                emptyText={
                  "You haven't taken any trades. Once you do, your activity and statuses will be updated here."
                }
              />
            </div>
            <div className={""}>
              <BaseCard
                title={"Transaction Activity"}
                subtitle={"Your most recent account transactions."}
                cardContent={
                  recentTransactions?.length ? (
                    <TransactionsTable
                      transactions={recentTransactions ?? []}
                      showBottomLink={true}
                    />
                  ) : null
                }
                emptyText={
                  "You haven't made any transactions yet. Once you do, this table will update."
                }
              />
            </div>
          </div>
        </div>
      </div>
    </PageInfoProvider>
  );
}
