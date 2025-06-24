"use client";

import Link from "next/link";
import { Ellipsis, Icon, Loader2 } from "lucide-react";
import { redirect, usePathname } from "next/navigation";

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
import { useHealthCheckQuery } from "@/lib/hooks/query/queries";
import LoadingPage from "@/app/loading";
import { logErrors } from "@/lib/functions/util-functions";
import ErrorPage from "@/app/error";
import { resolveIcon } from "@/lib/functions/util-component-functions";
import { Icons } from "@/lib/enums";
import { useLogoutMutation } from "@/lib/hooks/query/mutations";
import React, { useEffect } from "react";
import { AUTH_ENABLED } from "@/lib/constants";

interface MenuProps {
  isOpen: boolean | undefined;
}

/**
 * Renders the navigation menu
 *
 * @param isOpen is open or closed
 * @author Stephen Prizio
 * @version 0.2.6
 */
export function Menu({ isOpen }: Readonly<MenuProps>) {
  const {
    mutate: logout,
    isError: isLogoutError,
    isPending: isLogoutLoading,
    error: logoutError,
    isSuccess: isLogoutSuccess,
  } = useLogoutMutation();
  const pathname = usePathname();
  const menuList = getMenuList(pathname);
  const { data, isError, isLoading, error } = useHealthCheckQuery();
  const footerItemStyles = "w-full text-center text-sm text-foreground";
  const showBuildVersion = process.env.ENABLE_BUILD_VERSION === "true";

  useEffect(() => {
    if (isLogoutSuccess) {
      redirect("/login");
    }
  }, [isLogoutSuccess]);

  function matchHref(val: string, href: string) {
    if (href.includes("?")) {
      return pathname.startsWith(href.substring(0, href.indexOf("?")));
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

  if (isLoading) {
    return <LoadingPage />;
  }

  if (isError || isLogoutError) {
    logErrors(error, logoutError);
    return <ErrorPage />;
  }

  //  RENDER

  return (
    <nav className="mt-8 h-full w-full flex flex-col">
      <div>
        <ul className="flex flex-col items-start space-y-1 px-2  w-full">
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
                            <TooltipContent side="right">
                              {label}
                            </TooltipContent>
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
          {AUTH_ENABLED && (
            <li className={"w-full"}>
              <div className="w-full">
                <TooltipProvider disableHoverableContent>
                  <Tooltip delayDuration={100}>
                    <TooltipTrigger asChild>
                      <Button
                        variant={"ghost"}
                        className="w-full justify-start h-10 mb-1"
                        asChild
                        onClick={() => logout()}
                      >
                        <Link href={"/"}>
                          <span className={cn(isOpen === false ? "" : "mr-4")}>
                            {isLogoutLoading ? (
                              <Loader2 className="animate-spin" size={18} />
                            ) : (
                              resolveIcon(Icons.Logout, "rotate-180", 18)
                            )}
                          </span>
                          <p
                            className={cn(
                              "max-w-[200px] truncate",
                              isOpen === false
                                ? "-translate-x-96 opacity-0"
                                : "translate-x-0 opacity-100",
                            )}
                          >
                            {isLogoutLoading ? "Signing out" : "Sign Out"}
                          </p>
                        </Link>
                      </Button>
                    </TooltipTrigger>
                    {isOpen === false && (
                      <TooltipContent side="right">{"Sign out"}</TooltipContent>
                    )}
                  </Tooltip>
                </TooltipProvider>
              </div>
            </li>
          )}
        </ul>
      </div>
      <div
        className={"grow flex flex-col space-y-1 mt-2 items-end justify-end"}
      >
        {isOpen && showBuildVersion && (
          <ul
            className={
              "flex flex-col items-start space-y-1 px-2 w-full text-center"
            }
          >
            <li className={"font-bold text-primary " + footerItemStyles}>
              sepal
              <span className={"font-normal text-foreground"}>
                &nbsp;-&nbsp;v{process.env.SEPAL_VERSION ?? "0"}
              </span>
            </li>
            <li className={"font-bold text-primary " + footerItemStyles}>
              greenhouse
              <span className={"font-normal text-foreground"}>
                &nbsp;-&nbsp;v{data?.version}
              </span>
            </li>
          </ul>
        )}
      </div>
    </nav>
  );
}
