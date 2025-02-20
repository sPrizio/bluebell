"use client";

import React from "react";
import {InfiniteMovingCards} from "../ui/infinite-moving-cards";

/**
 * Renders scrolling cards that never end
 *
 * @param className custom css classes
 * @param testimonials testimonials (card content)
 * @param cardClassName custom css classes for the specific cards
 * @param variant color variant
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function InfiniteScrollingCards(
  {
    className = '',
    testimonials = [],
    cardClassName = '',
    variant = 'white'
  }
    : Readonly<{
    className?: string,
    cardClassName?: string,
    testimonials: Array<TestimonialType>,
    variant?: 'white' | 'primary' | 'secondary' | 'tertiary';
  }>
) {


  //  FUNCTIONS

  function computeVariant() {
    switch (variant) {
      case 'secondary':
        return 'secondary';
      case 'tertiary':
        return 'tertiary';
      case 'white':
        return 'white';
      default:
        return 'primary';
    }
  }


  //  RENDER

  return (
    <div
      className="rounded-lg flex flex-col antialiased items-center justify-center relative overflow-hidden">
      <InfiniteMovingCards
        items={testimonials}
        direction="left"
        speed="slower"
        variant={computeVariant()}
      />
    </div>
  )
}