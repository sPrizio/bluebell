"use client";

import { PageInfoProvider } from "@/lib/context/PageInfoProvider";
import { Icons, UserPrivilege } from "@/lib/enums";
import { BaseCard } from "@/components/Card/BaseCard";
import { useCallback, useState } from "react";
import { UserJobControlSelection } from "@/types/uiTypes";
import moment from "moment/moment";
import JobsTable from "@/components/Table/Job/JobsTable";
import { Button } from "@/components/ui/button";
import JobsFilterDrawer from "@/components/Drawer/JobsFilterDrawer";
import { useJobTypesQuery } from "@/lib/hooks/query/queries";
import { logErrors } from "@/lib/functions/util-functions";
import ErrorPage from "@/app/error";
import LoadingPage from "@/app/loading";
import { CONTROL_GAP, PAGE_GAP } from "@/lib/constants";

/**
 * The page that shows the system's job executions
 *
 * @author Stephen Prizio
 * @version 1.0.0
 */
export default function JobsPage() {
  const initialPageSize = 10;
  const [pageSize, setPageSize] = useState(initialPageSize);
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
    privilege: UserPrivilege.SYSTEM,
    breadcrumbs: [
      { label: "Dashboard", href: "/dashboard", active: false },
      {
        label: "Jobs",
        href: "/jobs",
        active: true,
      },
    ],
  };

  //  GENERAL FUNCTIONS

  /**
   * Handles the toggling of showing all jobs
   */
  const handleClick = useCallback(() => {
    if (pageSize > initialPageSize) {
      setPageSize(initialPageSize);
    } else {
      setPageSize(100000);
    }
  }, [pageSize]);

  if (isJobTypesLoading) {
    return <LoadingPage />;
  }

  if (isJobTypesError) {
    logErrors(jobTypesError);
    return <ErrorPage />;
  }

  //  RENDER

  return (
    <PageInfoProvider value={pageInfo}>
      <div className={`grid grid-cols-1 w-full ${PAGE_GAP}`}>
        <div className={`flex items-end justify-end ${CONTROL_GAP}`}>
          <div>
            <Button key={0} variant={"outline"} onClick={handleClick}>
              {pageSize > initialPageSize ? "Minimize" : "Show All"}
            </Button>
          </div>
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
          />
        </div>
      </div>
    </PageInfoProvider>
  );
}
