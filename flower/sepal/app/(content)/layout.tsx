"use client";

import React, { useEffect } from "react";
import { ContentLayout } from "@/components/ui/admin-panel/content-layout";
import AdminPanelLayout from "@/components/ui/admin-panel/admin-panel-layout";
import { Toaster } from "@/components/ui/toaster";
import { useSessionQuery } from "@/lib/hooks/query/queries";
import { logErrors } from "@/lib/functions/util-functions";
import ErrorPage from "@/app/error";
import LoadingPage from "@/app/loading";
import { SessionContext } from "@/lib/context/SessionContext";
import { useRouter } from "next/navigation";
import { AUTH_ENABLED } from "@/lib/constants";

/**
 * Generic layout for Content pages
 *
 * @param children Content
 * @author Stephen Prizio
 * @version 0.2.6
 */
export default function ContentPageLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  const router = useRouter();
  const {
    data: session,
    isLoading: isSessionLoading,
    isError: isSessionError,
    error: sessionError,
  } = useSessionQuery();

  useEffect(() => {
    if (AUTH_ENABLED && !isSessionLoading && !(session?.isLoggedIn ?? false)) {
      router.replace("/login");
    }
  }, [router, session]);

  if (isSessionError) {
    logErrors(sessionError);
    return <ErrorPage />;
  }

  if (isSessionLoading) {
    return <LoadingPage />;
  }

  return (
    <SessionContext.Provider value={session}>
      <AdminPanelLayout>
        <ContentLayout title={""}>
          <div>{children}</div>
        </ContentLayout>
        <Toaster />
      </AdminPanelLayout>
    </SessionContext.Provider>
  );
}
