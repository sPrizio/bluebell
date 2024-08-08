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
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function SimpleSection(
  {
    title = '',
    subtitle = '',
    content,
    className = ''
  }
    : Readonly<{
    title?: string,
    subtitle?: string
    content: React.ReactNode;
    className?: string
  }>
) {

  const ref = useRef(null)
  const isInView = useInView(ref, { once: true })

  useEffect(() => {
  }, [isInView])


  //  RENDER

  return (
    <div className={"py-12 px-4 " + className} ref={ref}>
      <div className="container px-8 my-10 sm:my-16 md:my-24">
        {
          title?.length ?? -1 > 0 ?
            <div className={"text-4xl font-extrabold text-primary " + (isInView ? " animate__animated animate__lightSpeedInLeft " : "")}>
              {title}
            </div> : null
        }
        {
          subtitle?.length ?? -1 > 0 ?
            <blockquote className={(isInView ? " animate__animated animate__lightSpeedInLeft animate__slow " : "")}>
              <p className="text-lg text-tertiary">
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