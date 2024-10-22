'use client'

import {
  Dialog,
  DialogContent,
  DialogDescription, DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog"
import {Button} from "@/components/ui/button";
import {ReactNode, useEffect, useState} from "react";
import {SepalModalContext, useSepalModalContext} from "@/lib/context/SepalContext";

/**
 * Basic, re-usable modal
 *
 * @param isOpen is open
 * @param trigger component to trigger the modal
 * @param title modal title
 * @param description subtitle
 * @param content modal content
 * @param closeHandler custom handler on modal close
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function BaseModal(
  {
    isOpen = false,
    trigger,
    title = '',
    description = '',
    content = null,
    closeHandler
  }
    : Readonly<{
    isOpen?: boolean,
    trigger?: ReactNode,
    title: string,
    description?: string,
    content: ReactNode,
    closeHandler?: Function
  }>
) {

  const [open, setOpen] = useState(isOpen)

  useEffect(() => {
    setOpen(isOpen)
  }, [isOpen]);

  useEffect(() => {
    if (!open) {
      if (closeHandler) {
        closeHandler.call({})
      }
    }
  }, [open]);


  //  RENDER

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      {
        trigger ?
          <DialogTrigger asChild>
            {trigger}
          </DialogTrigger> : null
      }
      <DialogContent>
        <DialogHeader>
          <DialogTitle>{title}</DialogTitle>
          {description ? <DialogDescription>{description}</DialogDescription> : null}
        </DialogHeader>
        <div className="grid gap-4 py-4">
          <SepalModalContext.Provider value={{open, setOpen}}>
            {content}
          </SepalModalContext.Provider>
        </div>
      </DialogContent>
    </Dialog>
  )
}