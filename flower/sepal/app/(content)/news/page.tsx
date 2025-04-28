'use client'

import {useSepalPageInfoContext} from "@/lib/context/SepalContext";
import {useEffect, useState} from "react";
import {Icons} from "@/lib/enums";
import {BaseCard} from "@/components/Card/BaseCard";
import NewsTable from "@/components/Table/News/NewsTable";
import {getNews} from "@/lib/functions/news-functions";
import moment from "moment";
import {DateTime} from "@/lib/constants";
import {MarketNews} from "@/types/apiTypes";
import FetchMarketNewsButton from "@/components/Button/FetchMarketNewsButton";

/**
 * Renders the market News page
 *
 * @author Stephen Prizio
 * @version 0.2.2
 */
export default function MarketNewsPage() {

  const {
    pageTitle,
    pageSubtitle,
    pageIconCode,
    breadcrumbs,
    user,
    setPageTitle,
    setPageSubtitle,
    setPageIconCode,
    setBreadcrumbs,
    setUser
  } = useSepalPageInfoContext()

  const [isLoading, setIsLoading] = useState(false)
  const [news, setNews] = useState<Array<MarketNews>>()

  useEffect(() => {
    setPageTitle('Market News')
    setPageSubtitle(`A look at your local market news.`)
    setPageIconCode(Icons.MarketNews)
    setBreadcrumbs([
      {label: 'Dashboard', href: '/dashboard', active: false},
      {label: 'Market News', href: '/market-News', active: true},
    ])

    getMarketNews()
  }, [])

  /**
   * Fetches the market news for the current week
   */
  async function getMarketNews() {

    setIsLoading(true)

    const data = await getNews(moment().startOf('week').add(1, 'days').format(DateTime.ISODateFormat), moment().startOf('week').add(6, 'days').format(DateTime.ISODateFormat))
    if (data && data.length > 0) {
      setNews(data)
    }

    setIsLoading(false)
  }


  //  RENDER

  return (
    <div className={''}>
      <BaseCard
        title={'Market News'}
        subtitle={'A look at your local market News.'}
        cardContent={<NewsTable news={news} />}
        headerControls={[<FetchMarketNewsButton key={0} />]}
      />
    </div>
  )
}