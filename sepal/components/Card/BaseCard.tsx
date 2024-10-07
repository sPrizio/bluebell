import {Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle,} from "@/components/ui/card"

/**
 * Base card component
 *
 * @param title card title
 * @param subtitle card subtitle
 * @param cardContent card content
 * @param headerControl card header button
 * @param footerControls card footer buttons
 * @author Stephen Prizio
 * @version 0.0.1
 */
export function BaseCard(
  {
    title = '',
    subtitle = '',
    cardContent = null,
    headerControl = null,
    footerControls = [],
  }
    : Readonly<{
    title: string,
    subtitle?: string,
    cardContent: React.ReactNode,
    headerControl?: React.ReactNode,
    footerControls?: Array<React.ReactNode>
  }>
) {


  //  RENDER

  return (
    <Card className={'w-full'}>
      <CardHeader className={'pb-4'}>
        <div className={"flex flex-row gap-4 items-start w-full"}>
          <div className={"flex-1"}>
            <CardTitle>{title}</CardTitle>
            {subtitle && subtitle.length > 0 ? <CardDescription>{subtitle}</CardDescription> : null}
          </div>
          {headerControl ? <div>{headerControl}</div> : null}
        </div>
      </CardHeader>
      <CardContent className={'pb-4'}>
        {cardContent}
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
  )
}
