'use client'

import React, {useState} from "react";
import {SepalPageInfoContext} from "@/lib/context/SepalContext";
import PageHeaderSection from "@/components/Section/PageHeaderSection";
import {ScrollArea} from "@/components/ui/scroll-area";

/**
 * Generic layout for content pages
 *
 * @param children content
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function ContentPageLayout(
  {
    children,
  }: Readonly<{
    children: React.ReactNode;
  }>
) {

  const [pageTitle, setPageTitle] = useState('')
  const [pageSubtitle, setPageSubtitle] = useState('')
  const [pageIconCode, setPageIconCode] = useState('')
  const [breadcrumbs, setBreadcrumbs] = useState<Array<AppLink>>([])


  //  RENDER

  return (
    <SepalPageInfoContext.Provider value={{ pageTitle, pageSubtitle, pageIconCode, breadcrumbs, setPageTitle, setPageSubtitle, setPageIconCode, setBreadcrumbs }}>
      <div className={"block"}>
        <PageHeaderSection
          title={pageTitle}
          subtitle={pageSubtitle}
          iconCode={pageIconCode}
          breadcrumbs={breadcrumbs}
        />
        <ScrollArea className={`block h-[77vh] w-full [mask-image:linear-gradient(#000_85%,transparent_95%)]`}>
          {children}
          <div className={'h-[8vh]'} />
        </ScrollArea>
      </div>
    </SepalPageInfoContext.Provider>
  )
}