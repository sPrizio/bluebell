'use client'

import {useSepalPageInfoContext} from "@/lib/context/SepalContext";
import {useEffect, useState} from "react";
import {Icons} from "@/lib/enums";
import {BaseCard} from "@/components/Card/BaseCard";
import {Button} from "@/components/ui/button";
import {Loader2} from "lucide-react";
import NewsTable from "@/components/Table/News/NewsTable";
import {fetchNews, getNews} from "@/lib/functions/news-functions";
import moment from "moment";
import {DateTime} from "@/lib/constants";

/**
 * Renders the market News page
 *
 * @author Stephen Prizio
 * @version 0.0.2
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

    getTheNews()
  }, [])

  async function getTheNews() {

    setIsLoading(true)

    const data = await getNews(moment().startOf('week').add(1, 'days').format(DateTime.ISODateFormat), moment().startOf('week').add(6, 'days').format(DateTime.ISODateFormat))
    if (data && data.length > 0) {
      setNews(data)
    }

    setIsLoading(false)
  }

  /**
   * Fetches the market news
   */
  async function fetch() {

    setIsLoading(true)

    const data = await fetchNews()
    if (data) {
      window.location.reload()
    } else {
      console.log('Error fetching the news.')
    }

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
            <Button key={0} onClick={fetch} variant={"outline"}>Update</Button>
        ]}
        cardContent={<NewsTable news={news} />}
      />
    </div>
  )
}