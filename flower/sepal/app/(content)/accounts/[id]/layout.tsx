import React from "react";
import type {Metadata} from "next";

export const metadata: Metadata = {
  title: "Account Overview - bluebell",
  description: "View your Account details",
};

/**
 * The base layout for the Account detail page
 *
 * @param children Content
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function AccountsLayout(
  {
    children,
  }: Readonly<{
    children: React.ReactNode;
  }>
) {


  //  RENDER

  return children
}