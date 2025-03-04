'use client'

import BreadcrumbItem from "@/components/Navigation/Breadcrumb/BreadcrumbItem";
import React from "react";
import {AppLink} from "@/types/uiTypes";

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