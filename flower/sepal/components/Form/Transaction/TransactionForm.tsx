"use client";

import { useToast } from "@/lib/hooks/ui/use-toast";
import { useEffect } from "react";
import { useSepalModalContext } from "@/lib/context/SepalContext";
import { CRUDTransactionSchema, DateTime } from "@/lib/constants";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { z } from "zod";
import { logErrors } from "@/lib/functions/util-functions";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import ReusableDatePicker from "@/components/DateTime/ReusableDatePicker";
import { Button } from "@/components/ui/button";
import { Loader2 } from "lucide-react";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Input } from "@/components/ui/input";
import moment from "moment";
import {
  Account,
  CreateUpdateTransactionRequest,
  Transaction,
} from "@/types/apiTypes";
import {
  useCreateTransactionMutation,
  useUpdateTransactionMutation,
} from "@/lib/hooks/query/mutations";

/**
 * Renders a form that can create or update a transaction
 *
 * @param mode should create / edit
 * @param account Account info
 * @param transaction transaction info
 * @author Stephen Prizio
 * @version 0.2.5
 */
export default function TransactionForm({
  mode = "create",
  account,
  transaction,
}: Readonly<{
  mode?: "create" | "edit";
  account: Account | null;
  transaction?: Transaction | null;
}>) {
  if (!isCreateMode() && !account?.accountNumber) {
    throw new Error("Missing Account for edit mode");
  }

  if (!isCreateMode() && !(transaction?.transactionDate ?? null)) {
    throw new Error("Missing transaction for edit mode");
  }

  const { toast } = useToast();
  const { setOpen } = useSepalModalContext();
  const {
    mutate: createTransaction,
    isPending: isCreateTransactionLoading,
    isSuccess: isCreateTransactionSuccess,
    isError: isCreateTransactionError,
    error: createTransactionError,
  } = useCreateTransactionMutation(account?.accountNumber ?? -1);

  const {
    mutate: updateTransaction,
    isPending: isUpdateTransactionLoading,
    isSuccess: isUpdateTransactionSuccess,
    isError: isUpdateTransactionError,
    error: updateTransactionError,
  } = useUpdateTransactionMutation(
    account?.accountNumber ?? -1,
    transaction?.transactionNumber ?? -1,
  );

  useEffect(() => {
    if (isCreateTransactionSuccess) {
      renderSuccessNotification();
      setOpen(false);
    } else if (isCreateTransactionError) {
      logErrors(createTransactionError);
      renderErrorNotification();
    }
  }, [isCreateTransactionSuccess, isCreateTransactionError]);

  useEffect(() => {
    if (isUpdateTransactionSuccess) {
      renderSuccessNotification();
      setOpen(false);
    } else if (isUpdateTransactionError) {
      logErrors(updateTransactionError);
      renderErrorNotification();
    }
  }, [isUpdateTransactionSuccess, isUpdateTransactionError]);

  const isLoading = isCreateTransactionLoading || isUpdateTransactionLoading;
  const formSchema = CRUDTransactionSchema();
  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      date: isCreateMode()
        ? new Date()
        : moment(transaction?.transactionDate).toDate(),
      type: isCreateMode()
        ? "default"
        : transaction?.transactionType.code.toUpperCase(),
      amount: isCreateMode() ? 0.0 : transaction?.amount,
      account: account?.accountNumber ?? -1,
      name: isCreateMode() ? "" : transaction?.name,
      status: isCreateMode()
        ? "default"
        : transaction?.transactionStatus?.code.toUpperCase(),
    },
  });

  //  GENERAL FUNCTIONS

  /**
   * Renders the success notifications
   */
  function renderSuccessNotification() {
    toast({
      title: isCreateMode() ? "Transaction Created!" : "Transaction Updated!",
      description: isCreateMode()
        ? "The transaction was successfully created."
        : "The transaction was updated successfully.",
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
        ? "An error occurred while creating the transaction. Please check your inputs and try again."
        : "An error occurred while updating the transaction. Please check your inputs and try again.",
      variant: "danger",
    });
  }

  /**
   * Submits the form
   *
   * @param values form values
   */
  async function onSubmit(values: z.infer<typeof formSchema>) {
    const req: CreateUpdateTransactionRequest = {
      ...values,
      transactionNumber: transaction?.transactionNumber ?? undefined,
      name: values.name,
      originalName: isCreateMode() ? values.name : (transaction?.name ?? ""),
      transactionType: values.type,
      transactionDate: moment(values.date).format(
        DateTime.ISOEasyDateTimeFormat,
      ),
      transactionStatus: values.status,
    };

    if (isCreateMode()) {
      createTransaction(req);
    } else {
      updateTransaction(req);
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
            <div className={""}>
              <FormField
                control={form.control}
                name="date"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Transaction Date</FormLabel>
                    <ReusableDatePicker
                      label={"Transaction Date"}
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
                name="account"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="!text-current">Account</FormLabel>
                    <FormControl>
                      <Input {...field} type={"number"} disabled={true} />
                    </FormControl>
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
                    <FormLabel className="!text-current">Name</FormLabel>
                    <FormControl>
                      <Input placeholder={"Funds deposit"} {...field} />
                    </FormControl>
                    <FormMessage className={"text-primaryRed font-semibold"} />
                  </FormItem>
                )}
              />
            </div>
            <div>
              <FormField
                control={form.control}
                name="amount"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="!text-current">Amount</FormLabel>
                    <FormControl>
                      <Input placeholder="1000.00" {...field} type={"number"} />
                    </FormControl>
                    <FormMessage className={"text-primaryRed font-semibold"} />
                  </FormItem>
                )}
              />
            </div>
            <div>
              <FormField
                control={form.control}
                name="type"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="!text-current">
                      Transaction Type
                    </FormLabel>
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
                        <SelectItem value={"default"}>Select a type</SelectItem>
                        <SelectItem value={"DEPOSIT"}>Deposit</SelectItem>
                        <SelectItem value={"WITHDRAWAL"}>Withdrawal</SelectItem>
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
                name="status"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="!text-current">
                      Transaction Status
                    </FormLabel>
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
                        <SelectItem value={"default"}>Select a type</SelectItem>
                        <SelectItem value={"FAILED"}>Failed</SelectItem>
                        <SelectItem value={"IN_PROGRESS"}>
                          In Progress
                        </SelectItem>
                        <SelectItem value={"PENDING"}>Pending</SelectItem>
                        <SelectItem value={"COMPLETED"}>Completed</SelectItem>
                      </SelectContent>
                    </Select>
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
