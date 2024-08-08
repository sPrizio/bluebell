'use client'

import Image from "next/image";
import {motion, useInView} from "framer-motion";
import {useEffect, useRef} from "react";

export default function ImageSection(
  {
    title = '',
    subtitle = '',
    content,
    className = '',
    alignment = 'left',
    image = ''
  }
    : Readonly<{
    title?: string,
    subtitle?: string
    content: React.ReactNode;
    className?: string
    alignment?: 'left' | 'right',
    image: string
  }>
) {

  const ref = useRef(null)
  const isInView = useInView(ref, { once: true })

  useEffect(() => {
  }, [isInView])


  //  GENERAL FUNCTIONS

  function includeAnimation(rightToLeft: boolean) {

    if (isInView) {
      return !rightToLeft ? ' animate__animated animate__slideInLeft ' : ' animate__animated animate__slideInRight '
    }

    return '';
  }


  //  RENDER

  return (
    <div className={"py-12 px-4 " + className} ref={ref}>
      <div className="container my-10 sm:my-16 md:my-24">
        <div className="flex items-start">
          <div className={"basis-1/2 " + (alignment == 'right' ? (" " + includeAnimation(true)) : " order-first md:mr-24 " + includeAnimation(false))}>
            {
              title?.length ?? -1 > 0 ?
                <div
                  className={"text-4xl font-extrabold text-primary "}>
                  {title}
                </div> : null
            }
            {
              subtitle?.length ?? -1 > 0 ?
                <blockquote className={(isInView ? " animate__animated animate__lightSpeedInLeft animate__slow " : "")}>
                  <div className="text-lg text-tertiary">
                    {subtitle}
                  </div>
                </blockquote> : null
            }
            <div className={"basis-1/2 text-base leading-relaxed mt-6 font-regular"}>
              We are Finance Corp, We provide Finance consulting from 30 years.<br/><br/>
              Accumsan est in tempus etos ullamcorper sem quam suscipit lacus maecenas tortor. Suspendisse gravida
              ornare
              non mattis velit rutrum modest sed do eiusmod tempor incididunt ut labore et dolore.<br/><br/>
              We have one of the philo sophia nec mei maiorum appell antur. Orci varius natoque penatibus et magnis
              dis
              parturient montes, nascetur ridiculus mus egestas varius penatibus.
            </div>
          </div>
          <div className={"basis-1/2 " + (alignment == 'right' ? (" order-first mr-24 " + includeAnimation(false)) : (" " + includeAnimation(true)))}>
            <div className={"relative h-[15vh] md:h-[25vh] lg:h-[30vh]"}>
              <Image
                className={'rounded-3xl'}
                src={image}
                alt={'Section Image'}
                fill={true}
                objectFit={'cover'}
              />
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}