import {usePortfolioStore} from '@/lib/store/portfolioStore';
import {Portfolio} from '@/types/apiTypes';
import {useMemo} from 'react';
import {useUserQuery} from '../query/queries';

export function useActivePortfolio() {
  const { data: user, isError, error, isLoading } = useUserQuery();
  const { selectedPortfolioId } = usePortfolioStore();

  const activePortfolio: Portfolio | null = useMemo(() => {
    if (!user || !selectedPortfolioId || selectedPortfolioId === -1) {
      return null;
    }

    return user.portfolios?.find(p => p.portfolioNumber === selectedPortfolioId) ?? null;
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
