import { Account, CreateUpdateTradeRequest, Trade } from "@/types/apiTypes";
import { useToast } from "@/lib/hooks/ui/use-toast";
import { useSepalModalContext } from "@/lib/context/SepalContext";
import {
  useCreateTradeMutation,
  useUpdateTradeMutation,
} from "@/lib/hooks/query/mutations";
import React, { useEffect } from "react";
import { logErrors } from "@/lib/functions/util-functions";
import { CRUDTradeSchema, DateTime } from "@/lib/constants";
import { useForm } from "react-hook-form";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import moment from "moment/moment";
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
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import LoadingPage from "@/app/loading";
import ReusableDatePicker from "@/components/DateTime/ReusableDatePicker";
import { Button } from "@/components/ui/button";
import { Loader2 } from "lucide-react";

/**
 * Renders a form that can create or update a trade
 *
 * @param account account
 * @param mode should create / edit
 * @param trade trade info
 * @author Stephen Prizio
 * @version 0.2.4
 */
export default function TradeForm({
  account,
  mode = "create",
  trade,
}: Readonly<{
  account: Account;
  mode: "create" | "edit";
  trade?: Trade;
}>) {
  if (!isCreateMode() && !trade?.tradeId) {
    throw new Error("Missing Trade for edit mode");
  }

  if ((account?.accountNumber ?? -1) === -1) {
    throw new Error("Missing account!");
  }

  const { toast } = useToast();
  const { setOpen } = useSepalModalContext();
  const {
    mutate: createTrade,
    isPending: isCreateTradeLoading,
    isSuccess: isCreateTradeSuccess,
    isError: isCreateTradeError,
    error: createTradeError,
  } = useCreateTradeMutation(account?.accountNumber ?? -1);
  const {
    mutate: updateTrade,
    isPending: isUpdateTradeLoading,
    isSuccess: isUpdateTradeSuccess,
    isError: isUpdateTradeError,
    error: updateTradeError,
  } = useUpdateTradeMutation(
    account?.accountNumber ?? -1,
    trade?.tradeId ?? "",
  );

  const isLoading = isCreateTradeLoading || isUpdateTradeLoading;

  useEffect(() => {
    if (isCreateTradeSuccess) {
      renderSuccessNotification();
      setOpen(false);
    } else if (isCreateTradeError) {
      logErrors(createTradeError);
      renderErrorNotification();
    }
  }, [isCreateTradeSuccess, isCreateTradeError]);

  useEffect(() => {
    if (isUpdateTradeSuccess) {
      renderSuccessNotification();
      setOpen(false);
      window.location.reload();
    } else if (isUpdateTradeError) {
      logErrors(updateTradeError);
      renderErrorNotification();
    }
  }, [isUpdateTradeSuccess, isUpdateTradeError]);

  const formSchema = CRUDTradeSchema();
  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      tradeId: isCreateMode() ? "" : trade?.tradeId,
      product: isCreateMode() ? "" : trade?.product,
      tradePlatform: account?.tradePlatform?.code ?? "METATRADER4",
      tradeType: isCreateMode() ? "default" : trade?.tradeType,
      tradeOpenTime: isCreateMode()
        ? undefined
        : moment(trade?.tradeOpenTime).toDate(),
      tradeCloseTime: isCreateMode()
        ? undefined
        : trade?.tradeCloseTime
          ? moment(trade?.tradeCloseTime).toDate()
          : undefined,
      lotSize: isCreateMode() ? 0.0 : trade?.lotSize,
      openPrice: isCreateMode() ? 0.0 : trade?.openPrice,
      closePrice: isCreateMode() ? 0.0 : trade?.closePrice,
      netProfit: isCreateMode() ? 0.0 : trade?.netProfit,
      stopLoss: isCreateMode() ? 0.0 : trade?.stopLoss,
      takeProfit: isCreateMode() ? 0.0 : trade?.takeProfit,
    },
  });

  //  GENERAL FUNCTIONS

  /**
   * Renders the success notifications
   */
  function renderSuccessNotification() {
    toast({
      title: isCreateMode() ? "Account Created!" : "Account Updated!",
      description: isCreateMode()
        ? "The trade was successfully created."
        : `Trade ${trade?.tradeId ?? ""} was updated successfully.`,
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
        ? "An error occurred while creating the trade. Please check your inputs and try again."
        : `An error occurred while updating trade ${trade?.tradeId ?? ""}. Please check your inputs and try again.`,
      variant: "danger",
    });
  }

  /**
   * Returns true if the form is set to be in create mode, i.e. creating a new Account
   */
  function isCreateMode() {
    return mode === "create";
  }

  /**
   * Safely parses a date for the backend request
   *
   * @param val input date
   */
  function getDate(val: Date | null | undefined): string | null {
    if (val) {
      return moment(val).format(DateTime.ISODateTimeFormat);
    } else {
      return null;
    }
  }

  /**
   * Submits the form
   *
   * @param values form values
   */
  function onSubmit(values: z.infer<typeof formSchema>) {
    const req: CreateUpdateTradeRequest = {
      ...values,
      tradeOpenTime: getDate(values.tradeOpenTime),
      tradeCloseTime: getDate(values.tradeCloseTime),
    };

    if (isCreateMode()) {
      createTrade(req);
    } else {
      updateTrade(req);
    }
  }

  //  RENDER

  if (isLoading) {
    return <LoadingPage />;
  }

  return (
    <div>
      <Form {...form}>
        <form
          onSubmit={form.handleSubmit(onSubmit, (e) => console.log(e))}
          className="space-y-8"
        >
          <div className="grid grid-cols-2 gap-4">
            <div>
              <FormField
                control={form.control}
                name="tradeId"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="!text-current">Trade ID</FormLabel>
                    <FormControl>
                      <Input placeholder="1234" {...field} />
                    </FormControl>
                    <FormDescription>
                      To auto-generate a unique trade id, leave this field blank
                    </FormDescription>
                    <FormMessage className={"text-primaryRed font-semibold"} />
                  </FormItem>
                )}
              />
            </div>
            <div>
              <FormField
                control={form.control}
                name="product"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="!text-current">
                      Symbol/Equity
                    </FormLabel>
                    <FormControl>
                      <Input placeholder="AAPL" {...field} />
                    </FormControl>
                    <FormMessage className={"text-primaryRed font-semibold"} />
                  </FormItem>
                )}
              />
            </div>
            <div>
              <FormField
                control={form.control}
                name="tradePlatform"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="!text-current">
                      Trade Platform
                    </FormLabel>
                    <Select
                      onValueChange={field.onChange}
                      defaultValue={field.value}
                      disabled={true}
                    >
                      <FormControl>
                        <SelectTrigger>
                          <SelectValue />
                        </SelectTrigger>
                      </FormControl>
                      <SelectContent>
                        <SelectItem value={"default"}>
                          Select a platform
                        </SelectItem>
                        <SelectItem
                          value={account?.tradePlatform?.code ?? "UNKNOWN"}
                        >
                          {account?.tradePlatform?.label ?? "Unknown"}
                        </SelectItem>
                      </SelectContent>
                    </Select>
                  </FormItem>
                )}
              />
            </div>
            <div>
              <FormField
                control={form.control}
                name="tradeType"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="!text-current">Trade Type</FormLabel>
                    <Select
                      onValueChange={field.onChange}
                      defaultValue={field.value}
                    >
                      <FormControl>
                        <SelectTrigger>
                          <SelectValue />
                        </SelectTrigger>
                      </FormControl>
                      <SelectContent>
                        <SelectItem value={"default"}>
                          Select a trade type
                        </SelectItem>
                        <SelectItem value={"BUY"}>Buy Trade</SelectItem>
                        <SelectItem value={"SELL"}>Sell Trade</SelectItem>
                      </SelectContent>
                    </Select>
                    <FormMessage className={"text-primaryRed font-semibold"} />
                  </FormItem>
                )}
              />
            </div>
            <div>
              <FormField
                control={form.control}
                name="tradeOpenTime"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="!text-current">Open Time</FormLabel>
                    <ReusableDatePicker
                      label={"Open Time"}
                      hasIcon={true}
                      field={field}
                    />
                    <FormMessage className={"text-primaryRed font-semibold"} />
                  </FormItem>
                )}
              />
            </div>
            <div>
              <FormField
                control={form.control}
                name="tradeCloseTime"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="!text-current">Close Time</FormLabel>
                    <ReusableDatePicker
                      label={"Close Time"}
                      hasIcon={true}
                      field={field}
                    />
                    <FormDescription>
                      This field can be left empty. If empty, this trade can be
                      assumed to be open/active. (Along with an empty close
                      price)
                    </FormDescription>
                    <FormMessage className={"text-primaryRed font-semibold"} />
                  </FormItem>
                )}
              />
            </div>
            <div>
              <FormField
                control={form.control}
                name="lotSize"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="!text-current">Lot Size</FormLabel>
                    <FormControl>
                      <Input placeholder="0.25" {...field} type={"number"} />
                    </FormControl>
                    <FormMessage className={"text-primaryRed font-semibold"} />
                  </FormItem>
                )}
              />
            </div>
            <div>
              <FormField
                control={form.control}
                name="openPrice"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="!text-current">Open Price</FormLabel>
                    <FormControl>
                      <Input placeholder="184.37" {...field} type={"number"} />
                    </FormControl>
                    <FormMessage className={"text-primaryRed font-semibold"} />
                  </FormItem>
                )}
              />
            </div>
            <div>
              <FormField
                control={form.control}
                name="closePrice"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="!text-current">Close Price</FormLabel>
                    <FormControl>
                      <Input placeholder="191.56" {...field} type={"number"} />
                    </FormControl>
                    <FormDescription>
                      This field can be left empty. If empty, this trade can be
                      assumed to be open/active. (Along with an empty close
                      time)
                    </FormDescription>
                    <FormMessage className={"text-primaryRed font-semibold"} />
                  </FormItem>
                )}
              />
            </div>
            <div>
              <FormField
                control={form.control}
                name="netProfit"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="!text-current">Net Profit</FormLabel>
                    <FormControl>
                      <Input placeholder="369.02" {...field} type={"number"} />
                    </FormControl>
                    <FormDescription>
                      This field can be left empty.
                    </FormDescription>
                    <FormMessage className={"text-primaryRed font-semibold"} />
                  </FormItem>
                )}
              />
            </div>
            <div>
              <FormField
                control={form.control}
                name="stopLoss"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="!text-current">Stop Loss</FormLabel>
                    <FormControl>
                      <Input placeholder="185.12" {...field} type={"number"} />
                    </FormControl>
                    <FormDescription>
                      This field can be left empty.
                    </FormDescription>
                    <FormMessage className={"text-primaryRed font-semibold"} />
                  </FormItem>
                )}
              />
            </div>
            <div>
              <FormField
                control={form.control}
                name="takeProfit"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="!text-current">Take Profit</FormLabel>
                    <FormControl>
                      <Input placeholder="192.56" {...field} type={"number"} />
                    </FormControl>
                    <FormDescription>
                      This field can be left empty.
                    </FormDescription>
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
