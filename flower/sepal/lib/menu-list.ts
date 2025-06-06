import {
  ArrowRightLeft,
  BrainIcon,
  BriefcaseBusinessIcon,
  ChartPie,
  ChartScatter,
  CircleUserRound,
  LayoutDashboard,
  LogOut,
  LucideIcon,
  Newspaper,
  ReplaceAll,
  Search,
} from "lucide-react";

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
};

type Group = {
  groupLabel: string;
  menus: Menu[];
};

export function getMenuList(pathname: string): Group[] {
  return [
    {
      groupLabel: "",
      menus: [
        {
          href: "/dashboard",
          label: "Dashboard",
          icon: LayoutDashboard,
          submenus: [],
        },
        {
          href: "/portfolios",
          label: "Portfolios",
          icon: BriefcaseBusinessIcon,
          submenus: [],
        },
      ],
    },
    {
      groupLabel: "Portfolio Information",
      menus: [
        {
          href: "/accounts",
          label: "Accounts",
          icon: ChartPie,
          /*submenus: [
            {
              href: "/posts",
              label: "All Posts"
            },
            {
              href: "/posts/new",
              label: "New Post"
            }
          ]*/
        },
        {
          href: "/transactions?account=default",
          label: "Transactions",
          icon: ArrowRightLeft,
        },
        {
          href: "/performance?account=default",
          label: "Performance",
          icon: ChartScatter,
        },
        {
          href: "/analysis?account=default",
          label: "Analysis",
          icon: Search,
        },
        {
          href: "/trades?account=default",
          label: "Trades",
          icon: ReplaceAll,
        },
      ],
    },
    {
      groupLabel: "Insights",
      menus: [
        {
          href: "/news",
          label: "Market News",
          icon: Newspaper,
        },
      ],
    },
    {
      groupLabel: "System",
      menus: [
        {
          href: "/jobs",
          label: "Jobs",
          icon: BrainIcon,
        },
      ],
    },
    {
      groupLabel: "Settings",
      menus: [
        {
          href: "/profile",
          label: "My Profile",
          icon: CircleUserRound,
        },
        {
          href: "/logout",
          label: "Sign Out",
          icon: LogOut,
        },
      ],
    },
  ];
}
