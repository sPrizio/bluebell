"use client";

import React from "react";
import { BaseCard } from "@/components/Card/BaseCard";
import { Button } from "@/components/ui/button";
import BaseModal from "@/components/Modal/BaseModal";
import AccountsTable from "@/components/Table/Account/AccountsTable";
import AccountForm from "@/components/Form/Account/AccountForm";
import { Account } from "@/types/apiTypes";
import LoadingPage from "@/app/loading";
import { logErrors } from "@/lib/functions/util-functions";
import ErrorPage from "@/app/error";
import { Icons, UserPrivilege } from "@/lib/enums";
import { PageInfoProvider } from "@/lib/context/PageInfoProvider";
import ReusableSelect from "@/components/Input/ReusableSelect";
import { usePortfolioStore } from "@/lib/store/portfolioStore";
import { useActivePortfolio } from "@/lib/hooks/api/useActivePortoflio";
import { useUserQuery } from "@/lib/hooks/query/queries";
import { redirect } from "next/navigation";
import { resolveIcon } from "@/lib/functions/util-component-functions";
import { useSessionContext } from "@/lib/context/SessionContext";
import { CONTROL_GAP, PAGE_GAP } from "@/lib/constants";

/**
 * The page that shows all of a user's accounts
 *
 * @author Stephen Prizio
 * @version 1.0.0
 */
export default function AccountsPage() {
  const session = useSessionContext();
  const { data: user } = useUserQuery(session?.username ?? "");
  const { isLoading, isError, error, activePortfolio, hasMismatch } =
    useActivePortfolio();
  const { selectedPortfolioId, setSelectedPortfolioId } = usePortfolioStore();

  if (isLoading) {
    return <LoadingPage />;
  }

  if (isError || (!isError && !activePortfolio)) {
    redirect("/portfolios");
  }

  if (hasMismatch || isError) {
    logErrors("User and portfolio mismatch!", error);
    return <ErrorPage />;
  }

  const pageInfo = {
    title: "Accounts",
    subtitle: "A list of all of your trading accounts.",
    iconCode: Icons.PieChart,
    privilege: UserPrivilege.TRADER,
    breadcrumbs: [
      { label: "Dashboard", href: "/dashboard", active: false },
      {
        label: `${activePortfolio?.name ?? ""} Accounts`,
        href: "/accounts",
        active: true,
      },
    ],
  };

  const activeAccounts =
    activePortfolio?.accounts?.filter((acc: Account) => acc.active) ?? [];
  const inactiveAccounts =
    activePortfolio?.accounts?.filter((acc: Account) => !acc.active) ?? [];

  //  RENDER

  if (selectedPortfolioId === null) {
    setSelectedPortfolioId(activePortfolio?.portfolioNumber ?? -1);
  }

  let inactiveData = null;
  let activeData = (
    <div className={"text-center"}>
      No active accounts for this portfolio, consider adding one!
    </div>
  );

  if ((activeAccounts?.length ?? 0) > 0) {
    activeData = (
      <div className={""}>
        <BaseCard
          title={"Active Accounts"}
          subtitle={"All actively traded accounts."}
          cardContent={
            <AccountsTable
              accounts={activeAccounts}
              allowAccountSelection={true}
              showCompactTable={false}
            />
          }
        />
      </div>
    );
  }

  if ((inactiveAccounts?.length ?? 0) > 0) {
    inactiveData = (
      <div className={""}>
        <BaseCard
          title={"Inactive Accounts"}
          subtitle={
            "Inactive or disabled accounts are ones that are not currently being traded."
          }
          cardContent={
            <AccountsTable
              accounts={inactiveAccounts}
              allowAccountSelection={true}
              showCompactTable={false}
            />
          }
        />
      </div>
    );
  }

  return (
    <PageInfoProvider value={pageInfo}>
      <div className={`grid grid-cols-1 ${PAGE_GAP} w-full`}>
        <div className={`flex w-full items-end justify-end ${CONTROL_GAP}`}>
          <div className={""}>
            <ReusableSelect
              title={"Portfolio"}
              initialValue={selectedPortfolioId?.toString()}
              options={
                user?.portfolios?.map((p) => {
                  return { label: p.name, value: p.portfolioNumber };
                }) ?? []
              }
              handler={(val: string) => {
                setSelectedPortfolioId(parseInt(val));
              }}
            />
          </div>
          <div className={""}>
            <BaseModal
              title={"Add a new Trading Account"}
              description={
                "Adding a new Account will include it as part of your portfolio. If you do not wish to track your Account in your portfolio, mark it as inactive. These settings can be changed at anytime from the Account page."
              }
              trigger={
                <Button className="w-full text-white">
                  {resolveIcon(Icons.CirclePlus)}
                  &nbsp;New Account
                </Button>
              }
              content={
                <AccountForm
                  portfolioNumber={selectedPortfolioId ?? -1}
                  mode={"create"}
                />
              }
            />
          </div>
        </div>

        <div className={"flex items-center text-sm justify-end w-full"}>
          <span className={"inline-block"}>
            {resolveIcon(Icons.Flag3Filled, "text-primary")}
          </span>
          &nbsp;&nbsp;indicates default account.
        </div>

        {activeData}
        {inactiveData}
      </div>
    </PageInfoProvider>
  );
}
