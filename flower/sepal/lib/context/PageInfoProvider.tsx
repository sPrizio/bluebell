'use client'

import React, {createContext, useContext} from 'react';
import {Icons} from '@/lib/enums';
import PageHeaderSection from "@/components/Section/PageHeaderSection";

type PageInfo = {
  title: string;
  subtitle: string;
  iconCode: Icons;
  breadcrumbs: Array<{ label: string; href: string; active: boolean }>;
};

const PageInfoContext = createContext<PageInfo | null>(null);

export function usePageInfo() {
  const context = useContext(PageInfoContext);
  if (!context) throw new Error('usePageInfo must be used inside PageInfoProvider');
  return context;
}

export function PageInfoProvider({children, value,}: Readonly<{
  children: React.ReactNode; value: PageInfo;
}>) {
  return (
    <PageInfoContext.Provider value={value}>
      <PageHeaderSection
        title={value.title}
        subtitle={value.subtitle}
        iconCode={value.iconCode}
        breadcrumbs={value.breadcrumbs}
      />
      {children}
    </PageInfoContext.Provider>
  );
}