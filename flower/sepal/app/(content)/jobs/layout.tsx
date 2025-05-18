import React from "react";
import type { Metadata } from "next";

export const metadata: Metadata = {
  title: "Jobs - bluebell",
  description: "View a list of system jobs",
};

/**
 * The base layout for the job listing page
 *
 * @param children react content
 * @author Stephen Prizio
 * @version 0.2.1
 */
export default function JobsLayout({
  children,
}: Readonly<{ children: React.ReactNode }>) {
  //  RENDER

  return children;
}
