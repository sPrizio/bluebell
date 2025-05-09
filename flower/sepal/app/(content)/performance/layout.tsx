import React from "react";
import type { Metadata } from "next";

export const metadata: Metadata = {
  title: "Performance - bluebell",
  description: "View your account's performance",
};

/**
 * The base layout for the performance page
 *
 * @param children Content
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function AccountsLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  //  RENDER

  return children;
}
