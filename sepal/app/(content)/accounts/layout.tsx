import React from "react";
import type {Metadata} from "next";

export const metadata: Metadata = {
  title: "Accounts - bluebell",
  description: "View a list of your accounts",
};

/**
 * The base layout for the accounts listing page
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