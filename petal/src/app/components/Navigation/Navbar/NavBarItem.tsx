import React from "react";
import styles from "./NavBarItem.module.scss";
import Link from "next/link";

/**
 * Represents each individual menu item that can redirect the app wherever it needs to go
 *
 * @param route nextjs route link
 * @param label display text
 * @param active if true, highlight as active
 * @param handler on click function
 * @author Stephen Prizio
 * @version 0.0.1
 */
function NavBarItem(
  {
    route = '',
    label = '',
    active = false,
    handler,
  }: Readonly<{
    route: string,
    label: string,
    active: boolean,
    handler: Function,
  }>
) {

  const baseClass = "nav-bar-item"


  //  RENDER

  return (
    <Link href={route} className={styles[`${baseClass}__reset-anchor`]} onClick={() => handler(route)}>
      <div className={styles[baseClass] + ' ' + (active ? styles[`${baseClass}--active`] : '')}>
        <div className={styles[`${baseClass}__item`] + ' - ' + styles[`${baseClass}__text`]}>
          <div className={styles[`${baseClass}__content`]}>
            <span>{label}</span>
          </div>
        </div>
      </div>
    </Link>
  )
}

export default NavBarItem;