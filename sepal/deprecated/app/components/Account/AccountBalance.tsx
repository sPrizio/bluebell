import React from "react";
import styles from './AccountBalance.module.scss'
import {formatNumberForDisplay} from "@/app/services/data/dataIntegrityService";
import {HiOutlineTrendingUp} from "react-icons/hi";

/**
 * Component that shows an account's balance
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
function AccountBalance() {

  const baseClass = "account-balance"


  //  RENDER

  return (
    <div className={styles[baseClass]}>
      <div className={styles[`${baseClass}__column`]}>
        <div className={styles[`${baseClass}__account-info`]}>
          Account 1234
        </div>
        <div className={styles[`${baseClass}__account-value`]}>
          ${formatNumberForDisplay(10359.37)}<small>CAD</small>
        </div>
        <div className={styles[`${baseClass}__balance-info`]}>
          <div className={styles[`${baseClass}__delta`] + ' ' + styles[`${baseClass}__delta--positive`]}>
            <div className={styles[`${baseClass}__delta-symbol`]}><HiOutlineTrendingUp/></div>
            {/*<div className={styles[`${baseClass}__delta-symbol`]}><HiOutlineTrendingDown/></div>*/}
            &nbsp;&nbsp;5.09%
          </div>
          <div className={styles[`${baseClass}__timeline`]}>
            This month
          </div>
        </div>
      </div>
      <div className={styles[`${baseClass}__break`]}/>
      <div className={styles[`${baseClass}__column`] + ' ' + styles[`${baseClass}__column--align-right`]}>
        <div className={styles[`${baseClass}__money`]}>
          <div>
            <small>Deposits</small>
          </div>
          <div>
            ${formatNumberForDisplay(25000.00)}
          </div>
        </div>
        <div className={styles[`${baseClass}__money`]}>
          <div>
            <small>Withdrawals</small>
          </div>
          <div>
            ${formatNumberForDisplay(5000.00)}
          </div>
        </div>
        <div className={styles[`${baseClass}__money`]}>
          <div>
            <small>Gain/Loss</small>
          </div>
          <div>
            ${formatNumberForDisplay(359.37)}
          </div>
        </div>
      </div>
    </div>
  )
}

export default AccountBalance;