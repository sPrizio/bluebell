"use client";

import React, { useMemo } from "react";
import { ContentLayout } from "@/components/ui/admin-panel/content-layout";
import AdminPanelLayout from "@/components/ui/admin-panel/admin-panel-layout";
import { Toaster } from "@/components/ui/toaster";
import { useSessionQuery } from "@/lib/hooks/query/queries";
import { logErrors } from "@/lib/functions/util-functions";
import Error from "@/app/error";
import LoadingPage from "@/app/loading";
import { SessionContext } from "@/lib/context/SessionContext";
import { redirect } from "next/navigation";

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
  const {
    data: session,
    isLoading: isSessionLoading,
    isError: isSessionError,
    error: sessionError,
  } = useSessionQuery();

  const needsAuth = useMemo(() => {
    return process.env.AUTH_ENABLED === "true";
  }, []);

  if (isSessionError) {
    logErrors(sessionError);
    return <Error />;
  }

  if (isSessionLoading) {
    return <LoadingPage />;
  }

  if (needsAuth && !(session?.isLoggedIn ?? false)) {
    redirect("/login");
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
