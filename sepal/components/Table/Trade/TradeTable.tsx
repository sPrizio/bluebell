'use client'

import {Table, TableBody, TableCell, TableHead, TableHeader, TableRow} from "@/components/ui/table";
import moment from "moment/moment";
import {DateTime} from "@/lib/constants";
import React, {useEffect, useState} from "react";
import {formatNegativePoints, formatNumberForDisplay} from "@/lib/functions/util-functions";
import {
  Pagination,
  PaginationContent,
  PaginationEllipsis,
  PaginationItem,
  PaginationLink,
  PaginationNext,
  PaginationPrevious,
} from "@/components/ui/pagination"
import {getPagedTrades} from "@/lib/functions/trade-functions";

/**
 * Renders a table of trades
 *
 * @param account account
 * @author Stephen Prizio
 * @version 0.0.2
 */
export default function TradeTable(
  {
    account,
  }
    : Readonly<{
    account: Account,
  }>
) {

  const [isLoading, setIsLoading] = useState(false)
  const [totalElements, setTotalElements] = useState(0)
  const [currentPage, setCurrentPage] = useState(0)
  const [pageSize, setPageSize] = useState(10)
  const [data, setData] = useState<Array<Trade>>()
  const [pages, setPages] = useState(calculatePages())

  useEffect(() => {
    getAccTrades();
  }, []);

  useEffect(() => {
    getAccTrades()
  }, [currentPage]);


  //  GENERAL FUNCTIONS

  /**
   * Fetches the paginated trades list
   */
  async function getAccTrades() {

    setIsLoading(true)

    const trs = await getPagedTrades(account?.accountNumber ?? '', moment(account?.accountOpenTime).format(DateTime.ISODateTimeFormat), moment().add(1, 'years').format(DateTime.ISODateTimeFormat), currentPage, pageSize)

    setTotalElements(trs?.totalTrades ?? 0)
    setPages(trs?.totalPages ?? 10)
    setData(trs?.trades ?? [])
    setPageSize(trs?.pageSize ?? 0)

    setIsLoading(false)
  }

  /**
   * Handles the clicking of a new page button
   *
   * @param e event
   * @param page selected page
   */
  function handleClick(e: React.MouseEvent, page: number) {
    e.preventDefault()
    setCurrentPage(page)
  }

  /**
   * Calculates the total number of pages
   */
  function calculatePages() {

    let val = totalElements;
    if (val % pageSize === 0) {
      return val / pageSize
    }

    return ((val - (val % pageSize)) / pageSize) + 1
  }


  //  RENDER

  return (
    <div className={'mt-4 pb-2 flex flex-col'}>
      {(data?.length ?? 0) === 0 && <div className="text-center text-sm my-4 text-slate-500">No trades found.</div>}
      {
        (data?.length ?? 0) > 0 &&
          <div className={'min-h-[450px]'}>
              <div className={'flex-grow'}>
                  <Table>
                      <TableHeader className={'border-b-2 border-primaryLight'}>
                          <TableRow>
                              <TableHead className={'text-center text-primary font-bold'}>Trade Id</TableHead>
                              <TableHead className={'text-left text-primary font-bold'}>Product</TableHead>
                              <TableHead className={'text-left text-primary font-bold'}>Open Time</TableHead>
                              <TableHead className={'text-center text-primary font-bold'}>Open Price</TableHead>
                              <TableHead className={'text-center text-primary font-bold'}>Lot Size</TableHead>
                              <TableHead className={'text-left text-primary font-bold'}>Close Time</TableHead>
                              <TableHead className={'text-center text-primary font-bold'}>Close Price</TableHead>
                              <TableHead className={'text-right text-primary font-bold'}>Net Profit</TableHead>
                              <TableHead className={'text-right text-primary font-bold'}>Points</TableHead>
                          </TableRow>
                      </TableHeader>
                      <TableBody>
                        {
                          data?.map((item, key) => {
                            return (
                              <TableRow key={item.uid} className={'hover:bg-transparent'}>
                                <TableCell className={'text-center'}>{item.tradeId}</TableCell>
                                <TableCell className={'text-left'}>{item.product}</TableCell>
                                <TableCell
                                  className={'text-left'}>{moment(item.tradeOpenTime).format(DateTime.ISOShortMonthDayYearWithTimeFormat)}</TableCell>
                                <TableCell
                                  className={'text-center'}>{formatNumberForDisplay(item.openPrice)}</TableCell>
                                <TableCell className={'text-center'}>{item.lotSize}</TableCell>
                                <TableCell
                                  className={'text-left'}>{moment(item.tradeCloseTime).format(DateTime.ISOShortMonthDayYearWithTimeFormat)}</TableCell>
                                <TableCell
                                  className={'text-center'}>{formatNumberForDisplay(item.closePrice)}</TableCell>
                                <TableCell
                                  className={'text-right'}>$&nbsp;{formatNumberForDisplay(item.netProfit)}</TableCell>
                                <TableCell className={'text-right'}>{formatNegativePoints(item.points)}</TableCell>
                              </TableRow>
                            )
                          })
                        }
                      </TableBody>
                  </Table>
              </div>
              <div className={'mt-4'}>
                  <Pagination className={'flex items-center justify-end text-right'}>
                      <PaginationContent>
                        {
                          currentPage > 0 ?
                            <PaginationItem onClick={(e) => handleClick(e, currentPage - 1)}>
                              <PaginationPrevious href="#"/>
                            </PaginationItem> : null
                        }
                        {
                          currentPage > 1 ?
                            <PaginationItem>
                              <PaginationEllipsis/>
                            </PaginationItem> : null
                        }
                        {
                          currentPage > 0 ?
                            <PaginationItem onClick={(e) => handleClick(e, currentPage - 1)}>
                              <PaginationLink href="#">{currentPage}</PaginationLink>
                            </PaginationItem> : null
                        }
                          <PaginationItem>
                              <PaginationLink href="#" isActive>
                                {currentPage + 1}
                              </PaginationLink>
                          </PaginationItem>
                        {
                          (currentPage + 1) < pages ?
                            <PaginationItem onClick={(e) => handleClick(e, currentPage + 1)}>
                              <PaginationLink href="#">
                                {currentPage + 2}
                              </PaginationLink>
                            </PaginationItem> : null
                        }
                        {
                          (currentPage + 1) < (pages - 1) ?
                            <PaginationItem>
                              <PaginationEllipsis/>
                            </PaginationItem> : null
                        }
                        {
                          (currentPage + 1) < pages ?
                            <PaginationItem onClick={(e) => handleClick(e, currentPage + 1)}>
                              <PaginationNext href="#"/>
                            </PaginationItem> : null
                        }
                      </PaginationContent>
                  </Pagination>
              </div>
          </div>
      }
    </div>
  )
}