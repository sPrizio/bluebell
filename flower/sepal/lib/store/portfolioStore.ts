import { create } from 'zustand'
import {persist} from "zustand/middleware";

type PortfolioStore = {
  selectedPortfolioId: number | null
  setSelectedPortfolioId: (id: number) => void
}

export const usePortfolioStore = create<PortfolioStore>()(
  persist(
    (set) => ({
      selectedPortfolioId: -1,
      setSelectedPortfolioId: (id) => set({ selectedPortfolioId: id }),
    }),
    {
      name: 'portfolio-storage',
    }
  )
)
