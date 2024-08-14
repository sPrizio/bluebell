import type { Metadata } from "next";
import "./globals.css";
import { cn } from "@/lib/utils"
import React from "react";
import { Inter as FontSans } from "next/font/google"
import 'animate.css'

const fontSans = FontSans({
  subsets: ["latin"],
  variable: "--font-sans",
})

export const metadata: Metadata = {
  title: "bluebell - Finances and planning made simple.",
  description: "Welcome to bluebell! bluebell aims to help you build a robust, achievable and digestible financial plan that will help you reach your goals faster and focus on living your life.",
};

/**
 * The root layout, Next.js default config
 *
 * @param children react content
 * @author Stephen Prizio
 */
export default function RootLayout({children,}: Readonly<{ children: React.ReactNode; }>) {
  return (
    <html lang="en">
      <body className={cn("min-h-screen bg-background font-sans antialiased text-slate-600 tracking-tighter", fontSans.variable)}>
        {children}
      </body>
    </html>
  );
}
