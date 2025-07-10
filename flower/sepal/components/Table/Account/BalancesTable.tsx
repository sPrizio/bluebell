import { Account } from "@/types/apiTypes";
import BaseTableContainer from "@/components/Table/BaseTableContainer";
import { TableCell, TableHead, TableRow } from "@/components/ui/table";
import { formatNumberForDisplay } from "@/lib/functions/util-functions";
import { resolveIcon } from "@/lib/functions/util-component-functions";
import { Icons } from "@/lib/enums";
import Link from "next/link";
import React from "react";

/**
 * Renders a table just for account balances
 *
 * @param accounts accounts
 * @param showAllLink link to accounts page
 * @param allowAccountSelection allow clicking on rows
 * @author Stephen Prizio
 * @version 1.0.0
 */
export default function BalancesTable({
  accounts = [],
  showAllLink = false,
  allowAccountSelection = false,
}: Readonly<{
  accounts: Array<Account>;
  showAllLink?: boolean;
  allowAccountSelection?: boolean;
}>) {
  //  RENDER
  return (
    <div className="pb-2">
      {accounts && accounts.length > 0 ? (
        <BaseTableContainer
          headerContent={
            <TableRow className="hover:bg-transparent">
              <TableHead className="">Name</TableHead>
              <TableHead className="text-center">Type</TableHead>
              <TableHead className="text-right">Balance</TableHead>
            </TableRow>
          }
          bodyContent={accounts.map((item) => (
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
              <TableCell className="w-[120px] text-right ">
                $&nbsp;{formatNumberForDisplay(item.balance)}
              </TableCell>
            </TableRow>
          ))}
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
