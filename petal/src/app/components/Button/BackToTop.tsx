'use client'

import styles from './BackToTop.module.scss'
import {LuArrowUpToLine} from "react-icons/lu";
import {useEffect, useState} from "react";

/**
 * Back to top button to scroll to top of the page
 * @param variant
 * @param size
 * @param trigger offset value to show the button
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function BackToTop(
  {
    variant = 'primary',
    size = 'medium',
    trigger = 500
  }
    : Readonly<{
    variant?: 'primary' | 'secondary' | 'tertiary' | 'white',
    size?: 'small' | 'medium' | 'large',
    trigger?: number,
  }>
) {

  const baseClass = "back-to-top"
  const [showButton, setShowButton] = useState(false)

  useEffect(() => {
    function handleScroll() {
      window.scrollY > trigger ? setShowButton(true) : setShowButton(false)
    }

    window.addEventListener('scroll', handleScroll)

    return () => {
      window.removeEventListener("scroll", handleScroll)
    }
  }, [])


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


  //  RENDER

  return (
    <div className={computeClass(variant, size) + ' ' + (showButton ? styles[`${baseClass}--active`] : '')}
         onClick={() => window.scrollTo({top: 0, behavior: "smooth"})}>
      <LuArrowUpToLine />
    </div>
  )
}