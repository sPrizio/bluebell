'use client'

import React, {ReactNode} from "react";
import Link from "next/link";
import {cn} from "@/lib/utils";
import {motion} from "framer-motion";

/**
 * A navigation item used in the sidebar
 *
 * @param active is link active
 * @param open is open
 * @param animate should animate
 * @param label display text
 * @param href link location
 * @param icon icon
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function SidebarNavigationLink(
  {
    active = false,
    open = false,
    animate = false,
    label = 'Link',
    href = '#',
    icon = null,

  }
    : Readonly<{
    active?: boolean,
    open?: boolean
    animate?: boolean,
    label: string,
    href: string,
    icon: ReactNode,
  }>
) {


  //  RENDER

  return (
    <Link
      href={href}
      className={cn("flex items-center " + (open ? '' : 'justify-center') + " gap-2  group/sidebar py-2")}
    >
      <div className={active ? 'text-primary' : 'text-neutral-700'}>{icon}</div>

      <motion.span
        animate={{
          /*display: animate ? (open ? "inline-block" : "none") : "inline-block",*/
          display: animate ? (open ? "w-full" : "w-0") : "w-full",
          opacity: animate ? (open ? 1 : 0) : 1,
        }}
        className={(active ? ' text-primary font-bold' : " text-neutral-700") + " text-sm w-0 group-hover/sidebar:translate-x-1 transition duration-150 whitespace-pre inline-block !p-0 !m-0"}>
        {label}
      </motion.span>
    </Link>
  );
}