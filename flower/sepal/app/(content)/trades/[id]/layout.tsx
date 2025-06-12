"use client";

import { useAccountQuery, useTradeQuery } from "@/lib/hooks/query/queries";
import { Icons } from "@/lib/enums";
import React from "react";
import { useSearchParams } from "next/navigation";
import LoadingPage from "@/app/loading";
import { logErrors } from "@/lib/functions/util-functions";
import { PageInfoProvider } from "@/lib/context/PageInfoProvider";
import BaseModal from "@/components/Modal/BaseModal";
import DeleteTradeForm from "@/components/Form/Trade/DeleteTradeForm";
import { resolveIcon } from "@/lib/functions/util-component-functions";
import { Button } from "@/components/ui/button";
import TradeInformation from "@/components/Table/Trade/TradeInformation";
import { BaseCard } from "@/components/Card/BaseCard";
import TradeReviewCard from "@/components/Card/Trade/TradeReviewCard";
import NotFound from "@/app/not-found";
import TradeForm from "@/components/Form/Trade/TradeForm";
import { useActivePortfolio } from "@/lib/hooks/api/useActivePortoflio";
import Error from "@/app/error";

/**
 * The base layout for the trade detail page
 *
 * @param children react content
 * @param params parameters
 * @author Stephen Prizio
 * @version 0.2.4
 */
export default function TradeDetailsLayout({
  children,
  params,
}: Readonly<{
  children: React.ReactNode;
  params: { id: string };
}>) {
  const searchParams = useSearchParams();
  const accNumber = searchParams.get("account") ?? "-1";

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
  } = useAccountQuery(accNumber);

  const {
    data: trade,
    isError: isTradeError,
    error: tradeError,
    isLoading: isTradeLoading,
  } = useTradeQuery(accNumber, params.id);

  if (isTradeLoading || isAccountLoading || isPortfolioLoading) {
    return <LoadingPage />;
  }

  if (isTradeError) {
    logErrors(tradeError);
    return <NotFound />;
  }

  if (isAccountError || isPortfolioError || portfolioMisMatch) {
    logErrors(accountError, portfolioError, portfolioMisMatch);
    return <Error />;
  }

  const pageInfo = {
    title: "Trade Details",
    subtitle: `View the details for trade ${params.id}`,
    iconCode: Icons.ReplaceFilled,
    breadcrumbs: [
      { label: "Dashboard", href: "/dashboard", active: false },
      {
        label: `${activePortfolio?.name ?? ""} Accounts`,
        href: "/accounts",
        active: false,
      },
      {
        label: `${account?.name ?? "Account"}`,
        href: `/accounts/${accNumber}`,
        active: false,
      },
      {
        label: `Trades`,
        href: `/trades?account=${accNumber}`,
        active: false,
      },
      {
        label: `${params.id}`,
        href: `/trades/${params.id}`,
        active: true,
      },
    ],
  };

  //  RENDER

  return (
    <PageInfoProvider value={pageInfo}>
      <div className="grid grid-cols-1 gap-6">
        <div>
          <div className={"sm:col-span-1 lg:col-span-2 xl:col-span-4"}>
            <div className={"flex items-end justify-end gap-4"}>
              <div className={""}>
                <BaseModal
                  title={"Update Trade Information"}
                  description={
                    "Here you can edit/update any trade information. Note that some aspects of this trade cannot be changed after trade creation."
                  }
                  trigger={
                    <Button className="" variant={"outline"}>
                      {resolveIcon(Icons.Edit)}
                      &nbsp;Update
                    </Button>
                  }
                  content={
                    <TradeForm account={account} trade={trade} mode={"edit"} />
                  }
                />
              </div>
              <div className={""}>
                <BaseModal
                  key={1}
                  title={"Delete Trade"}
                  trigger={
                    <Button className="bg-primaryRed text-white hover:bg-primaryRedLight">
                      {resolveIcon(Icons.Trash)}
                      &nbsp;Delete
                    </Button>
                  }
                  content={<DeleteTradeForm trade={trade} />}
                />
              </div>
            </div>
          </div>
        </div>
        <div>
          <div
            className={
              "grid sm:grid-cols-1 lg:grid-cols-3 xl:grid-cols-4 gap-6"
            }
          >
            <div className={"lg:col-span-2 xl:col-span-3"}>
              <TradeReviewCard trade={trade} />
            </div>
            <div>
              <BaseCard
                title={"Trade Information"}
                subtitle={"View the base trade details."}
                cardContent={<TradeInformation trade={trade} />}
              />
            </div>
            <div className={"lg:col-span-3 xl:col-span-4"}>
              <BaseCard
                title={"Metrics"}
                subtitle={"See how this trade could have been improved."}
                cardContent={<p className={"py-4"}>Coming in Phase 2</p>}
              />
            </div>
            {children}
          </div>
        </div>
      </div>
    </PageInfoProvider>
  );
}
