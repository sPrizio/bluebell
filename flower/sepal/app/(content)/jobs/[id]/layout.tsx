"use client";

import React from "react";
import { useJobQuery } from "@/lib/hooks/query/queries";
import LoadingPage from "@/app/loading";
import Error from "@/app/error";
import { logErrors } from "@/lib/functions/util-functions";
import { PageInfoProvider } from "@/lib/context/PageInfoProvider";
import { Icons } from "@/lib/enums";
import JobInformation from "@/components/Job/JobInformation";
import { BaseCard } from "@/components/Card/BaseCard";
import ActionsTable from "@/components/Table/Job/ActionsTable";

/**
 * The base layout for the job detail page
 *
 * @param children react content
 * @param params parameters
 * @author Stephen Prizio
 * @version 0.2.2
 */
export default function JobLayout({
  children,
  params,
}: Readonly<{ children: React.ReactNode; params: { id: string } }>) {
  const { data: job, isError, isLoading, error } = useJobQuery(params.id);

  if (isLoading) {
    return <LoadingPage />;
  }

  if (isError) {
    logErrors(error);
    return <Error />;
  }

  const pageInfo = {
    title: "Job Details",
    subtitle: `View the details for ${job?.displayName ?? "a specific job"}`,
    iconCode: Icons.BrandReact,
    breadcrumbs: [
      { label: "Dashboard", href: "/dashboard", active: false },
      {
        label: `Jobs`,
        href: "/jobs",
        active: false,
      },
      {
        label: `${job?.displayName ?? "Job"}`,
        href: `/jobs/${params.id}`,
        active: true,
      },
    ],
    backCTA: {
      label: "View other jobs",
      href: "/jobs",
    },
  };

  //  RENDER

  return (
    <PageInfoProvider value={pageInfo}>
      <div
        className={"grid sm:grid-cols-1 lg:grid-cols-3 xl:grid-cols-4 gap-6"}
      >
        <div className={"col-span-1 lg:col-span-2 xl:col-span-3"}>
          <div className={"grid grid-cols-1 gap-4"}>
            <div>
              <BaseCard
                title={"Actions"}
                subtitle={"An overview of each action taken by this job."}
                cardContent={
                  <ActionsTable
                    entries={job?.jobResult?.entries ?? []}
                    actions={job?.actions ?? []}
                  />
                }
              />
            </div>
          </div>
        </div>
        <div>
          <BaseCard
            title={"Job Information"}
            cardContent={<JobInformation job={job} />}
          />
        </div>
      </div>
      <div>{children}</div>
    </PageInfoProvider>
  );
}
