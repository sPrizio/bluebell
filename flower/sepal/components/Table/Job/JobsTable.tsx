"use client";

import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { UserJobControlSelection } from "@/types/uiTypes";
import { usePagedJobsQuery } from "@/lib/hooks/query/queries";
import React, { useState } from "react";
import moment from "moment";
import { DateTime } from "@/lib/constants";
import LoadingPage from "@/app/loading";
import { logErrors } from "@/lib/functions/util-functions";
import Error from "@/app/error";
import {
  Pagination,
  PaginationContent,
  PaginationEllipsis,
  PaginationItem,
  PaginationLink,
  PaginationNext,
  PaginationPrevious,
} from "@/components/ui/pagination";
import { IconPointFilled } from "@tabler/icons-react";
import { useRouter } from "next/navigation";

/**
 * Renders the jobs table
 * @param filters user selection filters
 * @param initialPageSize initial page size
 * @param initialPage initial page
 * @author Stephen Prizio
 * @version 0.2.1
 */
export default function JobsTable({
  filters,
  initialPageSize = 10,
  initialPage = 0,
}: Readonly<{
  filters: UserJobControlSelection;
  initialPageSize?: number;
  initialPage?: number;
}>) {
  const router = useRouter();
  const [currentPage, setCurrentPage] = useState(initialPage);

  const {
    data: pagedJobs,
    isLoading: isLoadingJobs,
    isError: isErrorJobs,
    error: jobsError,
  } = usePagedJobsQuery(
    moment(filters.start).format(DateTime.ISODateFormat) ?? "",
    moment(filters.end).add(1, "days").format(DateTime.ISODateFormat) ?? "",
    currentPage,
    initialPageSize,
    filters.jobType,
    filters.jobStatus,
    filters.sort,
  );

  //  GENERAL FUNCTIONS

  /**
   * Handles the clicking of a new page button
   *
   * @param e event
   * @param page selected page
   */
  function handleClick(e: React.MouseEvent, page: number) {
    e.preventDefault();
    setCurrentPage(page);
  }

  /**
   * Computes the appropriate colors depending on the value of the status
   *
   * @param val status
   */
  function computeColors(val: string) {
    if (val === "COMPLETED") {
      return " text-primaryGreen ";
    } else if (val === "FAILED") {
      return " text-primaryRed ";
    }

    return " text-primaryYellow ";
  }

  /**
   * Redirect to the account page based on input value
   *
   * @param val account number
   */
  function redirectToJob(val: number) {
    router.push(`/jobs/${val}`);
  }

  //  RENDER

  if (isLoadingJobs) {
    return <LoadingPage />;
  }

  if (isErrorJobs) {
    logErrors(jobsError);
    return <Error />;
  }

  const pages = pagedJobs?.totalPages ?? 0;
  return (
    <div className={"mt-4 pb-2 flex flex-col"}>
      {(pagedJobs?.jobs?.length ?? 0) === 0 && (
        <div className="text-center text-sm my-4 text-slate-500">
          No jobs found.
        </div>
      )}
      {(pagedJobs?.jobs?.length ?? 0) > 0 && (
        <div className={"min-h-[450px]"}>
          <div className={"flex-grow"}>
            <Table>
              <TableHeader className={"border-b-2 border-primaryLight"}>
                <TableRow className={"hover:bg-transparent"}>
                  <TableHead className={"text-left text-primary font-bold"}>
                    Name
                  </TableHead>
                  <TableHead className={"text-left text-primary font-bold"}>
                    Type
                  </TableHead>
                  <TableHead className={"text-left text-primary font-bold"}>
                    Start time
                  </TableHead>
                  <TableHead className={"text-left text-primary font-bold"}>
                    Completion Time
                  </TableHead>
                  <TableHead className={"text-center text-primary font-bold"}>
                    Status
                  </TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {pagedJobs?.jobs?.map((job, index) => {
                  return (
                    <TableRow
                      key={job.jobId + index}
                      className={"hover:cursor-pointer"}
                      onClick={() => redirectToJob(job.id)}
                    >
                      <TableCell className={"text-left"}>{job.name}</TableCell>
                      <TableCell className={"text-left"}>
                        {job.type.label}
                      </TableCell>
                      <TableCell className={"text-left"}>
                        {moment(job.executionTime).format(
                          DateTime.ISOShortMonthDayYearWithTimeFormat,
                        )}
                      </TableCell>
                      <TableCell className={"text-left"}>
                        {job.completionTime === null ? "In Progress" : null}
                        {moment(job.completionTime).format(
                          DateTime.ISOShortMonthDayYearWithTimeFormat,
                        )}
                      </TableCell>
                      <TableCell className={"text-right h-full"}>
                        <div className={"flex items-center justify-end"}>
                          {job.status.label}&nbsp;
                          <span
                            className={
                              "inline-block " + computeColors(job.status.code)
                            }
                          >
                            <IconPointFilled size={15} />
                          </span>
                        </div>
                      </TableCell>
                    </TableRow>
                  );
                })}
              </TableBody>
            </Table>
          </div>
          <div className={"mt-4"}>
            {pages === 1 ? null : (
              <Pagination
                className={"flex items-center justify-end text-right"}
              >
                <PaginationContent>
                  {currentPage > 0 ? (
                    <PaginationItem
                      onClick={(e) => handleClick(e, currentPage - 1)}
                    >
                      <PaginationPrevious href="#" />
                    </PaginationItem>
                  ) : null}
                  {currentPage > 1 ? (
                    <PaginationItem>
                      <PaginationEllipsis />
                    </PaginationItem>
                  ) : null}
                  {currentPage > 0 ? (
                    <PaginationItem
                      onClick={(e) => handleClick(e, currentPage - 1)}
                    >
                      <PaginationLink href="#">{currentPage}</PaginationLink>
                    </PaginationItem>
                  ) : null}
                  <PaginationItem>
                    <PaginationLink href="#" isActive>
                      {currentPage + 1}
                    </PaginationLink>
                  </PaginationItem>
                  {currentPage + 1 < pages ? (
                    <PaginationItem
                      onClick={(e) => handleClick(e, currentPage + 1)}
                    >
                      <PaginationLink href="#">
                        {currentPage + 2}
                      </PaginationLink>
                    </PaginationItem>
                  ) : null}
                  {currentPage + 1 < pages - 1 ? (
                    <PaginationItem>
                      <PaginationEllipsis />
                    </PaginationItem>
                  ) : null}
                  {currentPage + 1 < pages ? (
                    <PaginationItem
                      onClick={(e) => handleClick(e, currentPage + 1)}
                    >
                      <PaginationNext href="#" />
                    </PaginationItem>
                  ) : null}
                </PaginationContent>
              </Pagination>
            )}
          </div>
        </div>
      )}
    </div>
  );
}
