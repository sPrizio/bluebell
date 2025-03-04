'use client'

import React, {createContext, useContext, useState} from "react";

const AppContext = createContext<any>(undefined);

export function AppWrapper({ children } : Readonly<{ children: React.ReactNode }>) {

  const [modalActive, setModalActive] = useState(false);


  //  RENDER

  return (
    <AppContext.Provider value={{
      modalActive,
      setModalActive
    }}>
      {children}
    </AppContext.Provider>
  )
}

export function useAppContext() {
  return useContext(AppContext);
}