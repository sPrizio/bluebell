"use client";

import { TableCell, TableHead, TableRow } from "@/components/ui/table";
import Link from "next/link";
import { formatNumberForDisplay } from "@/lib/functions/util-functions";
import {
  getBrokerImageForCode,
  getFlagForCode,
  resolveIcon,
} from "@/lib/functions/util-component-functions";
import moment from "moment";
import { DateTime } from "@/lib/constants";
import { useRouter } from "next/navigation";
import { Account } from "@/types/apiTypes";
import { Icons } from "@/lib/enums";
import React from "react";
import BaseTableContainer from "@/components/Table/BaseTableContainer";

/**
 * Renders a table containing the user's accounts
 *
 * @param accounts active accounts
 * @param showAllLink show link below table to direct to accounts page
 * @param allowAccountSelection allow clicking on rows
 * @param showCompactTable minimal table flag
 * @author Stephen Prizio
 * @version 1.0.0
 */
export default function AccountsTable({
  accounts = [],
  showAllLink = false,
  allowAccountSelection = false,
  showCompactTable = true,
}: Readonly<{
  accounts: Array<Account>;
  showAllLink?: boolean;
  allowAccountSelection?: boolean;
  showCompactTable?: boolean;
}>) {
  const router = useRouter();

  //  GENERAL FUNCTIONS

  /**
   * Obtains the image representing the given broker
   *
   * @param val incoming key
   */
  function getBrokerImage(val: string): React.ReactNode {
    return getBrokerImageForCode(val ?? "", 25, 25);
  }

  /**
   * Redirect to account page based on input value
   *
   * @param val account number
   */
  function redirectToAccount(val: number) {
    if (allowAccountSelection) {
      router.push(`/accounts/${val}`);
    }
  }

  return (
    <div className="pb-2">
      {accounts && accounts.length > 0 ? (
        <BaseTableContainer
          headerContent={
            showCompactTable ? (
              <TableRow className="hover:bg-transparent">
                <TableHead className="w-[150px]">Name</TableHead>
                <TableHead className="w-[100px] text-center">Type</TableHead>
                <TableHead className="w-[100px] text-center">Broker</TableHead>
                <TableHead className="w-[120px] text-right">Balance</TableHead>
              </TableRow>
            ) : (
              <TableRow className="hover:bg-transparent">
                <TableHead className="w-[50px] text-center" />
                <TableHead className="w-[75px]">Number</TableHead>
                <TableHead className="w-[175px]">Name</TableHead>
                <TableHead className="w-[175px]">Opened</TableHead>
                <TableHead className="w-[60px] text-center">Currency</TableHead>
                <TableHead className="w-[125px] text-center">
                  Platform
                </TableHead>
                <TableHead className="w-[85px] text-center">Type</TableHead>
                <TableHead className="w-[60px] text-center">Broker</TableHead>
                <TableHead className="w-[175px]">Last Traded</TableHead>
                <TableHead className="w-[120px] text-right">Balance</TableHead>
              </TableRow>
            )
          }
          bodyContent={
            showCompactTable ? (
              <>
                {accounts.map((item) => (
                  <TableRow
                    key={item.uid}
                    className={
                      allowAccountSelection
                        ? "hover:cursor-pointer"
                        : "hover:bg-transparent"
                    }
                  >
                    <TableCell className="w-[150px]">
                      <div className="truncate">{item.name}</div>
                    </TableCell>
                    <TableCell className="w-[100px] text-center">
                      {item.accountType?.label ?? "-"}
                    </TableCell>
                    <TableCell className="w-[100px] text-center">
                      {item.broker?.label ?? ""}
                    </TableCell>
                    <TableCell className="w-[120px] text-right">
                      $&nbsp;{formatNumberForDisplay(item.balance)}
                    </TableCell>
                  </TableRow>
                ))}
              </>
            ) : (
              <>
                {accounts.map((item) => (
                  <TableRow
                    key={item.uid}
                    className={
                      allowAccountSelection
                        ? "hover:cursor-pointer"
                        : "hover:bg-transparent"
                    }
                    onClick={() => redirectToAccount(item.accountNumber)}
                  >
                    <TableCell className="w-[50px] text-center">
                      {item.defaultAccount &&
                        resolveIcon(Icons.Flag3Filled, "text-primary")}
                    </TableCell>
                    <TableCell className="w-[75px]">
                      {item.accountNumber}
                    </TableCell>
                    <TableCell className="w-[175px]">{item.name}</TableCell>
                    <TableCell className="w-[175px]">
                      {moment(item.accountOpenTime).format(
                        DateTime.ISOShortMonthShortDayYearWithTimeFormat,
                      )}
                    </TableCell>
                    <TableCell className="w-[60px] text-center">
                      <div className="flex items-center justify-center">
                        {getFlagForCode(item.currency?.code ?? "")}
                      </div>
                    </TableCell>
                    <TableCell className="w-[125px] text-center">
                      {item.tradePlatform?.label ?? "-"}
                    </TableCell>
                    <TableCell className="w-[85px] text-center">
                      {item.accountType?.label ?? "-"}
                    </TableCell>
                    <TableCell className="w-[60px] text-center">
                      <div className="flex items-center justify-center">
                        {getBrokerImage(item.broker.code)}
                      </div>
                    </TableCell>
                    <TableCell className="w-[175px]">
                      {item.lastTraded ? (
                        moment(item.lastTraded).format(
                          DateTime.ISOShortMonthDayYearWithTimeFormat,
                        )
                      ) : (
                        <span>No trades</span>
                      )}
                    </TableCell>
                    <TableCell className="w-[120px] text-right">
                      $&nbsp;{formatNumberForDisplay(item.balance)}
                    </TableCell>
                  </TableRow>
                ))}
              </>
            )
          }
          caption={
            showAllLink ? (
              <div className="flex items-center justify-center gap-1 pb-2 mt-4 text-sm text-muted-foreground">
                <div>
                  <Link href="/accounts">View All Accounts</Link>
                </div>
                <div>
                  <Link href="#">
                    {resolveIcon(Icons.ExternalLink, "", 18)}
                  </Link>
                </div>
              </div>
            ) : null
          }
        />
      ) : (
        <div className="text-center pb-2 text-slate-600">
          No accounts found.
        </div>
      )}
    </div>
  );
}
