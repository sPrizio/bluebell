'use client'

import {Table, TableBody, TableCell, TableHead, TableHeader, TableRow} from "@/components/ui/table";
import moment from "moment/moment";
import {DateTime} from "@/lib/constants";
import React, {useEffect, useState} from "react";
import {formatNegativePoints, formatNumberForDisplay} from "@/lib/functions";
import {
  Pagination,
  PaginationContent,
  PaginationEllipsis,
  PaginationItem,
  PaginationLink,
  PaginationNext,
  PaginationPrevious,
} from "@/components/ui/pagination"

/**
 * Renders a table of trades
 *
 * @param trades array of trades
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function TradeTable(
  {
    trades = [],
    totalElements = 100,
    pageSize = 10,
    page = 0
  }
    : Readonly<{
    trades: Array<Trade>,
    totalElements?: number,
    pageSize?: number,
    page?: number
  }>
) {

  const [isLoading, setIsLoading] = useState(false)
  const [currentPage, setCurrentPage] = useState(page)
  const [data, setData] = useState<Array<Trade>>()
  const [pages, setPages] = useState(calculatePages())

  useEffect(() => {
    getTrades()
  }, [currentPage]);


  //  GENERAL FUNCTIONS

  //TODO: TEMP
  function getTrades() {

    setIsLoading(true)

    setData(trades.slice((pageSize * currentPage), (pageSize * (currentPage + 1))))

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
    <div className={'mt-4 pb-2 min-h-[450px]'}>
      <Table>
        <TableHeader className={'border-b-2 border-primaryLight'}>
          <TableHead className={'text-center text-primary font-bold'}>Trade Id</TableHead>
          <TableHead className={'text-left text-primary font-bold'}>Product</TableHead>
          <TableHead className={'text-left text-primary font-bold'}>Open Time</TableHead>
          <TableHead className={'text-center text-primary font-bold'}>Open Price</TableHead>
          <TableHead className={'text-center text-primary font-bold'}>Lot Size</TableHead>
          <TableHead className={'text-left text-primary font-bold'}>Close Time</TableHead>
          <TableHead className={'text-center text-primary font-bold'}>Close Price</TableHead>
          <TableHead className={'text-right text-primary font-bold'}>Net Profit</TableHead>
          <TableHead className={'text-right text-primary font-bold'}>Points</TableHead>
        </TableHeader>
        <TableBody>
          {
            data?.map(item => {
              return (
                <TableRow key={item.uid} className={'hover:bg-transparent'}>
                  <TableCell className={'text-center'}>{item.tradeId}</TableCell>
                  <TableCell className={'text-left'}>{item.product}</TableCell>
                  <TableCell className={'text-left'}>{moment(item.tradeOpenTime).format(DateTime.ISOShortMonthDayYearWithTimeFormat)}</TableCell>
                  <TableCell className={'text-center'}>{formatNumberForDisplay(item.openPrice)}</TableCell>
                  <TableCell className={'text-center'}>{item.lotSize}</TableCell>
                  <TableCell className={'text-left'}>{moment(item.tradeCloseTime).format(DateTime.ISOShortMonthDayYearWithTimeFormat)}</TableCell>
                  <TableCell className={'text-center'}>{formatNumberForDisplay(item.closePrice)}</TableCell>
                  <TableCell className={'text-right'}>$&nbsp;{formatNumberForDisplay(item.netProfit)}</TableCell>
                  <TableCell className={'text-right'}>{formatNegativePoints(item.points)}</TableCell>
                </TableRow>
              )
            })
          }
        </TableBody>
      </Table>
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
                  <PaginationEllipsis />
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
              (currentPage + 1) < calculatePages() ?
                <PaginationItem onClick={(e) => handleClick(e, currentPage + 1)}>
                  <PaginationLink href="#">
                    {currentPage + 2}
                  </PaginationLink>
                </PaginationItem> : null
            }
            {
              (currentPage + 1) < (calculatePages() - 1) ?
                <PaginationItem>
                  <PaginationEllipsis />
                </PaginationItem> : null
            }
            {
              (currentPage + 1) < calculatePages() ?
                <PaginationItem onClick={(e) => handleClick(e, currentPage + 1)}>
                  <PaginationNext href="#" />
                </PaginationItem> : null
            }
          </PaginationContent>
        </Pagination>
      </div>
    </div>
  )
}