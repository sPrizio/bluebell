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
    variant?: 'white' | 'primary' | 'secondary' | 'tertiary' | 'custom'
  }>
) {

  const ref = useRef(null)
  const isInView = useInView(ref, {once: true, amount: 'some'})

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
      case 'primary':
        return ' bg-primary ';
      default:
        return ' ';
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
      case 'primary':
        return ' text-white '
      default:
        return ' text-primary ';
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
        case 'primary':
          return ' text-secondary ';
      default:
        return ' text-tertiary ';
    }
  }


  //  RENDER

  return (
    <div className={"py-6 px-4 " + computeVariant() + " " + className} ref={ref}>
      <div className={(!isInView ? " invisible " : "") + " container px-8 my-6 sm:my-8 md:my-12 "}>
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