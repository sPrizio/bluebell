"use client";

import { cn } from "@/lib/utils";
import React, { useEffect, useState } from "react";

export const InfiniteMovingCards = ({
  items,
  direction = "left",
  speed = "fast",
  pauseOnHover = true,
  className,
  variant = 'white'
}: {
  items: {
    quote: string;
    name: string;
    title: string;
  }[];
  direction?: "left" | "right";
  speed?: "fast" | "normal" | "slow" | 'slower';
  pauseOnHover?: boolean;
  className?: string;
  variant?: 'white' | 'primary' | 'secondary' | 'tertiary';
}) => {
  const containerRef = React.useRef<HTMLDivElement>(null);
  const scrollerRef = React.useRef<HTMLUListElement>(null);

  useEffect(() => {
    addAnimation();
  }, []);
  const [start, setStart] = useState(false);


  //  FUNCTIONS

  function addAnimation() {
    if (containerRef.current && scrollerRef.current) {
      const scrollerContent = Array.from(scrollerRef.current.children);

      scrollerContent.forEach((item) => {
        const duplicatedItem = item.cloneNode(true);
        if (scrollerRef.current) {
          scrollerRef.current.appendChild(duplicatedItem);
        }
      });

      getDirection();
      getSpeed();
      setStart(true);
    }
  }
  const getDirection = () => {
    if (containerRef.current) {
      if (direction === "left") {
        containerRef.current.style.setProperty(
          "--animation-direction",
          "forwards"
        );
      } else {
        containerRef.current.style.setProperty(
          "--animation-direction",
          "reverse"
        );
      }
    }
  };
  const getSpeed = () => {
    if (containerRef.current) {
      if (speed === "fast") {
        containerRef.current.style.setProperty("--animation-duration", "20s");
      } else if (speed === "normal") {
        containerRef.current.style.setProperty("--animation-duration", "40s");
      } else if (speed === "slow") {
        containerRef.current.style.setProperty("--animation-duration", "80s");
      } else {
        containerRef.current.style.setProperty("--animation-duration", "100s");
      }
    }
  };

  /**
   * Computes the card background based on the variant
   */
  function computeCardVariant() {
    switch (variant) {
      case 'secondary':
        return 'bg-secondary';
      case 'tertiary':
        return 'bg-tertiary';
      case 'white':
        return 'bg-white';
      default:
        return 'bg-primary';
    }
  }

  /**
   * Computes the card title color based on the variant
   */
  function computeCardTitle() {
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
   * Computes the card subtitle color based on the variant
   */
  function computeCardSubtitle() {
    switch (variant) {
      case 'secondary':
        return 'text-tertiary';
      case 'tertiary':
        return 'text-white';
      case 'white':
        return 'text-muted-foreground';
      default:
        return 'text-secondary';
    }
  }

  /**
   * Computes the card text color based on the variant
   */
  function computeCardText() {
    switch (variant) {
      case 'secondary':
        return ' text-tertiary ';
      case 'tertiary':
        return ' text-secondary ';
      case 'white':
        return ' text-foreground ';
      default:
        return ' text-muted ';
    }
  }


  //  RENDER

  return (
    <div
      ref={containerRef}
      className={cn(
        "scroller relative z-20  max-w-7xl overflow-hidden ",
        className
      )}
    >
      <ul
        ref={scrollerRef}
        className={cn(
          " flex min-w-full shrink-0 gap-4 py-4 w-max flex-nowrap ",
          start && "animate-scroll ",
          pauseOnHover && "hover:[animation-play-state:paused]"
        )}
      >
        {items.map((item, idx) => (
          <li
            className={"w-[350px] max-w-full relative rounded-2xl flex-shrink-0 px-8 py-6 md:w-[450px] shadow-md border border-slate-200 " + computeCardVariant()}
            key={item.name}
          >
            <div className={'flex flex-col space-y-1.5 pb-3'}>
              <h3 className={'font-semibold leading-none tracking-tight ' + computeCardTitle()}>{item.name}</h3>
              <p className={'text-sm text-muted-foreground ' + computeCardSubtitle()}>{item.title}</p>
            </div>
            <div className="">
              <div
                aria-hidden="true"
                className="user-select-none -z-1 pointer-events-none absolute -left-0.5 -top-0.5 h-[calc(100%_+_4px)] w-[calc(100%_+_4px)]"
              ></div>
              <span
                className={" relative z-20 text-sm leading-[1.6] tracking-tighter font-normal " + computeCardText()}>
                {item.quote}
              </span>
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
};
