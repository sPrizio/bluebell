import { resolveIcon } from "@/lib/functions/util-component-functions";
import { DEFAULT_PAGE_HEADER_SECTION_ICON_SIZE } from "@/lib/constants";
import Breadcrumbs from "@/components/Navigation/Breadcrumb/Breadcrumbs";
import { AppLink } from "@/types/uiTypes";
import { Button } from "@/components/ui/button";
import Link from "next/link";
import React from "react";
import { Icons } from "@/lib/enums";

/**
 * A generic page header section
 *
 * @param iconCode icon
 * @param title header title
 * @param subtitle head subtitle
 * @param breadcrumbs breadcrumbs
 * @param backCTA cta for returning to another url
 * @author Stephen Prizio
 * @version 0.2.0
 */
export default function PageHeaderSection({
  iconCode = "",
  title = "",
  subtitle = "",
  breadcrumbs = [],
  backCTA,
}: Readonly<{
  iconCode?: string;
  title: string;
  subtitle?: string;
  breadcrumbs: Array<AppLink>;
  backCTA?: {
    label: string;
    href: string;
  };
}>) {
  //  RENDER

  return (
    <div className={"grid grid-cols-1 lg:grid-cols-2 gap-6 items-center pb-12"}>
      {backCTA && (
        <div className={"col-span-2 mb-2"}>
          <div className={"flex items-center"}>
            <Link
              href={backCTA.href}
              className={
                "text-primary font-semibold pb-1 px-2 inline-flex items-center"
              }
            >
              <span className={"inline-block mr-2"}>
                {resolveIcon(Icons.ArrowLeft, "", 22)}
              </span>
              <span className={"inline-block"}>{backCTA.label}</span>
            </Link>
          </div>
        </div>
      )}
      <div className={"flex items-center"}>
        <div className={"mr-6 bg-primary text-white p-4 rounded-lg"}>
          {resolveIcon(
            iconCode,
            undefined,
            DEFAULT_PAGE_HEADER_SECTION_ICON_SIZE,
          )}
        </div>
        <div className={""}>
          <div className={"text-slate-700 text-xl lg:text-2xl font-bold"}>
            {title}
          </div>
          <div className={"text-slate-700 text-sm lg:text-md"}>{subtitle}</div>
        </div>
      </div>
      <div className={"flex items-center justify-end"}>
        <Breadcrumbs links={breadcrumbs} />
      </div>
    </div>
  );
}
