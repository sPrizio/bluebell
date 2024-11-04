'use client'

import {useToast} from "@/hooks/use-toast";
import {useEffect, useState} from "react";
import {useSepalModalContext} from "@/lib/context/SepalContext";
import {CRUDTransactionSchema} from "@/lib/constants";
import {zodResolver} from "@hookform/resolvers/zod";
import {useForm} from "react-hook-form"
import {z} from "zod";
import {delay} from "@/lib/functions";
import {Form, FormControl, FormField, FormItem, FormLabel, FormMessage,} from "@/components/ui/form"
import TransactionDatePicker from "@/components/DateTime/TransactionDatePicker";
import {Button} from "@/components/ui/button";
import {Loader2} from "lucide-react";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select";
import {Input} from "@/components/ui/input";
import moment from "moment";

/**
 * Renders a form that can create or update a transaction
 *
 * @param mode should create / edit
 * @param account Account info
 * @param transaction transaction info
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function TransactionForm(
  {
    mode = 'create',
    account,
    transaction,
  }
    : Readonly<{
    mode?: 'create' | 'edit'
    account: Account,
    transaction?: Transaction
  }>
) {

  const {toast} = useToast();

  const [isLoading, setIsLoading] = useState(false)
  const [success, setSuccess] = useState<'success' | 'failed' | 'undefined'>('undefined')

  useEffect(() => {
    if (success === 'success') {
      toast(
        {
          title: isCreateMode() ? 'Transaction Created!' : 'Transaction Updated!',
          description: isCreateMode() ? 'The transaction was successfully created.' : 'The transaction was updated successfully.',
          variant: 'success'
        }
      )
    } else if (success === 'failed') {
      toast(
        {
          title: isCreateMode() ? 'Creation Failed!' : 'Update Failed!',
          description: isCreateMode() ? 'An error occurred while creating the transaction. Please check your inputs and try again.' : 'An error occurred while updating the transaction. Please check your inputs and try again.',
          variant: 'danger'
        }
      )
    }
  }, [success]);

  const {open, setOpen} = useSepalModalContext()
  const formSchema = CRUDTransactionSchema()

  if (!isCreateMode() && (!transaction || !transaction.date)) {
    throw new Error('Missing transaction for edit mode');
  }

  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      date: isCreateMode() ? new Date() : moment(transaction?.date).toDate(),
      type: isCreateMode() ? 'default' : transaction?.type,
      amount: isCreateMode() ? 0.0 : transaction?.amount,
      account: account.accountNumber
    }
  })


  //  GENERAL FUNCTIONS

  /**
   * Submits the form
   *
   * @param values form values
   */
  async function onSubmit(values: z.infer<typeof formSchema>) {
    // Do something with the form values.
    // ✅ This will be type-safe and validated.
    setIsLoading(true)
    await delay(4000);
    console.log(values)
    setIsLoading(false)

    setSuccess('success')
    setOpen(false)
  }

  /**
   * Returns true if the form is set to be in create mode, i.e. creating a new Account
   */
  function isCreateMode() {
    return mode === 'create';
  }


  //  RENDER

  return (
    <div>
      <Form {...form}>
        <form onSubmit={form.handleSubmit(onSubmit, (e) => console.log(e))} className={'space-y-8'}>
          <div className={'grid grid-cols-2 gap-4'}>
            <div className={''}>
              <FormField
                control={form.control}
                name='date'
                render={({field}) => (
                  <FormItem>
                    <FormLabel>Transaction Date</FormLabel>
                    <TransactionDatePicker field={field}/>
                    <FormMessage className={'text-primaryRed font-semibold'} />
                  </FormItem>
                )}
              />
            </div>
            <div>
              <FormField
                control={form.control}
                name="account"
                render={({field}) => (
                  <FormItem>
                    <FormLabel className="!text-current">Account</FormLabel>
                    <FormControl>
                      <Input {...field} type={'number'} disabled={true} />
                    </FormControl>
                    <FormMessage className={'text-primaryRed font-semibold'} />
                  </FormItem>
                )}
              />
            </div>
            <div>
              <FormField
                control={form.control}
                name="type"
                render={({field}) => (
                  <FormItem>
                    <FormLabel className="!text-current">Transaction Type</FormLabel>
                    <Select onValueChange={field.onChange} defaultValue={field.value}>
                      <FormControl>
                        <SelectTrigger>
                          <SelectValue />
                        </SelectTrigger>
                      </FormControl>
                      <SelectContent>
                        <SelectItem value={'default'}>Select a type</SelectItem>
                        <SelectItem value={'Deposit'}>Deposit</SelectItem>
                        <SelectItem value={'Withdrawal'}>Withdrawal</SelectItem>
                      </SelectContent>
                    </Select>
                    <FormMessage className={'text-primaryRed font-semibold'} />
                  </FormItem>
                )}
              />
            </div>
            <div>
              <FormField
                control={form.control}
                name="amount"
                render={({field}) => (
                  <FormItem>
                    <FormLabel className="!text-current">Balance</FormLabel>
                    <FormControl>
                      <Input placeholder="1000.00" {...field} type={'number'} />
                    </FormControl>
                    <FormMessage className={'text-primaryRed font-semibold'} />
                  </FormItem>
                )}
              />
            </div>
          </div>
          <div className={'flex w-full justify-end items-center gap-4'}>
            <Button type="submit" className={'bg-primary text-white'} disabled={isLoading}>
              {isLoading ? <Loader2 className="mr-2 h-4 w-4 animate-spin"/> : null}
              Submit
            </Button>
            <Button
              type="button"
              className={'border border-gray-400'}
              variant={"outline"}
              onClick={() => setOpen(false)}>
              Cancel
            </Button>
          </div>
        </form>
      </Form>
    </div>
  )
}