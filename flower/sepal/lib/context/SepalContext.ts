import {createContext, useContext} from "react";
import {ModalContext} from "@/types/uiTypes";

//  modal context
export const SepalModalContext = createContext<ModalContext | undefined>(undefined)

export const useSepalModalContext = () => {
  const context = useContext(SepalModalContext);

  if (context === undefined) {
    throw new Error('context not found');
  }

  return context;
}