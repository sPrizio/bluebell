'use client'

import React, {useState} from "react";
import styles from "./NavBar.module.scss";
import {usePathname} from "next/navigation";
import Link from "next/link";
import NavBarItem from "@/app/components/Navigation/navbar/NavBarItem";
import {RxHamburgerMenu} from "react-icons/rx";
import MainLogo from "@/app/components/Navigation/Logo/MainLogo";
import SimpleButton from "@/app/components/Button/SimpleButton";
import exp from "node:constants";
import {SimpleImage} from "@/app/types/appTypes";

/**
 * The top-page navigation component
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function NavBar(
  {
    variant = 'primary',
    size = 'medium',
  }
    : Readonly<{
    variant?: 'primary' | 'secondary' | 'tertiary' | 'transparent' | 'white',
    size?: 'small' | 'medium' | 'large',
  }>
) {

  const baseClass : string = "nav-bar"

  const [currentTab, setCurrentTab] = useState(usePathname())
  const [userMenuActive, setUserMenuActive] = useState(false)


  //  GENERAL FUNCTIONS

  /**
   * Computes the css class based on the given props
   *
   * @param variant - determines color & shape. Accepted values are : 'primary', 'secondary', 'tertiary'.
   *                 if the value is not one of the above or is missing, the button will not render
   * @param size - font size
   */
  function computeClass(variant, size) {
    const v = variant ? styles[`${baseClass}--${variant}`] : ""
    const s = size ? styles[`${baseClass}--${size}`] : ""

    return `${styles[baseClass]} ${v} ${s}`.trim()
  }

  if (!variant || variant.length === 0) {
    return null
  }

  /**
   * Handles selecting a new tab on link clicks
   *
   * @param val new route
   */
  function handleClick(val: string) {
    setCurrentTab(val)
    setUserMenuActive(false)
  }

  /**
   * Determines which button style to include based on the nav bar variant
   */
  function computeButtonVariant(): 'primary' | 'secondary' | 'tertiary' {
    switch (variant) {
      case 'primary':
      case 'tertiary':
        return 'secondary';
      case 'white':
      case 'transparent':
      case 'secondary':
        return 'tertiary';
    }
  }


  //  RENDER

  return (
    <div className={computeClass(variant, size)}>
      <div className={styles[`${baseClass}__container`]}>
        <div className={styles[`${baseClass}__items`] + ' ' + styles[`${baseClass}__items--left`]}>
          <div className={styles[`${baseClass}__item`] + ' ' + styles[`${baseClass}__item--brand`]}>
            <Link href={'/home'} onClick={() => handleClick('/home')}>
              <MainLogo variant={variant} />
            </Link>
          </div>
        </div>
        <div className={styles[`${baseClass}__items`] + ' ' + styles[`${baseClass}__items--right`]}>
          <div className={styles[`${baseClass}__item`]}>
            <NavBarItem route={'/about'} label={'Who We Are'} active={'/about' === currentTab}
                        handler={handleClick} variant={variant}/>
          </div>
          <div className={styles[`${baseClass}__item`]}>
            <NavBarItem route={'/services'} label={'What We Offer'} active={'/services' === currentTab}
                        handler={handleClick} variant={variant}/>
          </div>
          <div className={styles[`${baseClass}__item`]}>
            <NavBarItem route={'/management'} label={'Why Choose Us?'} active={'/management' === currentTab}
                        handler={handleClick} variant={variant}/>
          </div>
          <div className={styles[`${baseClass}__item`]}>
            <NavBarItem route={'/faq'} label={'FAQ'} active={'/faq' === currentTab}
                        handler={handleClick} variant={variant}/>
          </div>
          {/*<!-- The mobile element cannot be neither first nor last -->*/}
          <div className={styles[`${baseClass}__item`] + ' ' + styles[`${baseClass}__item--mobile`]}>
            <div className={styles[`${baseClass}__mobile-menu`]}>
              <RxHamburgerMenu/>
            </div>
          </div>
          <div className={styles[`${baseClass}__item`]}>
            <SimpleButton text={'free consult'}
                          variant={computeButtonVariant()}
                          highlightText={true}
            />
          </div>
        </div>
      </div>
    </div>
  )
}