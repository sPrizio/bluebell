import {Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle,} from "@/components/ui/card"
import {Skeleton} from "@/components/ui/skeleton";

/**
 * Base card component
 *
 * @param title card title
 * @param subtitle card subtitle
 * @param cardContent card content
 * @param headerControl card header button
 * @param footerControls card footer buttons
 * @param loading loading flag on the card
 * @author Stephen Prizio
 * @version 0.0.1
 */
export function BaseCard(
  {
    title = '',
    subtitle = '',
    cardContent = null,
    headerControls = [],
    footerControls = [],
    icon = null,
    loading = false,
  }
    : Readonly<{
    title?: string,
    subtitle?: string,
    cardContent?: React.ReactNode,
    headerControls?: Array<React.ReactNode>,
    footerControls?: Array<React.ReactNode>
    icon?: React.ReactNode,
    loading?: boolean,
  }>
) {


  //  RENDER

  return (
    <>
      {
        loading ?
          <div className="flex flex-col space-y-3">
            <Skeleton className="h-[175px] w-[350px] rounded-xl"/>
            <div className="space-y-2">
              <Skeleton className="h-4 w-[250px]"/>
              <Skeleton className="h-4 w-[200px]"/>
            </div>
          </div>
          :
          <Card className={'w-full'}>
            {
              title && title.length > 0 ?
                <CardHeader className={'pb-2'}>
                  <div className={"flex flex-row gap-4 items-start w-full"}>
                    <div className={"flex-1"}>
                      <CardTitle>{title}</CardTitle>
                      {subtitle && subtitle.length > 0 ? <CardDescription>{subtitle}</CardDescription> : null}
                    </div>
                    {
                      headerControls?.map((item, key) => {
                        return (
                          <div key={key}>{item}</div>
                        )
                      })
                    }
                    {
                      icon ? <div className={'text-primary'}>{icon}</div> : null
                    }
                  </div>
                </CardHeader>
                : <div className={'p-6'}/>
            }
            <CardContent className={'pb-2'}>
              {cardContent || <p className={'text-slate-400 text-center'}>No data present.</p>}
            </CardContent>
            {
              footerControls && footerControls.length > 0 ?
                <CardFooter>
                  <div className={"flex flex-row gap-4 items-center w-full"}>
                    {
                      footerControls && footerControls.length > 0 && footerControls.map((item, itx) => {
                        return (
                          <div key={itx} className={"grow"}>{item}</div>
                        )
                      })
                    }
                  </div>
                </CardFooter> : null
            }
          </Card>
      }

    </>
  )
}
