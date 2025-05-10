"use client";

import { useRouter, useSearchParams } from "next/navigation";
import React from "react";
import { selectNewAccount } from "@/lib/functions/util-functions";
import { Icons } from "@/lib/enums";
import TradeTable from "@/components/Table/Trade/TradeTable";
import { BaseCard } from "@/components/Card/BaseCard";
import { useActiveAccount } from "@/lib/hooks/api/useActiveAccount";
import { PageInfoProvider } from "@/lib/context/PageInfoProvider";
import ReusableSelect from "@/components/Input/ReusableSelect";
import { validatePageQueryFlow } from "@/lib/functions/util-component-functions";

/**
 * Renders the Trade history page
 *
 * @author Stephen Prizio
 * @version 0.2.0
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

  validatePageQueryFlow(
    isLoading,
    isError,
    activePortfolio,
    hasMismatch,
    error,
  );

  const accNumber = activeAccount?.accountNumber ?? -1;
  const pageInfo = {
    title: "Trades",
    subtitle: `A look at the trades for ${activeAccount?.name ?? ""}`,
    iconCode: Icons.Trades,
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

  //  RENDER

  return (
    <PageInfoProvider value={pageInfo}>
      <div className={""}>
        {
          <div className={"grid grid-cols-1 gap-8"}>
            <div className={"flex items-center justify-end gap-4"}>
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
            </div>
            <div>
              <BaseCard
                loading={isLoading}
                title={"Trades"}
                subtitle={"A view of each trade taken in this account."}
                cardContent={
                  <TradeTable account={activeAccount} initialPageSize={15} />
                }
              />
            </div>
          </div>
        }
      </div>
    </PageInfoProvider>
  );
}
