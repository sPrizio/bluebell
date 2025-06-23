"use client";

import React, { useState } from "react";
import BaseModal from "@/components/Modal/BaseModal";
import { Button } from "@/components/ui/button";
import AccountForm from "@/components/Form/Account/AccountForm";
import DeleteAccountForm from "@/components/Form/Account/DeleteAccountForm";
import { BaseCard } from "@/components/Card/BaseCard";
import AccountInformation from "@/components/Table/Account/AccountInformation";
import { Switch } from "@/components/ui/switch";
import { Label } from "@/components/ui/label";
import SimpleBanner from "@/components/Banner/SimpleBanner";
import AccountEquityChart from "@/components/Chart/Account/AccountEquityChart";
import { Progress } from "@/components/ui/progress";
import AccountInsights from "@/components/Account/AccountInsights";
import AccountStatistics from "@/components/Account/AccountStatistics";
import TradeRecordTable from "@/components/Table/Trade/TradeRecordTable";
import TradeTable from "@/components/Table/Trade/TradeTable";
import Link from "next/link";
import ImportTradesForm from "@/components/Form/Trade/ImportTradesForm";
import { Account } from "@/types/apiTypes";
import {
  useAccountDetailsQuery,
  useRecentTradeRecordsQuery,
} from "@/lib/hooks/query/queries";
import Error from "@/app/error";
import { logErrors } from "@/lib/functions/util-functions";
import moment from "moment";
import { resolveIcon } from "@/lib/functions/util-component-functions";
import { Icons } from "@/lib/enums";
import LoadingPage from "@/app/loading";
import { useSessionContext } from "@/lib/context/SessionContext";

/**
 * Renders the account details layout
 *
 * @param account Account info
 * @author Stephen Prizio
 * @version 0.2.6
 */
export default function AccountDetailsCmp({
  account,
}: Readonly<{
  account: Account;
}>) {
  const session = useSessionContext();
  const tradeRecordReportLookBack = 8;
  const {
    data: accountDetails,
    isError: isAccountDetailsError,
    error: accountDetailsError,
    isLoading: isAccountDetailsLoading,
  } = useAccountDetailsQuery(
    account.accountNumber.toString(),
    session?.username ?? "",
  );
  const {
    data: recentTradeRecords,
    isError: isRecentTradeRecordsError,
    error: recentTradeRecordsError,
    isLoading: isRecentTradeRecordsLoading,
  } = useRecentTradeRecordsQuery(
    session?.username ?? "",
    account.accountNumber.toString(),
    "DAILY",
    tradeRecordReportLookBack,
  );

  const [showPoints, setShowPoints] = useState(false);

  //  GENERAL FUNCTIONS

  /**
   * Computes the consistency general color
   */
  function computeConsistencyColor() {
    const val = accountDetails?.consistency ?? 0;
    switch (true) {
      case val === 0:
        return "slate-500";
      case val < 35:
        return "primaryRed";
      case val < 75:
        return "primaryYellow";
      case val <= 100:
        return "primaryGreen";
      default:
        return "primary";
    }
  }

  /**
   * Computes the consistency general value
   */
  function computeConsistency() {
    const val = accountDetails?.consistency ?? 0;
    switch (true) {
      case val === 0:
        return "neutral";
      case val < 35:
        return "danger";
      case val < 75:
        return "warning";
      case val <= 100:
        return "success";
      default:
        return "info";
    }
  }

  /**
   * Computes the consistency status text
   */
  function computeConsistencyStatus() {
    const val = accountDetails?.consistency ?? 0;
    switch (true) {
      case val === 0:
        return "Ready";
      case val < 35:
        return "Poor";
      case val < 75:
        return "Average";
      case val <= 100:
        return "Great!";
      default:
        return "";
    }
  }

  //  RENDER

  const isLoading = isAccountDetailsLoading || isRecentTradeRecordsLoading;
  const intervalStyles =
    " text-center font-bold text-sm bg-opacity-15 border-r-4 py-2 ";

  if (isAccountDetailsError || isRecentTradeRecordsError) {
    logErrors(accountDetailsError, recentTradeRecordsError);
    return <Error />;
  }

  if (isLoading) {
    return <LoadingPage />;
  }

  return (
    <div className={"grid sm:grid-cols-1 lg:grid-cols-2 xl:grid-cols-4 gap-6"}>
      <div className={"sm:col-span-1 lg:col-span-2 xl:col-span-4"}>
        <div className={"flex gap-12 items-center"}>
          <div className={"flex-1"}>
            <SimpleBanner
              text={
                (account?.active ?? false)
                  ? "This account is currently active."
                  : "This account is inactive."
              }
              variant={(account?.active ?? false) ? "info" : "danger"}
            />
          </div>
          <div className={"flex gap-4 items-center justify-end"}>
            <div className={""}>
              <BaseModal
                key={0}
                title={"Import Trades"}
                description={
                  "Here you may manually import trades into the account for tracking."
                }
                trigger={
                  <Button className="" variant={"primary"}>
                    {resolveIcon(Icons.CirclePlus)}
                    &nbsp;Import Trades
                  </Button>
                }
                content={<ImportTradesForm account={account} />}
              />
            </div>
            <div className={""}>
              <BaseModal
                key={0}
                title={"Update Trading Account Information"}
                description={
                  "Here you can edit/update any account information. Note that some aspects of this account cannot be changed after account creation."
                }
                trigger={
                  <Button className="" variant={"outline"}>
                    {resolveIcon(Icons.Edit)}
                    &nbsp;Update
                  </Button>
                }
                content={
                  <AccountForm
                    portfolioNumber={account.portfolioNumber}
                    mode={"edit"}
                    account={account}
                  />
                }
              />
            </div>
            <div className={""}>
              <BaseModal
                key={1}
                title={"Delete Trading Account"}
                trigger={
                  <Button className="bg-primaryRed text-white hover:bg-primaryRedLight">
                    {resolveIcon(Icons.Trash)}
                    &nbsp;Delete
                  </Button>
                }
                content={<DeleteAccountForm account={account ?? null} />}
              />
            </div>
          </div>
        </div>
      </div>
      <div className={"xl:col-span-3"}>
        <div className={"col-span-1 lg:col-span-3"}>
          <div className={"grid col-span-1 gap-6"}>
            <div>
              <BaseCard
                title={"Account Equity"}
                subtitle={
                  "A look at the evolution of your account since inception."
                }
                cardContent={
                  (accountDetails?.equity?.length ?? 0) > 1 ? (
                    <AccountEquityChart
                      key={accountDetails?.equity?.length ?? 0}
                      data={accountDetails?.equity ?? []}
                      showPoints={showPoints}
                    />
                  ) : null
                }
                emptyText={
                  "Once you start taking some trades in this account, this chart will update."
                }
                headerControls={[
                  <div key={0} className="flex items-center space-x-2">
                    <Label htmlFor="airplane-mode">Show as Points</Label>
                    <Switch
                      id="airplane-mode"
                      checked={showPoints}
                      onCheckedChange={setShowPoints}
                      disabled={(accountDetails?.equity ?? []).length <= 1}
                    />
                  </div>,
                  <div key={1}>
                    <Link
                      href={`/transactions?account=${account?.accountNumber}`}
                    >
                      <Button variant={"outline"}>
                        {resolveIcon(Icons.ExternalLink, "", 18)}
                        &nbsp;Transactions
                      </Button>
                    </Link>
                  </div>,
                ]}
              />
            </div>
            <div>
              <BaseCard
                title={"Consistency"}
                subtitle={
                  "This calculation includes both sizing, RRR and general performance. A greater score indicates higher consistency."
                }
                cardContent={
                  <div
                    className={
                      "grid grid-cols-1 items-center justify-end gap-2 pb-6"
                    }
                  >
                    <div className={"flex items-center justify-end gap-2"}>
                      Consistency Score:{" "}
                      <span
                        className={
                          "font-bold text-" + computeConsistencyColor()
                        }
                      >
                        {computeConsistencyStatus()}&nbsp;&nbsp;(
                        {accountDetails?.consistency ?? 0}%)
                      </span>
                    </div>
                    <div>
                      <Progress
                        className={"h-6"}
                        value={accountDetails?.consistency ?? 0}
                        variant={computeConsistency()}
                      />
                    </div>
                    <div
                      className={"flex items-center justify-end w-full gap-1"}
                    >
                      <div
                        className={
                          intervalStyles +
                          " basis-[34%] bg-primaryRed border-primaryRed text-primaryRed"
                        }
                      >
                        &lt;&nbsp;35%
                      </div>
                      <div
                        className={
                          intervalStyles +
                          " basis-[40%] bg-primaryYellow border-primaryYellow text-primaryYellow"
                        }
                      >
                        35-75%
                      </div>
                      <div
                        className={
                          intervalStyles +
                          " basis-[26%] bg-primaryGreen border-primaryGreen text-primaryGreen"
                        }
                      >
                        &gt;&nbsp;75%
                      </div>
                    </div>
                  </div>
                }
              />
            </div>
          </div>
        </div>
      </div>
      <div className={""}>
        <BaseCard
          title={"Account Information"}
          cardContent={<AccountInformation account={account} />}
        />
      </div>
      <div className={"sm:col-span-1 lg:col-span-2 xl:col-span-4"}>
        {accountDetails?.insights ? (
          <BaseCard
            title={"Insights"}
            subtitle={
              "A quick look at some of the key markers of this account's performance."
            }
            cardContent={<AccountInsights insights={accountDetails.insights} />}
          />
        ) : null}
      </div>
      <div className={"xl:col-span-2"}>
        {accountDetails?.statistics ? (
          <BaseCard
            title={"Statistics"}
            subtitle={
              "A look some of this account's key statistical measures for performance."
            }
            cardContent={
              <AccountStatistics statistics={accountDetails.statistics} />
            }
          />
        ) : null}
      </div>
      <div className={"xl:col-span-2 flex justify-end"}>
        <BaseCard
          title={"Performance"}
          subtitle={`Reviewing the last ${tradeRecordReportLookBack} days of daily trading performances.`}
          cardContent={
            recentTradeRecords?.tradeRecords?.length ? (
              <TradeRecordTable report={recentTradeRecords} showTotals={true} />
            ) : null
          }
          emptyText={
            "Once you start trading, your history & performance will update here."
          }
          headerControls={[
            <Link
              key={0}
              href={`/performance?account=${account?.accountNumber}`}
            >
              <Button className="" variant={"outline"}>
                {resolveIcon(Icons.ExternalLink, "", 18)}
                &nbsp;View Full Performance
              </Button>
            </Link>,
          ]}
        />
      </div>
      <div className={"sm:col-span-1 lg:col-span-2 xl:col-span-4"}>
        <BaseCard
          title={"Trades"}
          subtitle={"A view of some recent trades taken in this account."}
          headerControls={[
            <Link key={0} href={`/trades?account=${account?.accountNumber}`}>
              <Button className="" variant={"outline"}>
                {resolveIcon(Icons.ExternalLink, "", 18)}
                &nbsp;View All Trades
              </Button>
            </Link>,
          ]}
          cardContent={
            <TradeTable
              account={account}
              filters={{
                start: moment().subtract(1, "weeks").toDate(),
                end: moment().add(1, "days").toDate(),
                sort: "desc",
                type: "ALL",
                symbol: "ALL",
              }}
            />
          }
        />
      </div>
    </div>
  );
}
