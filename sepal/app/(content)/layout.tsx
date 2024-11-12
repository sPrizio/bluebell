'use client'

import React, {useState} from "react";
import {SepalPageInfoContext} from "@/lib/context/SepalContext";
import PageHeaderSection from "@/components/Section/PageHeaderSection";
import {ContentLayout} from "@/components/ui/admin-panel/content-layout";
import {sampleUser} from "@/lib/sample-data";
import {AppLink} from "@/types/uiTypes";
import AdminPanelLayout from "@/components/ui/admin-panel/admin-panel-layout";
import {Toaster} from "@/components/ui/toaster";

/**
 * Generic layout for Content pages
 *
 * @param children Content
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
      <AdminPanelLayout>
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
        <Toaster />
      </AdminPanelLayout>
    </SepalPageInfoContext.Provider>
  )
}