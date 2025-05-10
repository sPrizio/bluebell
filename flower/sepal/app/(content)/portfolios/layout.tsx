import React from "react";
import type { Metadata } from "next";

export const metadata: Metadata = {
  title: "Portfolios - bluebell",
  description: "View a list of your portfolios",
};

/**
 * The base layout for the portfolios listing page
 *
 * @param children react content
 * @author Stephen Prizio
 * @version 0.2.0
 */
export default function PortfoliosLayout({
  children,
}: Readonly<{ children: React.ReactNode }>) {
  //  RENDER

  return children;
}
