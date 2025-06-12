import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Skeleton } from "@/components/ui/skeleton";
import React from "react";
import SepalLoader from "@/components/Svg/SepalLoader";

/**
 * Base card component
 *
 * @param title card title
 * @param subtitle card subtitle
 * @param cardContent card Content
 * @param headerControl card header button
 * @param footerControls card footer buttons
 * @param loading loading flag on the card
 * @param emptyText empty text to display when the card is rendered without content
 * @param includeSkeleton if true, show a skeleton instead of a loader
 * @author Stephen Prizio
 * @version 0.2.4
 */
export function BaseCard({
  title = "",
  subtitle = "",
  cardContent = null,
  headerControls = [],
  footerControls = [],
  icon = null,
  loading = false,
  emptyText = "No data present.",
  includeSkeleton = true,
}: Readonly<{
  title?: string;
  subtitle?: string;
  cardContent?: React.ReactNode;
  headerControls?: Array<React.ReactNode>;
  footerControls?: Array<React.ReactNode>;
  icon?: React.ReactNode;
  loading?: boolean;
  emptyText?: string;
  includeSkeleton?: boolean;
}>) {
  //  RENDER

  return (
    <>
      {loading && includeSkeleton && (
        <div className="flex flex-col space-y-3">
          <Skeleton className="h-[175px] w-1/2 rounded-xl" />
          <div className="space-y-2">
            <Skeleton className="h-4 w-2/5" />
            <Skeleton className="h-4 w-1/5" />
          </div>
        </div>
      )}
      {loading && !includeSkeleton && (
        <Card className={"w-full"}>
          <CardContent className="p-6">
            <div
              className={
                "sm:h-[175px] md:h-[300px] flex items-center justify-center"
              }
            >
              <SepalLoader />
            </div>
          </CardContent>
        </Card>
      )}
      {!loading && (
        <Card className={"w-full"}>
          {title && title.length > 0 ? (
            <CardHeader className={"pb-2"}>
              <div className={"flex flex-row gap-4 items-start w-full"}>
                <div className={"flex-1"}>
                  <CardTitle>{title}</CardTitle>
                  {subtitle && subtitle.length > 0 ? (
                    <CardDescription>{subtitle}</CardDescription>
                  ) : null}
                </div>
                <div className={"flex items-center gap-4"}>
                  {headerControls?.map((item, key) => {
                    return (
                      <div key={key + 1} className={"flex items-center"}>
                        {item}
                      </div>
                    );
                  })}
                  {icon ? <div className={"text-primary"}>{icon}</div> : null}
                </div>
              </div>
            </CardHeader>
          ) : (
            <div className={"p-6"} />
          )}
          <CardContent className={"pb-2"}>
            {cardContent || (
              <div className="text-center text-slate-500 mt-2 mb-6 text-sm">
                {emptyText}
              </div>
            )}
          </CardContent>
          {footerControls && footerControls.length > 0 ? (
            <CardFooter>
              <div className={"flex flex-row gap-4 items-center w-full"}>
                {footerControls &&
                  footerControls.length > 0 &&
                  footerControls.map((item, itx) => {
                    return (
                      <div key={itx + 1} className={"grow"}>
                        {item}
                      </div>
                    );
                  })}
              </div>
            </CardFooter>
          ) : null}
        </Card>
      )}
    </>
  );
}
