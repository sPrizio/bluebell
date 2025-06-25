"use client";

import React from "react";
import { Icons, UserPrivilege } from "@/lib/enums";
import { PageInfoProvider } from "@/lib/context/PageInfoProvider";
import { useAccountQuery } from "@/lib/hooks/query/queries";
import LoadingPage from "@/app/loading";
import { logErrors } from "@/lib/functions/util-functions";
import ErrorPage from "@/app/error";
import AccountDetailsCmp from "@/components/Account/AccountDetailsCmp";
import { useActivePortfolio } from "@/lib/hooks/api/useActivePortoflio";
import { useSessionContext } from "@/lib/context/SessionContext";

/**
 * The base layout for the Account detail page
 *
 * @param children Content
 * @param params Account ID
 * @author Stephen Prizio
 * @version 0.2.6
 */
export default function AccountDetailsLayout({
  children,
  params,
}: Readonly<{
  children: React.ReactNode;
  params: { id: string };
}>) {
  const session = useSessionContext();
  const {
    isLoading: isPortfolioLoading,
    isError: isPortfolioError,
    error: portfolioError,
    activePortfolio,
    hasMismatch: portfolioMisMatch,
  } = useActivePortfolio();
  const {
    data: account,
    isError: isAccountError,
    error: accountError,
    isLoading: isAccountLoading,
  } = useAccountQuery(params.id, session?.username ?? "");

  if (isPortfolioLoading || isAccountLoading) {
    return <LoadingPage />;
  }

  if (
    isPortfolioError ||
    portfolioError ||
    isAccountError ||
    portfolioMisMatch
  ) {
    logErrors(
      "User and portfolio mismatch!",
      portfolioError,
      portfolioMisMatch,
      accountError,
    );
    return <ErrorPage />;
  }

  //  GENERAL FUNCTIONS

  /**
   * Computes a dynamic Account description
   */
  function computeDescription() {
    let string = "";
    if (account?.accountType?.code?.length ?? -1 > 0) {
      string +=
        account?.accountType.label + " Account " + account?.accountNumber;
    }

    if (account?.broker?.code?.length ?? -1 > 0) {
      string += " with " + account?.broker.label;
    }

    return string;
  }

  const pageInfo = {
    title: "Account Overview",
    subtitle: computeDescription(),
    iconCode: Icons.Mountain,
    privilege: UserPrivilege.TRADER,
    breadcrumbs: [
      { label: "Dashboard", href: "/dashboard", active: false },
      {
        label: `${activePortfolio?.name ?? ""} Accounts`,
        href: "/accounts",
        active: false,
      },
      {
        label: `${account?.name ?? "Account"}`,
        href: `/accounts/${params.id}`,
        active: true,
      },
    ],
  };

  //  RENDER

  return (
    <PageInfoProvider value={pageInfo}>
      <div className={""}>
        <div className={"grid grid-cols-1 gap-4"}>
          {!account ? (
            <div>No Content</div>
          ) : (
            <AccountDetailsCmp account={account} />
          )}
          {children}
        </div>
      </div>
    </PageInfoProvider>
  );
}
