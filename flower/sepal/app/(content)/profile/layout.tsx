import React from "react";
import type { Metadata } from "next";

export const metadata: Metadata = {
  title: "My Profile - bluebell",
  description: "View your profile information",
};

/**
 * The base layout for the profile page
 *
 * @param children Content
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function ProfileLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  //  RENDER

  return children;
}
