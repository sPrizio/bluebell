import {
  ArrowRightLeft,
  BrainIcon,
  BriefcaseBusinessIcon,
  ChartPie,
  ChartScatter,
  CircleUserRound,
  LayoutDashboard,
  LucideIcon,
  Newspaper,
  ReplaceAll,
  Search,
} from "lucide-react";
import { UserPrivilege } from "@/lib/enums";

type Submenu = {
  href: string;
  label: string;
  active?: boolean;
};

type Menu = {
  href: string;
  label: string;
  active?: boolean;
  icon: LucideIcon;
  submenus?: Submenu[];
  privilege: UserPrivilege;
};

type Group = {
  groupLabel: string;
  privilege: UserPrivilege;
  menus: Menu[];
};

export function getMenuList(pathname: string): Group[] {
  return [
    {
      groupLabel: "",
      privilege: UserPrivilege.TRADER,
      menus: [
        {
          href: "/dashboard",
          label: "Dashboard",
          icon: LayoutDashboard,
          submenus: [],
          privilege: UserPrivilege.TRADER,
        },
        {
          href: "/portfolios",
          label: "Portfolios",
          icon: BriefcaseBusinessIcon,
          submenus: [],
          privilege: UserPrivilege.TRADER,
        },
      ],
    },
    {
      groupLabel: "Portfolio Information",
      privilege: UserPrivilege.TRADER,
      menus: [
        {
          href: "/accounts",
          label: "Accounts",
          icon: ChartPie,
          privilege: UserPrivilege.TRADER,
        },
        {
          href: "/transactions?account=default",
          label: "Transactions",
          icon: ArrowRightLeft,
          privilege: UserPrivilege.TRADER,
        },
        {
          href: "/performance?account=default",
          label: "Performance",
          icon: ChartScatter,
          privilege: UserPrivilege.TRADER,
        },
        {
          href: "/analysis?account=default",
          label: "Analysis",
          icon: Search,
          privilege: UserPrivilege.TRADER,
        },
        {
          href: "/trades?account=default",
          label: "Trades",
          icon: ReplaceAll,
          privilege: UserPrivilege.TRADER,
        },
      ],
    },
    {
      groupLabel: "Insights",
      privilege: UserPrivilege.TRADER,
      menus: [
        {
          href: "/news",
          label: "Market News",
          icon: Newspaper,
          privilege: UserPrivilege.TRADER,
        },
      ],
    },
    {
      groupLabel: "System",
      privilege: UserPrivilege.SYSTEM,
      menus: [
        {
          href: "/jobs",
          label: "Jobs",
          icon: BrainIcon,
          privilege: UserPrivilege.SYSTEM,
        },
      ],
    },
    {
      groupLabel: "Settings",
      privilege: UserPrivilege.TRADER,
      menus: [
        {
          href: "/profile",
          label: "My Profile",
          icon: CircleUserRound,
          privilege: UserPrivilege.TRADER,
        },
      ],
    },
  ];
}
