import React from "react";
import { Table, TableBody, TableCell, TableRow } from "@/components/ui/table";
import moment from "moment";
import { DateTime } from "@/lib/constants";
import { formatNumberForDisplay } from "@/lib/functions/util-functions";
import {
  getBrokerImageForCode,
  getFlagForCode,
} from "@/lib/functions/util-component-functions";
import Badge from "@/components/Badge/Badge";
import { Account } from "@/types/apiTypes";

/**
 * Renders basic Account information
 *
 * @param account Account info
 * @author Stephen Prizio
 * @version 0.2.0
 */
export default function AccountInformation({
  account,
}: Readonly<{
  account?: Account;
}>) {
  //  GENERAL FUNCTIONS

  /**
   * Renders the base styles for the header column cells
   *
   * @param last if last, remove the border
   */
  function getHeaderColumnStyles(last = false) {
    const base = "text-right bg-primary bg-opacity-5 py-3";
    if (last) {
      return base;
    }

    return `${base} border-b border-gray-200`;
  }

  //  RENDER

  return (
    <div>
      <Table>
        <TableBody>
          <TableRow className={"hover:bg-transparent"}>
            <TableCell className={getHeaderColumnStyles()}>
              Account Number
            </TableCell>
            <TableCell>{account?.accountNumber}</TableCell>
          </TableRow>
          <TableRow className={"hover:bg-transparent"}>
            <TableCell className={getHeaderColumnStyles()}>
              Account Type
            </TableCell>
            <TableCell>{account?.accountType.label}</TableCell>
          </TableRow>
          <TableRow className={"hover:bg-transparent"}>
            <TableCell className={getHeaderColumnStyles()}>
              Date Opened
            </TableCell>
            <TableCell>
              {moment(account?.accountOpenTime).format(
                DateTime.ISOShortMonthDayYearFormat,
              )}
            </TableCell>
          </TableRow>
          <TableRow className={"hover:bg-transparent"}>
            <TableCell className={getHeaderColumnStyles()}>
              Date Closed
            </TableCell>
            <TableCell>
              {account?.accountCloseTime &&
                account?.accountCloseTime !== "-1" &&
                moment(account?.accountCloseTime).format(
                  DateTime.ISOShortMonthDayYearFormat,
                )}
              {(!account?.accountCloseTime ||
                account.accountCloseTime === "-1") && <span>Open</span>}
            </TableCell>
          </TableRow>
          <TableRow className={"hover:bg-transparent"}>
            <TableCell className={getHeaderColumnStyles()}>Currency</TableCell>
            <TableCell className={"flex items-center gap-2"}>
              <span className={"inline-block"}>
                {getFlagForCode(account?.currency?.code ?? "", 25, 25)}
              </span>
              {account?.currency?.label}
            </TableCell>
          </TableRow>
          <TableRow className={"hover:bg-transparent"}>
            <TableCell className={getHeaderColumnStyles()}>Balance</TableCell>
            <TableCell className={""}>
              $&nbsp;{formatNumberForDisplay(account?.balance ?? 0)}
            </TableCell>
          </TableRow>
          <TableRow className={"hover:bg-transparent"}>
            <TableCell className={getHeaderColumnStyles()}>Name</TableCell>
            <TableCell>{account?.name}</TableCell>
          </TableRow>
          <TableRow className={"hover:bg-transparent"}>
            <TableCell className={getHeaderColumnStyles()}>Status</TableCell>
            <TableCell>
              {(account?.active ?? false) ? (
                <Badge text={"Active"} variant={"success"} />
              ) : (
                <Badge text={"Inactive"} variant={"danger"} />
              )}
            </TableCell>
          </TableRow>
          <TableRow className={"hover:bg-transparent"}>
            <TableCell className={getHeaderColumnStyles()}>Broker</TableCell>
            <TableCell className={"flex items-center gap-2"}>
              <span className={"inline-block"}>
                {getBrokerImageForCode(account?.broker?.code ?? "", 25, 25)}
              </span>
              {account?.broker?.label}
            </TableCell>
          </TableRow>
          <TableRow className={"hover:bg-transparent"}>
            <TableCell className={getHeaderColumnStyles()}>Platform</TableCell>
            <TableCell>{account?.tradePlatform.label}</TableCell>
          </TableRow>
          <TableRow className={"hover:bg-transparent"}>
            <TableCell className={getHeaderColumnStyles()}>
              Last Traded
            </TableCell>
            <TableCell>
              {account?.lastTraded &&
                moment(account.lastTraded).format(
                  DateTime.ISOShortMonthDayYearFormat,
                )}
              {!account?.lastTraded && <span>No trades</span>}
            </TableCell>
          </TableRow>
          <TableRow className={"hover:bg-transparent"}>
            <TableCell className={getHeaderColumnStyles(true)}>
              Default Account
            </TableCell>
            <TableCell>
              {(account?.defaultAccount ?? false) ? "Yes" : "No"}
            </TableCell>
          </TableRow>
        </TableBody>
      </Table>
    </div>
  );
}
