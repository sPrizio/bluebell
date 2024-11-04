import React from "react";
import styles from './TradeLogEntry.module.scss';
import {TradeRecord} from "@/app/types/apiTypes";
import {formatNegativePoints, formatNumberForDisplay} from "@/app/services/data/dataIntegrityService";
import moment from "moment";
import {CoreConstants} from "@/app/constants";

/**
 * Individual Trade log entry using a TradeRecord
 *
 * @param tradeRecord Trade record
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function TradeLogEntry({ tradeRecord }: {tradeRecord: TradeRecord}) {

  const baseClass = "Trade-log-entry"


  //  RENDER

  return (
    <div className={styles[baseClass]}>
      <div className={styles[`${baseClass}__column`]}>
        <span className={styles[`${baseClass}__date`]}>
          {moment(tradeRecord.start).format(CoreConstants.DateTime.ISOMonthDayFormat)}
        </span>
      </div>
      <div className={styles[`${baseClass}__column`] + ' ' + styles[`${baseClass}__column--centered`]}>
        {tradeRecord.trades}
      </div>
      <div className={styles[`${baseClass}__column`] + ' ' + styles[`${baseClass}__column--centered`]}>
        {formatNumberForDisplay(tradeRecord.netProfit)}
      </div>
      <div className={styles[`${baseClass}__column`] + ' ' + styles[`${baseClass}__column--centered`]}>
        {formatNegativePoints(tradeRecord.points)}
      </div>
    </div>
  )
}