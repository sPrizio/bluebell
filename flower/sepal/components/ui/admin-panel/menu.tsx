"use client";

import Link from "next/link";
import { Ellipsis } from "lucide-react";
import { usePathname } from "next/navigation";

import { cn } from "@/lib/utils";
import { getMenuList } from "@/lib/menu-list";
import { Button } from "@/components/ui/button";
import { CollapseMenuButton } from "@/components/ui/admin-panel/collapse-menu-button";
import {
  Tooltip,
  TooltipContent,
  TooltipProvider,
  TooltipTrigger,
} from "@/components/ui/tooltip";

interface MenuProps {
  isOpen: boolean | undefined;
}

export function Menu({ isOpen }: Readonly<MenuProps>) {
  const pathname = usePathname();
  const menuList = getMenuList(pathname);

  function matchHref(val: string, href: string) {
    if (href.includes("?")) {
      return pathname.startsWith(href.substr(0, href.indexOf("?")));
    }

    return pathname.startsWith(href);
  }

  function generateTooltip(isOpen: boolean, groupLabel: string) {
    if (!isOpen && isOpen !== undefined && groupLabel) {
      return (
        <TooltipProvider>
          <Tooltip delayDuration={100}>
            <TooltipTrigger className="w-full">
              <div className="w-full flex justify-center items-center">
                <Ellipsis className="h-5 w-5" />
              </div>
            </TooltipTrigger>
            <TooltipContent side="right">
              <p>{groupLabel}</p>
            </TooltipContent>
          </Tooltip>
        </TooltipProvider>
      );
    } else {
      <p className="pb-2" />;
    }
  }

  //  RENDER

  return (
    <nav className="mt-8 h-full w-full">
      <ul className="flex flex-col min-h-[calc(100vh-48px-36px-16px-32px)] lg:min-h-[calc(100vh-32px-40px-32px)] items-start space-y-1 px-2">
        {menuList.map(({ groupLabel, menus }, index) => (
          <li
            className={cn("w-full", groupLabel ? "pt-5" : "")}
            key={index + 1}
          >
            {(isOpen && groupLabel) || isOpen === undefined ? (
              <p className="text-sm font-medium text-muted-foreground px-4 pb-2 max-w-[248px] truncate">
                {groupLabel}
              </p>
            ) : (
              generateTooltip(isOpen, groupLabel)
            )}
            {menus.map(
              ({ href, label, icon: Icon, active, submenus }, index) =>
                !submenus || submenus.length === 0 ? (
                  <div className="w-full" key={index + 1}>
                    <TooltipProvider disableHoverableContent>
                      <Tooltip delayDuration={100}>
                        <TooltipTrigger asChild>
                          <Button
                            variant={
                              (active === undefined &&
                                matchHref(pathname, href)) ||
                              active
                                ? "primary"
                                : "ghost"
                            }
                            className="w-full justify-start h-10 mb-1"
                            asChild
                          >
                            <Link href={href}>
                              <span
                                className={cn(isOpen === false ? "" : "mr-4")}
                              >
                                <Icon size={18} />
                              </span>
                              <p
                                className={cn(
                                  "max-w-[200px] truncate",
                                  isOpen === false
                                    ? "-translate-x-96 opacity-0"
                                    : "translate-x-0 opacity-100",
                                )}
                              >
                                {label}
                              </p>
                            </Link>
                          </Button>
                        </TooltipTrigger>
                        {isOpen === false && (
                          <TooltipContent side="right">{label}</TooltipContent>
                        )}
                      </Tooltip>
                    </TooltipProvider>
                  </div>
                ) : (
                  <div className="w-full" key={index + 1}>
                    <CollapseMenuButton
                      icon={Icon}
                      label={label}
                      active={active ?? matchHref(pathname, href)}
                      submenus={submenus}
                      isOpen={isOpen}
                    />
                  </div>
                ),
            )}
          </li>
        ))}
      </ul>
    </nav>
  );
}
