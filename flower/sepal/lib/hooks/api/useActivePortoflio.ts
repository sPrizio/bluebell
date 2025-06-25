import { usePortfolioStore } from "@/lib/store/portfolioStore";
import { Portfolio } from "@/types/apiTypes";
import { useMemo } from "react";
import { useUserQuery } from "../query/queries";
import { useSessionContext } from "@/lib/context/SessionContext";

/**
 * Gets the active portfolio
 */
export function useActivePortfolio() {
  const session = useSessionContext();
  const {
    data: user,
    isError,
    error,
    isLoading,
  } = useUserQuery(session?.username ?? "");
  const { selectedPortfolioId } = usePortfolioStore();

  const activePortfolio: Portfolio | null = useMemo(() => {
    if (!user || !selectedPortfolioId || selectedPortfolioId === -1) {
      return null;
    }

    return (
      user.portfolios?.find((p) => p.portfolioNumber === selectedPortfolioId) ??
      null
    );
  }, [user, selectedPortfolioId]);

  const hasMismatch = !isLoading && !!user && !activePortfolio;
  return {
    isLoading,
    isError,
    error,
    activePortfolio,
    hasMismatch,
  };
}
