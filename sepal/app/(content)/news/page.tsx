'use client'

import {useSepalPageInfoContext} from "@/lib/context/SepalContext";
import {useEffect, useState} from "react";
import {Icons} from "@/lib/enums";
import {BaseCard} from "@/components/Card/BaseCard";
import {Button} from "@/components/ui/button";
import {delay} from "@/lib/functions";
import {Loader2} from "lucide-react";
import {marketNews} from "@/lib/sample-data";
import NewsTable from "@/components/Table/News/NewsTable";

/**
 * Renders the market News page
 *
 * @author Stephen Prizio
 * @version 0.0.1
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
  const [news, setNews] = useState<Array<MarketNews>>(marketNews)

  useEffect(() => {
    setPageTitle('Market News')
    setPageSubtitle(`A look at your local market news.`)
    setPageIconCode(Icons.MarketNews)
    setBreadcrumbs([
      {label: 'Dashboard', href: '/dashboard', active: false},
      {label: 'Market News', href: '/market-News', active: true},
    ])
  }, [])

  /**
   * Fetches the market news
   */
  async function getMarketNews() {

    setIsLoading(true)

    //  TODO: temp
    await delay(3000)
    setNews(marketNews)

    setIsLoading(false)
  }


  //  RENDER

  return (
    <div className={''}>
      <BaseCard
        title={'Market News'}
        subtitle={'A look at your local market News.'}
        headerControls={[
          isLoading ?
            <Button variant={'outline'} disabled={isLoading}>
              {isLoading ? <Loader2 className="mr-2 h-4 w-4 animate-spin"/> : null}
              Update
            </Button>
            :
            <Button key={0} onClick={getMarketNews} variant={"outline"}>Update</Button>
        ]}
        cardContent={<NewsTable news={news} />}
      />
    </div>
  )
}