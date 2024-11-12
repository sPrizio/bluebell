'use client'

import React, {useState} from "react";
import {zodResolver} from "@hookform/resolvers/zod"
import {useForm} from "react-hook-form"
import {z} from "zod"
import {Form, FormControl, FormField, FormItem, FormLabel, FormMessage} from "@/components/ui/form";
import {Input} from "@/components/ui/input";
import {Button} from "@/components/ui/button";
import {LoginSchema} from "@/lib/constants";
import {delay} from "@/lib/functions/util-functions";
import MainLogo from "@/components/Navigation/MainLogo";
import {Loader2} from "lucide-react";
import {IconBrandAppleFilled, IconBrandFacebookFilled, IconBrandGoogleFilled} from "@tabler/icons-react";

export default function Login() {

  const [isLoading, setIsLoading] = useState(false)
  const [success, setSuccess] = useState<'success' | 'failed' | 'undefined'>('undefined')

  const formSchema = LoginSchema()

  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      username: '',
      password: ''
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

    setSuccess("success")
    setIsLoading(false)
  }


  //  RENDER

  return (
    <div className={'h-[100vh] flex items-center justify-center w-full'}>
      <div className={'grid grid-cols-1 justify-items-center w-full'}>
        <div className={'bg-white rounded-2xl shadow-lg p-8 w-4/5 lg:w-3/5'}>
          <p>Forgot password page</p>
          <p>Create account page</p>
          <div className={'grid grid-cols-2'}>
            <div className={'col-span-2 mb-8'}>
              <div className={'flex items-center justify-center'}>
                <MainLogo/>
              </div>
            </div>
            <div className={'max-lg:col-span-2 lg:pr-12 max-lg:pb-12 max-lg:border-b-1 lg:border-r-1 border-gray-100'}>
              <Form {...form}>
                <form onSubmit={form.handleSubmit(onSubmit, (e) => console.log(e))}>
                  <div className={'grid grid-cols-1 gap-4 items-center'}>
                    <div>
                      <FormField
                        control={form.control}
                        name="username"
                        render={({field}) => (
                          <FormItem>
                            <FormLabel className="!text-current">Username</FormLabel>
                            <FormControl>
                              <Input placeholder="john.trader" {...field} type={'text'}/>
                            </FormControl>
                            <FormMessage className={'text-primaryRed font-semibold'}/>
                          </FormItem>
                        )}
                      />
                    </div>
                    <div>
                      <FormField
                        control={form.control}
                        name="password"
                        render={({field}) => (
                          <FormItem>
                            <FormLabel className="!text-current">Password</FormLabel>
                            <FormControl>
                              <Input placeholder="Password" {...field} type={'password'}/>
                            </FormControl>
                            <FormMessage className={'text-primaryRed font-semibold'}/>
                          </FormItem>
                        )}
                      />
                    </div>
                    <div className={'flex w-full items-center mt-4'}>
                      <Button type="submit" className={'w-full bg-primary text-white'} disabled={isLoading}>
                        {isLoading ? <Loader2 className="mr-2 h-4 w-4 animate-spin"/> : null}
                        Log In
                      </Button>
                    </div>
                  </div>
                </form>
              </Form>
            </div>
            <div
              className={'max-lg:col-span-2 lg:pl-12 max-lg:pt-12 max-lg:border-t-1 lg:border-l-1 border-gray-100 flex items-center w-full'}>
              <div className={'w-full'}>
                <div className={'grid grid-cols-1 gap-4 items-center'}>
                  <div>
                    <Button className={'w-full'} variant={'outline'}>
                      <IconBrandGoogleFilled size={18} className={'mr-2 text-primary'}/>Continue with Google
                    </Button>
                  </div>
                  <div>
                    <Button className={'w-full'} variant={'outline'}>
                      <IconBrandAppleFilled size={18} className={'mr-2 text-primary'}/>Continue with Apple
                    </Button>
                  </div>
                  <div>
                    <Button className={'w-full'} variant={'outline'}>
                      <IconBrandFacebookFilled size={18} className={'mr-2 text-primary'}/>Continue with Facebook
                    </Button>
                  </div>
                </div>
              </div>
            </div>
            <div className={'col-span-2 mt-8 text-center'}>
              <div className={'grid grid-cols-2 gap-2 items-center'}>
                <div className={'text-left'}>
                  <Button variant={'link'} className={'text-current'}>Trouble logging in?</Button>
                </div>
                <div className={'text-right'}>
                  <Button variant={'link'} className={'text-current'}>Create Account</Button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}