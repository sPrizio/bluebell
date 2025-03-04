"use client"

import * as React from "react"
import * as ProgressPrimitive from "@radix-ui/react-progress"

import { cn } from "@/lib/utils"

type CustomProgressProps = React.ComponentPropsWithoutRef<typeof ProgressPrimitive.Root> & {
  variant: 'info' | 'success' | 'warning' | 'danger'
}


//  GENERAL FUNCTIONS

function computeVariant(val: 'info' | 'success' | 'warning' | 'danger', withOpacity = false) {

  let color = 'bg-primary'
  switch (val) {
    case 'success':
      color += 'Green'
      break;
    case 'warning':
      color += 'Yellow'
      break;
    case 'danger':
      color += 'Red'
      break;
    default:
      break;
  }

  return withOpacity ? (color + 'Light') : color
}


//  RENDER

const Progress = React.forwardRef<
  React.ElementRef<typeof ProgressPrimitive.Root>,
  CustomProgressProps
>(({ className, value, variant, ...props }, ref) => (
  <ProgressPrimitive.Root
    ref={ref}
    className={cn(
      "relative h-2 w-full overflow-hidden rounded-full " + computeVariant(variant, true),
      className
    )}
    {...props}
  >
    <ProgressPrimitive.Indicator
      className={"h-full w-full flex-1 transition-all " + computeVariant(variant, false)}
      style={{ transform: `translateX(-${100 - (value || 0)}%)` }}
    />
  </ProgressPrimitive.Root>
))
Progress.displayName = ProgressPrimitive.Root.displayName

export { Progress }
