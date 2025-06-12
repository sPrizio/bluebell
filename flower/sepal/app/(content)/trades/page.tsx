"use client";

import { useRouter, useSearchParams } from "next/navigation";
import React, { useEffect, useState } from "react";
import { logErrors, selectNewAccount } from "@/lib/functions/util-functions";
import { Icons } from "@/lib/enums";
import { BaseCard } from "@/components/Card/BaseCard";
import { useActiveAccount } from "@/lib/hooks/api/useActiveAccount";
import { PageInfoProvider } from "@/lib/context/PageInfoProvider";
import ReusableSelect from "@/components/Input/ReusableSelect";
import {
  resolveIcon,
  validatePageQueryFlow,
} from "@/lib/functions/util-component-functions";
import TradeFilterDrawer from "@/components/Drawer/TradeFilterDrawer";
import { UserTradeControlSelection } from "@/types/uiTypes";
import moment from "moment";
import { Button } from "@/components/ui/button";
import LoadingPage from "@/app/loading";
import TradeTable from "@/components/Table/Trade/TradeTable";
import { DateTime } from "@/lib/constants";
import { useTradedSymbolsQuery } from "@/lib/hooks/query/queries";
import Error from "@/app/error";
import BaseModal from "@/components/Modal/BaseModal";
import TradeForm from "@/components/Form/Trade/TradeForm";

/**
 * Renders the Trade history page
 *
 * @author Stephen Prizio
 * @version 0.2.4
 */
export default function TradesPage() {
  const searchParams = useSearchParams();
  const router = useRouter();
  const {
    isLoading,
    isError,
    error,
    activePortfolio,
    activeAccount,
    hasMismatch,
  } = useActiveAccount();

  const {
    data: tradedSymbols,
    isError: isTradedSymbolsError,
    error: tradedSymbolsError,
    isLoading: isTradedSymbolsLoading,
  } = useTradedSymbolsQuery(activeAccount?.accountNumber ?? -1);

  validatePageQueryFlow(
    isLoading,
    isError,
    activePortfolio,
    hasMismatch,
    error,
  );

  const [pageSize, setPageSize] = useState(15);
  const [hasSubmitted, setHasSubmitted] = useState(false);
  const [userSelection, setUserSelection] = useState<UserTradeControlSelection>(
    {
      start: activeAccount?.accountOpenTime
        ? moment(activeAccount?.accountOpenTime ?? "").toDate()
        : moment().toDate(),
      end: moment().toDate(),
      sort: "desc",
      type: "ALL",
      symbol: "ALL",
    },
  );
  const [submittedFilters, setSubmittedFilters] = useState(userSelection);

  useEffect(() => {
    if (activeAccount?.accountOpenTime) {
      const newStart = moment(activeAccount.accountOpenTime).toDate();
      setUserSelection((prev) => ({
        ...prev,
        start: newStart,
      }));
      setSubmittedFilters((prev) => ({
        ...prev,
        start: newStart,
      }));
    }
  }, [activeAccount]);

  const accNumber = activeAccount?.accountNumber ?? -1;
  const pageInfo = {
    title: "Trades",
    subtitle: `A look at the trades for ${activeAccount?.name ?? ""}`,
    iconCode: Icons.ReplaceFilled,
    breadcrumbs: [
      { label: "Dashboard", href: "/dashboard", active: false },
      { label: "Accounts", href: "/accounts", active: false },
      {
        label: `${activeAccount?.name ?? ""}`,
        href: "/accounts/" + accNumber,
        active: false,
      },
      { label: "Trades", href: "/trades?account=default", active: true },
    ],
  };

  //  GENERAL FUNCTIONS

  /**
   * Formats the given date into a nicer string
   *
   * @param date date to format
   */
  function formatDate(date: Date): string {
    return moment(date).format(DateTime.ISOLongMonthDayYearFormat);
  }

  //  RENDER

  if (isTradedSymbolsError) {
    logErrors(tradedSymbolsError);
    return <Error />;
  }

  if (!activeAccount || isTradedSymbolsLoading) {
    return <LoadingPage />;
  }

  return (
    <PageInfoProvider value={pageInfo}>
      <div className={""}>
        {
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
                <TradeFilterDrawer
                  userSelection={userSelection}
                  onChange={setUserSelection}
                  onSubmit={() => {
                    setSubmittedFilters(userSelection);
                    setHasSubmitted(true);
                  }}
                  onCancel={() => {
                    setUserSelection(submittedFilters);
                    setSubmittedFilters(userSelection);
                    setHasSubmitted(false);
                  }}
                  symbols={tradedSymbols}
                />
              </div>
              <div>
                <BaseModal
                  title={"Add a new Trade"}
                  description={
                    "Add a new trade to this trading account. Additions will update the account's balance and related information."
                  }
                  trigger={
                    <Button className={"w-full text-white"}>
                      {resolveIcon(Icons.CirclePlus)}&nbsp;Add Trade
                    </Button>
                  }
                  content={
                    <TradeForm
                      account={activeAccount}
                      trade={undefined}
                      mode={"create"}
                    />
                  }
                />
              </div>
            </div>
            <div>
              <BaseCard
                loading={isLoading}
                title={"Trades"}
                subtitle={
                  formatDate(submittedFilters.start) +
                  " - " +
                  formatDate(submittedFilters.end)
                }
                cardContent={
                  <TradeTable
                    account={activeAccount}
                    filters={submittedFilters}
                    initialPageSize={pageSize}
                  />
                }
                headerControls={[
                  <Button
                    key={0}
                    className=""
                    variant={"outline"}
                    onClick={() => setPageSize(100000)}
                  >
                    View All Trades
                  </Button>,
                ]}
              />
            </div>
          </div>
        }
      </div>
    </PageInfoProvider>
  );
}
