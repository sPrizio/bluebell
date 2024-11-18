'use client'

import {useState} from "react";
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

  const [isLoading, setIsLoading] = useState(false)
  const [file, setFile] = useState<File | null>(null)

  const { open, setOpen } = useSepalModalContext()
  const formSchema = TradeImportSchema()

  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
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

    console.log(file)
    //file[0]

    //setSuccess('success')
    setOpen(false)
    setFile(null)
    //window.location.reload()
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