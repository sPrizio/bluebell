'use client'

import React, {useState} from "react";
import styles from "./NavBar.module.scss";
import {usePathname} from "next/navigation";
import Link from "next/link";
import NavBarItem from "@/app/components/Navigation/navbar/NavBarItem";
import {RxHamburgerMenu} from "react-icons/rx";
import MainLogo from "@/app/components/Navigation/Logo/MainLogo";
import SimpleButton from "@/app/components/Button/SimpleButton";

/**
 * The top-page navigation component
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
function NavBar() {

  const baseClass = "nav-bar"

  const [currentTab, setCurrentTab] = useState(usePathname())
  const [userMenuActive, setUserMenuActive] = useState(false)


  //  GENERAL FUNCTIONS

  /**
   * Handles selecting a new tab on link clicks
   *
   * @param val new route
   */
  function handleClick(val: string) {
    setCurrentTab(val)
    setUserMenuActive(false)
  }


  //  RENDER

  return (
    <div className={styles[baseClass]}>
      <div className={styles[`${baseClass}__container`]}>
        <div className={styles[`${baseClass}__items`] + ' ' + styles[`${baseClass}__items--left`]}>
          <div className={styles[`${baseClass}__item`] + ' ' + styles[`${baseClass}__item--brand`]}>
            <Link href={'/dashboard'} onClick={() => handleClick('/dashboard')}>
              <MainLogo/>
            </Link>
          </div>
        </div>
        <div className={styles[`${baseClass}__items`] + ' ' + styles[`${baseClass}__items--right`]}>
          <div className={styles[`${baseClass}__item`]}>
            <NavBarItem route={'/planning'} label={'Planning'} active={'/planning' === currentTab}
                        handler={handleClick}/>
          </div>
          <div className={styles[`${baseClass}__item`]}>
            <NavBarItem route={'/management'} label={'Management'} active={'/management' === currentTab}
                        handler={handleClick}/>
          </div>
          <div className={styles[`${baseClass}__item`]}>
            <NavBarItem route={'/about'} label={'About Us'} active={'/about' === currentTab}
                        handler={handleClick}/>
          </div>
          <div className={styles[`${baseClass}__item`]}>
            <NavBarItem route={'/faq'} label={'FAQ'} active={'/faq' === currentTab}
                        handler={handleClick}/>
          </div>
          <div className={styles[`${baseClass}__item`]}>
            <SimpleButton text={'free consult'} variant={"primary"} highlightText={true} />
          </div>
          <div className={styles[`${baseClass}__item`] + ' ' + styles[`${baseClass}__item--mobile`]}>
            <div className={styles[`${baseClass}__mobile-menu`]}>
              <RxHamburgerMenu/>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default NavBar;