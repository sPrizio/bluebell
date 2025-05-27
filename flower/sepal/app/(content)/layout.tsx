import React from "react";
import { ContentLayout } from "@/components/ui/admin-panel/content-layout";
import AdminPanelLayout from "@/components/ui/admin-panel/admin-panel-layout";
import { Toaster } from "@/components/ui/toaster";

/**
 * Generic layout for Content pages
 *
 * @param children Content
 * @author Stephen Prizio
 * @version 0.2.0
 */
export default function ContentPageLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <AdminPanelLayout>
      <ContentLayout title={""}>
        <div>{children}</div>
      </ContentLayout>
      <Toaster />
    </AdminPanelLayout>
  );
}
