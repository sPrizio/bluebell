import styles from './layout.module.scss'
import PageHeaderSection from "@/app/components/Section/Header/PageHeaderSection";
import React from "react";
import {GoHistory} from "react-icons/go";

/**
 * The default layout for the trading history page
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function TradingHistoryLayout({children}: { children: React.ReactNode }) {

  const baseClass = "trading-history-layout"


  //  RENDER

  return (
    <div className={styles[baseClass]}>
      <PageHeaderSection
        title={'Trading History'}
        controls={[]}
        icon={<GoHistory/>}
      />
      <div>
        {children}
      </div>
    </div>
  )
}