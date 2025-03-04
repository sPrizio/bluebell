'use client'

import {Card, CardContent, CardDescription, CardHeader, CardTitle} from "@/components/ui/card";
import Image, {StaticImageData} from "next/image";
import {useEffect, useRef} from "react";
import {useInView} from "framer-motion";

/**
 * A universal card
 *
 * @param title card title
 * @param subtitle card subtitle
 * @param content card content
 * @param icon card icon
 * @param animated should animate the card
 * @param delay delay between animations
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function SimpleCard(
  {
    title = '',
    subtitle = '',
    content = '',
    icon = undefined,
    animated = false,
    delay = 0
  }
    : Readonly<{
    title: string,
    subtitle?: string
    content: string,
    icon?: React.ReactNode,
    animated?: boolean,
    delay?: number
  }>
) {

  const ref = useRef(null)
  const isInView = useInView(ref, { once: true,amount: 'some' })

  useEffect(() => {
  }, [isInView])


  //  FUNCTIONS

  /**
   * Computes the animation class
   */
  function computeAnimation() {
    if (animated) {
      return (isInView ? ' animate__animated animate__flipInX animate__slow ' : '');
    }

    return '';
  }

  /**
   * Computes the delays between animations
   */
  function computeDelay() {

    let del = 0;
    if (delay >= 1) {
      if (delay > 5) {
        del = 5;
      } else {
        del = delay
      }

      return ` animate__delay-${del}s `
    }

    return ''
  }


  //  RENDER

  return (
    <Card className={(!isInView ? " invisible " : "") + " h-full shadow-md text-slate-600 " + computeAnimation() + computeDelay()} ref={ref}>
      <CardHeader>
        <div className="flex items-stretch justify-between">
          <div className={"w-full basis-2/3"}>
            <CardTitle className=" font-extrabold text-primary text-xl">
              {title}
            </CardTitle>
            {subtitle?.length ?? -1 > 0 ? <CardDescription>{subtitle}</CardDescription> : null}
          </div>
          {
            icon ?
              <div className={' basis-1/3 flex text-center justify-end items-center'}>
                {icon}
              </div> : null
          }
        </div>
      </CardHeader>
      <CardContent className={'text-md text-foreground'}>
        {content}
      </CardContent>
    </Card>
  )
}