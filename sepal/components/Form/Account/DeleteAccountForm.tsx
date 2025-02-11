'use client'

import {Button} from "@/components/ui/button";
import {Loader2} from "lucide-react";
import {useEffect, useState} from "react";
import {useSepalModalContext} from "@/lib/context/SepalContext";
import {toast} from "@/hooks/use-toast";
import {useRouter} from 'next/navigation'
import {deleteAccount} from "@/lib/functions/account-functions";
import {Account} from "@/types/apiTypes";
import {delay} from "@/lib/functions/util-functions";

/**
 * Renders a form for deleting accounts
 *
 * @param account account
 * @author Stephen Prizio
 * @version 0.0.2
 */
export default function DeleteAccountForm(
  {
    account,
  }
    : Readonly<{
    account: Account,
  }>
) {

  const [isLoading, setIsLoading] = useState(false)
  const { open, setOpen } = useSepalModalContext()
  const [success, setSuccess] = useState<'success' | 'failed' | 'undefined'>('undefined')
  const router = useRouter();

  useEffect(() => {
    if (success === 'success') {
      toast(
        {
          title: 'Deletion Successful!',
          description: 'Your trading account was successfully deleted.',
          variant: 'success'
        }
      )
    } else if (success === 'failed') {
      toast(
        {
          title: 'Deletion Failed!',
          description: 'An error occurred while deleting your trading account. Please try again.',
          variant: 'danger'
        }
      )
    }
  }, [success]);


  //  GENERAL FUNCTIONS

  /**
   * Deletes the account
   */
  async function handleDelete() {

    setIsLoading(true)

    const data = await deleteAccount(account.accountNumber)
    if (data) {
      setSuccess('success')
      setIsLoading(false)
      setOpen(false)
      router.push('/accounts')
      await delay(1000)
      window.location.reload()
    } else {
      setSuccess('failed')
      setIsLoading(false)
      setOpen(false)
    }
  }


  //  RENDER

  return (
    <div>
      <div className={'mb-3'}>Are you sure you want to delete this account? This action cannot be undone.</div>
      <div className={'flex w-full justify-end items-center gap-4'}>
        <Button type="submit" className={'bg-primaryRed hover:bg-primaryRedLight text-white'} disabled={isLoading} onClick={handleDelete}>
          {isLoading ? <Loader2 className="mr-2 h-4 w-4 animate-spin"/> : null}
          Delete
        </Button>
        <Button type="button" className={'border border-gray-400'} variant={"outline"} onClick={() => setOpen(false)}>
          Cancel
        </Button>
      </div>
    </div>
  )
}