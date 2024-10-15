import React from "react";
import type {Metadata} from "next";

export const metadata: Metadata = {
  title: "Account Overview - bluebell",
  description: "View your account details",
};

/**
 * The base layout for the account detail page
 *
 * @param children content
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