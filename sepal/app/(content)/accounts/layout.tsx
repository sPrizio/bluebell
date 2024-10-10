import React from "react";
import type {Metadata} from "next";

export const metadata: Metadata = {
  title: "Accounts - bluebell",
  description: "View a list of your accounts",
};

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