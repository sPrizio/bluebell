"use client";

import { TableCell, TableHead, TableRow } from "@/components/ui/table";
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
 * @version 1.0.0
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
          headerContent={
            <TableRow className={"hover:bg-transparent"}>
              <TableHead className="text-primary font-bold w-[90px]">
                ID
              </TableHead>
              <TableHead className="text-primary font-bold w-[200px]">
                Name
              </TableHead>
              <TableHead className="text-primary font-bold w-[130px]">
                Date
              </TableHead>
              <TableHead className="text-center text-primary font-bold w-[120px]">
                Type
              </TableHead>
              <TableHead className="text-center text-primary font-bold w-[80px]">
                Amount
              </TableHead>
              <TableHead className="text-right text-primary font-bold w-[100px]">
                Status
              </TableHead>
              {showActions && (
                <>
                  <TableHead className="w-[20px]" />
                  <TableHead className="text-primary font-bold w-[50px]" />
                  <TableHead className="text-primary font-bold w-[50px]" />
                </>
              )}
            </TableRow>
          }
          bodyContent={pagedTransactions?.transactions?.map((item) => {
            return (
              <TableRow key={item.uid} className={"hover:bg-transparent"}>
                <TableCell className="w-[90px]">
                  {item.transactionNumber}
                </TableCell>
                <TableCell className="w-[200px]">
                  <TooltipProvider>
                    <Tooltip>
                      <TooltipTrigger className="truncate">
                        <div className="truncate">{item.name}</div>
                      </TooltipTrigger>
                      <TooltipContent>{item.name}</TooltipContent>
                    </Tooltip>
                  </TooltipProvider>
                </TableCell>
                <TableCell className="w-[130px]">
                  {moment(item.transactionDate).format(
                    DateTime.ISOLongMonthDayYearFormat,
                  )}
                </TableCell>
                <TableCell className="text-center w-[120px]">
                  {item.transactionType.label}
                </TableCell>
                <TableCell className="text-center w-[80px]">
                  $ {formatNumberForDisplay(item.amount)}
                </TableCell>
                <TableCell className="text-right w-[100px]">
                  <div className="flex items-center justify-end">
                    {item.transactionStatus.label}&nbsp;
                    <span
                      className={`inline-block ${computeColors(item.transactionStatus.code)}`}
                    >
                      {resolveIcon(Icons.PointFilled, "", 15)}
                    </span>
                  </div>
                </TableCell>
                {showActions && account && (
                  <>
                    <TableCell className="w-[20px]" />
                    <TableCell className="w-[50px]">
                      <BaseModal
                        title="Edit Transaction"
                        description="Update this transaction's information. Any updates to the value will trigger an account balance recalculation."
                        content={
                          <TransactionForm
                            account={account}
                            mode="edit"
                            transaction={item}
                          />
                        }
                        trigger={
                          <Button variant="outline">
                            {resolveIcon(Icons.Edit)}
                          </Button>
                        }
                      />
                    </TableCell>
                    <TableCell className="w-[50px]">
                      <BaseModal
                        title="Delete Transaction"
                        description="This action is permanent and will trigger an account balance recalculation."
                        content={
                          <DeleteTransactionForm
                            account={account}
                            transaction={item}
                          />
                        }
                        trigger={
                          <Button variant="outline">
                            {resolveIcon(Icons.Trash)}
                          </Button>
                        }
                      />
                    </TableCell>
                  </>
                )}
              </TableRow>
            );
          })}
          caption={
            showBottomLink && (
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
            )
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
