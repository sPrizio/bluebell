"use client";

import React from "react";
import { Icons } from "@/lib/enums";
import { BaseCard } from "@/components/Card/BaseCard";
import { Button } from "@/components/ui/button";
import BaseModal from "@/components/Modal/BaseModal";
import moment from "moment";
import { DateTime } from "@/lib/constants";
import UserForm from "@/components/Form/User/UserForm";
import { PageInfoProvider } from "@/lib/context/PageInfoProvider";
import { useUserQuery } from "@/lib/hooks/query/queries";
import LoadingPage from "@/app/loading";
import { logErrors } from "@/lib/functions/util-functions";
import Error from "@/app/error";
import { resolveIcon } from "@/lib/functions/util-component-functions";

/**
 * Renders the user profile page
 *
 * @author Stephen Prizio
 * @version 0.2.4
 */
export default function ProfilePage() {
  const { data: user, isError, error, isLoading } = useUserQuery();

  //  GENERAL FUNCTIONS

  /**
   * Formats a user's roles for display purposes
   *
   * @param vals roles
   */
  function formatRoles(vals: string[]) {
    return (
      vals
        ?.map(
          (val) =>
            val.substring(0, 1).toUpperCase() + val.substring(1).toLowerCase(),
        )
        .join(" \u00B7 ") ?? ""
    );
  }

  const pageInfo = {
    title: "Profile Information",
    subtitle: "Your profile at a glance",
    iconCode: Icons.UserCircle,
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
    return <Error />;
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
          headerControls={[
            <BaseModal
              key={0}
              title={"Update Profile Information"}
              description={
                "Here you can edit/update any Profile information. Note that some aspects of this profile cannot be changed after registration."
              }
              trigger={
                <Button className="" variant={"outline"}>
                  {resolveIcon(Icons.Edit)}
                  &nbsp;Edit
                </Button>
              }
              content={<UserForm mode={"edit"} user={user} />}
            />,
          ]}
        />
      </div>
    </PageInfoProvider>
  );
}
