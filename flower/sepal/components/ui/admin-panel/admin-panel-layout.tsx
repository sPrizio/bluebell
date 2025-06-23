"use client";

import { SepalSidebar } from "@/components/ui/admin-panel/sepalSidebar";
import { useSidebar } from "@/lib/hooks/ui/use-sidebar";
import { useStore } from "@/lib/hooks/ui/use-store";
import { cn } from "@/lib/utils";
import Footer from "@/components/Footer/Footer";
import React from "react";

/**
 * Layout for the admin panel view (default sepal view)
 *
 * @param children react content
 * @author Stephen Prizio
 * @version 0.2.5
 */
export default function AdminPanelLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  const sidebar = useStore(useSidebar, (x) => x);
  if (!sidebar) return null;
  const { getOpenState, settings } = sidebar;

  //  RENDER

  return (
    <>
      <SepalSidebar />
      <main
        className={cn(
          "min-h-[calc(95vh_-_56px)] bg-[#f6f8fb] transition-[margin-left] ease-in-out duration-300",
          !settings.disabled && (!getOpenState() ? "lg:ml-[90px]" : "lg:ml-72"),
        )}
      >
        {children}
      </main>
      <footer
        className={cn(
          "transition-[margin-left] ease-in-out duration-300",
          !settings.disabled && (!getOpenState() ? "lg:ml-[90px]" : "lg:ml-72"),
        )}
      >
        <Footer />
      </footer>
    </>
  );
}
