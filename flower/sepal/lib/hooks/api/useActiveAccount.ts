import { useSearchParams } from "next/navigation";
import { useMemo } from "react";
import { useActivePortfolio } from "./useActivePortoflio";
import { getAccountNumber } from "@/lib/functions/util-functions";
import { Account } from "@/types/apiTypes";

export function useActiveAccount() {
  const searchParams = useSearchParams();
  const {
    isLoading: isPortfolioLoading,
    isError,
    error,
    activePortfolio,
    hasMismatch: hasPortfolioMismatch,
  } = useActivePortfolio();

  const accountNumber = useMemo(() => {
    return getAccountNumber(searchParams, activePortfolio?.accounts ?? []);
  }, [searchParams, activePortfolio]);

  const activeAccount: Account | null = useMemo(() => {
    return (
      activePortfolio?.accounts?.find(
        (a) => a.accountNumber === accountNumber,
      ) ?? null
    );
  }, [activePortfolio, accountNumber]);

  const hasAccountMismatch =
    !isPortfolioLoading && !!activePortfolio && !activeAccount;
  return {
    isLoading: isPortfolioLoading,
    isError,
    error,
    activeAccount,
    activePortfolio,
    hasMismatch: hasPortfolioMismatch || hasAccountMismatch,
  };
}
