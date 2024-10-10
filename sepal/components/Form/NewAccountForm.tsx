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
import {Loader2} from "lucide-react";
import {useSepalModalContext} from "@/lib/context/SepalContext";

/**
 * Renders a form that will be used to create a new account
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function NewAccountForm() {

  const [isLoading, setIsLoading] = useState(false)
  const [accInfo, setAccInfo] = useState<AccountCreationInfo>()

  useEffect(() => {
    getAccountCreationInfo();
  }, [])

  const { open, setOpen } = useSepalModalContext()

  const formSchema = z.object({
    defaultAccount: z.boolean(),
    balance: z.coerce.number().min(1, { message: 'Please enter a number between 1 and 999999999.' }).max(999999999, { message: 'Please enter a number between 1 and 999999999.' }),
    active: z.boolean(),
    name: z.string().min(3, { message: 'Please enter an account name with a minimum of 3 characters.' }).max(75, { message: 'Please enter an account name with at most 75 characters.' }),
    accountNumber: z.coerce.number().min(1, { message: 'Please enter a number between 1 and 99999999999.' }).max(99999999999, { message: 'Please enter a number between 1 and 99999999999.' }),
    currency: z.enum(safeConvertEnum(accInfo?.currencies?.map(item => item.code) ?? []), { message: 'Please select one of the given currencies.' }),
    broker: z.enum(safeConvertEnum(accInfo?.brokers?.map(item => item.code) ?? []), { message: 'Please select one of the given brokers.' }),
    accountType: z.enum(safeConvertEnum(accInfo?.accountTypes?.map(item => item.code) ?? []), { message: 'Please select one of the given account types.' }),
    tradePlatform: z.enum(safeConvertEnum(accInfo?.platforms?.map(item => item.code) ?? []), { message: 'Please select one of the given trading platforms.' })
  })

  /**
   * Converts string array in an enum for zos
   *
   * @param val array of strings
   */
  function safeConvertEnum(val: string[]) : [string, ...string[]] {
    // @ts-ignore
    return val
  }

  //  set form default values
  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      defaultAccount: false,
      balance: undefined,
      active: true,
      name: '',
      accountNumber: undefined,
      currency: 'default',
      broker: 'default',
      accountType: 'default',
      tradePlatform: 'default'
    },
  })

  /**
   * Submits the form
   *
   * @param values form values
   */
  const delay = (ms: number) => new Promise(res => setTimeout(res, ms));
  async function onSubmit(values: z.infer<typeof formSchema>) {
    // Do something with the form values.
    // ✅ This will be type-safe and validated.
    setIsLoading(true)
    await delay(5000);
    console.log(values)
    setIsLoading(false)
    console.log(open)
  }

  /**
   * Fetches the account creation information
   */
  async function getAccountCreationInfo() {

    setIsLoading(true)

    await delay(2000)
    setAccInfo(accountCreationInfo)

    setIsLoading(false)
  }


  //  RENDER

  return (
    <div>
      <Form {...form}>
        <form onSubmit={form.handleSubmit(onSubmit, (e) => console.log(e))} className="space-y-8">
          <div className={'grid grid-cols-1 gap-4'}>
            <FormField
              control={form.control}
              name="name"
              render={({field}) => (
                <FormItem>
                  <FormLabel className="!text-current">Account Name</FormLabel>
                  <FormControl>
                    <Input placeholder="Demo Trading Account" {...field} />
                  </FormControl>
                  <FormMessage className={'text-primaryRed font-semibold'} />
                </FormItem>
              )}
            />
          </div>
          <div className={'grid grid-cols-2 gap-4'}>
            <div className={""}>
              <FormField
                control={form.control}
                name="accountNumber"
                render={({field}) => (
                  <FormItem>
                    <FormLabel className="!text-current">Account Number</FormLabel>
                    <FormControl>
                      <Input placeholder="123" {...field} type={'number'} />
                    </FormControl>
                    <FormMessage className={'text-primaryRed font-semibold'} />
                  </FormItem>
                )}
              />
            </div>
            <div className={""}>
              <FormField
                control={form.control}
                name="balance"
                render={({field}) => (
                  <FormItem>
                    <FormLabel className="!text-current">Balance</FormLabel>
                    <FormControl>
                      <Input placeholder="10000.00" {...field} type={'number'} />
                    </FormControl>
                    <FormMessage className={'text-primaryRed font-semibold'} />
                  </FormItem>
                )}
              />
            </div>
            <div className={""}>
              <FormField
                control={form.control}
                name="currency"
                render={({field}) => (
                  <FormItem>
                    <FormLabel className="!text-current">Currency</FormLabel>
                    <Select onValueChange={field.onChange} defaultValue={field.value}>
                      <FormControl>
                        <SelectTrigger>
                          <SelectValue />
                        </SelectTrigger>
                      </FormControl>
                      <SelectContent>
                        <SelectItem value={'default'}>Select a currency</SelectItem>
                        {
                          accountCreationInfo?.currencies?.map((item : AccountCreationInfoOption) => {
                            return (
                              <SelectItem key={item.uid} value={item.code}>{item.label}</SelectItem>
                            )
                          }) ?? null
                        }
                      </SelectContent>
                    </Select>
                    <FormMessage className={'text-primaryRed font-semibold'} />
                  </FormItem>
                )}
              />
            </div>
            <div className={""}>
              <FormField
                control={form.control}
                name="broker"
                render={({field}) => (
                  <FormItem>
                    <FormLabel className="!text-current">Broker</FormLabel>
                    <Select onValueChange={field.onChange} defaultValue={field.value}>
                      <FormControl>
                        <SelectTrigger>
                          <SelectValue />
                        </SelectTrigger>
                      </FormControl>
                      <SelectContent>
                        <SelectItem value={'default'}>Select a broker</SelectItem>
                        {
                          accountCreationInfo?.brokers?.map((item : AccountCreationInfoOption) => {
                            return (
                              <SelectItem key={item.uid} value={item.code}>{item.label}</SelectItem>
                            )
                          }) ?? null
                        }
                      </SelectContent>
                    </Select>
                    <FormMessage className={'text-primaryRed font-semibold'} />
                  </FormItem>
                )}
              />
            </div>
            <div className={""}>
              <FormField
                control={form.control}
                name="accountType"
                render={({field}) => (
                  <FormItem>
                    <FormLabel className="!text-current">Type of Account</FormLabel>
                    <Select onValueChange={field.onChange} defaultValue={field.value}>
                      <FormControl>
                        <SelectTrigger>
                          <SelectValue />
                        </SelectTrigger>
                      </FormControl>
                      <SelectContent>
                        <SelectItem value={'default'}>Select a security type</SelectItem>
                        {
                          accountCreationInfo?.accountTypes?.map((item : AccountCreationInfoOption) => {
                            return (
                              <SelectItem key={item.uid} value={item.code}>{item.label}</SelectItem>
                            )
                          }) ?? null
                        }
                      </SelectContent>
                    </Select>
                    <FormDescription>
                      This refers to the type of security traded within the account.
                    </FormDescription>
                    <FormMessage className={'text-primaryRed font-semibold'} />
                  </FormItem>
                )}
              />
            </div>
            <div className={""}>
              <FormField
                control={form.control}
                name="tradePlatform"
                render={({field}) => (
                  <FormItem>
                    <FormLabel className="!text-current">Trade Platform</FormLabel>
                    <Select onValueChange={field.onChange} defaultValue={field.value}>
                      <FormControl>
                        <SelectTrigger>
                          <SelectValue />
                        </SelectTrigger>
                      </FormControl>
                      <SelectContent>
                        <SelectItem value={'default'}>Select a trading platform</SelectItem>
                        {
                          accountCreationInfo?.platforms?.map((item : AccountCreationInfoOption) => {
                            return (
                              <SelectItem key={item.uid} value={item.code}>{item.label}</SelectItem>
                            )
                          }) ?? null
                        }
                      </SelectContent>
                    </Select>
                    <FormDescription>
                      This field refers to the platform used to execute trades for this account.
                      If multiple platforms, select the most frequently used one.
                    </FormDescription>
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
            <Button type="button" className={'border border-gray-400'} variant={"outline"} onClick={() => setOpen(false)}>
              Cancel
            </Button>
          </div>
        </form>
      </Form>
    </div>
  )
}