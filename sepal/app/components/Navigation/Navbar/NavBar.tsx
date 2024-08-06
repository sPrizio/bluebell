'use client'

import React, {useState} from "react";
import styles from "./NavBar.module.scss";
import {resolveIcon} from "@/app/services/resolver/iconResolverService";
import {RxHamburgerMenu} from "react-icons/rx";
import {usePathname} from "next/navigation";
import Link from "next/link";
import MainLogo from "@/app/components/Navigation/Logo/MainLogo";
import NavBarItem from "@/app/components/Navigation/Navbar/NavBarItem";
import Avatar from "@/app/components/Navigation/Avatar/Avatar";
import UserMenu from "@/app/components/Navigation/Menu/UserMenu";

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
          <div className={styles[`${baseClass}__item`]}>
            <NavBarItem route={'/trades'} label={'Trades'} active={'/trades' === currentTab}
                        icon={resolveIcon('CgArrowsExchange')} handler={handleClick}/>
          </div>
          <div className={styles[`${baseClass}__item`]}>
            <NavBarItem route={'/trading-history'} label={'History'} active={'/trading-history' === currentTab}
                        icon={resolveIcon('MdOutlineHistory')} handler={handleClick}/>
          </div>
          <div className={styles[`${baseClass}__item`]}>
            <NavBarItem route={'/news'} label={'Market News'} active={'/news' === currentTab}
                        icon={resolveIcon('FaNewspaper')} handler={handleClick}/>
          </div>
          <div className={styles[`${baseClass}__item`]}>
            <NavBarItem route={'/chart'} label={'Chart'} active={'/chart' === currentTab}
                        icon={resolveIcon('FaRegChartBar ')} handler={handleClick}/>
          </div>
        </div>
        <div className={styles[`${baseClass}__items`] + ' ' + styles[`${baseClass}__items--right`]}>
          <div className={styles[`${baseClass}__item`]}>
            <Avatar handler={() => setUserMenuActive(!userMenuActive)}/>
            <UserMenu
              active={userMenuActive}
              options={[
                {label: 'Account', link: '/account', icon: 'VscAccount'},
                {label: 'Report an Issue', link: '/report', icon: 'BsPatchExclamation'},
                {label: 'Switch Accounts', link: '/swap', icon: 'PiUserSwitchLight'},
                {label: 'Sign Out', link: '/logout', icon: 'RiLogoutCircleLine'}
              ]}
              handler={handleClick}
            />
          </div>
          <div className={styles[`${baseClass}__item`] + ' ' + styles[`${baseClass}__item--mobile`]}>
            <div className={styles[`${baseClass}__mobile-menu`]}>
              <RxHamburgerMenu />
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default NavBar;