"use client";

import { useTradeQuery } from "@/lib/hooks/query/queries";
import { Icons } from "@/lib/enums";
import React from "react";
import { useSearchParams } from "next/navigation";
import LoadingPage from "@/app/loading";
import { logErrors } from "@/lib/functions/util-functions";
import Error from "@/app/error";
import { PageInfoProvider } from "@/lib/context/PageInfoProvider";
import BaseModal from "@/components/Modal/BaseModal";
import DeleteTradeForm from "@/components/Form/Trade/DeleteTradeForm";
import { resolveIcon } from "@/lib/functions/util-component-functions";
import { Button } from "@/components/ui/button";

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

  const {
    data: trade,
    isError: isTradeError,
    error: tradeError,
    isLoading: isTradeLoading,
  } = useTradeQuery(searchParams.get("account") ?? "-1", params.id);

  if (isTradeLoading) {
    return <LoadingPage />;
  }

  if (isTradeError) {
    logErrors(tradeError);
    return <Error />;
  }

  const pageInfo = {
    title: "Trade Details",
    subtitle: `View the details for trade ${params.id}`,
    iconCode: Icons.ReplaceFilled,
    breadcrumbs: [
      { label: "Dashboard", href: "/dashboard", active: false },
      {
        label: `Trades`,
        href: `/trades?account=${searchParams.get("account")}`,
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
      <div className={"sm:col-span-1 lg:col-span-2 xl:col-span-4"}>
        <div className={"flex items-end justify-end gap-4"}>
          <div className={""}>Update</div>
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
      <div
        className={"grid sm:grid-cols-1 lg:grid-cols-2 xl:grid-cols-4 gap-6"}
      >
        <p>Trades Page</p>
        <p>Trade: {params.id}</p>
        <p>Profit: {trade?.netProfit ?? 0}</p>
        <p>Account: {searchParams.get("account")}</p>
        {children}
      </div>
    </PageInfoProvider>
  );
}
