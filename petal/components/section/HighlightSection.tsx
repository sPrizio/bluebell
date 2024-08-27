'use client'

import Image from "next/image";
import React, {useEffect, useId, useRef, useState} from "react";
import {AnimatePresence, motion} from "framer-motion";
import {useOutsideClick} from "@/hooks/use-outside-click";
import SimpleSection from "@/components/section/SimpleSection";

/**
 * A special component to highlight entries via modal
 *
 * @param title title
 * @param subtitle subtitle
 * @param className custom classes
 * @param highlights entries to highlight
 * @param cardClassName classes for the highlight cards
 * @param variant color variant
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function HighlightSection(
  {
    title = '',
    subtitle = '',
    className = '',
    highlights = [],
    cardClassName = '',
    variant = 'white'
  }
    : Readonly<{
    title: string,
    subtitle?: string
    className?: string,
    cardClassName?: string,
    highlights: Array<HighlightCardType>,
    variant?: 'white' | 'primary' | 'secondary' | 'tertiary';
  }>
) {

  const [active, setActive] = useState<(typeof highlights)[number] | boolean | null>(
    null
  );
  const id = useId();
  const ref = useRef<HTMLDivElement>(null);

  useEffect(() => {
    function onKeyDown(event: KeyboardEvent) {
      if (event.key === "Escape") {
        setActive(false);
      }
    }

    if (active && typeof active === "object") {
      document.body.style.overflow = "hidden";
    } else {
      document.body.style.overflow = "auto";
    }

    window.addEventListener("keydown", onKeyDown);
    return () => window.removeEventListener("keydown", onKeyDown);
  }, [active]);

  useOutsideClick(ref, () => setActive(null));


  //  FUNCTIONS

  /**
   * Computes title color
   */
  function computeTitle() {
    switch (variant) {
      case 'secondary':
        return 'text-tertiary';
      case 'tertiary':
        return 'text-secondary';
      case 'white':
        return 'text-primary';
      default:
        return 'text-white';
    }
  }

  /**
   * Computes subtitle color
   */
  function computeSubtitle() {
    switch (variant) {
      case 'secondary':
        return 'text-tertiary';
      case 'tertiary':
        return 'text-secondary';
      case 'white':
        return 'text-primary';
      default:
        return 'text-white';
    }
  }


  //  RENDER

  return (
    <SimpleSection
      title={title}
      subtitle={subtitle}
      content={
        <>
          <AnimatePresence>
            {active && typeof active === "object" && (
              <motion.div
                initial={{ opacity: 0 }}
                animate={{ opacity: 1 }}
                exit={{ opacity: 0 }}
                className="fixed inset-0 bg-black/100 h-full w-full z-[1000]"
              />
            )}
          </AnimatePresence>
          <AnimatePresence>
            {active && typeof active === "object" ? (
              <div className="fixed inset-0  grid place-items-center z-[1000]">
                <motion.button
                  key={`button-${active.title}-${id}`}
                  layout
                  initial={{
                    opacity: 0,
                  }}
                  animate={{
                    opacity: 1,
                  }}
                  exit={{
                    opacity: 0,
                    transition: {
                      duration: 0.05,
                    },
                  }}
                  className="flex absolute top-2 right-2 lg:hidden items-center justify-center bg-white rounded-full h-6 w-6"
                  onClick={() => setActive(null)}
                >
                  <CloseIcon />
                </motion.button>
                <motion.div
                  layoutId={`card-${active.title}-${id}`}
                  ref={ref}
                  className="w-full max-w-[500px] h-fit md:h-fit md:max-h-[90%] z-[1000] flex flex-col bg-white sm:rounded-3xl overflow-hidden"
                >
                  <motion.div layoutId={`image-${active.title}-${id}`}>
                    <Image
                      priority
                      width={1000}
                      height={1000}
                      src={active.src}
                      alt={active.title}
                      className="w-full h-96 sm:rounded-tr-lg sm:rounded-tl-lg object-cover object-top"
                    />
                  </motion.div>

                  <div className="pt-6 pb-12">
                    <div className="flex justify-between items-start p-4">
                      <div className="">
                        <motion.h3
                          layoutId={`title-${active.title}-${id}`}
                          className="font-bold text-primary"
                        >
                          {active.title}
                        </motion.h3>
                        <motion.p
                          layoutId={`description-${active.description}-${id}`}
                          className="text-muted-foreground"
                        >
                         {active.description}
                        </motion.p>
                      </div>
                    </div>
                    <div className="pt-4 relative px-4">
                      <motion.div
                        layout
                        initial={{ opacity: 0 }}
                        animate={{ opacity: 1 }}
                        exit={{ opacity: 0 }}
                        className="text-foreground tracking-tight text-sm h-40 md:h-fit pb-10 flex flex-col items-start gap-4 overflow-auto [scrollbar-width:none] [-ms-overflow-style:none] [-webkit-overflow-scrolling:touch]"
                      >
                        {active.content}
                      </motion.div>
                    </div>
                  </div>
                </motion.div>
              </div>
            ) : null}
          </AnimatePresence>
          <ul className="mx-auto w-full grid grid-cols-2 lg:grid-cols-4 items-start gap-12">
            {highlights.map((card, index) => (
              <motion.div
                layoutId={`card-${card.title}-${id}`}
                key={card.title}
                onClick={() => setActive(card)}
                className="flex flex-col rounded-xl cursor-pointer"
              >
                <div className="flex gap-4 flex-col w-full">
                  <motion.div layoutId={`image-${card.title}-${id}`}>
                    <Image
                      width={1000}
                      height={1000}
                      src={card.src}
                      alt={card.title}
                      className="h-48 w-full rounded-lg object-cover object-top"
                    />
                  </motion.div>
                  <div className="flex justify-center items-center flex-col">
                    <motion.h3
                      layoutId={`title-${card.title}-${id}`}
                      className={"w-full text-left md:text-left tracking-[2px] font-bold text-[14px] uppercase " + computeTitle()}
                    >
                      {card.title}
                    </motion.h3>
                    <motion.p
                      layoutId={`description-${card.description}-${id}`}
                      className={"w-full text-left text-sm " + computeSubtitle()}
                    >
                      {card.description}
                    </motion.p>
                  </div>
                </div>
              </motion.div>
            ))}
          </ul>
        </>
      }
      variant={variant}
    />
  )
}

export const CloseIcon = () => {
  return (
    <motion.svg
      initial={{
        opacity: 0,
      }}
      animate={{
        opacity: 1,
      }}
      exit={{
        opacity: 0,
        transition: {
          duration: 0.05,
        },
      }}
      xmlns="http://www.w3.org/2000/svg"
      width="24"
      height="24"
      viewBox="0 0 24 24"
      fill="none"
      stroke="currentColor"
      strokeWidth="2"
      strokeLinecap="round"
      strokeLinejoin="round"
      className="h-4 w-4 text-black"
    >
      <path stroke="none" d="M0 0h24v24H0z" fill="none" />
      <path d="M18 6l-12 12" />
      <path d="M6 6l12 12" />
    </motion.svg>
  );
};