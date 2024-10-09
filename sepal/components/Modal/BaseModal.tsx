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
 * @param trigger component to trigger the modal
 * @param title modal title
 * @param content modal content
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function BaseModal(
  {
    trigger = <Button className={'bg-primary text-white'}>Hello</Button>,
    title = '',
    content = null,
  }
    : Readonly<{
    trigger?: ReactNode,
    title: string,
    content: ReactNode
  }>
) {

  const [open, setOpen] = useState(false)


  //  RENDER

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <DialogTrigger asChild>
        {trigger}
      </DialogTrigger>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>{title}</DialogTitle>
          <DialogDescription>
            Adding a new account will include it as part of your portfolio. If you do not wish to track your account in your portfolio,
            mark it as inactive. These settings can be changed at anytime from the account page.
          </DialogDescription>
        </DialogHeader>
        <div className="grid gap-4 py-4">
          <SepalModalContext.Provider value={{ open, setOpen }}>
            {content}
          </SepalModalContext.Provider>
        </div>
        {/*<DialogFooter>
          <Button type="submit" className={'bg-primary text-white'}>Save changes</Button>
        </DialogFooter>*/}
      </DialogContent>
    </Dialog>
  )
}