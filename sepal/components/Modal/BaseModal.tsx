import {
  Dialog,
  DialogContent,
  DialogDescription, DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog"
import {Button} from "@/components/ui/button";
import {ReactNode} from "react";

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
  return (
    <Dialog>
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
          {content}
        </div>
        {/*<DialogFooter>
          <Button type="submit" className={'bg-primary text-white'}>Save changes</Button>
        </DialogFooter>*/}
      </DialogContent>
    </Dialog>
  )
}