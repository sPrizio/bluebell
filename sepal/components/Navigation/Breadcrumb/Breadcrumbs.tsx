'use client'

import {usePathname} from "next/navigation";
import BreadcrumbItem from "@/components/Navigation/Breadcrumb/BreadcrumbItem";

/**
 * Renders the breadcrumbs for any page
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function Breadcrumbs() {


  //  GENERAL FUNCTIONS

  function getCurrentPage() {
    const list = getPageList();
    return beautifyWord(list[list.length - 1]);
  }

  function getPageList() {
    const list = usePathname().split('/');
    list.unshift('Home');
    return list.filter(el => el.length > 0).map(url => beautifyWord(url))
  }

  function beautifyWord(word: string) {
    const list = word.replace('-', ' ').trim().split(' ')
    return list.map(word => word.charAt(0).toUpperCase() + word.slice(1)).join(' ')
  }


  //  RENDER

  return (
    <div>
      {getPageList().map((page, i, pages) => {
        return (
          <BreadcrumbItem key={page} label={page} isLast={(i + 1) === pages.length} active={page === getCurrentPage()} />
        )
      })}
    </div>
  )
}