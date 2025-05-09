import React from "react";
import type {Metadata} from "next";

export const metadata: Metadata = {
  title: "Dashboard - bluebell",
  description: "View all of your Account information for a specific portfolio in one convenient place",
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


  //  RENDER

  return children
}