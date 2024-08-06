'use client'

import PageHeaderSection from "@/app/components/Section/Header/PageHeaderSection";
import React, {useState} from "react";
import styles from './layout.module.scss'
import {resolveIcon} from "@/app/services/resolver/iconResolverService";
import SimpleButton from "@/app/components/Button/SimpleButton";
import {TbPackageImport} from "react-icons/tb";
import TradeImportModal from "@/app/components/Modal/trade/TradeImportModal";

/**
 * The default layout for the trades page
 *
 * @param children react components
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function TradesLayout({children}: {children: React.ReactNode}) {

  const baseClass = "trades-layout"

  const [importModalActive, setImportModalActive] = useState(false)


  //  GENERAL FUNCTIONS

  /**
   * Toggles the import trades modal active/inactive
   */
  function toggleTradeImportModal() {
    setImportModalActive(true)
  }


  //  RENDER

  return (
    <div className={styles[baseClass]}>
      <PageHeaderSection
        icon={resolveIcon('CgArrowsExchange')}
        title={'Trades'}
        controls={
          [
            <SimpleButton
              key={0}
              variant={"primary"}
              text={'Import Trades'}
              icon={<TbPackageImport/>}
              iconPosition={"right"}
              handler={toggleTradeImportModal}
              plain={true}
            />
          ]
        }
      />
      <div>
        {children}
        <TradeImportModal active={importModalActive} closeHandler={() => setImportModalActive(false)}/>
      </div>
    </div>
  )
}