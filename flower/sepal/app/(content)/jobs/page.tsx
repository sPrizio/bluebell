"use client";

import { PageInfoProvider } from "@/lib/context/PageInfoProvider";
import { Icons } from "@/lib/enums";
import { BaseCard } from "@/components/Card/BaseCard";
import { useState } from "react";
import { UserJobControlSelection } from "@/types/uiTypes";
import moment from "moment/moment";
import JobsTable from "@/components/Table/Job/JobsTable";
import { Button } from "@/components/ui/button";
import JobsFilterDrawer from "@/components/Drawer/JobsFilterDrawer";
import { useJobTypesQuery } from "@/lib/hooks/query/queries";
import { logErrors } from "@/lib/functions/util-functions";
import Error from "@/app/error";
import LoadingPage from "@/app/loading";

/**
 * The page that shows the system's job executions
 *
 * @author Stephen Prizio
 * @version 0.2.4
 */
export default function JobsPage() {
  const [pageSize, setPageSize] = useState(10);
  const [hasSubmitted, setHasSubmitted] = useState(false);

  const {
    data: jobTypes,
    isLoading: isJobTypesLoading,
    isError: isJobTypesError,
    error: jobTypesError,
  } = useJobTypesQuery();

  const [userSelection, setUserSelection] = useState<UserJobControlSelection>({
    start: moment().subtract(1, "years").toDate(),
    end: moment().toDate(),
    jobType: "ALL",
    jobStatus: "ALL",
    sort: "desc",
  });
  const [submittedFilters, setSubmittedFilters] = useState(userSelection);

  const pageInfo = {
    title: "Jobs",
    subtitle:
      "A list of background and chronic processes executed by bluebell.",
    iconCode: Icons.Brain,
    breadcrumbs: [
      { label: "Dashboard", href: "/dashboard", active: false },
      {
        label: "Jobs",
        href: "/jobs",
        active: true,
      },
    ],
  };

  if (isJobTypesLoading) {
    return <LoadingPage />;
  }

  if (isJobTypesError) {
    logErrors(jobTypesError);
    return <Error />;
  }

  //  RENDER

  return (
    <PageInfoProvider value={pageInfo}>
      <div className={"grid grid-cols-1 gap-8 w-full"}>
        <div className={"flex items-end justify-end gap-4"}>
          <JobsFilterDrawer
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
            jobTypes={jobTypes}
          />
        </div>
        <div>
          <BaseCard
            title={"Jobs"}
            subtitle={
              "A list of background and chronic processes executed by bluebell."
            }
            cardContent={
              <JobsTable
                filters={submittedFilters}
                initialPageSize={pageSize}
              />
            }
            headerControls={[
              <Button
                key={0}
                variant={"outline"}
                onClick={() => setPageSize(100000)}
              >
                View All Jobs
              </Button>,
            ]}
          />
        </div>
      </div>
    </PageInfoProvider>
  );
}
