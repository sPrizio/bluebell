'use client'

import styles from './layout.module.scss'
import ClientNavBar from "@/app/components/Navigation/Navbar/ClientNavBar";
import Footer from "@/app/components/Footer/Footer";
import React from "react";
import NavBar from "@/app/components/Navigation/Navbar/NavBar";
import {usePathname} from "next/navigation";

/**
 * The default layout for content pages
 *
 * @param children react components
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function ContentLayout({children}: { children: React.ReactNode }) {

  const baseClass : string = "content-page";
  const pathname : string = usePathname();


  //  RENDER

  return (
    <div className={styles[baseClass]}>
      <ClientNavBar variant={'tertiary'}/>
      {pathname && pathname.includes('home') ? null : <NavBar variant={"white"} />}
      <div className={styles[`${baseClass}__content`]}>
        {children}
      </div>
      <Footer/>
    </div>
  )
}