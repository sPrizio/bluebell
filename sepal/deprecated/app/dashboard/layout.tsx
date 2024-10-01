import {MdDashboard} from "react-icons/md";
import PageHeaderSection from "@/app/components/Section/Header/PageHeaderSection";
import React from "react";
import styles from './layout.module.scss'

/**
 * The default layout for the dashboard page
 *
 * @param children react components
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function DashboardLayout({children}: {children: React.ReactNode}) {

  const baseClass = "dashboard-layout"


  //  RENDER

  return (
    <div className={styles[baseClass]}>
      <PageHeaderSection
        title={'Hello, Test User'}
        controls={[]}
        icon={<MdDashboard/>}
      />
      <div>
        {children}
      </div>
    </div>
  )
}