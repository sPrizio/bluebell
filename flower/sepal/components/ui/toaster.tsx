"use client";

import { useToast } from "@/lib/hooks/ui/use-toast";
import {
  Toast,
  ToastClose,
  ToastDescription,
  ToastProvider,
  ToastTitle,
  ToastViewport,
} from "@/components/ui/toast";
import { resolveIcon } from "@/lib/functions/util-component-functions";
import { Icons } from "@/lib/enums";

export function Toaster() {
  const { toasts } = useToast();

  //  GENERAL FUNCTIONS

  /**
   * Computes the icon to display depending on the variant selected
   *
   * @param val variant
   */
  function computeIcon(
    val?:
      | "default"
      | "destructive"
      | "danger"
      | "warning"
      | "info"
      | "success"
      | null
      | undefined,
  ) {
    if (!val || val.length < 1) {
      return resolveIcon(Icons.InfoSquareRoundedFilled, "text-primary", 40);
    }

    switch (val) {
      case "success":
        return resolveIcon(Icons.CircleCheckFilled, "text-primaryGreen", 40);
      case "danger":
        return resolveIcon(Icons.XboxXFilled, "text-primaryRed", 40);
      case "warning":
        return resolveIcon(Icons.AlertTriangleFilled, "text-primaryYellow", 40);
      default:
        return resolveIcon(Icons.InfoSquareRoundedFilled, "text-primary", 40);
    }
  }

  //  RENDER

  return (
    <ToastProvider>
      {toasts.map(function ({
        id,
        title,
        description,
        action,
        variant,
        ...props
      }) {
        return (
          <Toast key={id} variant={variant} {...props}>
            <div className={"grid gap-2 grid-cols-4 items-center"}>
              <div className={"flex justify-center items-center text-center"}>
                {computeIcon(variant)}
              </div>
              <div className={"col-span-3"}>
                {title && <ToastTitle>{title}</ToastTitle>}
                {description && (
                  <ToastDescription>{description}</ToastDescription>
                )}
              </div>
            </div>
            <ToastClose />
          </Toast>
        );
      })}
      <ToastViewport />
    </ToastProvider>
  );
}
