'use client'

import {useEffect, useState} from "react";
import {useSepalModalContext} from "@/lib/context/SepalContext";
import {useRouter} from "next/navigation";
import {toast} from "@/hooks/use-toast";
import {delay} from "@/lib/functions/util-functions";
import {Button} from "@/components/ui/button";
import {Loader2} from "lucide-react";

/**
 * Renders a form for deleting transactions
 *
 * @param account Account
 * @param transaction
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function DeleteTransactionForm(
  {
    account,
    transaction
  }
    : Readonly<{
    account: Account,
    transaction: Transaction
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
          description: 'The transaction was successfully deleted.',
          variant: 'success'
        }
      )
    } else if (success === 'failed') {
      toast(
        {
          title: 'Deletion Failed!',
          description: 'An error occurred while updating the transaction. Please try again.',
          variant: 'danger'
        }
      )
    }
  }, [success]);


  //  GENERAL FUNCTIONS

  /**
   * Deletes the transaction
   */
  async function handleDelete() {

    setIsLoading(true)
    await delay(4000)
    setIsLoading(false)

    setSuccess('success')
    setOpen(false)
    router.push(`/transactions?account=${account.accountNumber}`)
  }


  //  RENDER

  return (
    <div>
      <div className={'mb-3'}>Are you sure you want to delete this transaction? This action cannot be undone.</div>
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