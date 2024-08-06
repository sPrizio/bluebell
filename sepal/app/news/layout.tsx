'use client'

import React, {useState} from "react";
import styles from './layout.module.scss'
import PageHeaderSection from "@/app/components/Section/Header/PageHeaderSection";
import {FaNewspaper} from "react-icons/fa";
import SimpleButton from "@/app/components/Button/SimpleButton";
import {AiOutlineImport} from "react-icons/ai";
import {CoreConstants} from "@/app/constants";
import {getAuthHeader} from "@/app/services/configuration/configurationService";

/**
 * The default layout for the market news page
 *
 * @param children react components
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function MarketNewsLayout({children}: { children: React.ReactNode }) {

  const baseClass = "news-layout"
  const [isLoading, setIsLoading] = useState(false)

  //  TODO TEMP: only show these controls if an admin is logged in
  const controls = [
    <SimpleButton
      key={0}
      loading={isLoading}
      variant={"primary"}
      text={'Fetch News'}
      icon={<AiOutlineImport/>}
      iconPosition={"right"}
      handler={fetchNews}
      plain={true}
    />
  ]


  //  GENERAL FUNCTIONS

  /**
   * Calls the fetch news endpoint to trigger flowerpot to obtain up-to-date news
   */
  async function fetchNews() {

    setIsLoading(true)

    try {
      const res = await fetch(CoreConstants.ApiUrls.News.Fetch, {method: 'POST', headers: getAuthHeader()})
      if (res.ok) {
        const data = await res.json()
        if (data.success) {
          window.location.reload()
        }
      }
    } catch (e) {
      console.log(e)
    }

    setIsLoading(false)
  }


  //  RENDER

  return (
    <div className={styles[baseClass]}>
      <PageHeaderSection title={'Market News'} controls={controls} icon={<FaNewspaper/>}/>
      <div>{children}</div>
    </div>
  )
}