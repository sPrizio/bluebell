import { AggregateInterval } from "@/lib/enums";
import React from "react";

export interface SidebarNavigationLinkType {
  label: string;
  href: string;
  icon: React.ReactNode;
}

export interface ModalContext {
  open: boolean;
  setOpen: React.Dispatch<React.SetStateAction<boolean>>;
}

export interface AppLink {
  href: string;
  label: string;
  active: boolean;
}

export interface UserTradeRecordControlSelection {
  aggInterval: AggregateInterval;
  month: string;
  year: string;
}

export interface UserTradeControlSelection {
  start: Date;
  end: Date;
  sort: "asc" | "desc";
  type: string;
  symbol: string;
}

export interface UserJobControlSelection {
  start: Date;
  end: Date;
  jobType: string;
  jobStatus: string;
  sort: "asc" | "desc";
}
