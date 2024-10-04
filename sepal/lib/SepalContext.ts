import {createContext, useContext} from "react";

export const SepalPageInfoContext = createContext<PageInfoContext | undefined>(undefined);

export const useSepalPageInfoContext = () => {
  const context = useContext(SepalPageInfoContext);

  if (context === undefined) {
    throw new Error('context not found');
  }

  return context;
};