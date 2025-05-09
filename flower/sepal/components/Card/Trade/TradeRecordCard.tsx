"use client";

import TradeRecordChart from "@/components/Chart/Trade/TradeRecordChart";
import { BaseCard } from "@/components/Card/BaseCard";
import moment from "moment";
import { DateTime } from "@/lib/constants";
import { Label } from "@/components/ui/label";
import { Switch } from "@/components/ui/switch";
import { useState } from "react";
import {
  formatNegativePoints,
  formatNumberForDisplay,
} from "@/lib/functions/util-functions";
import { IconCircleCheckFilled, IconXboxXFilled } from "@tabler/icons-react";
import { TradeRecord } from "@/types/apiTypes";

/**
 * Renders a card for displaying Trade records
 *
 * @param tradeRecord Trade record
 * @param aggInterval aggregated interval
 * @author Stephen Prizio
 * @version 0.2.0
 */
export default function TradeRecordCard({
  tradeRecord,
  aggInterval,
}: Readonly<{
  tradeRecord: TradeRecord;
  aggInterval: string;
}>) {
  const iconSize = 16;
  const [showPoints, setShowPoints] = useState(false);

  //  GENERAL FUNCTIONS

  /**
   * Formats the card date based on the aggregate interval
   *
   * @param val input date
   */
  function formatDate(val: string) {
    switch (aggInterval) {
      case "DAILY":
        return moment(val).format(DateTime.ISOMonthWeekDayFormat);
      case "MONTHLY":
        return moment(val).format(DateTime.ISOMonthYearFormat);
      case "YEARLY":
        return moment(val).format(DateTime.ISOYearFormat);
      default:
        return val;
    }
  }

  //  RENDER

  /**
   * Returns a simple cell for data with no bg
   *
   * @param val data to render
   * @param className custom css classes
   */
  const simpleCellNoBg = (val: string, className = "") => {
    return <div className={`py-1 px-2 text-sm ${className}`}>{val}</div>;
  };

  /**
   * Returns a simple cell for data
   *
   * @param val data to render
   * @param className custom css classes
   */
  const simpleCellWithBg = (val: string, className = "") => {
    return (
      <div className={`py-1 px-2 bg-primary bg-opacity-10 ${className}`}>
        {val}
      </div>
    );
  };

  return (
    <BaseCard
      title={formatDate(tradeRecord.start)}
      cardContent={
        <div
          className={"grid grid-cols-1 lg:grid-cols-5 gap-8 items-center mb-4"}
        >
          <div className={"lg:col-span-2"}>
            <TradeRecordChart
              data={tradeRecord.equityPoints}
              showAsPoints={showPoints}
            />
          </div>
          <div className={"lg:col-span-3"}>
            <div className={"grid grid-cols-3 gap-4 text-sm"}>
              <div>
                <div className={"grid grid-cols-3 items-center gap-1"}>
                  <div className={"col-span-3 bg-primary bg-opacity-10 p-2"}>
                    <Label className={"font-semibold"}>Trading</Label>
                  </div>
                  <div>
                    {tradeRecord.trades}
                    <small>&nbsp;trades</small>
                  </div>
                  <div className={"col-span-2"}>
                    <div className={"flex flex-col gap-1"}>
                      <div
                        className={
                          "flex items-center bg-primary bg-opacity-10 py-1 px-2 justify-end"
                        }
                      >
                        <div>{tradeRecord.wins}</div>
                        <div
                          className={
                            "ml-2 inline-block text-primaryGreen text-right"
                          }
                        >
                          <IconCircleCheckFilled size={iconSize} />
                        </div>
                      </div>
                      <div
                        className={
                          "flex items-center bg-primary bg-opacity-10 py-1 px-2 justify-end"
                        }
                      >
                        <div>{tradeRecord.losses}</div>
                        <div className={"ml-2 inline-block text-primaryRed"}>
                          <IconXboxXFilled size={iconSize} />
                        </div>
                      </div>
                    </div>
                  </div>
                  <div>
                    {formatNegativePoints(tradeRecord.points)}
                    <br />
                    <small>&nbsp;points</small>
                  </div>
                  <div className={"col-span-2"}>
                    <div className={"flex flex-col gap-1"}>
                      <div
                        className={
                          "flex items-center bg-primary bg-opacity-10 py-1 px-2 justify-end"
                        }
                      >
                        <div>
                          {formatNumberForDisplay(tradeRecord.pointsGained)}
                        </div>
                        <div className={"ml-2 inline-block text-primaryGreen"}>
                          <IconCircleCheckFilled size={iconSize} />
                        </div>
                      </div>
                      <div
                        className={
                          "flex items-center bg-primary bg-opacity-10 py-1 px-2 justify-end"
                        }
                      >
                        <div>
                          {formatNumberForDisplay(
                            Math.abs(tradeRecord.pointsLost),
                          )}
                        </div>
                        <div className={"ml-2 inline-block text-primaryRed"}>
                          <IconXboxXFilled size={iconSize} />
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              <div className={""}>
                <div className={"grid grid-cols-2 items-center gap-1"}>
                  <div className={"col-span-2 bg-primary bg-opacity-10 p-2"}>
                    <Label className={"font-semibold"}>Statistics</Label>
                  </div>
                  <div className={""}>Average</div>
                  <div className={""}>
                    <div className={"flex flex-col gap-1"}>
                      <div
                        className={
                          "flex items-center bg-primary bg-opacity-10 py-1 px-2 justify-end"
                        }
                      >
                        <div>
                          $&nbsp;
                          {formatNumberForDisplay(tradeRecord.winAverage)}
                        </div>
                        <div
                          className={
                            "ml-2 inline-block text-primaryGreen text-right"
                          }
                        >
                          <IconCircleCheckFilled size={iconSize} />
                        </div>
                      </div>
                      <div
                        className={
                          "flex items-center bg-primary bg-opacity-10 py-1 px-2 justify-end"
                        }
                      >
                        <div>$&nbsp;{tradeRecord.lossAverage}</div>
                        <div className={"ml-2 inline-block text-primaryRed"}>
                          <IconXboxXFilled size={iconSize} />
                        </div>
                      </div>
                    </div>
                  </div>
                  <div className={""}>Largest</div>
                  <div className={""}>
                    <div className={"flex flex-col gap-1"}>
                      <div
                        className={
                          "flex items-center bg-primary bg-opacity-10 py-1 px-2 justify-end"
                        }
                      >
                        <div>
                          $&nbsp;
                          {formatNumberForDisplay(tradeRecord.largestWin)}
                        </div>
                        <div className={"ml-2 inline-block text-primaryGreen"}>
                          <IconCircleCheckFilled size={iconSize} />
                        </div>
                      </div>
                      <div
                        className={
                          "flex items-center bg-primary bg-opacity-10 py-1 px-2 justify-end"
                        }
                      >
                        <div>
                          $&nbsp;
                          {formatNumberForDisplay(
                            Math.abs(tradeRecord.largestLoss),
                          )}
                        </div>
                        <div className={"ml-2 inline-block text-primaryRed"}>
                          <IconXboxXFilled size={iconSize} />
                        </div>
                      </div>
                    </div>
                  </div>
                  <div className={""}>Drawdown</div>
                  <div className={""}>
                    <div className={"flex flex-col gap-1"}>
                      <div
                        className={
                          "flex items-center bg-primary bg-opacity-10 py-1 px-2 justify-end"
                        }
                      >
                        <div>
                          $&nbsp;
                          {formatNumberForDisplay(tradeRecord.lowestPoint)}
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div className={""}>
                <div className={"grid grid-cols-2 items-center gap-1"}>
                  <div className={"col-span-2 bg-primary bg-opacity-10 p-2"}>
                    <Label className={"font-semibold"}>Results</Label>
                  </div>
                  {simpleCellNoBg("P & L")}
                  {simpleCellWithBg(
                    "$ " + formatNumberForDisplay(tradeRecord.netProfit),
                    "text-right",
                  )}
                  {simpleCellNoBg("Win%")}
                  {simpleCellWithBg(
                    tradeRecord.winPercentage + "%",
                    "text-right",
                  )}
                  {simpleCellNoBg("Profitability")}
                  {simpleCellWithBg(
                    tradeRecord?.profitability.toString() ?? "",
                    "text-right",
                  )}
                  {simpleCellNoBg("Retention")}
                  {simpleCellWithBg(tradeRecord.retention + "%", "text-right")}
                </div>
              </div>
            </div>
          </div>
        </div>
      }
      headerControls={[
        <div key={0} className="flex items-center space-x-2">
          <Label htmlFor="airplane-mode">Show as Points</Label>
          <Switch
            id="airplane-mode"
            checked={showPoints}
            onCheckedChange={setShowPoints}
          />
        </div>,
      ]}
    />
  );
}
