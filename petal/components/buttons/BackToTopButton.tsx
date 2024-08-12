'use client'

import {useEffect, useState} from "react";
import {IconChevronsUp} from "@tabler/icons-react";

/**
 * Back to top button to scroll to top of the page
 * @param variant
 * @param size
 * @param trigger offset value to show the button
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function BackToTopButton(
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
   */
  function computeClass() {

    let val = ''
    switch (variant) {
      case 'primary':
        val += ' bg-primary text-white ';
        break
      case 'secondary':
        val += ' bg-secondary text-tertiary ';
        break
      case 'tertiary':
        val += ' bg-tertiary text-secondary ';
        break
      default: val += ' bg-white text-primary '
    }

    switch (size) {
      case 'small':
        val += ' w-[45px] h-[45px] ';
        break
      case 'medium':
        val += ' w-[60px] h-[60px] ';
        break
      default: val += ' w-[75px] h-[75px] '
    }

    return (val + size).trim()
  }


  //  RENDER

  if (!variant || variant.length === 0) {
    return null
  }

  return (
    <div className={(!showButton ? ' hidden ' : '') + ' flex items-center justify-center h-[150px] w-[150px] fixed bottom-0 right-0 z-50 animate__animated animate__fadeIn '}>
      <div className={computeClass() + " flex items-center rounded-full text-center shadow-[0_35px_60px_-15px_rgba(0,0,0,0.5)] hover:cursor-pointer "} onClick={() => window.scrollTo({top: 0, behavior: "smooth"})}>
        <div className={"w-full flex items-center justify-center"}>
          <IconChevronsUp size={35} />
        </div>
      </div>
    </div>
  )
}