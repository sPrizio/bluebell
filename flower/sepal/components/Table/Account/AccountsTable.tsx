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
 * @version 0.2.4
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

  //  RENDER

  return (
    <div className={"pb-2"}>
      {accounts && accounts.length > 0 ? (
        <BaseTableContainer
          table={
            <Table>
              {showAllLink ? (
                <TableCaption>
                  <div
                    className={
                      "flex items-center justify-center gap-1 pb-2 mt-4 text-sm"
                    }
                  >
                    <div className={""}>
                      <Link href={"/accounts"}>View All Accounts</Link>
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
                {showCompactTable ? (
                  <TableRow className={"hover:bg-transparent"}>
                    <TableHead>Name</TableHead>
                    <TableHead className={"text-center"}>Type</TableHead>
                    <TableHead className={"text-center"}>Broker</TableHead>
                    <TableHead className="text-right">Balance</TableHead>
                  </TableRow>
                ) : (
                  <TableRow className={"hover:bg-transparent"}>
                    <TableHead className={"w-[50px] text-center"} />
                    <TableHead className={"w-[150px]"}>Number</TableHead>
                    <TableHead className={"w-[175px]"}>Name</TableHead>
                    <TableHead className={"w-[175px]"}>Opened</TableHead>
                    {/*<TableHead className={'w-[175px]'}>Status</TableHead>*/}
                    <TableHead className={"w-[60px] text-center"}>
                      Currency
                    </TableHead>
                    <TableHead className={"w-[125px] text-center"}>
                      Platform
                    </TableHead>
                    <TableHead className={"w-[85px] text-center"}>
                      Type
                    </TableHead>
                    <TableHead className={"w-[60px] text-center"}>
                      Broker
                    </TableHead>
                    <TableHead className={"w-[175px]"}>Last Traded</TableHead>
                    <TableHead className="text-right">Balance</TableHead>
                  </TableRow>
                )}
              </TableHeader>
              {showCompactTable ? (
                <TableBody>
                  {accounts?.map((item) => {
                    return (
                      <TableRow
                        key={item.uid}
                        className={
                          allowAccountSelection
                            ? "hover:cursor-pointer"
                            : "hover:bg-transparent"
                        }
                      >
                        <TableCell>
                          <div
                            className={
                              "sm:w-[100px] md:w-[125px] lg:w-[75px] xl:lg:w-[75px] text-nowrap overflow-hidden text-ellipsis"
                            }
                          >
                            {item.name}
                          </div>
                        </TableCell>
                        <TableCell className={"text-center"}>
                          {item.accountType?.label ?? "-"}
                        </TableCell>
                        <TableCell className={"text-center"}>
                          {item.broker?.label ?? ""}
                        </TableCell>
                        <TableCell className="text-right">
                          $&nbsp;{formatNumberForDisplay(item.balance)}
                        </TableCell>
                      </TableRow>
                    );
                  }) ?? null}
                </TableBody>
              ) : (
                <TableBody>
                  {accounts?.map((item) => {
                    return (
                      <TableRow
                        key={item.uid}
                        className={
                          allowAccountSelection
                            ? "hover:cursor-pointer"
                            : "hover:bg-transparent"
                        }
                        onClick={() => redirectToAccount(item.accountNumber)}
                      >
                        <TableCell className={"text-center"}>
                          {item.defaultAccount
                            ? resolveIcon(Icons.Flag3Filled, "text-primary")
                            : null}
                        </TableCell>
                        <TableCell className={""}>
                          {item.accountNumber}
                        </TableCell>
                        <TableCell>{item.name}</TableCell>
                        <TableCell className={""}>
                          {moment(item.accountOpenTime).format(
                            DateTime.ISOShortMonthShortDayYearWithTimeFormat,
                          )}
                        </TableCell>
                        {/*<TableCell className={''}>
                            {
                              (item.accountCloseTime === '-1' || !item.accountCloseTime || item.accountCloseTime.length === 0) ?
                                <Badge text={'Open'} variant={'success'} /> : <Badge text={'Closed'} variant={'danger'} />/*moment(item.accountCloseTime).format(DateTime.ISOShortMonthDayYearWithTimeFormat)*/}
                        {/*</TableCell>*/}
                        <TableCell className={"text-center"}>
                          <div className={"flex items-center justify-center"}>
                            {getFlagForCode(item.currency?.code ?? "")}
                          </div>
                        </TableCell>
                        <TableCell className={"text-center"}>
                          {item.tradePlatform?.label ?? "-"}
                        </TableCell>
                        <TableCell className={"text-center"}>
                          {item.accountType?.label ?? "-"}
                        </TableCell>
                        <TableCell className={"text-center"}>
                          <div className={"flex items-center justify-center"}>
                            {getBrokerImage(item.broker.code)}
                          </div>
                        </TableCell>
                        <TableCell className={""}>
                          {item.lastTraded &&
                            moment(item.lastTraded).format(
                              DateTime.ISOShortMonthDayYearWithTimeFormat,
                            )}
                          {!item.lastTraded && <span>No trades</span>}
                        </TableCell>
                        <TableCell className="text-right">
                          $&nbsp;{formatNumberForDisplay(item.balance)}
                        </TableCell>
                      </TableRow>
                    );
                  }) ?? null}
                </TableBody>
              )}
            </Table>
          }
        />
      ) : (
        <div className={"text-center pb-2 text-slate-600"}>
          No accounts found.
        </div>
      )}
    </div>
  );
}
