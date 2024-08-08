'use client'

import FeatureCard from "@/components/cards/FeatureCard";
import {useEffect, useRef} from "react";
import {useInView} from "framer-motion";

export default function FeatureSection(
  {
    title = '',
    subtitle = '',
    features = [],
    className = ''
  }
    : Readonly<{
    title: string,
    subtitle?: string
    features: Array<FeatureType>,
    className?: string
  }>
) {

  const ref = useRef(null)
  const isInView = useInView(ref, { once: true })

  useEffect(() => {
  }, [isInView])


  //  RENDER

  return (
    <div className={"py-24 px-4 " + className} ref={ref}>
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
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 relative z-10 py-10 max-w-7xl mx-auto">
          {features.map((feature, index) => (
            <FeatureCard key={feature.title} feature={feature}/>
          ))}
        </div>
      </div>
    </div>
  )
}