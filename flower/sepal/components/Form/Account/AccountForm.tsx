'use client'

import {zodResolver} from "@hookform/resolvers/zod"
import {useForm} from "react-hook-form"
import {z} from "zod"

import {Button} from "@/components/ui/button"
import {Form, FormControl, FormDescription, FormField, FormItem, FormLabel, FormMessage,} from "@/components/ui/form"
import {Input} from "@/components/ui/input"
import {Select, SelectContent, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select";
import {Loader2} from "lucide-react";
import {useSepalModalContext} from "@/lib/context/SepalContext";
import {useToast} from "@/hooks/use-toast"
import {CRUDAccountSchema} from "@/lib/constants";
import {Switch} from "@/components/ui/switch";
import {Account, AccountType, Broker, Currency, TradePlatform} from "@/types/apiTypes"
import {useAccountCreationInfoQuery} from "@/lib/hooks/queries";
import {logErrors} from "@/lib/functions/util-functions";
import {useCreateAccountMutation, useUpdateAccountMutation} from "@/lib/hooks/mutations";
import React, {useEffect} from "react";

/**
 * Renders a form that can create or update an Account
 *
 * @param create should create / edit
 * @param portfolioNumber portfolio number
 * @param account Account info
 * @author Stephen Prizio
 * @version 0.2.0
 */
export default function AccountForm(
  {
    mode = 'create',
    portfolioNumber,
    account,
  }
  : Readonly<{
    mode?: 'create' | 'edit'
    portfolioNumber: number
    account?: Account
  }>
) {

  if (!isCreateMode() && !account?.accountNumber) {
    throw new Error('Missing Account for edit mode');
  }

  const {toast} = useToast();
  const {setOpen} = useSepalModalContext()
  const {
    mutate: createAccount,
    isPending: isCreateAccountLoading,
    isSuccess: isCreateAccountSuccess,
    isError: isCreateAccountError,
    error: createAccountError
  } = useCreateAccountMutation(portfolioNumber);
  const {
    mutate: updateAccount,
    isPending: isUpdateAccountLoading,
    isSuccess: isUpdateAccountSuccess,
    isError: isUpdateAccountError,
    error: updateAccountError,
  } = useUpdateAccountMutation(portfolioNumber, account?.accountNumber ?? -1);
  const {
    data: accCreateInfo,
    error: accCreateInfoError,
    isError: isAccCreateInfoError,
    isLoading: isAccCreateInfoLoading
  } = useAccountCreationInfoQuery();

  const isLoading = isCreateAccountLoading ?? isAccCreateInfoLoading ?? isUpdateAccountLoading

  useEffect(() => {
    if (isCreateAccountSuccess) {
      renderSuccessNotification()
      setOpen(false)
    } else if (isCreateAccountError) {
      logErrors(createAccountError)
      renderErrorNotification()
    }
  }, [isCreateAccountSuccess, isCreateAccountError]);

  useEffect(() => {
    if (isUpdateAccountSuccess) {
      renderSuccessNotification()
      setOpen(false)
      window.location.reload();
    } else if (isUpdateAccountError) {
      logErrors(updateAccountError)
      renderErrorNotification()
    }
  }, [isUpdateAccountSuccess, isUpdateAccountError]);

  if (isAccCreateInfoError) {
    logErrors(accCreateInfoError)
    setOpen(false)
    renderErrorNotification()

    throw new Error('Acc Create Info Error');
  }

  const formSchema = CRUDAccountSchema(accCreateInfo)
  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      isDefault: isCreateMode() ? false : account?.defaultAccount,
      balance: isCreateMode() ? 0 : account?.balance,
      active: isCreateMode() ? true : account?.active,
      name: isCreateMode() ? '' : account?.name,
      number: isCreateMode() ? 0 : account?.accountNumber,
      currency: isCreateMode() ? 'default' : account?.currency?.code,
      broker: isCreateMode() ? 'default' : account?.broker?.code,
      type: isCreateMode() ? 'default' : account?.accountType?.code,
      tradePlatform: isCreateMode() ? 'default' : account?.tradePlatform?.code
    },
  })


  //  GENERAL FUNCTIONS

  /**
   * Renders the success notifications
   */
  function renderSuccessNotification() {
    toast(
      {
        title: isCreateMode() ? 'Account Created!' : 'Account Updated!',
        description: isCreateMode() ? 'Your new trading account was successfully created.' : 'Your trading account was updated successfully.',
        variant: 'success'
      }
    )
  }

  /**
   * Renders the error notifications
   */
  function renderErrorNotification() {
    toast(
      {
        title: isCreateMode() ? 'Creation Failed!' : 'Update Failed!',
        description: isCreateMode() ? 'An error occurred while creating new trading Account. Please check your inputs and try again.' : 'An error occurred while updating your trading account. Please check your inputs and try again.',
        variant: 'danger'
      }
    )
  }

  /**
   * Submits the form
   *
   * @param values form values
   */
  function onSubmit(values: z.infer<typeof formSchema>) {
    if (isCreateMode()) {
      createAccount(values)
    } else {
      updateAccount(values)
    }
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
                  <FormMessage className={'text-primaryRed font-semibold'}/>
                </FormItem>
              )}
            />
          </div>
          <div className={'grid grid-cols-2 gap-4'}>
            <div className={""}>
              <FormField
                control={form.control}
                name="number"
                render={({field}) => (
                  <FormItem>
                    <FormLabel className="!text-current">Account Number</FormLabel>
                    <FormControl>
                      <Input placeholder="123" {...field} type={'number'}/>
                    </FormControl>
                    <FormMessage className={'text-primaryRed font-semibold'}/>
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
                      <Input placeholder="10000.00" {...field} type={'number'}/>
                    </FormControl>
                    <FormMessage className={'text-primaryRed font-semibold'}/>
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
                          <SelectValue/>
                        </SelectTrigger>
                      </FormControl>
                      <SelectContent>
                        <SelectItem value={'default'}>Select a currency</SelectItem>
                        {
                          accCreateInfo?.currencies?.map((item: Currency) => {
                            return (
                              <SelectItem key={item.uid} value={item.code}>{item.label}</SelectItem>
                            )
                          }) ?? null
                        }
                      </SelectContent>
                    </Select>
                    <FormMessage className={'text-primaryRed font-semibold'}/>
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
                          <SelectValue/>
                        </SelectTrigger>
                      </FormControl>
                      <SelectContent>
                        <SelectItem value={'default'}>Select a broker</SelectItem>
                        {
                          accCreateInfo?.brokers?.map((item: Broker) => {
                            return (
                              <SelectItem key={item.uid} value={item.code}>{item.label}</SelectItem>
                            )
                          }) ?? null
                        }
                      </SelectContent>
                    </Select>
                    <FormMessage className={'text-primaryRed font-semibold'}/>
                  </FormItem>
                )}
              />
            </div>
            <div className={""}>
              <FormField
                control={form.control}
                name="type"
                render={({field}) => (
                  <FormItem>
                    <FormLabel className="!text-current">Type of Account</FormLabel>
                    <Select onValueChange={field.onChange} defaultValue={field.value}>
                      <FormControl>
                        <SelectTrigger>
                          <SelectValue/>
                        </SelectTrigger>
                      </FormControl>
                      <SelectContent>
                        <SelectItem value={'default'}>Select a security type</SelectItem>
                        {
                          accCreateInfo?.accountTypes?.map((item: AccountType) => {
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
                    <FormMessage className={'text-primaryRed font-semibold'}/>
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
                          <SelectValue/>
                        </SelectTrigger>
                      </FormControl>
                      <SelectContent>
                        <SelectItem value={'default'}>Select a trading platform</SelectItem>
                        {
                          accCreateInfo?.platforms?.map((item: TradePlatform) => {
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
                    <FormMessage className={'text-primaryRed font-semibold'}/>
                  </FormItem>
                )}
              />
            </div>
            <div className={''}>
              <FormField
                control={form.control}
                name="isDefault"
                render={({field}) => (
                  <FormItem>
                    <div className={'flex items-center gap-4 w-full'}>
                      <div className={'w-full'}>
                        <FormLabel className="!text-current">Default Account</FormLabel>
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
                      A default account is an account that will be considered the primary or main account.
                    </FormDescription>
                    <FormMessage className={'text-primaryRed font-semibold'}/>
                  </FormItem>
                )}
              />
            </div>
            <div className={''}>
              <FormField
                control={form.control}
                name="active"
                render={({field}) => (
                  <FormItem>
                    <div className={'flex items-center gap-4 w-full'}>
                      <div className={'w-full'}>
                        <FormLabel className="!text-current">Active</FormLabel>
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
                      Active accounts will have their trades tracked and periodically updated, inactive accounts
                      will be archived for reference purposes.
                    </FormDescription>
                    <FormMessage className={'text-primaryRed font-semibold'}/>
                  </FormItem>
                )}
              />
            </div>
          </div>
          <div className={'flex w-full justify-end items-center gap-4'}>
            <Button type="submit" className={'bg-primary text-white'} disabled={isLoading}>
              {(isLoading) ? <Loader2 className="mr-2 h-4 w-4 animate-spin"/> : null}
              Submit
            </Button>
            <Button type="button" className={'border border-gray-400'} variant={"outline"}
                    onClick={() => setOpen(false)}>
              Cancel
            </Button>
          </div>
        </form>
      </Form>
    </div>
  )
}