import React from "react";
import type {Metadata} from "next";

export const metadata: Metadata = {
  title: "Dashboard - bluebell",
  description: "View all of your account information in one convenient place",
};

/**
 * The root layout for the dashboard page
 *
 * @param children content
 * @author Stephen Prizio
 * @version 0.0.1
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