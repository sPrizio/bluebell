import React from "react";
import type {Metadata} from "next";
import { Icons } from "@/lib/enums";
import {PageInfoProvider} from "@/lib/context/PageInfoProvider";
import PageHeaderSection from "@/components/Section/PageHeaderSection";

export const metadata: Metadata = {
  title: "Dashboard - bluebell",
  description: "View all of your Account information in one convenient place",
};

/**
 * The root layout for the dashboard page
 *
 * @param children Content
 * @author Stephen Prizio
 * @version 0.2.0
 */
export default function DashboardLayout(
  {
    children,
  }: Readonly<{
    children: React.ReactNode;
  }>
) {

  const pageInfo = {
    title: "Dashboard",
    subtitle: "An overview of your trading portfolios",
    iconCode: Icons.Dashboard,
    breadcrumbs: [
      {label: 'Dashboard', href: '/dashboard', active: true},
    ]
  }


  //  RENDER

  return (
    <PageInfoProvider value={pageInfo}>
      <PageHeaderSection
        title={pageInfo.title}
        subtitle={pageInfo.subtitle}
        iconCode={pageInfo.iconCode}
        breadcrumbs={pageInfo.breadcrumbs}
      />
      {children}
    </PageInfoProvider>
  )
}