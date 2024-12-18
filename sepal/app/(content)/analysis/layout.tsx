import React from "react";
import type {Metadata} from "next";

export const metadata: Metadata = {
  title: "Analysis - bluebell",
  description: "Analyze various aspects of you account's performance",
};

/**
 * The base layout for the analysis page
 *
 * @param children Content
 * @author Stephen Prizio
 * @version 0.0.2
 */
export default function AnalysisLayout(
  {
    children,
  }: Readonly<{
    children: React.ReactNode;
  }>
) {


  //  RENDER

  return children
}