import React from "react";
import type {Metadata} from "next";

export const metadata: Metadata = {
  title: "Market News - bluebell",
  description: "View your local market News",
};

/**
 * The base layout for the market News page
 *
 * @param children Content
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function MarketNewsLayout(
  {
    children,
  }: Readonly<{
    children: React.ReactNode;
  }>
) {


  //  RENDER

  return children
}