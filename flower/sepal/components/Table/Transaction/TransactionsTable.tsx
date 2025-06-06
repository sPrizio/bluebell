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
import { formatNumberForDisplay } from "@/lib/functions/util-functions";
import React from "react";
import { Transaction } from "@/types/apiTypes";
import { resolveIcon } from "@/lib/functions/util-component-functions";
import { Icons } from "@/lib/enums";
import SepalLoader from "@/components/Svg/SepalLoader";

/**
 * Renders the account transactions as a table
 *
 * @param transactions list of Account transactions
 * @param showBottomLink show table caption
 * @author Stephen Prizio
 * @version 0.2.4
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
                    {resolveIcon(Icons.ExternalLink, "", 18)}
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
              <TableHead className={"text-center"}>Status</TableHead>
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
                  <TableCell>
                    <div
                      className={
                        "sm:w-[150px] md:w-[150px] lg:w-[150px] xl:lg:w-[75px] text-nowrap overflow-hidden text-ellipsis"
                      }
                    >
                      {item.accountName}
                    </div>
                  </TableCell>
                  <TableCell className={"text-center"}>
                    <div
                      className={
                        "flex grow items-stretch content-stretch justify-center h-full"
                      }
                    >
                      {item.transactionType.code === "DEPOSIT" &&
                        resolveIcon(Icons.Download, "text-foreground", 15)}
                      {item.transactionType.code === "WITHDRAWAL" &&
                        resolveIcon(Icons.Upload, "text-foreground", 15)}
                    </div>
                  </TableCell>
                  <TableCell className={"text-center"}>
                    $&nbsp;{formatNumberForDisplay(item.amount)}
                  </TableCell>
                  <TableCell>
                    <div className={"flex items-center justify-center"}>
                      {item.transactionStatus.code === "PENDING" && (
                        <SepalLoader className={"m-0 !h-5 !w-5"} />
                      )}
                      {item.transactionStatus.code === "IN_PROGRESS" && (
                        <SepalLoader className={"m-0 !h-5 !w-5"} />
                      )}
                      {item.transactionStatus.code === "COMPLETED" &&
                        resolveIcon(
                          Icons.ShieldCheckFilled,
                          "text-primaryGreen",
                          20,
                        )}
                      {item.transactionStatus.code === "FAILED" &&
                        resolveIcon(
                          Icons.HexagonLetterXFilled,
                          "text-primaryRed",
                          20,
                        )}
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
