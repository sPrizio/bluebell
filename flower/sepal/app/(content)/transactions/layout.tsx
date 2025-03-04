import React from "react";
import type {Metadata} from "next";

export const metadata: Metadata = {
  title: "Transactions - bluebell",
  description: "View a list of your transactions per Account",
};

/**
 * The base layout for the transactions listing page
 *
 * @param children Content
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function TransactionsLayout(
  {
    children,
  }: Readonly<{
    children: React.ReactNode;
  }>
) {


  //  RENDER

  return children
}