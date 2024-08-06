import PageHeaderSection from "@/app/components/Section/Header/PageHeaderSection";
import React from "react";
import styles from './layout.module.scss'
import {FaRegChartBar} from "react-icons/fa";

/**
 * The default layout for the chart page
 *
 * @param children react components
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function ChartLayout({children}: {children: React.ReactNode}) {

  const baseClass = "chart-layout"


  //  RENDER

  return (
    <div className={styles[baseClass]}>
      <PageHeaderSection
        title={'Charting'}
        subtitle={'Review the price action for the NDX as far back as 2008'}
        controls={[]}
        icon={<FaRegChartBar/>}
      />
      <div>
        {children}
      </div>
    </div>
  )
}