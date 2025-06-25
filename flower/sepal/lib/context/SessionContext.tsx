"use client";

import React, { createContext, useContext } from "react";
import { SessionData } from "@/lib/auth/session";

export const SessionContext = createContext<SessionData | undefined | null>(
  null,
);

export const useSessionContext = () => useContext(SessionContext);
