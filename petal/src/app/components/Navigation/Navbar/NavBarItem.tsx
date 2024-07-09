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
 * @param variant color variant
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function NavBarItem(
  {
    route = '',
    label = '',
    active = false,
    handler,
    variant = 'primary'
  }: Readonly<{
    route: string,
    label: string,
    active: boolean,
    handler: Function,
    variant?: 'primary' | 'secondary' | 'tertiary' | 'transparent' | 'white',
  }>
) {

  const baseClass : string = "nav-bar-item"


  //  GENERAL FUNCTIONS

  /**
   * Computes the css class based on the given props
   *
   * @param variant - determines color & shape. Accepted values are : 'primary', 'secondary', 'tertiary'.
   *                 if the value is not one of the above or is missing, the button will not render
   */
  function computeClass(variant) {
    const v = variant ? styles[`${baseClass}--${variant}`] : ""
    const a = active ? styles[`${baseClass}--active`] : ""
    const av = active ? styles[`${baseClass}--active_${variant}`] : ""

    return `${styles[baseClass]} ${v} ${a} ${av}`.trim()
  }

  if (!variant || variant.length === 0) {
    return null
  }


  //  RENDER

  return (
    <Link href={route} className={styles[`${baseClass}__reset-anchor`]} onClick={() => handler(route)}>
      <div className={computeClass(variant)}>
        <div className={styles[`${baseClass}__item`] + ' - ' + styles[`${baseClass}__text`]}>
          <div className={styles[`${baseClass}__content`]}>
            <span>{label}</span>
          </div>
        </div>
      </div>
    </Link>
  )
}