'use client'

import { zodResolver } from "@hookform/resolvers/zod"
import { useForm } from "react-hook-form"
import { z } from "zod"

import { Button } from "@/components/ui/button"
import {
  Form,
  FormControl,
  FormDescription,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form"
import { Input } from "@/components/ui/input"
import {useEffect, useState} from "react";
import {accountCreationInfo} from "@/lib/sample-data";
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select";
import Link from "next/link";

const formSchema = z.object({
  defaultAccount: z.boolean(),
  balance: z.coerce.number().min(1).max(999999999),
  active: z.boolean(),
  name: z.string().min(3).max(75),
  accountNumber: z.coerce.number().min(1).max(99999999999),
  currency: z.string().min(3).max(3),
  broker: z.string().min(3).max(50),
  accountType: z.string().min(3).max(50),
  tradePlatform: z.string().min(3).max(50)
})

export default function NewAccountForm() {

  const [isLoading, setIsLoading] = useState(false)
  const [accInfo, setAccInfo] = useState<AccountCreationInfo>()

  useEffect(() => {
    //getAccountCreationInfo();
  }, [])

  //  set form default values
  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      defaultAccount: false,
      balance: 0.0,
      active: true,
      name: '',
      accountNumber: 0.0,
      currency: '',
      broker: '',
      accountType: '',
      tradePlatform: ''
    },
  })

  /**
   * Submits the form
   *
   * @param values form values
   */
  const delay = (ms: number) => new Promise(res => setTimeout(res, ms));
  function onSubmit(values: z.infer<typeof formSchema>) {
    console.log('clicked')
    // Do something with the form values.
    // ✅ This will be type-safe and validated.
    setIsLoading(true)
    //await delay(5000);
    console.log(values)
    setIsLoading(false)
  }

  //  temp delay
  async function getAccountCreationInfo() {

    setIsLoading(true)

    await delay(2000)
    setAccInfo(accountCreationInfo)

    setIsLoading(false)
  }

//TODO: fix valdiation errors by adding all fields
  //  RENDER

  return (
    <div>
      <Form {...form}>
        <form onSubmit={form.handleSubmit(onSubmit, (e) => console.log(e))} className="space-y-8">
          <FormField
            control={form.control}
            name="name"
            render={({field}) => (
              <FormItem>
                <FormLabel>Account Name</FormLabel>
                <FormControl>
                  <Input placeholder="Demo Trading Account" {...field} />
                </FormControl>
                <FormMessage/>
              </FormItem>
            )}
          />
          {/*<FormField
            control={form.control}
            name="accountNumber"
            render={({field}) => (
              <FormItem>
                <FormLabel>Account Number</FormLabel>
                <FormControl>
                  <Input placeholder="123456" {...field} type={'number'} />
                </FormControl>
                <FormMessage/>
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="balance"
            render={({field}) => (
              <FormItem>
                <FormLabel>Balance</FormLabel>
                <FormControl>
                  <Input placeholder="10000.00" {...field} type={'number'} />
                </FormControl>
                <FormMessage/>
              </FormItem>
            )}
          />*/}
          {/*<FormField
            control={form.control}
            name="currency"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Currency</FormLabel>
                <Select onValueChange={field.onChange} defaultValue={field.value}>
                  <FormControl>
                    <SelectTrigger>
                      <SelectValue placeholder="Select the account's currency" />
                    </SelectTrigger>
                  </FormControl>
                  <SelectContent>
                    <SelectItem value="CAD">CAD</SelectItem>
                    <SelectItem value="USD">USD</SelectItem>
                    <SelectItem value="EUR">EUR</SelectItem>
                  </SelectContent>
                </Select>
                <FormDescription>
                  You can manage email addresses in your{" "}
                  <Link href="/examples/forms">email settings</Link>.
                </FormDescription>
                <FormMessage />
              </FormItem>
            )}
          />*/}
          <Button type="submit" className={'bg-primary text-white'}>Submit</Button>
        </form>
      </Form>
    </div>
  )
}