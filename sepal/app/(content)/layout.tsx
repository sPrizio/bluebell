'use client'

import React, {useState} from "react";
import {SepalPageInfoContext} from "@/lib/context/SepalContext";
import PageHeaderSection from "@/components/Section/PageHeaderSection";
import {ScrollArea} from "@/components/ui/scroll-area";
import {ContentLayout} from "@/components/ui/admin-panel/content-layout";
import {sampleUser} from "@/lib/sample-data";

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
  const [user, setUser] = useState<User>(sampleUser)


  //  RENDER

  return (
    <SepalPageInfoContext.Provider value={{
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
    }}>
      <ContentLayout title={''}>
        <div className={""}>
          <PageHeaderSection
            title={pageTitle}
            subtitle={pageSubtitle}
            iconCode={pageIconCode}
            breadcrumbs={breadcrumbs}
          />
          <div className={''}>
            {children}
          </div>
        </div>
      </ContentLayout>
    </SepalPageInfoContext.Provider>
  )
}