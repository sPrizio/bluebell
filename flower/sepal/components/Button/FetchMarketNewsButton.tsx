import {useState} from "react";
import { Button } from "../ui/button";
import {Loader2} from "lucide-react";
import {fetchNews} from "@/lib/functions/news-functions";

/**
 * Button that will manually fetch market news
 *
 * @author Stephen Prizio
 * @version 0.2.2
 */
export default function FetchMarketNewsButton() {

    const [isLoading, setIsLoading] = useState(false)


    //  GENERAL FUNCTIONS

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

    //  disable this button when fetching market news is disallowed
    if (process.env.ALLOW_FETCHING_MARKET_NEWS !== 'true') {
        return null;
    }

    return (
        isLoading ?
            <Button variant={'outline'} disabled={isLoading}>
                {isLoading ? <Loader2 className="mr-2 h-4 w-4 animate-spin"/> : null}
                Update
            </Button>
            :
            <Button key={0} onClick={fetch} variant={"outline"}>Update</Button>
    )
}