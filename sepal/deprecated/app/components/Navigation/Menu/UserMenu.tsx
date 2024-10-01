import React from "react";
import styles from "./UserMenu.module.scss";
import {UserMenuOption} from "@/app/types/appTypes";
import {resolveIcon} from "@/app/services/resolver/iconResolverService";
import Link from "next/link";

/**
 * Context menu attached to the user avatar on the navigation bar
 *
 * @param active flag to toggle the menu as active (visible)
 * @param options menu options / links
 * @author Stephen Prizio
 * @version 0.0.1
 */
function UserMenu({active = false, options = [], handler}: {active?: boolean, options: Array<UserMenuOption>, handler: Function}) {

  const baseClass = "user-menu"

  function isLastItem(key: number) {
    if ((key + 1) === options.length) {
      return styles[`${baseClass}__menu-item--last`]
    }

    return ''
  }


  //  RENDER

  return (
    <div className={styles[baseClass] + ' ' + (active ? styles[`${baseClass}--active`] : '')}>
      <div className={styles[`${baseClass}__menu-wrapper`]}>
        {
          options && options.map((item, key) => {
            return (
              <div key={key} className={styles[`${baseClass}__menu-item`] + ' ' + isLastItem(key)} onClick={() => handler(item.link)}>
                <div className={styles[`${baseClass}__icon`]}>
                  {resolveIcon(item.icon)}
                </div>
                <div className={styles[`${baseClass}__text`]}>
                  <Link href={item.link} className={styles[`${baseClass}__text-link`]}>
                    {item.label}
                  </Link>
                </div>
              </div>
            )
          })
        }
      </div>
    </div>
  )
}

export default UserMenu;