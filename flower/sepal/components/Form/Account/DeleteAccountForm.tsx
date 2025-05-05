'use client'

import {Button} from "@/components/ui/button";
import {Loader2} from "lucide-react";
import {useEffect} from "react";
import {useSepalModalContext} from "@/lib/context/SepalContext";
import {useToast} from "@/hooks/use-toast";
import {useRouter} from 'next/navigation'
import {Account} from "@/types/apiTypes";
import {logErrors} from "@/lib/functions/util-functions";
import {useDeleteAccountMutation} from "@/lib/hooks/mutations";

/**
 * Renders a form for deleting accounts
 *
 * @param account account
 * @author Stephen Prizio
 * @version 0.2.0
 */
export default function DeleteAccountForm(
  {
    account,
  }
  : Readonly<{
    account: Account,
  }>
) {

  const {toast} = useToast();
  const {
    mutate: deleteAccount,
    isPending: isDeleteAccountLoading,
    isSuccess: isDeleteAccountSuccess,
    isError: isDeleteAccountError,
    error: deleteAccountError
  } = useDeleteAccountMutation(account.portfolioNumber, account.accountNumber)

  const {setOpen} = useSepalModalContext()
  const router = useRouter();

  useEffect(() => {
    if (isDeleteAccountSuccess) {
      toast({
          title: 'Deletion Successful!',
          description: 'Your trading account was successfully deleted.',
          variant: 'success'
        })

      setOpen(false)
      router.push('/accounts')
    } else if (isDeleteAccountError) {
      toast({
          title: 'Deletion Failed!',
          description: 'An error occurred while deleting your trading account. Please try again.',
          variant: 'danger'
        })

      logErrors(deleteAccountError)
      setOpen(false)
    }
  }, [isDeleteAccountSuccess, isDeleteAccountError]);


  //  RENDER

  return (
    <div>
      <div className={'mb-3'}>Are you sure you want to delete this account? This action cannot be undone.</div>
      <div className={'flex w-full justify-end items-center gap-4'}>
        <Button type="submit" className={'bg-primaryRed hover:bg-primaryRedLight text-white'} disabled={isDeleteAccountLoading}
                onClick={deleteAccount}>
          {isDeleteAccountLoading ? <Loader2 className="mr-2 h-4 w-4 animate-spin"/> : null}
          Delete
        </Button>
        <Button type="button" className={'border border-gray-400'} variant={"outline"} onClick={() => setOpen(false)}>
          Cancel
        </Button>
      </div>
    </div>
  )
}