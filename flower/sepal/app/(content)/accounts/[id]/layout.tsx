"use client";

import React from "react";
import { Icons } from "@/lib/enums";
import { PageInfoProvider } from "@/lib/context/PageInfoProvider";
import { useAccountQuery } from "@/lib/hooks/query/queries";
import LoadingPage from "@/app/loading";
import { logErrors } from "@/lib/functions/util-functions";
import Error from "@/app/error";
import AccountDetailsCmp from "@/components/Account/AccountDetailsCmp";
import { useActivePortfolio } from "@/lib/hooks/api/useActivePortoflio";

/**
 * The base layout for the Account detail page
 *
 * @param children Content
 * @param params Account ID
 * @author Stephen Prizio
 * @version 0.2.0
 */
export default function AccountDetailsLayout({
  children,
  params,
}: Readonly<{
  children: React.ReactNode;
  params: { id: string };
}>) {
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
  } = useAccountQuery(params.id);

  if (isPortfolioLoading || isAccountLoading) {
    return <LoadingPage />;
  }

  if (isPortfolioError || portfolioError || isAccountError) {
    logErrors(
      "User and portfolio mismatch!",
      portfolioError,
      portfolioMisMatch,
      accountError,
    );
    return <Error />;
  }

  const pageInfo = {
    title: "Account Overview",
    subtitle: computeDescription(),
    iconCode: Icons.Mountain,
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
    backCTA: {
      label: "Return to your accounts",
      href: "/accounts",
    },
  };

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
