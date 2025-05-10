"use client";

import { selectNewAccount } from "@/lib/functions/util-functions";
import React, { useState } from "react";
import { Icons } from "@/lib/enums";
import { BaseCard } from "@/components/Card/BaseCard";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import TimeBucketAnalysis from "@/components/Analysis/TimeBucketAnalysis";
import {
  FilterSelector,
  TradeDurationFilterSelector,
  Weekday,
} from "@/types/apiTypes";
import WeekdayAnalysis from "@/components/Analysis/WeekdayAnalysis";
import WeekdayTimeBucketAnalysis from "@/components/Analysis/WeekdayTimeBucketAnalysis";
import TradeDurationAnalysis from "@/components/Analysis/TradeDurationAnalysis";
import { PageInfoProvider } from "@/lib/context/PageInfoProvider";
import { useActiveAccount } from "@/lib/hooks/api/useActiveAccount";
import { useRouter, useSearchParams } from "next/navigation";
import ReusableSelect from "@/components/Input/ReusableSelect";
import { validatePageQueryFlow } from "@/lib/functions/util-component-functions";

/**
 * The page that shows an analysis of an account's performance
 *
 * @author Stephen Prizio
 * @version 0.2.0
 */
export default function AnalysisPage() {
  const searchParams = useSearchParams();
  const router = useRouter();
  const {
    isLoading,
    isError,
    error,
    activePortfolio,
    activeAccount,
    hasMismatch,
  } = useActiveAccount();
  const [openTbType, setOpenTbType] = useState<FilterSelector>("PROFIT");
  const [closedTbType, setClosedTbType] = useState<FilterSelector>("PROFIT");
  const [wdType, setWdType] = useState<FilterSelector>("PROFIT");
  const [tbWdType, setTbWdType] = useState<FilterSelector>("PROFIT");
  const [tdType, setTdType] = useState<FilterSelector>("PROFIT");
  const [weekday, setWeekday] = useState<Weekday>("MONDAY");
  const [tdFilter, setTdFilter] = useState<TradeDurationFilterSelector>("ALL");

  validatePageQueryFlow(
    isLoading,
    isError,
    activePortfolio,
    hasMismatch,
    error,
  );

  const accNumber = activeAccount?.accountNumber ?? -1;
  const pageInfo = {
    title: "Analysis",
    subtitle: `A more in-depth look at ${activeAccount?.name ?? ""}'s performance.`,
    iconCode: Icons.Analysis,
    breadcrumbs: [
      { label: "Dashboard", href: "/dashboard", active: false },
      { label: "Accounts", href: "/accounts", active: false },
      {
        label: activeAccount?.name ?? "",
        href: "/accounts/" + accNumber,
        active: false,
      },
      { label: "Analysis", href: "/analysis?account=default", active: true },
    ],
  };

  //  COMPONENTS

  /**
   * Renders a re-usable select for selecting chart types
   *
   * @param value state variable
   * @param callBack setState function
   */
  const select = (value: string, callBack: React.Dispatch<FilterSelector>) => {
    return (
      <div>
        <Select
          value={value}
          onValueChange={(val: FilterSelector) => callBack(val)}
        >
          <SelectTrigger className="w-[120px] bg-white">
            <SelectValue placeholder={"Select a value..."} />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value={"PROFIT"}>Net Profit</SelectItem>
            <SelectItem value={"POINTS"}>Points</SelectItem>
            <SelectItem value={"PERCENTAGE"}>Win %</SelectItem>
          </SelectContent>
        </Select>
      </div>
    );
  };

  //  RENDER

  return (
    <PageInfoProvider value={pageInfo}>
      <div className={""}>
        {
          <div className={"grid grid-cols-2 justify-center gap-8"}>
            <div className={"col-span-2"}>
              <div className={"flex items-center justify-end gap-4"}>
                <div>
                  <ReusableSelect
                    title={"Account"}
                    initialValue={accNumber.toString()}
                    options={
                      activePortfolio?.accounts
                        ?.filter((acc) => acc.active)
                        ?.map((a) => {
                          return {
                            label: a.name,
                            value: a.accountNumber.toString(),
                          };
                        }) ?? []
                    }
                    handler={(val: string) => {
                      selectNewAccount(router, searchParams, parseInt(val));
                    }}
                  />
                </div>
              </div>
            </div>
            <div>
              <BaseCard
                title={"Time Buckets (Opened)"}
                subtitle={
                  "How your trades performed for each segment of the day when opened."
                }
                headerControls={[select(openTbType, setOpenTbType)]}
                cardContent={
                  <TimeBucketAnalysis
                    accountNumber={accNumber}
                    filter={openTbType}
                    opened={true}
                  />
                }
              />
            </div>
            <div>
              <BaseCard
                title={"Time Buckets (Closed)"}
                subtitle={
                  "How your trades performed for each segment of the day when closed."
                }
                headerControls={[select(closedTbType, setClosedTbType)]}
                cardContent={
                  <TimeBucketAnalysis
                    accountNumber={accNumber}
                    filter={closedTbType}
                  />
                }
              />
            </div>
            <div>
              <BaseCard
                title={"Weekday Performance"}
                subtitle={"How your trades performed for each day of the week."}
                headerControls={[select(wdType, setWdType)]}
                cardContent={
                  <WeekdayAnalysis accountNumber={accNumber} filter={wdType} />
                }
              />
            </div>
            <div>
              <BaseCard
                title={"Weekday & Time Bucket Performance"}
                subtitle={
                  "How your trades performed at different times on a specific weekday."
                }
                headerControls={[
                  <div key={0}>
                    <Select
                      value={weekday}
                      onValueChange={(val) => setWeekday(val as Weekday)}
                    >
                      <SelectTrigger className="w-[120px] bg-white">
                        <SelectValue placeholder={"Select a value..."} />
                      </SelectTrigger>
                      <SelectContent>
                        <SelectItem value={"MONDAY"}>Monday</SelectItem>
                        <SelectItem value={"TUESDAY"}>Tuesday</SelectItem>
                        <SelectItem value={"WEDNESDAY"}>Wednesday</SelectItem>
                        <SelectItem value={"THURSDAY"}>Thursday</SelectItem>
                        <SelectItem value={"FRIDAY"}>Friday</SelectItem>
                      </SelectContent>
                    </Select>
                  </div>,
                  select(tbWdType, setTbWdType),
                ]}
                cardContent={
                  <WeekdayTimeBucketAnalysis
                    accountNumber={accNumber}
                    weekday={weekday}
                    filter={wdType}
                  />
                }
              />
            </div>
            <div>
              <BaseCard
                title={"Trade Duration Performance"}
                subtitle={
                  "How your trades performed for various lengths of time."
                }
                headerControls={[
                  <div key={0}>
                    <Select
                      value={tdFilter}
                      onValueChange={(val) =>
                        setTdFilter(val as TradeDurationFilterSelector)
                      }
                    >
                      <SelectTrigger className="w-[120px] bg-white">
                        <SelectValue placeholder={"Select a value..."} />
                      </SelectTrigger>
                      <SelectContent>
                        <SelectItem value={"ALL"}>All Trades</SelectItem>
                        <SelectItem value={"WINS"}>Wins Only</SelectItem>
                        <SelectItem value={"LOSSES"}>Losses Only</SelectItem>
                      </SelectContent>
                    </Select>
                  </div>,
                  select(tdType, setTdType),
                ]}
                cardContent={
                  <TradeDurationAnalysis
                    accountNumber={accNumber}
                    filter={tdType}
                    tdFilter={tdFilter}
                  />
                }
              />
            </div>
            <div>
              Add average count and change color of bar if the count is above
              average/std
              <br />
            </div>
          </div>
        }
      </div>
    </PageInfoProvider>
  );
}
