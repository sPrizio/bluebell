import React from "react";
import type { Metadata } from "next";

export const metadata: Metadata = {
  title: "Trades - bluebell",
  description: "View your account's trading history",
};

/**
 * The base layout for the Trade history page
 *
 * @param children Content
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function TradesLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  //  RENDER

  return children;
}
