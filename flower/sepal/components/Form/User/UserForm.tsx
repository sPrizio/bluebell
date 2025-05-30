"use client";

import { useToast } from "@/lib/hooks/ui/use-toast";
import React, { useEffect } from "react";
import { delay, logErrors } from "@/lib/functions/util-functions";
import { useSepalModalContext } from "@/lib/context/SepalContext";
import { CRUDUserSchema } from "@/lib/constants";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { z } from "zod";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Loader2 } from "lucide-react";
import { User } from "@/types/apiTypes";
import { useUpdateUserMutation } from "@/lib/hooks/query/mutations";

/**
 * Renders a form that can create or update a User
 *
 * @param mode should create / edit
 * @param user User info
 * @author Stephen Prizio
 * @version 0.2.4
 */
export default function UserForm({
  mode = "create",
  user,
}: Readonly<{
  mode?: "create" | "edit";
  user?: User;
}>) {
  const { toast } = useToast();
  const { setOpen } = useSepalModalContext();
  const formSchema = CRUDUserSchema(!isCreateMode());
  const username = "";

  const {
    mutate: updateUser,
    isPending: isUpdateUserLoading,
    isSuccess: isUpdateUserSuccess,
    isError: isUpdateUserError,
    error: updateUserError,
  } = useUpdateUserMutation(username);

  const isLoading = isUpdateUserLoading;
  useEffect(() => {
    if (isUpdateUserSuccess) {
      renderSuccessNotification();
      setOpen(false);
    } else if (isUpdateUserError) {
      logErrors(updateUserError);
      renderErrorNotification();
    }
  }, [isUpdateUserSuccess, isUpdateUserError]);

  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      firstName: user?.firstName ?? "",
      lastName: user?.lastName ?? "",
      username: user?.username ?? "",
      email: user?.email ?? "",
      password: "this is a temp password that is unused.!!",
      confirmPassword: "this is a temp password that is unused.!!",
    },
  });

  //  GENERAL FUNCTIONS

  /**
   * Renders the success notifications
   */
  function renderSuccessNotification() {
    toast({
      title: isCreateMode() ? "User Created!" : "User Modified!",
      description: isCreateMode()
        ? "Your profile was successfully registered."
        : "Your profile was updated successfully.",
      variant: "success",
    });
  }

  /**
   * Renders the error notifications
   */
  function renderErrorNotification() {
    toast({
      title: isCreateMode() ? "Registration Failed!" : "Update Failed!",
      description: isCreateMode()
        ? "An error occurred while registering your profile. Please check your inputs and try again."
        : "An error occurred while updating your profile. Please check your inputs and try again.",
      variant: "danger",
    });
  }

  /**
   * Submits the form
   *
   * @param values form values
   */
  async function onSubmit(values: z.infer<typeof formSchema>) {
    // TODO: as part of BB-35, will likely need to re-format request to take in a list of phone numbers
    if (isCreateMode()) {
      //  TODO: create user logic
      await delay(4000);
      console.log(values);
    } else {
      updateUser(values);
    }
  }

  /**
   * Returns true if the form is set to be in create mode, i.e. creating a new account
   */
  function isCreateMode() {
    return mode === "create";
  }

  //  RENDER

  return (
    <div>
      <Form {...form}>
        <form
          onSubmit={form.handleSubmit(onSubmit, (e) => console.log(e))}
          className={"space-y-8"}
        >
          <div className={"grid grid-cols-2 gap-4"}>
            <div className={""}>
              <FormField
                control={form.control}
                name="firstName"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="!text-current">First Name</FormLabel>
                    <FormControl>
                      <Input placeholder="John" {...field} type={"text"} />
                    </FormControl>
                    <FormMessage className={"text-primaryRed font-semibold"} />
                  </FormItem>
                )}
              />
            </div>
            <div className={""}>
              <FormField
                control={form.control}
                name="lastName"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="!text-current">Last Name</FormLabel>
                    <FormControl>
                      <Input placeholder="Trader" {...field} type={"text"} />
                    </FormControl>
                    <FormMessage className={"text-primaryRed font-semibold"} />
                  </FormItem>
                )}
              />
            </div>
            <div className={""}>
              <FormField
                control={form.control}
                name="username"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="!text-current">Username</FormLabel>
                    <FormControl>
                      <Input
                        placeholder="john.trader"
                        {...field}
                        type={"text"}
                        disabled={!isCreateMode()}
                      />
                    </FormControl>
                    <FormMessage className={"text-primaryRed font-semibold"} />
                  </FormItem>
                )}
              />
            </div>
            <div className={""}>
              <FormField
                control={form.control}
                name="email"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="!text-current">Email</FormLabel>
                    <FormControl>
                      <Input
                        placeholder="j.trader@example.com"
                        {...field}
                        type={"text"}
                      />
                    </FormControl>
                    <FormMessage className={"text-primaryRed font-semibold"} />
                  </FormItem>
                )}
              />
            </div>
          </div>
          <div className={"flex w-full justify-end items-center gap-4"}>
            <Button
              type="submit"
              className={"bg-primary text-white"}
              disabled={isLoading}
            >
              {isLoading ? (
                <Loader2 className="mr-2 h-4 w-4 animate-spin" />
              ) : null}
              Submit
            </Button>
            <Button
              type="button"
              className={"border border-gray-400"}
              variant={"outline"}
              onClick={() => setOpen(false)}
            >
              Cancel
            </Button>
          </div>
        </form>
      </Form>
    </div>
  );
}
