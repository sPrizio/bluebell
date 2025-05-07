import {useEffect, useState} from "react";
import {Button} from "../ui/button";
import {Loader2} from "lucide-react";
import {useFetchMarketNewsMutation} from "@/lib/hooks/query/mutations";
import {logErrors} from "@/lib/functions/util-functions";
import {useToast} from "@/lib/hooks/ui/use-toast";

/**
 * Button that will manually fetch market news
 *
 * @author Stephen Prizio
 * @version 0.2.0
 */
export default function FetchMarketNewsButton() {

  const {toast} = useToast();
  const [disabled, setDisabled] = useState(false)
  const {
    mutate: fetch,
    isPending: isFetching,
    isSuccess: isFetched,
    isError: isFetchError,
    error: fetchError
  } = useFetchMarketNewsMutation()

  useEffect(() => {
    if (isFetched) {
      window.location.reload();
    }
  }, [isFetched]);


  //  RENDER

  //  disable this button when fetching market news is disallowed
  if (process.env.ALLOW_FETCHING_MARKET_NEWS !== 'true') {
    return null;
  }

  if (isFetchError) {
    logErrors(fetchError)
    setDisabled(true)
    toast({
      title: 'Fetching News Failure',
      description: 'Market news could not be fetched. Please try again later.',
      variant: 'danger'
    })
  }

  return (
    isFetching ?
      <Button variant={'outline'} disabled={isFetching}>
        {isFetching ? <Loader2 className="mr-2 h-4 w-4 animate-spin"/> : null}
        Update
      </Button>
      :
      <Button key={0} onClick={fetch} variant={"outline"} disabled={disabled}>Update</Button>
  )
}