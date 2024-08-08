'use client'

import {motion, useInView} from "framer-motion";
import {useEffect, useRef} from "react";

/**
 * A simple component to render a section of a page
 *
 * @param title section title
 * @param subtitle section subtitle
 * @param content react content
 * @param className custom class names
 * @param variant color variant
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function SimpleSection(
  {
    title = '',
    subtitle = '',
    content,
    className = '',
    variant = 'white'
  }
    : Readonly<{
    title?: string,
    subtitle?: string
    content: React.ReactNode;
    className?: string,
    variant?: 'white' | 'primary' | 'secondary' | 'tertiary'
  }>
) {

  const ref = useRef(null)
  const isInView = useInView(ref, {once: true})

  useEffect(() => {
  }, [isInView])


  //  FUNCTIONS

  /**
   * Computes the background based on the given variant
   */
  function computeVariant() {
    switch (variant) {
      case 'secondary':
        return ' bg-secondary ';
      case 'tertiary':
        return ' bg-tertiary ';
      case 'white':
        return ' bg-white ';
      default:
        return ' bg-primary ';
    }
  }

  /**
   * Computes the title based on the given variant
   */
  function computeVariantTitle() {
    switch (variant) {
      case 'secondary':
        return ' text-tertiary ';
      case 'tertiary':
        return ' text-secondary ';
      case 'white':
        return ' text-primary ';
      default:
        return ' text-white ';
    }
  }

  /**
   * Computes the subtitle based on the given variant
   */
  function computeVariantSubtitle() {
    switch (variant) {
      case 'secondary':
        return ' text-slate-600 ';
      case 'tertiary':
        return ' text-slate-300 ';
      case 'white':
        return ' text-tertiary ';
      default:
        return ' text-secondary  ';
    }
  }


  //  RENDER

  return (
    <div className={"py-12 px-4 " + className + computeVariant()} ref={ref}>
      <div className="container px-8 my-10 sm:my-16 md:my-24">
        {
          title?.length ?? -1 > 0 ?
            <div
              className={"text-4xl font-extrabold " + computeVariantTitle() + (isInView ? " animate__animated animate__lightSpeedInLeft " : "")}>
              {title}
            </div> : null
        }
        {
          subtitle?.length ?? -1 > 0 ?
            <blockquote className={(isInView ? " animate__animated animate__lightSpeedInLeft animate__slow " : "")}>
              <p className={"text-lg " + computeVariantSubtitle()}>
                {subtitle}
              </p>
            </blockquote> : null
        }
        <div className="my-12 ">
          <motion.div>
            {content}
          </motion.div>
        </div>
      </div>
    </div>
  )
}