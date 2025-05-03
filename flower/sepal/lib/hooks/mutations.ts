import {useMutation, useQueryClient} from "@tanstack/react-query"
import {Account} from "@/types/apiTypes";
import {del, post, put} from "../functions/client";
import {ApiUrls} from "../constants";
import {useToast} from "@/hooks/use-toast";

export const useCreateAccountMutation = (portfolioNumber: number) => {
  const queryClient = useQueryClient()
  const toast = useToast();

  return useMutation<Account, Error, any>({
    mutationFn: (payload) => post<Account>(ApiUrls.Account.CreateAccount, { portfolioNumber: portfolioNumber }, payload),
    onSuccess: (data) => {
      // @ts-ignore
      toast({
        title: 'Account Created!',
        description: 'Your new trading account was successfully created.',
        variant: 'success'
      })

      queryClient.invalidateQueries({queryKey: ['user']});
      queryClient.invalidateQueries({queryKey: ['portfolio']});
      queryClient.invalidateQueries({queryKey: ['portfolio-record']});
      queryClient.invalidateQueries({queryKey: ['recent-transactions']});
    },
    onError: (err) => {
      // @ts-ignore
      toast({
        title: 'Creation Failed!',
        description: 'An error occurred while creating new trading Account. Please check your inputs and try again.',
        variant: 'danger'
      })
    },
  })
}

export const useUpdateAccountMutation = (portfolioNumber: number, accNumber: number) => {
  const queryClient = useQueryClient()
  const toast = useToast();

  return useMutation<Account, Error, any>({
    mutationFn: (payload) => put<Account>(ApiUrls.Account.UpdateAccount, { portfolioNumber: portfolioNumber, accountNumber: accNumber.toString() }, payload),
    onSuccess: (data) => {
      // @ts-ignore
      toast({
        title: 'Account Updated!',
        description: 'Your trading account was successfully updated.',
        variant: 'success'
      })

      queryClient.invalidateQueries({queryKey: ['user']});
      queryClient.invalidateQueries({queryKey: ['portfolio']});
      queryClient.invalidateQueries({queryKey: ['portfolio-record']});
      queryClient.invalidateQueries({queryKey: ['recent-transactions']});
    },
    onError: (err) => {
      // @ts-ignore
      toast({
        title: 'Update Failed!',
        description: 'An error occurred while updating your trading account. Please check your inputs and try again.',
        variant: 'danger'
      })
    },
  })
}

export const useDeleteAccountMutation = (portfolioNumber: number, accNumber: number) => {

  const queryClient = useQueryClient()
  const toast = useToast();

  return useMutation<boolean, Error, any>({
    mutationFn: (payload) => del<boolean>(ApiUrls.Account.DeleteAccount, { portfolioNumber: portfolioNumber, accountNumber: accNumber.toString() }),
    onSuccess: (data) => {
      // @ts-ignore
      toast({
        title: 'Deletion Successful!',
        description: 'Your trading account was successfully deleted.',
        variant: 'success'
      })

      queryClient.invalidateQueries({queryKey: ['user']});
      queryClient.invalidateQueries({queryKey: ['portfolio']});
      queryClient.invalidateQueries({queryKey: ['portfolio-record']});
      queryClient.invalidateQueries({queryKey: ['recent-transactions']});
    },
    onError: (err) => {
      // @ts-ignore
      toast({
        title: 'Deletion Failed!',
        description: 'An error occurred while deleting your trading account. Please try again.',
        variant: 'danger'
      })
    },
  })
}