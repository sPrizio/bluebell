import {useMutation, useQueryClient} from "@tanstack/react-query"
import {Account} from "@/types/apiTypes";
import {del, post, postFile, put} from "../../functions/client";
import {ApiUrls} from "../../constants";

export const useCreateAccountMutation = (portfolioNumber: number) => {
  const queryClient = useQueryClient()
  return useMutation<Account, Error, any>({
    mutationFn: (payload) => post<Account>(ApiUrls.Account.CreateAccount, { portfolioNumber: portfolioNumber }, payload),
    onSuccess: (data) => {
      queryClient.invalidateQueries({queryKey: ['user']});
      queryClient.invalidateQueries({queryKey: ['portfolio']});
      queryClient.invalidateQueries({queryKey: ['portfolio-record']});
      queryClient.invalidateQueries({queryKey: ['recent-transactions']});
    }
  })
}

export const useUpdateAccountMutation = (portfolioNumber: number, accNumber: number) => {
  const queryClient = useQueryClient()
  return useMutation<Account, Error, any>({
    mutationFn: (payload) => put<Account>(ApiUrls.Account.UpdateAccount, { portfolioNumber: portfolioNumber, accountNumber: accNumber.toString() }, payload),
    onSuccess: (data) => {
      queryClient.invalidateQueries({queryKey: ['user']});
      queryClient.invalidateQueries({queryKey: ['portfolio']});
      queryClient.invalidateQueries({queryKey: ['portfolio-record']});
      queryClient.invalidateQueries({queryKey: ['recent-transactions']});
    }
  })
}

export const useDeleteAccountMutation = (portfolioNumber: number, accNumber: number) => {
  const queryClient = useQueryClient()
  return useMutation<boolean, Error, any>({
    mutationFn: () => del<boolean>(ApiUrls.Account.DeleteAccount, { portfolioNumber: portfolioNumber, accountNumber: accNumber.toString() }),
    onSuccess: (data) => {
      queryClient.invalidateQueries({queryKey: ['user']});
      queryClient.invalidateQueries({queryKey: ['portfolio']});
      queryClient.invalidateQueries({queryKey: ['portfolio-record']});
      queryClient.invalidateQueries({queryKey: ['recent-transactions']});
    }
  })
}

export const useImportTradesMutation = (accNumber: number) => {
  const queryClient = useQueryClient()

  return useMutation<boolean, Error, any>({
    mutationFn: (payload) => {
      const formData = new FormData();
      formData.append('file', payload.filename[0] ?? '');
      formData.append('fileName', payload.filename[0].name ?? '');
      console.log(payload.filename[0])

      return postFile<boolean>(ApiUrls.Trade.ImportTrades, { accountNumber: accNumber.toString(), isStrategy: payload.isStrategy.toString() }, formData)
    },
    onSuccess: (data) => {
      queryClient.invalidateQueries({queryKey: ['user']});
    }
  })
}

export const useFetchMarketNewsMutation = () => {
  return useMutation<boolean, Error, any>({
    mutationFn: (payload) => post<boolean>(ApiUrls.News.FetchNews, {}, {})
  })
}