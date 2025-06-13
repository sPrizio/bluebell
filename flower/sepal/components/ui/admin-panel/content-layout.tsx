import { Navbar } from "@/components/ui/admin-panel/navbar";
import React from "react";

interface ContentLayoutProps {
  title: string;
  children: React.ReactNode;
}

/**
 * The base content layout
 *
 * @param title title of page
 * @param children content
 * @author Stephen Prizio
 * @version 0.2.5
 */
export function ContentLayout({
  title,
  children,
}: Readonly<ContentLayoutProps>) {
  return (
    <div>
      <div className={"hidden max-lg:block"}>
        <Navbar title={title} />
      </div>
      <div className="container pt-8 pb-8 px-4 sm:px-8">{children}</div>
    </div>
  );
}
