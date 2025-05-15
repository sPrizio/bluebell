"use client";

import { redirect, useRouter, useSearchParams } from "next/navigation";
import React, { useState } from "react";
import { AggregateInterval, Icons } from "@/lib/enums";
import { logErrors, selectNewAccount } from "@/lib/functions/util-functions";
import { Loader2 } from "lucide-react";
import TradeRecordCard from "@/components/Card/Trade/TradeRecordCard";
import { UserTradeRecordControlSelection } from "@/types/uiTypes";
import moment from "moment";
import { PageInfoProvider } from "@/lib/context/PageInfoProvider";
import LoadingPage from "@/app/loading";
import Error from "@/app/error";
import { useActiveAccount } from "@/lib/hooks/api/useActiveAccount";
import { useTradeData } from "@/lib/hooks/api/useTradeRecordsData";
import PerformanceDrawer from "@/components/Drawer/PerformanceDrawer";
import ReusableSelect from "@/components/Input/ReusableSelect";
import { DateTime } from "@/lib/constants";

/**
 * The page that shows an account's performance over time
 *
 * @author Stephen Prizio
 * @version 0.2.0
 */
export default function PerformancePage() {
  const searchParams = useSearchParams();
  const router = useRouter();

  const {
    isLoading: isActiveAccountLoading,
    isError: isActiveAccountError,
    error: activeAccountError,
    activeAccount,
    activePortfolio,
    hasMismatch,
  } = useActiveAccount();

  const accNumber = activeAccount?.accountNumber ?? -1;
  const [hasSubmitted, setHasSubmitted] = useState(false);
  const [userSelection, setUserSelection] =
    useState<UserTradeRecordControlSelection>({
      aggInterval: AggregateInterval.DAILY,
      month: moment().format(DateTime.ISOMonthFormat).toUpperCase(),
      year: moment().format(DateTime.ISOYearFormat),
    });

  const [submittedFilters, setSubmittedFilters] = useState(userSelection);
  const { tradeRecordControlsQuery, tradeRecordsQuery } = useTradeData(
    accNumber,
    submittedFilters,
    hasSubmitted,
  );

  const {
    data: tradeRecordControls,
    isLoading: isLoadingControls,
    isError: isErrorControls,
    error: errorControls,
  } = tradeRecordControlsQuery;

  const {
    data: tradeRecords,
    isLoading: isLoadingRecords,
    isError: isErrorRecords,
    error: errorRecords,
  } = tradeRecordsQuery;

  const isLoading =
    isActiveAccountLoading || isLoadingControls || isLoadingRecords;
  const isError = isActiveAccountError || isErrorControls || isErrorRecords;

  if (isLoading) {
    return <LoadingPage />;
  }

  if (isError || (!isError && !activePortfolio)) {
    redirect("/portfolios");
  }

  if ((activePortfolio?.accounts?.length ?? 0) === 0) {
    redirect("/accounts");
  }

  if (hasMismatch || isError) {
    logErrors(
      "User and portfolio mismatch!",
      hasMismatch,
      activeAccountError,
      errorControls,
      errorRecords,
    );
    return <Error />;
  }

  const pageInfo = {
    title: "Performance",
    subtitle: `A look at trading account ${activeAccount?.name ?? ""}'s performance over time`,
    iconCode: Icons.Performance,
    breadcrumbs: [
      { label: "Dashboard", href: "/dashboard", active: false },
      { label: "Accounts", href: "/accounts", active: false },
      {
        label: `${activeAccount?.name ?? ""}`,
        href: "/accounts/" + accNumber,
        active: false,
      },
      {
        label: "Performance",
        href: "/performance?account=default",
        active: true,
      },
    ],
  };

  //  RENDER

  return (
    <PageInfoProvider value={pageInfo}>
      <div className={""}>
        {isLoading ? (
          <div className={"h-[72vh] flex items-center justify-center"}>
            <div className={"grid grid-cols-1 justify-items-center gap-8"}>
              <div>
                <Loader2 className="animate-spin text-primary" size={50} />
              </div>
              <div className={"text-lg"}>Loading Performance</div>
            </div>
          </div>
        ) : (
          <>
            <div className={"grid grid-cols-1 gap-8"}>
              <div className={"flex items-end justify-end gap-4"}>
                <div>
                  <ReusableSelect
                    title={"Account"}
                    initialValue={accNumber.toString()}
                    options={
                      activePortfolio?.accounts
                        ?.filter((acc) => acc.active)
                        ?.map((a) => {
                          return {
                            label: a.name,
                            value: a.accountNumber.toString(),
                          };
                        }) ?? []
                    }
                    handler={(val: string) => {
                      selectNewAccount(router, searchParams, parseInt(val));
                    }}
                  />
                </div>
                <div>
                  <PerformanceDrawer
                    userSelection={userSelection}
                    onChange={setUserSelection}
                    onSubmit={() => {
                      setSubmittedFilters(userSelection);
                      setHasSubmitted(true);
                    }}
                    onCancel={() => {
                      setUserSelection(submittedFilters);
                      setHasSubmitted(false);
                    }}
                    tradeRecordControls={tradeRecordControls}
                  />
                </div>
              </div>
            </div>
            <div className={"grid grid-cols-1 gap-8 mt-8"}>
              {(tradeRecords?.tradeRecords?.length ?? 0) === 0 && (
                <div className="text-center text-slate-500">
                  No recent trading activity.
                </div>
              )}
              {(tradeRecords?.tradeRecords?.length ?? 0) > 0 &&
                tradeRecords?.tradeRecords?.map((item, idx) => {
                  return (
                    <TradeRecordCard
                      key={item.uid + "tr" + (idx + 1)}
                      tradeRecord={item}
                      aggInterval={userSelection.aggInterval.code}
                    />
                  );
                })}
            </div>
          </>
        )}
      </div>
    </PageInfoProvider>
  );
}
