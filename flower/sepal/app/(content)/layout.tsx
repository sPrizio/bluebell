'use client'

import React, {useEffect, useState} from "react";
import {SepalPageInfoContext} from "@/lib/context/SepalContext";
import PageHeaderSection from "@/components/Section/PageHeaderSection";
import {ContentLayout} from "@/components/ui/admin-panel/content-layout";
import {AppLink} from "@/types/uiTypes";
import AdminPanelLayout from "@/components/ui/admin-panel/admin-panel-layout";
import {Toaster} from "@/components/ui/toaster";
import {getUser} from "@/lib/functions/account-functions";
import {User} from "@/types/apiTypes";
import {PageInfoProvider} from "@/lib/context/PageInfoProvider";

/**
 * Generic layout for Content pages
 *
 * @param children Content
 * @author Stephen Prizio
 * @version 0.2.0
 */
export default function ContentPageLayout(
  {
    children,
  }: Readonly<{
    children: React.ReactNode;
  }>
) {

  const [isLoading, setIsLoading] = useState(false)
  const [pageTitle, setPageTitle] = useState('')
  const [pageSubtitle, setPageSubtitle] = useState('')
  const [pageIconCode, setPageIconCode] = useState('')
  const [breadcrumbs, setBreadcrumbs] = useState<Array<AppLink>>([])
  const [user, setUser] = useState<User | null>(null)

  useEffect(() => {
    getUserInfo()
  }, []);

  async function getUserInfo() {

    setIsLoading(true)
    //const user = await getUser('s.prizio')
    setUser(user)
    setIsLoading(false)
  }


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
            {/*<PageHeaderSection
              title={pageTitle}
              subtitle={pageSubtitle}
              iconCode={pageIconCode}
              breadcrumbs={breadcrumbs}
            />*/}
            <div className={''}>
              {/*{isLoading ? <p>Loading</p> : children}*/} {/* TODO: turn this into a global page loader component */}
              {children} {/* TODO: turn this into a global page loader component */}
            </div>
          </div>
        </ContentLayout>
        <Toaster/>
      </AdminPanelLayout>
    </SepalPageInfoContext.Provider>
  )
}