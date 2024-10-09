import {createContext, useContext} from "react";

//  page information context
export const SepalPageInfoContext = createContext<PageInfoContext | undefined>(undefined);

export const useSepalPageInfoContext = () => {
  const context = useContext(SepalPageInfoContext);

  if (context === undefined) {
    throw new Error('context not found');
  }

  return context;
};

//  modal context
export const SepalModalContext = createContext<ModalContext | undefined>(undefined)

export const useSepalModalContext = () => {
  const context = useContext(SepalModalContext);

  if (context === undefined) {
    throw new Error('context not found');
  }

  return context;
}