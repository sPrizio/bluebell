"use client";

import {
  Table,
  TableBody,
  TableCaption,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import Link from "next/link";
import moment from "moment/moment";
import { DateTime } from "@/lib/constants";
import {
  formatNumberForDisplay,
  logErrors,
} from "@/lib/functions/util-functions";
import React, { useState } from "react";

import { Button } from "@/components/ui/button";
import TransactionForm from "@/components/Form/Transaction/TransactionForm";
import BaseModal from "@/components/Modal/BaseModal";
import DeleteTransactionForm from "@/components/Form/Transaction/DeleteTransactionForm";
import { Account } from "@/types/apiTypes";
import { resolveIcon } from "@/lib/functions/util-component-functions";
import { Icons } from "@/lib/enums";
import BaseTableContainer from "@/components/Table/BaseTableContainer";
import {
  Tooltip,
  TooltipContent,
  TooltipProvider,
  TooltipTrigger,
} from "@/components/ui/tooltip";
import { usePagedTransactionsQuery } from "@/lib/hooks/query/queries";
import { UserTransactionControlSelection } from "@/types/uiTypes";
import ErrorPage from "@/app/error";
import LoadingPage from "@/app/loading";
import {
  Pagination,
  PaginationContent,
  PaginationEllipsis,
  PaginationItem,
  PaginationLink,
  PaginationNext,
  PaginationPrevious,
} from "@/components/ui/pagination";

/**
 * Renders the account transactions as a table
 *
 * @param account Account
 * @param filters filters
 * @param initialPageSize initial page size
 * @param initialPage initial page
 * @param showActions shows the modification actions
 * @param showBottomLink show table caption
 * @author Stephen Prizio
 * @version 0.2.6
 */
export default function AccountTransactionsTable({
  account,
  filters,
  initialPageSize = 10,
  initialPage = 0,
  showActions = false,
  showBottomLink = true,
}: Readonly<{
  account: Account | null;
  filters: UserTransactionControlSelection;
  initialPageSize?: number;
  initialPage?: number;
  showActions?: boolean;
  showBottomLink?: boolean;
}>) {
  const [currentPage, setCurrentPage] = useState(initialPage);

  const {
    data: pagedTransactions,
    isLoading: isPagedTransactionsLoading,
    isError: isPagedTransactionsError,
    error: pagedTransactionsError,
  } = usePagedTransactionsQuery(
    account?.accountNumber ?? -1,
    moment(filters.start).format(DateTime.ISODateTimeFormat) ?? "",
    moment(filters.end).format(DateTime.ISODateTimeFormat) ?? "",
    currentPage,
    initialPageSize,
    filters.type,
    filters.status,
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

  //  RENDER

  if (!account) {
    return <ErrorPage />;
  }

  if (isPagedTransactionsLoading) {
    return <LoadingPage />;
  }

  if (isPagedTransactionsError) {
    logErrors(pagedTransactionsError);
    return <ErrorPage />;
  }

  const pages = pagedTransactions?.totalPages ?? 0;
  return (
    <div className="py-4">
      {(pagedTransactions?.transactions?.length ?? 0) === 0 && (
        <div className="text-center text-slate-500 mt-2 mb-6 text-sm">
          No recent transactions.
        </div>
      )}

      {(pagedTransactions?.transactions?.length ?? 0) > 0 && (
        <BaseTableContainer
          height={500}
          table={
            <Table>
              {showBottomLink ? (
                <TableCaption>
                  <div
                    className={
                      "flex items-center justify-center gap-1 pb-2 mt-4 text-sm"
                    }
                  >
                    <div className={""}>
                      <Link href={"/transactions?account=default"}>
                        View All Transactions
                      </Link>
                    </div>
                    <div className={""}>
                      <Link href={"#"}>
                        {resolveIcon(Icons.ExternalLink, "", 18)}
                      </Link>
                    </div>
                  </div>
                </TableCaption>
              ) : null}
              <TableHeader>
                <TableRow className={"hover:bg-transparent"}>
                  <TableHead className={"text-primary font-bold"}>ID</TableHead>
                  <TableHead className={"text-primary font-bold"}>
                    Name
                  </TableHead>
                  <TableHead className={"text-primary font-bold"}>
                    Date
                  </TableHead>
                  <TableHead className={"text-center text-primary font-bold"}>
                    Type
                  </TableHead>
                  <TableHead className={"text-center text-primary font-bold"}>
                    Value
                  </TableHead>
                  <TableHead className={"text-right text-primary font-bold"}>
                    Status
                  </TableHead>
                  {showActions ? (
                    <>
                      <TableHead />
                      <TableHead
                        className={"text-primary font-bold w-[50px]"}
                      />
                      <TableHead
                        className={"text-primary font-bold w-[50px]"}
                      />
                    </>
                  ) : null}
                </TableRow>
              </TableHeader>
              <TableBody>
                {pagedTransactions?.transactions?.map((item) => {
                  return (
                    <TableRow key={item.uid} className={"hover:bg-transparent"}>
                      <TableCell>{item.transactionNumber}</TableCell>
                      <TableCell>
                        <TooltipProvider>
                          <Tooltip>
                            <TooltipTrigger className={"truncate"}>
                              <div
                                className={
                                  "text-left sm:w-[150px] md:w-[150px] lg:w-[150px] xl:lg:w-[150px] truncate"
                                }
                              >
                                {item.name}
                              </div>
                            </TooltipTrigger>
                            <TooltipContent>{item.name}</TooltipContent>
                          </Tooltip>
                        </TooltipProvider>
                      </TableCell>
                      <TableCell>
                        {moment(item.transactionDate).format(
                          DateTime.ISOShortMonthFullDayFormat,
                        )}
                      </TableCell>
                      <TableCell className={"text-center"}>
                        {item.transactionType.label}
                      </TableCell>
                      <TableCell className={"text-center"}>
                        $&nbsp;{formatNumberForDisplay(item.amount)}
                      </TableCell>
                      <TableCell className={"text-right h-full"}>
                        <div className={"flex items-center justify-end"}>
                          {item.transactionStatus.label}&nbsp;
                          <span
                            className={
                              "inline-block " +
                              computeColors(item.transactionStatus.code)
                            }
                          >
                            {resolveIcon(Icons.PointFilled, "", 15)}
                          </span>
                        </div>
                      </TableCell>
                      {showActions && account ? (
                        <>
                          <TableCell />
                          <TableCell>
                            <BaseModal
                              title={"Edit Transaction"}
                              description={
                                "Keep track of your account's transactions by adding withdrawals & deposits."
                              }
                              content={
                                <TransactionForm
                                  account={account}
                                  mode={"edit"}
                                  transaction={item}
                                />
                              }
                              trigger={
                                <Button variant={"outline"}>
                                  {resolveIcon(Icons.Edit)}
                                </Button>
                              }
                            />
                          </TableCell>
                          <TableCell>
                            <BaseModal
                              title={"Delete Transaction"}
                              description={"Delete this transaction."}
                              content={
                                <DeleteTransactionForm
                                  account={account}
                                  transaction={item}
                                />
                              }
                              trigger={
                                <Button variant={"outline"}>
                                  {resolveIcon(Icons.Trash)}
                                </Button>
                              }
                            />
                          </TableCell>
                        </>
                      ) : null}
                    </TableRow>
                  );
                })}
              </TableBody>
            </Table>
          }
          pagination={
            pages === 1 ? null : (
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
            )
          }
        />
      )}
    </div>
  );
}
