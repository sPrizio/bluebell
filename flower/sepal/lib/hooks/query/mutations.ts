import { useMutation, useQueryClient } from "@tanstack/react-query";
import { Account, Portfolio, Trade, Transaction, User } from "@/types/apiTypes";
import { del, post, postFile, put } from "../../functions/client";
import { ApiUrls } from "../../constants";

export const useLoginMutation = () => {
  const queryClient = useQueryClient();
  return useMutation<User, Error, any>({
    mutationFn: (payload) =>
      fetch(ApiUrls.Security.InternalLogin, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      }).then(async (res) => {
        if (!res.ok) {
          const error = await res.json();
          throw new Error(error.error ?? "Login failed");
        }
        return res.json();
      }),
    onSuccess: (data) => {
      queryClient.invalidateQueries({ queryKey: ["session"] });
    },
  });
};

export const useLogoutMutation = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: () =>
      fetch(ApiUrls.Security.InternalLogout, { method: "POST" }).then((res) => {
        if (!res.ok) throw new Error("Logout failed");
      }),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["session"] });
    },
  });
};

export const useIsUserTakenMutation = () => {
  return useMutation<boolean, Error, any>({
    mutationFn: (payload) =>
      post<boolean>(ApiUrls.Security.IsUserTaken, {}, payload),
    onSuccess: (data) => {},
  });
};

export const useCreateUserMutation = () => {
  const queryClient = useQueryClient();
  return useMutation<User, Error, any>({
    mutationFn: (payload) => post<User>(ApiUrls.User.RegisterUser, {}, payload),
    onSuccess: (data) => {
      queryClient.invalidateQueries({ queryKey: ["user"] });
    },
  });
};

export const useUpdateUserMutation = (username: string) => {
  const queryClient = useQueryClient();
  return useMutation<User, Error, any>({
    mutationFn: (payload) =>
      put<User>(ApiUrls.User.UpdateUser, { username: username }, payload),
    onSuccess: (data) => {
      queryClient.invalidateQueries({ queryKey: ["user"] });
    },
  });
};

export const useCreateAccountMutation = (portfolioNumber: number) => {
  const queryClient = useQueryClient();
  return useMutation<Account, Error, any>({
    mutationFn: (payload) =>
      post<Account>(
        ApiUrls.Account.CreateAccount,
        { portfolioNumber: portfolioNumber },
        payload,
      ),
    onSuccess: (data) => {
      queryClient.invalidateQueries({ queryKey: ["user"] });
      queryClient.invalidateQueries({ queryKey: ["portfolio"] });
      queryClient.invalidateQueries({ queryKey: ["portfolio-record"] });
      queryClient.invalidateQueries({ queryKey: ["recent-transactions"] });
    },
  });
};

export const useUpdateAccountMutation = (
  portfolioNumber: number,
  accNumber: number,
) => {
  const queryClient = useQueryClient();
  return useMutation<Account, Error, any>({
    mutationFn: (payload) =>
      put<Account>(
        ApiUrls.Account.UpdateAccount,
        {
          portfolioNumber: portfolioNumber,
          accountNumber: accNumber.toString(),
        },
        payload,
      ),
    onSuccess: (data) => {
      queryClient.invalidateQueries({ queryKey: ["user"] });
      queryClient.invalidateQueries({ queryKey: ["portfolio"] });
      queryClient.invalidateQueries({ queryKey: ["portfolio-record"] });
      queryClient.invalidateQueries({ queryKey: ["recent-transactions"] });
    },
  });
};

export const useDeleteAccountMutation = (
  portfolioNumber: number,
  accNumber: number,
) => {
  const queryClient = useQueryClient();
  return useMutation<boolean, Error, any>({
    mutationFn: () =>
      del<boolean>(ApiUrls.Account.DeleteAccount, {
        portfolioNumber: portfolioNumber,
        accountNumber: accNumber.toString(),
      }),
    onSuccess: (data) => {
      queryClient.invalidateQueries({ queryKey: ["user"] });
      queryClient.invalidateQueries({ queryKey: ["portfolio"] });
      queryClient.invalidateQueries({ queryKey: ["portfolio-record"] });
      queryClient.invalidateQueries({ queryKey: ["recent-transactions"] });
    },
  });
};

export const useCreatePortfolioMutation = () => {
  const queryClient = useQueryClient();
  return useMutation<Portfolio, Error, any>({
    mutationFn: (payload) =>
      post<Portfolio>(ApiUrls.Portfolio.CreatePortfolio, {}, payload),
    onSuccess: (data) => {
      queryClient.invalidateQueries({ queryKey: ["user"] });
    },
  });
};

export const useUpdatePortfolioMutation = (portfolioNumber: number) => {
  const queryClient = useQueryClient();
  return useMutation<Portfolio, Error, any>({
    mutationFn: (payload) =>
      put<Portfolio>(
        ApiUrls.Portfolio.UpdatePortfolio,
        { portfolioNumber: portfolioNumber },
        payload,
      ),
    onSuccess: (data) => {
      queryClient.invalidateQueries({ queryKey: ["user"] });
      queryClient.invalidateQueries({ queryKey: ["portfolio"] });
      queryClient.invalidateQueries({ queryKey: ["portfolio-record"] });
    },
  });
};

export const useDeletePortfolioMutation = (portfolioNumber: number) => {
  const queryClient = useQueryClient();
  return useMutation<boolean, Error, any>({
    mutationFn: () =>
      del<boolean>(ApiUrls.Portfolio.DeletePortfolio, {
        portfolioNumber: portfolioNumber,
      }),
    onSuccess: (data) => {
      queryClient.invalidateQueries({ queryKey: ["user"] });
      queryClient.invalidateQueries({ queryKey: ["portfolio"] });
      queryClient.invalidateQueries({ queryKey: ["portfolio-record"] });
    },
  });
};

export const useImportTradesMutation = (accNumber: number) => {
  const queryClient = useQueryClient();

  return useMutation<boolean, Error, any>({
    mutationFn: (payload) => {
      const formData = new FormData();
      formData.append("file", payload.filename[0] ?? "");
      formData.append("fileName", payload.filename[0].name ?? "");
      console.log(payload.filename[0]);

      return postFile<boolean>(
        ApiUrls.Trade.ImportTrades,
        {
          accountNumber: accNumber.toString(),
          isStrategy: payload.isStrategy.toString(),
        },
        formData,
      );
    },
    onSuccess: (data) => {
      queryClient.invalidateQueries({ queryKey: ["user"] });
    },
  });
};

export const useFetchMarketNewsMutation = () => {
  return useMutation<boolean, Error, any>({
    mutationFn: (payload) => post<boolean>(ApiUrls.News.FetchNews, {}, {}),
  });
};

export const useCreateTransactionMutation = (accountNumber: number) => {
  const queryClient = useQueryClient();
  return useMutation<Transaction, Error, any>({
    mutationFn: (payload) =>
      post<Transaction>(
        ApiUrls.Transaction.Create,
        { accountNumber: accountNumber.toString() },
        payload,
      ),
    onSuccess: (data) => {
      queryClient.invalidateQueries({
        queryKey: ["portfolio"],
      });
      queryClient.invalidateQueries({
        queryKey: ["account"],
      });
      queryClient.invalidateQueries({
        queryKey: ["paged-transactions"],
      });
    },
  });
};

export const useUpdateTransactionMutation = (
  accountNumber: number,
  transactionNumber: number,
) => {
  const queryClient = useQueryClient();
  return useMutation<Transaction, Error, any>({
    mutationFn: (payload) =>
      put<Transaction>(
        ApiUrls.Transaction.Update,
        {
          accountNumber: accountNumber.toString(),
          transactionNumber: transactionNumber.toString(),
        },
        payload,
      ),
    onSuccess: (data) => {
      queryClient.invalidateQueries({
        queryKey: ["portfolio"],
      });
      queryClient.invalidateQueries({
        queryKey: ["account"],
      });
      queryClient.invalidateQueries({
        queryKey: ["paged-transactions"],
      });
    },
  });
};

export const useDeleteTransactionMutation = (
  accountNumber: number,
  transactionNumber: number,
) => {
  const queryClient = useQueryClient();
  return useMutation<boolean, Error, any>({
    mutationFn: () =>
      del<boolean>(ApiUrls.Transaction.Delete, {
        accountNumber: accountNumber.toString(),
        transactionNumber: transactionNumber.toString(),
      }),
    onSuccess: (data) => {
      queryClient.invalidateQueries({
        queryKey: ["portfolio"],
      });
      queryClient.invalidateQueries({
        queryKey: ["account"],
      });
      queryClient.invalidateQueries({
        queryKey: ["paged-transactions"],
      });
    },
  });
};

export const useCreateTradeMutation = (accountNumber: number) => {
  const queryClient = useQueryClient();
  return useMutation<Trade, Error, any>({
    mutationFn: (payload) =>
      post<Trade>(
        ApiUrls.Trade.CreateTrade,
        { accountNumber: accountNumber.toString() },
        payload,
      ),
    onSuccess: (data) => {
      queryClient.invalidateQueries({
        queryKey: ["portfolio"],
      });
      queryClient.invalidateQueries({
        queryKey: ["account"],
      });
      queryClient.invalidateQueries({
        queryKey: ["paginated-trades"],
      });
    },
  });
};

export const useUpdateTradeMutation = (
  accountNumber: number,
  tradeId: string,
) => {
  const queryClient = useQueryClient();
  return useMutation<Trade, Error, any>({
    mutationFn: (payload) =>
      put<Trade>(
        ApiUrls.Trade.UpdateTrade,
        { accountNumber: accountNumber, tradeId: tradeId },
        payload,
      ),
    onSuccess: (data) => {
      queryClient.invalidateQueries({
        queryKey: ["trade", accountNumber.toString(), tradeId],
      });
      queryClient.invalidateQueries({
        queryKey: ["trade-insights", accountNumber.toString(), tradeId],
      });
      queryClient.invalidateQueries({
        queryKey: ["portfolio"],
      });
      queryClient.invalidateQueries({
        queryKey: ["account"],
      });
      queryClient.invalidateQueries({
        queryKey: ["paginated-trades"],
      });
    },
  });
};

export const useDeleteTradeMutation = (
  accountNumber: number,
  tradeId: string,
) => {
  const queryClient = useQueryClient();
  return useMutation<boolean, Error, any>({
    mutationFn: () =>
      del<boolean>(ApiUrls.Trade.DeleteTrade, {
        accountNumber: accountNumber.toString(),
        tradeId: tradeId,
      }),
    onSuccess: (data) => {
      queryClient.invalidateQueries({
        queryKey: ["account"],
      });
      queryClient.invalidateQueries({
        queryKey: ["portfolio"],
      });
      queryClient.invalidateQueries({
        queryKey: ["paginated-trades"],
      });
    },
  });
};
