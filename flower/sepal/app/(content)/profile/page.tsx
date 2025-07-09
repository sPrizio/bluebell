"use client";

import React from "react";
import { Icons, UserPrivilege } from "@/lib/enums";
import { BaseCard } from "@/components/Card/BaseCard";
import { Button } from "@/components/ui/button";
import BaseModal from "@/components/Modal/BaseModal";
import moment from "moment";
import { CONTROL_GAP, DateTime, PAGE_GAP } from "@/lib/constants";
import UserForm from "@/components/Form/User/UserForm";
import { PageInfoProvider } from "@/lib/context/PageInfoProvider";
import { useUserQuery } from "@/lib/hooks/query/queries";
import LoadingPage from "@/app/loading";
import { logErrors } from "@/lib/functions/util-functions";
import ErrorPage from "@/app/error";
import { resolveIcon } from "@/lib/functions/util-component-functions";
import { useSessionContext } from "@/lib/context/SessionContext";
import { EnumDisplay } from "@/types/apiTypes";

/**
 * Renders the user profile page
 *
 * @author Stephen Prizio
 * @version 1.0.0
 */
export default function ProfilePage() {
  const session = useSessionContext();
  const {
    data: user,
    isError,
    error,
    isLoading,
  } = useUserQuery(session?.username ?? "");

  //  GENERAL FUNCTIONS

  /**
   * Formats a user's roles for display purposes
   *
   * @param vals roles
   */
  function formatRoles(vals: EnumDisplay[]) {
    return (
      vals
        ?.map(
          (val) =>
            val.label.substring(0, 1).toUpperCase() +
            val.label.substring(1).toLowerCase(),
        )
        .join(" \u00B7 ") ?? ""
    );
  }

  const pageInfo = {
    title: "Profile Information",
    subtitle: "Your profile at a glance",
    iconCode: Icons.UserCircle,
    privilege: UserPrivilege.TRADER,
    breadcrumbs: [
      { label: "Dashboard", href: "/dashboard", active: false },
      { label: "Profile", href: "/profile", active: true },
    ],
  };

  //  RENDER

  if (isLoading) {
    return <LoadingPage />;
  }

  if (isError) {
    logErrors(error);
    return <ErrorPage />;
  }

  /**
   * Renders a basic cell for the profile page
   *
   * @param label label
   * @param val text
   */
  const basicCell = (label: string, val: string) => {
    return (
      <div className={"flex flex-col gap-1"}>
        <div
          className={
            "text-xxs uppercase font-bold tracking-normal text-primary"
          }
        >
          {label}
        </div>
        <div className={""}>{val}</div>
      </div>
    );
  };

  return (
    <PageInfoProvider value={pageInfo}>
      <div className={`grid grid-cols-1 ${PAGE_GAP}`}>
        <div className={`flex items-center justify-end ${CONTROL_GAP}`}>
          <BaseModal
            key={0}
            title={"Update Profile Information"}
            description={
              "Here you can edit/update any Profile information. Note that some aspects of this profile cannot be changed after registration."
            }
            trigger={
              <Button className="" variant={"outline"}>
                {resolveIcon(Icons.Edit)}
                &nbsp;Update Information
              </Button>
            }
            content={<UserForm mode={"edit"} user={user} />}
          />
        </div>
        <div className={""}>
          <BaseCard
            title={"Profile Information"}
            subtitle={"Your profile at a glance"}
            cardContent={
              <div className={"my-4 grid grid-cols-1 lg:grid-cols-3 gap-4"}>
                {basicCell("First Name", user?.firstName ?? "")}
                {basicCell("Last Name", user?.lastName ?? "")}
                {basicCell("Username", user?.username ?? "")}
                {basicCell("Email", user?.email ?? "")}
                {basicCell(
                  "Date Registered",
                  moment(user?.dateRegistered).format(
                    DateTime.ISOLongMonthDayYearWithTimeFormat,
                  ),
                )}
                {basicCell("Roles", formatRoles(user?.roles ?? []))}
              </div>
            }
          />
        </div>
      </div>
    </PageInfoProvider>
  );
}
