import type {Metadata} from "next";
import "./globals.css";
import SidebarNavigation from "@/components/Navigation/SidebarNavigation";
import {Inter as FontSans} from "next/font/google"
import {cn} from "@/lib/utils";
import React from "react";
import {resolveIcon} from "@/lib/services";
import {Icons} from "@/lib/enums";
import Footer from "@/components/Footer/Footer";

const fontSans = FontSans({
  subsets: ["latin"],
  variable: "--font-sans",
})

export const metadata: Metadata = {
  title: "bluebell",
  description: "bluebell Wealth Management is a software system designed to help track a trader's progress as they navigate the wonderfully tumultuous environment that is the stock market. " +
    "The system will aim to track trades through each day, offer basic insights and helpful news as well as a place to manage trading accounts. Additionally, bluebell offers basic " +
    "charting capabilities for back-testing and custom strategies that leverage real-world data to establish a successful trading system that can be used to execute mechanical systems trades",
};

const linkStyles = "h-6 w-6 flex-shrink-0";
const links = [
  {
    label: 'Dashboard',
    href: "/dashboard",
    icon: resolveIcon(Icons.Dashboard, linkStyles)
  },
  {
    label: "Account Overview",
    href: "/account",
    icon: resolveIcon(Icons.AccountOverview, linkStyles),
  },
  {
    label: "My Profile",
    href: "/profile",
    icon: resolveIcon(Icons.UserProfile, linkStyles),
  },
  {
    label: "Market News",
    href: "/market-news",
    icon: resolveIcon(Icons.MarketNews, linkStyles),
  },
  {
    label: "Performance",
    href: "/performance",
    icon: resolveIcon(Icons.Performance, linkStyles),
  },
  {
    label: "Logout",
    href: "/logout",
    icon: resolveIcon(Icons.Logout, linkStyles),
  },
];

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
      <body className={cn("min-h-screen font-sans antialiased text-slate-600 bg-neutral-100 tracking-tighter", fontSans.variable)}>
      <div className={"flex flex-row"}>
        <div className={""}>
          <SidebarNavigation variant={'primary'} links={links}/>
        </div>
        <div className={"flex-1 flex flex-col items-center justify-start"}>
          <div className={"w-5/6 2xl:w-4/5 max-w-[1440px] min-h-5/6 py-8"}>
            {children}
          </div>
          <div className={"w-full mt-auto"}>
            <Footer />
          </div>
        </div>
      </div>
      </body>
    </html>
  );
}
