'use client'

import {useEffect} from "react";
import {useSepalModalContext} from "@/lib/context/SepalContext";
import {TradeImportSchema} from "@/lib/constants";
import {useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import {z} from "zod";
import {Form, FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage} from "@/components/ui/form";
import {Input} from "@/components/ui/input";
import {Button} from "@/components/ui/button";
import {Loader2} from "lucide-react";
import {useToast} from "@/lib/hooks/ui/use-toast"
import {Switch} from "@/components/ui/switch";
import {Account} from "@/types/apiTypes";
import {useImportTradesMutation} from "@/lib/hooks/query/mutations";
import {logErrors} from "@/lib/functions/util-functions";

/**
 * Renders a form that can import trades
 *
 * @param account account info
 * @author Stephen Prizio
 * @version 0.2.0
 */
export default function ImportTradesForm(
  {
    account,
  }
  : Readonly<{
    account: Account
  }>
) {

  const {toast} = useToast();
  const {setOpen} = useSepalModalContext()
  const formSchema = TradeImportSchema()

  const {
    mutate: importTrades,
    isPending: isImportTradesLoading,
    isSuccess: isImportTradesSuccess,
    isError: isImportTradesError,
    error: importTradesError
  } = useImportTradesMutation(account?.accountNumber ?? -1)

  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
  })

  useEffect(() => {
    if (isImportTradesSuccess) {
      toast({
        title: 'Trade Import Successful!',
        description: 'Your trades were successfully imported.',
        variant: 'success'
      })

      setOpen(false)
    }
  }, [isImportTradesSuccess]);

  useEffect(() => {
    if (isImportTradesError) {
      toast({
        title: 'Trade Import Failed!',
        description: 'An error occurred while importing your trades. Please check your file and try again.',
        variant: 'danger'
      })

      logErrors(importTradesError)
    }
  }, [isImportTradesError]);


  //  GENERAL FUNCTIONS

  /**
   * Submits the form
   *
   * @param values form values
   */
  async function onSubmit(values: z.infer<typeof formSchema>) {
    importTrades(values)
  }


  //  RENDER

  const fileRef = form.register("filename");

  return (
    <div className={''}>
      <Form {...form}>
        <form onSubmit={form.handleSubmit(onSubmit, (e) => console.log(e))}>
          <div className={'grid grid-cols-1 gap-4 items-center'}>
            <div className={''}>
              <FormField
                control={form.control}
                name="filename"
                render={({field}) => (
                  <FormItem>
                    <FormLabel className="!text-current">Trades File</FormLabel>
                    <FormControl>
                      <Input placeholder="No file chosen" {...fileRef} type={'file'}/>
                    </FormControl>
                    <FormMessage className={'text-primaryRed font-semibold'}/>
                    <FormDescription>
                      only .csv & .html files are currently supported for file importing.
                    </FormDescription>
                  </FormItem>
                )}
              />
            </div>
            <div className={''}>
              <FormField
                control={form.control}
                name="isStrategy"
                render={({field}) => (
                  <FormItem>
                    <div className={'flex items-center gap-4 w-full'}>
                      <div className={'w-full'}>
                        <FormLabel className="!text-current">Simulated Account</FormLabel>
                      </div>
                      <div className={'flex items-center justify-end text-right'}>
                        <FormControl>
                          <Switch
                            checked={field.value}
                            onCheckedChange={field.onChange}
                          />
                        </FormControl>
                      </div>
                    </div>
                    <FormDescription>
                      Select this option if you&apos;re importing strategy/simulation results and not actual trades.
                    </FormDescription>
                    <FormMessage className={'text-primaryRed font-semibold'}/>
                  </FormItem>
                )}
              />
            </div>
            <div className={''}>
              <div className={'flex w-full justify-end items-center gap-4'}>
                <Button type="submit" className={'bg-primary text-white'} disabled={isImportTradesLoading}>
                  {isImportTradesLoading ? <Loader2 className="mr-2 h-4 w-4 animate-spin"/> : null}
                  Submit
                </Button>
                <Button type="button" className={'border border-gray-400'} variant={"outline"}
                        onClick={() => setOpen(false)}>
                  Cancel
                </Button>
              </div>
            </div>
          </div>
        </form>
      </Form>
    </div>
  )
}