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
import { IconExternalLink, IconPointFilled } from "@tabler/icons-react";
import moment from "moment/moment";
import { DateTime } from "@/lib/constants";
import { formatNumberForDisplay } from "@/lib/functions/util-functions";
import React from "react";
import { Transaction } from "@/types/apiTypes";

/**
 * Renders the account transactions as a table
 *
 * @param transactions list of Account transactions
 * @param showBottomLink show table caption
 * @author Stephen Prizio
 * @version 0.2.0
 */
export default function TransactionsTable({
  transactions = [],
  showBottomLink = false,
}: Readonly<{
  transactions: Array<Transaction>;
  showBottomLink?: boolean;
}>) {
  //  GENERAL FUNCTIONS

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

  return (
    <>
      {(!transactions || transactions.length === 0) && (
        <div className="text-center text-slate-500 mt-2 mb-6 text-sm">
          No recent transactions.
        </div>
      )}
      {transactions.length > 0 && (
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
                    <IconExternalLink size={18} />
                  </Link>
                </div>
              </div>
            </TableCaption>
          ) : null}
          <TableHeader>
            <TableRow className={"hover:bg-transparent"}>
              <TableHead>Date</TableHead>
              <TableHead>Account</TableHead>
              <TableHead className={"text-center"}>Type</TableHead>
              <TableHead className={"text-center"}>Value</TableHead>
              <TableHead className={"text-right"}>Status</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {transactions?.map((item) => {
              return (
                <TableRow key={item.uid} className={"hover:bg-transparent"}>
                  <TableCell>
                    {moment(item.transactionDate).format(
                      DateTime.ISOShortMonthFullDayFormat,
                    )}
                  </TableCell>
                  <TableCell>{item.accountName}</TableCell>
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
                        <IconPointFilled size={15} />
                      </span>
                    </div>
                  </TableCell>
                </TableRow>
              );
            })}
          </TableBody>
        </Table>
      )}
    </>
  );
}
