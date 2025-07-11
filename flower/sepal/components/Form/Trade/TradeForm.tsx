import { Account, CreateUpdateTradeRequest, Trade } from "@/types/apiTypes";
import { useToast } from "@/lib/hooks/ui/use-toast";
import { useSepalModalContext } from "@/lib/context/SepalContext";
import {
  useCreateTradeMutation,
  useUpdateTradeMutation,
} from "@/lib/hooks/query/mutations";
import React, { useEffect } from "react";
import { logErrors } from "@/lib/functions/util-functions";
import { CRUDTradeSchema, DateTime, ISO_TIME_REGEX } from "@/lib/constants";
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
  account: Account | null | undefined;
  mode: "create" | "edit";
  trade: Trade | null | undefined;
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
      tradeType: isCreateMode() ? "default" : trade?.tradeType.code,
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
      openTime: getOpenTime(),
      closeTime: getCloseTime(),
    },
  });

  //  GENERAL FUNCTIONS

  /**
   * Computes the open time for the input
   */
  function getOpenTime() {
    if (isCreateMode()) {
      return "09:30:00";
    } else {
      return moment(trade?.tradeOpenTime).format(DateTime.ISOTimeFormat);
    }
  }

  /**
   * Computes the close time for the input
   */
  function getCloseTime() {
    if (isCreateMode()) {
      return "16:00:00";
    } else if (trade?.tradeCloseTime) {
      return moment(trade.tradeCloseTime).format(DateTime.ISOTimeFormat);
    } else {
      return "16:00:00";
    }
  }

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
   * @param timeVal time aspect
   */
  function getDate(
    val: Date | null | undefined,
    timeVal: string,
  ): string | null {
    if (val) {
      const isoTime = sanitizeTimeInput(timeVal).split(":");
      return moment(val)
        .set({
          hour: parseInt(isoTime[0], 10),
          minute: parseInt(isoTime[1], 10),
          second: parseInt(isoTime[2], 10),
        })
        .format(DateTime.ISODateTimeFormat);
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
      tradeOpenTime: getDate(values.tradeOpenTime, values.openTime),
      tradeCloseTime: getDate(values.tradeCloseTime, values.closeTime),
    };

    if (isCreateMode()) {
      createTrade(req);
    } else {
      updateTrade(req);
    }
  }

  function sanitizeTimeInput(val: string): string {
    if (!val) {
      return "09:30:00";
    }

    if (ISO_TIME_REGEX.test(val)) {
      const parts = val.split(":");
      const hours = parts[0].padStart(2, "0");
      const minutes = parts[1].padStart(2, "0");
      const seconds = parts[2] ? parts[2].padStart(2, "0") : "00";

      return `${hours}:${minutes}:${seconds}`;
    }

    return "00:00:00";
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
                      <Input
                        placeholder="1234"
                        {...field}
                        disabled={!isCreateMode()}
                      />
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
                      disabled={!isCreateMode()}
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
            <div className="col-span-2 grid grid-cols-2 gap-4">
              <div>
                <FormField
                  control={form.control}
                  name="tradeOpenTime"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel className="!text-current">Open Date</FormLabel>
                      <ReusableDatePicker
                        label={"Open Time"}
                        hasIcon={true}
                        field={field}
                        disabled={!isCreateMode()}
                      />
                      <FormMessage
                        className={"text-primaryRed font-semibold"}
                      />
                    </FormItem>
                  )}
                />
              </div>
              <div>
                <FormField
                  control={form.control}
                  name="openTime"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel className="!text-current">Time</FormLabel>
                      <FormControl>
                        <Input
                          {...field}
                          placeholder="09:30:00"
                          className="bg-background appearance-none [&::-webkit-calendar-picker-indicator]:hidden [&::-webkit-calendar-picker-indicator]:appearance-none"
                          disabled={!isCreateMode()}
                        />
                      </FormControl>
                      <FormMessage
                        className={"text-primaryRed font-semibold"}
                      />
                    </FormItem>
                  )}
                />
              </div>
            </div>
            <div className="col-span-2 grid grid-cols-2 gap-4">
              <div>
                <FormField
                  control={form.control}
                  name="tradeCloseTime"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel className="!text-current">
                        Close Date
                      </FormLabel>
                      <ReusableDatePicker
                        label={"Close Time"}
                        hasIcon={true}
                        field={field}
                        disabled={!isCreateMode()}
                      />
                      <FormDescription>
                        This field can be left empty, unless a close price is
                        specified.
                      </FormDescription>
                      <FormMessage
                        className={"text-primaryRed font-semibold"}
                      />
                    </FormItem>
                  )}
                />
              </div>
              <div>
                <FormField
                  control={form.control}
                  name="closeTime"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel className="!text-current">Time</FormLabel>
                      <FormControl>
                        <Input
                          {...field}
                          placeholder="16:00:00"
                          className="bg-background appearance-none [&::-webkit-calendar-picker-indicator]:hidden [&::-webkit-calendar-picker-indicator]:appearance-none"
                          disabled={!isCreateMode()}
                        />
                      </FormControl>
                      <FormMessage
                        className={"text-primaryRed font-semibold"}
                      />
                    </FormItem>
                  )}
                />
              </div>
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
                      This field can be left empty unless a close time is
                      specified.
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
