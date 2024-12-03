'use client'

import {useEffect, useState} from "react";
import {useSepalModalContext} from "@/lib/context/SepalContext";
import {TradeImportSchema} from "@/lib/constants";
import {useForm} from "react-hook-form";
import {zodResolver} from "@hookform/resolvers/zod";
import {delay} from "@/lib/functions/util-functions";
import {z} from "zod";
import {Form, FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage} from "@/components/ui/form";
import {Input} from "@/components/ui/input";
import {Button} from "@/components/ui/button";
import {Loader2} from "lucide-react";
import {importTrades} from "@/lib/functions/trade-functions";
import {useToast} from "@/hooks/use-toast"

/**
 * Renders a form that can import trades
 *
 * @param account account info
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function ImportTradesForm(
  {
    account,
  }
  : Readonly<{
    account: Account
  }>
) {

  const { toast } = useToast();
  const [isLoading, setIsLoading] = useState(false)
  const [success, setSuccess] = useState<'success' | 'failed' | 'undefined'>('undefined')

  const {open, setOpen} = useSepalModalContext()
  const formSchema = TradeImportSchema()

  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
  })

  useEffect(() => {
    if (success === 'failed') {
      toast(
        {
          title: 'Trade Import Failed!',
          description: 'An error occurred while importing your trades. Please check your file and try again.',
          variant: 'danger'
        }
      )
    }
  }, [success]);


  //  GENERAL FUNCTIONS

  /**
   * Submits the form
   *
   * @param values form values
   */
  async function onSubmit(values: z.infer<typeof formSchema>) {

    setIsLoading(true)
    const data = await importTrades(account.accountNumber, values.filename[0])

    if (data) {
      setSuccess('success')
      setIsLoading(false)
      setOpen(false)
      window.location.reload()
    } else {
      setSuccess('failed')
      setIsLoading(false)
      setOpen(false)
    }
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
              <div className={'flex w-full justify-end items-center gap-4'}>
                <Button type="submit" className={'bg-primary text-white'} disabled={isLoading}>
                  {isLoading ? <Loader2 className="mr-2 h-4 w-4 animate-spin"/> : null}
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