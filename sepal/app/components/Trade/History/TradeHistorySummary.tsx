import React from "react";
import styles from './TradeHistorySummary.module.scss'
import {formatNegativePoints, formatNumberForDisplay} from "@/app/services/data/dataIntegrityService";
import {TradeRecord} from "@/app/types/apiTypes";

/**
 * Renders a summary table for trades within a time span
 *
 * @param tradeRecord Trade Record
 * @author Stephen Prizio
 * @version 0.0.1
 */
function TradeHistorySummary({tradeRecord = null}: Readonly<{ tradeRecord: TradeRecord | null }>) {

  const baseClass = "trade-history-summary"


  //  RENDER

  return (
    <div className={styles[baseClass]}>
      <div className={styles[`${baseClass}__column`]}>
        <div className={styles[`${baseClass}__column`] + ' ' + styles[`${baseClass}__column--no-margin`]}>
          <div className={styles[`${baseClass}__header`]}>
            <span className={styles[`${baseClass}__underlined-content`]}>Profit</span><br/>
            <small>Lowest Point</small>
          </div>
        </div>
        <div className={styles[`${baseClass}__column`] + ' ' + styles[`${baseClass}__column--no-margin`]}>
          <div className={styles[`${baseClass}__content`]}>
            {formatNumberForDisplay(tradeRecord?.netProfit ?? 0)}<br/>
            <small>{formatNumberForDisplay(tradeRecord?.lowestPoint ?? 0)}</small>
          </div>
        </div>
        <div className={styles[`${baseClass}__break`] + ' ' + styles[`${baseClass}__break--active`]}/>
        <div className={styles[`${baseClass}__column`] + ' ' + styles[`${baseClass}__column--no-margin`]}>
          <div className={styles[`${baseClass}__header`]}>
            <span className={styles[`${baseClass}__underlined-content`]}>Points</span><br/>
            <small>Gained</small><br/>
            <small>Lost</small>
          </div>
        </div>
        <div className={styles[`${baseClass}__column`] + ' ' + styles[`${baseClass}__column--no-margin`]}>
          <div className={styles[`${baseClass}__content`]}>
            {formatNegativePoints(tradeRecord?.points ?? 0)}<br/>
            <small>{formatNumberForDisplay(tradeRecord?.pointsGained ?? 0)}</small><br/>
            <small>{formatNegativePoints((tradeRecord?.pointsLost ?? 0) * -1)}</small>
          </div>
        </div>
      </div>
      <div className={styles[`${baseClass}__break`]}/>
      <div className={styles[`${baseClass}__column`]}>
        <div className={styles[`${baseClass}__column`] + ' ' + styles[`${baseClass}__column--no-margin`]}>
          <div className={styles[`${baseClass}__header`]}>
            <span className={styles[`${baseClass}__underlined-content`]}>Largest Win</span><br/>
            <small>Average</small>
          </div>
        </div>
        <div className={styles[`${baseClass}__column`] + ' ' + styles[`${baseClass}__column--no-margin`]}>
          <div className={styles[`${baseClass}__content`]}>
            {formatNumberForDisplay(tradeRecord?.largestWin ?? 0)}<br/>
            <small>{formatNumberForDisplay(tradeRecord?.winAverage ?? 0)}</small>
          </div>
        </div>
        <div className={styles[`${baseClass}__break`] + ' ' + styles[`${baseClass}__break--active`]}/>
        <div className={styles[`${baseClass}__column`] + ' ' + styles[`${baseClass}__column--no-margin`]}>
          <div className={styles[`${baseClass}__header`]}>
            <span className={styles[`${baseClass}__underlined-content`]}>Biggest Loss</span><br/>
            <small>Average</small>
          </div>
        </div>
        <div className={styles[`${baseClass}__column`] + ' ' + styles[`${baseClass}__column--no-margin`]}>
          <div className={styles[`${baseClass}__content`]}>
            {formatNumberForDisplay(tradeRecord?.largestLoss ?? 0)}<br/>
            <small>{formatNumberForDisplay(tradeRecord?.lossAverage ?? 0)}</small>
          </div>
        </div>
      </div>
      <div className={styles[`${baseClass}__break`]}/>
      <div className={styles[`${baseClass}__column`]}>
        <div className={styles[`${baseClass}__column`] + ' ' + styles[`${baseClass}__column--no-margin`]}>
          <div className={styles[`${baseClass}__header`]}>
            <span className={styles[`${baseClass}__underlined-content`]}>Win %</span><br/>
            <small>9 wins</small>
          </div>
        </div>
        <div className={styles[`${baseClass}__column`] + ' ' + styles[`${baseClass}__column--no-margin`]}>
          <div className={styles[`${baseClass}__content`]}>
            {tradeRecord?.winPercentage ?? 0}%<br/>
            <small>{tradeRecord?.trades ?? 0} trades</small>
          </div>
        </div>
        <div className={styles[`${baseClass}__break`] + ' ' + styles[`${baseClass}__break--active`]}/>
        <div className={styles[`${baseClass}__column`] + ' ' + styles[`${baseClass}__column--no-margin`]}>
          <div className={styles[`${baseClass}__header`]}>
            <span className={styles[`${baseClass}__underlined-content`]}>Profitability</span><br/>
            <small>Retention</small>
          </div>
        </div>
        <div className={styles[`${baseClass}__column`] + ' ' + styles[`${baseClass}__column--no-margin`]}>
          <div className={styles[`${baseClass}__content`]}>
            {formatNegativePoints(tradeRecord?.profitability ?? 0)}<br/>
            <small>{tradeRecord?.retention ?? 0}%</small>
          </div>
        </div>
      </div>
    </div>
  )
}

export default TradeHistorySummary;