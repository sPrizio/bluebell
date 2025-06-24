"use client";

import React, { createContext, useContext } from "react";
import { Icons } from "@/lib/enums";
import PageHeaderSection from "@/components/Section/PageHeaderSection";
import { useSessionContext } from "@/lib/context/SessionContext";
import { logErrors } from "@/lib/functions/util-functions";
import UnauthorizedPage from "@/app/unauthorized";

type PageInfo = {
  title: string;
  subtitle: string;
  iconCode: Icons;
  privilege: string;
  breadcrumbs: Array<{ label: string; href: string; active: boolean }>;
  backCTA?: {
    label: string;
    href: string;
  };
};

const PageInfoContext = createContext<PageInfo | null>(null);

export function usePageInfo() {
  const context = useContext(PageInfoContext);
  if (!context)
    throw new Error("usePageInfo must be used inside PageInfoProvider");
  return context;
}

/**
 * Provider for page information
 *
 * @param children content
 * @param value page attributes
 * @author Stephen Prizio
 * @version 0.2.6
 */
export function PageInfoProvider({
  children,
  value,
}: Readonly<{
  children: React.ReactNode;
  value: PageInfo;
}>) {
  const session = useSessionContext();
  if (!(session?.roles ?? []).includes(value.privilege)) {
    logErrors(
      `User ${session?.username} does not contain the required privilege ${value.privilege} to view this page`,
    );
    return <UnauthorizedPage />;
  }

  return (
    <PageInfoContext.Provider value={value}>
      <PageHeaderSection
        title={value.title}
        subtitle={value.subtitle}
        iconCode={value.iconCode}
        breadcrumbs={value.breadcrumbs}
        backCTA={value.backCTA}
      />
      {children}
    </PageInfoContext.Provider>
  );
}
