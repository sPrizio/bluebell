"use client";

import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import moment from "moment/moment";
import { DateTime } from "@/lib/constants";
import React, { useState } from "react";
import {
  formatNegativePoints,
  formatNumberForDisplay,
  logErrors,
} from "@/lib/functions/util-functions";
import {
  Pagination,
  PaginationContent,
  PaginationEllipsis,
  PaginationItem,
  PaginationLink,
  PaginationNext,
  PaginationPrevious,
} from "@/components/ui/pagination";
import { Account } from "@/types/apiTypes";
import { usePagedTradesQuery } from "@/lib/hooks/query/queries";
import Error from "@/app/error";

/**
 * Renders a table of trades
 *
 * @param account account
 * @param initialPageSize initial page size
 * @param initialPage initial page
 * @author Stephen Prizio
 * @version 0.2.0
 */
export default function TradeTable({
  account,
  initialPageSize = 10,
  initialPage = 0,
}: Readonly<{
  account: Account | null;
  initialPageSize?: number;
  initialPage?: number;
}>) {
  const [currentPage, setCurrentPage] = useState(initialPage);

  const {
    data: pagedTrades,
    isError: isPagedTradesError,
    error: pagedTradesError,
  } = usePagedTradesQuery(
    account?.accountNumber ?? -1,
    moment(account?.accountOpenTime).format(DateTime.ISODateTimeFormat),
    moment().add(1, "years").format(DateTime.ISODateTimeFormat),
    currentPage,
    initialPageSize,
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

  //  RENDER

  if (!account) {
    return <Error />;
  }

  if (isPagedTradesError) {
    logErrors(pagedTradesError);
    return <Error />;
  }

  const pages = pagedTrades?.totalPages ?? 0;
  return (
    <div className={"mt-4 pb-2 flex flex-col"}>
      {(pagedTrades?.trades?.length ?? 0) === 0 && (
        <div className="text-center text-sm my-4 text-slate-500">
          No trades found.
        </div>
      )}
      {(pagedTrades?.trades?.length ?? 0) > 0 && (
        <div className={"min-h-[450px]"}>
          <div className={"flex-grow"}>
            <Table>
              <TableHeader className={"border-b-2 border-primaryLight"}>
                <TableRow>
                  <TableHead className={"text-center text-primary font-bold"}>
                    Trade Id
                  </TableHead>
                  <TableHead className={"text-left text-primary font-bold"}>
                    Product
                  </TableHead>
                  <TableHead className={"text-left text-primary font-bold"}>
                    Open Time
                  </TableHead>
                  <TableHead className={"text-center text-primary font-bold"}>
                    Open Price
                  </TableHead>
                  <TableHead className={"text-center text-primary font-bold"}>
                    Lot Size
                  </TableHead>
                  <TableHead className={"text-left text-primary font-bold"}>
                    Close Time
                  </TableHead>
                  <TableHead className={"text-center text-primary font-bold"}>
                    Close Price
                  </TableHead>
                  <TableHead className={"text-right text-primary font-bold"}>
                    Net Profit
                  </TableHead>
                  <TableHead className={"text-right text-primary font-bold"}>
                    Points
                  </TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {pagedTrades?.trades?.map((item) => {
                  return (
                    <TableRow
                      key={item.tradeId}
                      className={"hover:bg-transparent"}
                    >
                      <TableCell className={"text-center"}>
                        {item.tradeId}
                      </TableCell>
                      <TableCell className={"text-left"}>
                        {item.product}
                      </TableCell>
                      <TableCell className={"text-left"}>
                        {moment(item.tradeOpenTime).format(
                          DateTime.ISOShortMonthDayYearWithTimeFormat,
                        )}
                      </TableCell>
                      <TableCell className={"text-center"}>
                        {formatNumberForDisplay(item.openPrice)}
                      </TableCell>
                      <TableCell className={"text-center"}>
                        {item.lotSize}
                      </TableCell>
                      <TableCell className={"text-left"}>
                        {moment(item.tradeCloseTime).format(
                          DateTime.ISOShortMonthDayYearWithTimeFormat,
                        )}
                      </TableCell>
                      <TableCell className={"text-center"}>
                        {formatNumberForDisplay(item.closePrice)}
                      </TableCell>
                      <TableCell className={"text-right"}>
                        $&nbsp;{formatNumberForDisplay(item.netProfit)}
                      </TableCell>
                      <TableCell className={"text-right"}>
                        {formatNegativePoints(item.points)}
                      </TableCell>
                    </TableRow>
                  );
                })}
              </TableBody>
            </Table>
          </div>
          <div className={"mt-4"}>
            <Pagination className={"flex items-center justify-end text-right"}>
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
                    <PaginationLink href="#">{currentPage + 2}</PaginationLink>
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
          </div>
        </div>
      )}
    </div>
  );
}
