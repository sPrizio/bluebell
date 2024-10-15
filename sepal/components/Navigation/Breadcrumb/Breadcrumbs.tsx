'use client'

import {usePathname} from "next/navigation";
import BreadcrumbItem from "@/components/Navigation/Breadcrumb/BreadcrumbItem";
import React from "react";

/**
 * Renders the breadcrumbs for any page
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function Breadcrumbs(
  {
    links = [],
  }: Readonly<{
    links: Array<AppLink>;
  }>
) {


  //  GENERAL FUNCTIONS

  /**
   * Gets the current page
   */
  function getCurrentPage() {
    const list = getPageList();
    return beautifyWord(list[list.length - 1]);
  }

  /**
   * Gets the list of pages
   */
  function getPageList() {
    const list = usePathname().split('/');
    list.unshift('Dashboard');
    return [...new Set(list.filter(el => el.length > 0).map(url => beautifyWord(url)))]
  }

  /**
   * Makes the words pretty and formatted
   *
   * @param word item
   */
  function beautifyWord(word: string) {
    const list = word.replace('-', ' ').trim().split(' ')
    return list.map(word => word.charAt(0).toUpperCase() + word.slice(1)).join(' ')
  }


  //  RENDER

  return (
    <div>
      {
        links.map((link, i, links) => {
          return (
            <BreadcrumbItem
              key={link.href}
              label={link.label}
              isLast={(i + 1) === links.length}
              active={link.active}
              href={link.href}
            />
          )
        })
      }
    </div>
  )
}