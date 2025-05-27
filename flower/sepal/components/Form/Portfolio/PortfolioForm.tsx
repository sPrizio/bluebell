"use client";

import { CreateUpdatePortfolioRequest, Portfolio } from "@/types/apiTypes";
import { useToast } from "@/lib/hooks/ui/use-toast";
import { useSepalModalContext } from "@/lib/context/SepalContext";
import {
  useCreatePortfolioMutation,
  useUpdatePortfolioMutation,
} from "@/lib/hooks/query/mutations";
import React, { useEffect } from "react";
import { logErrors } from "@/lib/functions/util-functions";
import { CRUDPortfolioSchema } from "@/lib/constants";
import { useForm } from "react-hook-form";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import {
  Form,
  FormControl,
  FormDescription,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Switch } from "@/components/ui/switch";
import { Button } from "@/components/ui/button";
import { Loader2 } from "lucide-react";

/**
 * Renders a form that can create or update a portfolio
 * @param mode
 * @param portfolio
 * @author Stephen Prizio
 * @version 0.2.2
 */
export default function PortfolioForm({
  mode = "create",
  portfolio,
}: Readonly<{
  mode: "create" | "edit";
  portfolio?: Portfolio;
}>) {
  if (!isCreateMode() && !portfolio?.portfolioNumber) {
    throw new Error("Missing portfolio or edit mode");
  }

  const { toast } = useToast();
  const { setOpen } = useSepalModalContext();

  const {
    mutate: createPortfolio,
    isPending: isCreatePortfolioLoading,
    isSuccess: isCreatePortfolioSuccess,
    isError: isCreatePortfolioError,
    error: createPortfolioError,
  } = useCreatePortfolioMutation();
  const {
    mutate: updatePortfolio,
    isPending: isUpdatePortfolioLoading,
    isSuccess: isUpdatePortfolioSuccess,
    isError: isUpdatePortfolioError,
    error: updatePortfolioError,
  } = useUpdatePortfolioMutation(portfolio?.portfolioNumber ?? -1);

  const isLoading = isCreatePortfolioLoading ?? isUpdatePortfolioLoading;

  useEffect(() => {
    if (isCreatePortfolioSuccess) {
      renderSuccessNotification();
      setOpen(false);
    } else if (isCreatePortfolioError) {
      logErrors(createPortfolioError);
      renderErrorNotification();
    }
  }, [isCreatePortfolioSuccess, isCreatePortfolioError]);

  useEffect(() => {
    if (isUpdatePortfolioSuccess) {
      renderSuccessNotification();
      setOpen(false);
      window.location.reload();
    } else if (isUpdatePortfolioError) {
      logErrors(updatePortfolioError);
      renderErrorNotification();
    }
  }, [isUpdatePortfolioSuccess, isUpdatePortfolioError]);

  const formSchema = CRUDPortfolioSchema();
  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      isDefault: isCreateMode() ? false : portfolio?.defaultPortfolio,
      active: isCreateMode() ? true : portfolio?.active,
      name: isCreateMode() ? "" : portfolio?.name,
    },
  });

  //  GENERAL FUNCTIONS

  /**
   * Renders the success notifications
   */
  function renderSuccessNotification() {
    toast({
      title: isCreateMode() ? "Portfolio Created!" : "Portfolio Updated!",
      description: isCreateMode()
        ? "Your new portfolio was successfully created."
        : "Your portfolio was updated successfully.",
      variant: "success",
    });
  }

  /**
   * Renders the error notifications
   */
  function renderErrorNotification() {
    toast({
      title: isCreateMode() ? "Creation Failed!" : "Update Failed!",
      description: isCreateMode()
        ? "An error occurred while creating your new portfolio. Please check your inputs and try again."
        : "An error occurred while updating your portfolio. Please check your inputs and try again.",
      variant: "danger",
    });
  }

  /**
   * Submits the form
   *
   * @param values form values
   */
  function onSubmit(values: z.infer<typeof formSchema>) {
    const req: CreateUpdatePortfolioRequest = {
      ...values,
      defaultPortfolio: values.isDefault,
    };

    if (isCreateMode()) {
      createPortfolio(req);
    } else {
      updatePortfolio(req);
    }
  }

  /**
   * Returns true if the form is set to be in create mode, i.e. creating a new Account
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
            <div>
              <FormField
                control={form.control}
                name="isDefault"
                render={({ field }) => (
                  <FormItem>
                    <div className={"flex items-center gap-4 w-full"}>
                      <div className={"w-full"}>
                        <FormLabel className="!text-current">
                          Default Portfolio
                        </FormLabel>
                      </div>
                      <div
                        className={"flex items-center justify-end text-right"}
                      >
                        <FormControl>
                          <Switch
                            checked={field.value}
                            onCheckedChange={field.onChange}
                          />
                        </FormControl>
                      </div>
                    </div>
                    <FormDescription>
                      A default portfolio is one that will be considered the
                      primary/main portfolio.
                    </FormDescription>
                    <FormMessage className={"text-primaryRed font-semibold"} />
                  </FormItem>
                )}
              />
            </div>
            <div>
              <FormField
                control={form.control}
                name="active"
                render={({ field }) => (
                  <FormItem>
                    <div className={"flex items-center gap-4 w-full"}>
                      <div className={"w-full"}>
                        <FormLabel className="!text-current">Active</FormLabel>
                      </div>
                      <div
                        className={"flex items-center justify-end text-right"}
                      >
                        <FormControl>
                          <Switch
                            checked={field.value}
                            onCheckedChange={field.onChange}
                          />
                        </FormControl>
                      </div>
                    </div>
                    <FormDescription>
                      Active portfolios will have their accounts & performances
                      tracked and periodically updated.
                    </FormDescription>
                    <FormMessage className={"text-primaryRed font-semibold"} />
                  </FormItem>
                )}
              />
            </div>
            <div>
              <FormField
                control={form.control}
                name="name"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="!text-current">
                      Portfolio Name
                    </FormLabel>
                    <FormControl>
                      <Input placeholder="MT4 Trading Accounts" {...field} />
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
