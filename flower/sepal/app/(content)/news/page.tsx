"use client";

import React from "react";
import { Icons } from "@/lib/enums";
import { BaseCard } from "@/components/Card/BaseCard";
import NewsTable from "@/components/Table/News/NewsTable";
import moment from "moment";
import { DateTime } from "@/lib/constants";
import FetchMarketNewsButton from "@/components/Button/FetchMarketNewsButton";
import { useMarketNewsQuery } from "@/lib/hooks/query/queries";
import { PageInfoProvider } from "@/lib/context/PageInfoProvider";
import LoadingPage from "@/app/loading";
import { logErrors } from "@/lib/functions/util-functions";

/**
 * Renders the market News page
 *
 * @author Stephen Prizio
 * @version 0.2.2
 */
export default function MarketNewsPage() {
  const {
    data: marketNews,
    isLoading: isLoadingNews,
    isError: isErrorNews,
    isSuccess: isSuccessNews,
    error: newsError,
  } = useMarketNewsQuery(
    moment().startOf("week").add(1, "days").format(DateTime.ISODateFormat),
    moment().startOf("week").add(6, "days").format(DateTime.ISODateFormat),
  );

  const pageInfo = {
    title: "Market News",
    subtitle: "A look at your local market news.",
    iconCode: Icons.News,
    breadcrumbs: [
      { label: "Dashboard", href: "/dashboard", active: false },
      { label: "Market News", href: "/market-News", active: true },
    ],
  };

  //  RENDER

  if (isLoadingNews) {
    return <LoadingPage />;
  }

  if (isErrorNews) {
    logErrors(newsError);
  }

  const content =
    isSuccessNews && !isErrorNews && marketNews?.length ? (
      <NewsTable news={marketNews ?? []} />
    ) : null;

  return (
    <PageInfoProvider value={pageInfo}>
      <div className={""}>
        <BaseCard
          title={"Market News"}
          subtitle={"A look at your local market news."}
          cardContent={content}
          headerControls={[<FetchMarketNewsButton key={0} />]}
          emptyText={
            "There doesn\'t seem to be any news for this week. Come back later for an update."
          }
        />
      </div>
    </PageInfoProvider>
  );
}
