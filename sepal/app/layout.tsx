import type {Metadata} from "next";
import "./globals.css";
import {Inter as FontSans} from "next/font/google"
import {cn} from "@/lib/utils";
import React from "react";
import {Toaster} from "@/components/ui/toaster";
import AdminPanelLayout from "@/components/ui/admin-panel/admin-panel-layout";

const fontSans = FontSans({
  subsets: ["latin"],
  variable: "--font-sans",
})

export const metadata: Metadata = {
  title: "bluebell",
  description: "bluebell Wealth Management is a software system designed to help track a trader's progress as they navigate the wonderfully tumultuous environment that is the stock market. " +
    "The system will aim to track trades through each day, offer basic insights and helpful News as well as a place to manage trading accounts. Additionally, bluebell offers basic " +
    "charting capabilities for back-testing and custom strategies that leverage real-world data to establish a successful trading system that can be used to execute mechanical systems trades",
};

/**
 * The root layout design
 *
 * @param children page info
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {

  return (
    <html lang="en">
      <body className={cn("min-h-screen font-sans antialiased text-slate-600 bg-[#f6f8fb] tracking-tighter", fontSans.variable)}>
      <AdminPanelLayout>
        {children}
        <Toaster />
      </AdminPanelLayout>
      </body>
    </html>
  );
}
