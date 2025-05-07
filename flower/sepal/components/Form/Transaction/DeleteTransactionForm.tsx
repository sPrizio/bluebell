'use client'

import {useEffect} from "react";
import {useSepalModalContext} from "@/lib/context/SepalContext";
import {useRouter} from "next/navigation";
import {useToast} from "@/lib/hooks/ui/use-toast";
import {delay, logErrors} from "@/lib/functions/util-functions";
import {Button} from "@/components/ui/button";
import {Loader2} from "lucide-react";
import {Account, Transaction} from "@/types/apiTypes";
import {useDeleteTransactionMutation} from "@/lib/hooks/query/mutations";

/**
 * Renders a form for deleting transactions
 *
 * @param account Account
 * @param transaction
 * @author Stephen Prizio
 * @version 0.2.0
 */
export default function DeleteTransactionForm(
  {
    account,
    transaction
  }
    : Readonly<{
    account: Account | null | undefined,
    transaction: Transaction | null | undefined
  }>
) {

  const { toast } = useToast();
  const { setOpen } = useSepalModalContext()
  const router = useRouter();
  const {
    mutate: deleteTransaction,
    isPending: isDeleteTransactionLoading,
    isSuccess: isDeleteTransactionSuccess,
    isError: isDeleteTransactionError,
    error: deleteTransactionError
  } = useDeleteTransactionMutation(account?.accountNumber ?? -1)

  useEffect(() => {
    if (isDeleteTransactionSuccess) {
      toast({
        title: 'Deletion Successful!',
        description: 'The transaction was successfully deleted.',
        variant: 'success'
      })

      setOpen(false)
      router.push(`/transactions?account=${account?.accountNumber ?? -1}`)
    } else if (isDeleteTransactionError) {
      toast({
        title: 'Deletion Failed!',
        description: 'An error occurred while updating the transaction. Please try again.',
        variant: 'danger'
      })

      logErrors(deleteTransactionError)
      setOpen(false)
    }
  }, [isDeleteTransactionSuccess, isDeleteTransactionError]);


  //  GENERAL FUNCTIONS

  /**
   * Deletes the transaction
   */
  async function handleDelete() {
    //  TODO: temp
    await delay(2000)
    deleteTransaction({ transactionName: transaction?.name ?? '', transactionDate: transaction?.transactionDate ?? '' })
  }


  //  RENDER

  return (
    <div>
      <div className={'mb-3'}>Are you sure you want to delete this transaction? This action cannot be undone.</div>
      <div className={'flex w-full justify-end items-center gap-4'}>
        <Button type="submit" className={'bg-primaryRed hover:bg-primaryRedLight text-white'} disabled={isDeleteTransactionLoading} onClick={handleDelete}>
          {isDeleteTransactionLoading ? <Loader2 className="mr-2 h-4 w-4 animate-spin"/> : null}
          Delete
        </Button>
        <Button type="button" className={'border border-gray-400'} variant={"outline"} onClick={() => setOpen(false)}>
          Cancel
        </Button>
      </div>
    </div>
  )
}