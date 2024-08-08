'use client'

import NavBar from "@/components/navigation/NavBar";
import {TextGenerateEffect} from "@/components/ui/text-generate-effect";
import {useEffect, useRef} from "react";
import {useInView} from "framer-motion";

/**
 * A simple hero component that can support simple color variants or background images
 *
 * @param title title
 * @param subtitle subtitle
 * @param variant color variant
 * @param size hero size
 * @param alignment alignment of text
 * @param position position of content
 * @param hasNavBar has a navbar
 * @param highlight text effects on title
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function SimpleHero(
  {
    title = '',
    subtitle = '',
    variant = 'primary',
    size = 'medium',
    alignment = 'left',
    position = 'left',
    hasNavBar = false,
    highlight = false
  }
    : Readonly<{
    title: string,
    subtitle: string
    variant?: 'primary' | 'secondary' | 'tertiary' | 'white' | 'transparent' | 'image',
    size?: 'small' | 'medium' | 'large' | 'full',
    alignment?: 'left' | 'center' | 'right',
    position?: 'left' | 'right' | 'center',
    hasNavBar?: boolean,
    highlight?: boolean
  }>
) {

  const ref = useRef(null)
  const isInView = useInView(ref, { once: true })

  useEffect(() => {
  }, [isInView])


  //  FUNCTIONS

  /**
   * Computes the css classes based on the given props
   */
  function computeVariant() {
    switch (variant) {
      case 'secondary':
        return ' bg-secondary text-tertiary ';
      case 'tertiary':
        return ' bg-tertiary text-white ';
      case 'white':
        return ' bg-white text-tertiary ';
      case 'transparent':
        return ' bg-transparent text-white ';
      case 'image':
        return ' bg-no-repeat bg-cover bg-center bg-[url(https://demo.themenio.com/finance/image/slider-lg-a.jpg)] text-white ';
      default:
        return ' bg-primary text-white ';
    }
  }

  /**
   * Computes the hero's size
   */
  function computeSize() {
    switch (size) {
      case 'small':
        return ' h-[25vh] '
      case 'medium':
        return ' h-[50vh] '
      case 'large':
        return ' h-[75vh] '
      default:
        return ' h-screen '
    }
  }

  /**
   * Computes the text alignment
   */
  function computeAlignment() {
    switch (alignment) {
      case 'left':
        return ' text-left '
      case 'center':
        return ' text-center '
      default:
        return ' text-right '
    }
  }

  /**
   * Computes the content positioning
   */
  function computePosition() {
    switch (position) {
      case 'left':
        return ' justify-start '
      case 'center':
        return ' justify-center '
      default:
        return ' justify-end '
    }
  }


  //  RENDER

  return (
    <div className={computeVariant()} ref={ref}>
      {hasNavBar ? <NavBar variant={variant == "image" ? 'transparent' : variant} /> : null}
      <div className={computeAlignment() + computePosition() + computeSize() + " container flex items-center"}>
        <div className="w-2/3">
          <div className="text-6xl font-extrabold">
            {highlight && isInView ? <TextGenerateEffect words={title} duration={1} /> : <>{title}</>}
          </div>
          <div className={"mt-4 text-md font-normal bg-primary/40 p-4 rounded-lg " + (highlight && isInView ? " animate__animated animate__lightSpeedInLeft animate__delay-1s" : "")}>
            {subtitle}
          </div>
        </div>
      </div>
    </div>
  )
}