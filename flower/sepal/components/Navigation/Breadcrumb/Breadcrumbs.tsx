"use client";

import React from "react";
import { AppLink } from "@/types/uiTypes";
import {
  Breadcrumb,
  BreadcrumbEllipsis,
  BreadcrumbItem,
  BreadcrumbLink,
  BreadcrumbList,
  BreadcrumbPage,
  BreadcrumbSeparator,
} from "@/components/ui/breadcrumb";
import {
  Drawer,
  DrawerClose,
  DrawerContent,
  DrawerDescription,
  DrawerFooter,
  DrawerHeader,
  DrawerTitle,
  DrawerTrigger,
} from "@/components/ui/drawer";
import Link from "next/link";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { Button } from "@/components/ui/button";
import { useMediaQuery } from "usehooks-ts";

/**
 * Renders the breadcrumbs for any page
 *
 * @param links breadcrumbs
 * @param count items to display before rendering a dropdown
 * @author Stephen Prizio
 * @version 0.2.4
 */
export default function Breadcrumbs({
  links = [],
  count = 3,
}: Readonly<{
  links: Array<AppLink>;
  count?: number;
}>) {
  const [open, setOpen] = React.useState(false);
  const isDesktop = useMediaQuery("(min-width: 768px)");
  const wrapRules = "px-1 max-w-[200px] truncate md:max-w-none";

  //  RENDER

  if (links?.length === 0) {
    throw new Error("No breadcrumbs provided.");
  }

  return (
    <Breadcrumb>
      <BreadcrumbList>
        {links.length >= count && (
          <>
            <BreadcrumbItem>
              <BreadcrumbLink asChild>
                <Link href={links[0].href ?? "/"}>{links[0].label}</Link>
              </BreadcrumbLink>
            </BreadcrumbItem>
            <BreadcrumbSeparator />
          </>
        )}
        {links.length > count ? (
          <>
            <BreadcrumbItem>
              {isDesktop ? (
                <DropdownMenu open={open} onOpenChange={setOpen}>
                  <DropdownMenuTrigger
                    className="flex items-center gap-1"
                    aria-label="Toggle menu"
                  >
                    <BreadcrumbEllipsis className="size-4" />
                  </DropdownMenuTrigger>
                  <DropdownMenuContent align="start">
                    {links.slice(1, -2).map((item, index) => (
                      <DropdownMenuItem key={index}>
                        <Link href={item.href ? item.href : "#"}>
                          {item.label}
                        </Link>
                      </DropdownMenuItem>
                    ))}
                  </DropdownMenuContent>
                </DropdownMenu>
              ) : (
                <Drawer open={open} onOpenChange={setOpen}>
                  <DrawerTrigger aria-label="Toggle Menu">
                    <BreadcrumbEllipsis className="h-4 w-4" />
                  </DrawerTrigger>
                  <DrawerContent>
                    <DrawerHeader className="text-left">
                      <DrawerTitle>Navigate to</DrawerTitle>
                      <DrawerDescription>
                        Select a page to navigate to.
                      </DrawerDescription>
                    </DrawerHeader>
                    <div className="grid gap-1 px-4">
                      {links.slice(1, -2).map((item, index) => (
                        <Link
                          key={index}
                          href={item.href ? item.href : "#"}
                          className="py-1 text-sm"
                        >
                          {item.label}
                        </Link>
                      ))}
                    </div>
                    <DrawerFooter className="pt-4">
                      <DrawerClose asChild>
                        <Button variant="outline">Close</Button>
                      </DrawerClose>
                    </DrawerFooter>
                  </DrawerContent>
                </Drawer>
              )}
            </BreadcrumbItem>
            <BreadcrumbSeparator />
          </>
        ) : null}
        {links.slice(-count + 1).map((item, index) => (
          <BreadcrumbItem key={index}>
            {!item.active ? (
              <>
                <BreadcrumbLink asChild className={wrapRules}>
                  <Link href={item.href}>{item.label}</Link>
                </BreadcrumbLink>
                <BreadcrumbSeparator />
              </>
            ) : (
              <BreadcrumbPage className={wrapRules + " text-primary font-bold"}>
                {item.label}
              </BreadcrumbPage>
            )}
          </BreadcrumbItem>
        ))}
        {/*{links.length > 0 &&
          links.map((link, idx) => {
            const isLast = idx + 1 === links.length;
            return (
              <>
                <BreadcrumbItem key={link.href}>
                  {link.active && (
                    <BreadcrumbPage
                      className={wrapRules + " text-primary font-bold"}
                    >
                      {link.label}
                    </BreadcrumbPage>
                  )}
                  {!link.active && (
                    <BreadcrumbLink asChild>
                      <Link href={link.href} className={wrapRules}>
                        {link.label}
                      </Link>
                    </BreadcrumbLink>
                  )}
                </BreadcrumbItem>
                {!isLast && <BreadcrumbSeparator />}
              </>
            );
          })}*/}
      </BreadcrumbList>
    </Breadcrumb>
  );
}
