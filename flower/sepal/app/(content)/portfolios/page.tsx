"use client";

import { Icons } from "@/lib/enums";
import { PageInfoProvider } from "@/lib/context/PageInfoProvider";
import { useUserQuery } from "@/lib/hooks/query/queries";
import BaseModal from "@/components/Modal/BaseModal";
import { Button } from "@/components/ui/button";
import React from "react";
import { BaseCard } from "@/components/Card/BaseCard";
import LoadingPage from "@/app/loading";
import { logErrors } from "@/lib/functions/util-functions";
import Error from "@/app/error";
import PortfoliosTable from "@/components/Table/Portfolio/PortfoliosTable";
import PortfolioForm from "@/components/Form/Portfolio/PortfolioForm";
import { resolveIcon } from "@/lib/functions/util-component-functions";

/**
 * The page that shows all of a user's portfolios
 *
 * @author Stephen Prizio
 * @version 0.2.2
 */
export default function PortfoliosPage() {
  const { data: user, isLoading, isError, error } = useUserQuery();

  if (isLoading) {
    return <LoadingPage />;
  }

  if (isError) {
    logErrors(error);
    return <Error />;
  }

  const activePortfolios = user?.portfolios?.filter((p) => p.active) ?? [];
  const inActivePortfolios = user?.portfolios?.filter((p) => !p.active) ?? [];

  const pageInfo = {
    title: "Portfolios",
    subtitle: "A list of all of your trading account portfolios.",
    iconCode: Icons.Briefcase,
    breadcrumbs: [
      ...((activePortfolios?.length ?? 0) > 0
        ? [{ label: "Dashboard", href: "/dashboard", active: false }]
        : []),
      {
        label: `Portfolios`,
        href: "/portfolios",
        active: true,
      },
    ],
  };

  //  RENDER

  let inactiveData = null;
  let activeData = (
    <div className={"text-center"}>
      No active portfolios found. Consider creating one!
    </div>
  );

  if ((activePortfolios?.length ?? 0) > 0) {
    activeData = (
      <div className={""}>
        <BaseCard
          title={"Active Portfolios"}
          subtitle={"All actively traded portfolios."}
          cardContent={<PortfoliosTable portfolios={activePortfolios} />}
        />
      </div>
    );
  }

  if ((inActivePortfolios?.length ?? 0) > 0) {
    inactiveData = (
      <div className={""}>
        <BaseCard
          title={"Inactive Portfolios"}
          subtitle={
            "Inactive or disabled portfolios are ones that are not currently being traded."
          }
          cardContent={<PortfoliosTable portfolios={inActivePortfolios} />}
        />
      </div>
    );
  }

  return (
    <PageInfoProvider value={pageInfo}>
      <div className={"grid grid-cols-1 gap-8 w-full"}>
        <div className={"flex gap-8 w-full items-end justify-end"}>
          <div className={"w-1/2 flex items-end justify-end gap-8"}>
            <div>
              <BaseModal
                title={"Add a new Portfolio"}
                description={
                  "A portfolio is a collection of accounts. Use it to capture the performance of a group of accounts and perform analysis on each of them. If you wish to track accounts purely for archive/historical reasons, mark the portfolio as inactive."
                }
                trigger={
                  <Button className={"w-full text-white"}>
                    {resolveIcon(Icons.CirclePlus)}
                    &nbsp;Add a new portfolio
                  </Button>
                }
                content={<PortfolioForm mode={"create"} />}
              />
            </div>
          </div>
        </div>

        <div className={"flex items-center text-sm justify-end w-full"}>
          <span className={"inline-block"}>
            {resolveIcon(Icons.Flag3Filled, "text-primary")}
          </span>
          &nbsp;&nbsp;indicates default portfolio.
        </div>

        {activeData}
        {inactiveData}
      </div>
    </PageInfoProvider>
  );
}
