import {
  ArrowRightLeft,
  ChartPie,
  ChartScatter,
  CircleUserRound,
  LayoutDashboard,
  LogOut,
  LucideIcon,
  Newspaper, ReplaceAll
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
          submenus: []
        }
      ]
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
          href: "/transactions",
          label: "Transactions",
          icon: ArrowRightLeft
        },
        {
          href: "/performance",
          label: "Performance",
          icon: ChartScatter
        },
        {
          href: "/trades",
          label: "Trades",
          icon: ReplaceAll
        }
      ]
    },
    {
      groupLabel: "Insights",
      menus: [
        {
          href: "/market-news",
          label: "Market News",
          icon: Newspaper
        },
      ]
    },
    {
      groupLabel: "Settings",
      menus: [
        {
          href: "/profile",
          label: "My Profile",
          icon: CircleUserRound
        },
        {
          href: "/logout",
          label: "Sign Out",
          icon: LogOut
        },
      ]
    }
  ];
}
