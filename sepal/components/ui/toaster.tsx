"use client"

import {useToast} from "@/hooks/use-toast"
import {Toast, ToastClose, ToastDescription, ToastProvider, ToastTitle, ToastViewport,} from "@/components/ui/toast"
import {
  IconAlertTriangleFilled,
  IconCircleCheckFilled,
  IconInfoSquareRoundedFilled,
  IconXboxXFilled
} from "@tabler/icons-react";

export function Toaster() {

  const {toasts} = useToast()


  //  GENERAL FUNCTIONS

  /**
   * Computes the icon to display depending on the variant selected
   *
   * @param val variant
   */
  function computeIcon(val?: "default" | "destructive" | "danger" | "warning" | "info" | "success" | null | undefined) {

    if (!val || val.length < 1) {
      return <IconInfoSquareRoundedFilled size={40} className={'text-primary'}/>
    }

    switch (val) {
      case 'success':
        return <IconCircleCheckFilled size={40} className={'text-primaryGreen'}/>
      case 'danger':
        return <IconXboxXFilled size={40} className={'text-primaryRed'}/>
      case 'warning':
        return <IconAlertTriangleFilled size={40} className={'text-primaryYellow'}/>
      default:
        return <IconInfoSquareRoundedFilled size={40} className={'text-primary'}/>
    }
  }


  //  RENDER

  return (
    <ToastProvider>
      {toasts.map(function ({id, title, description, action, variant, ...props}) {
        return (
          <Toast key={id} variant={variant} {...props}>
            <div className={'grid gap-2 grid-cols-4 items-center'}>
              <div className={'flex justify-center items-center text-center'}>
                {computeIcon(variant)}
              </div>
              <div className={'col-span-3'}>
                {title && <ToastTitle>{title}</ToastTitle>}
                {description && (
                  <ToastDescription>{description}</ToastDescription>
                )}
              </div>
            </div>
            <ToastClose/>
          </Toast>
        )
      })}
      <ToastViewport/>
    </ToastProvider>
  )
}
