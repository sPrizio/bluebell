import React from "react";
import styles from './TradeHistory.module.scss'
import TradeHistorySummary from "@/app/components/Trade/History/TradeHistorySummary";
import TradeHistoryChart from "@/app/components/Chart/Trade/TradeHistoryChart";
import {TradeRecord} from "@/app/types/apiTypes";

/**
 * The Trade history component, used viewing Trade records (summaries of Trade sessions)
 *
 * @param tradeRecord Trade Record
 * @author Stephen Prizio
 * @version 0.0.1
 */
function TradeHistory({tradeRecord = null}: Readonly<{ tradeRecord: TradeRecord | null }>) {

  const baseClass = "Trade-history"


  //  RENDER

  return (
    <div className={styles[baseClass]}>
      <div className={styles[`${baseClass}__item`] + ' ' + styles[`${baseClass}__chart`]}>
        <TradeHistoryChart />
      </div>
      <div className={styles[`${baseClass}__break`]}/>
      <div className={styles[`${baseClass}__item`] + ' ' + styles[`${baseClass}__summary`]}>
        <TradeHistorySummary tradeRecord={tradeRecord} />
      </div>
    </div>
  )
}

export default TradeHistory;