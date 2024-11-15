'use client'

import React, {useState} from "react";
import {zodResolver} from "@hookform/resolvers/zod"
import {useForm} from "react-hook-form"
import {z} from "zod"
import {Form, FormControl, FormField, FormItem, FormLabel, FormMessage} from "@/components/ui/form";
import {Input} from "@/components/ui/input";
import {Button} from "@/components/ui/button";
import {CRUDUserSchema, ForgotPasswordSchema, LoginSchema} from "@/lib/constants";
import {delay} from "@/lib/functions/util-functions";
import MainLogo from "@/components/Navigation/MainLogo";
import {Loader2} from "lucide-react";
import {
  IconArrowLeft,
  IconBrandAppleFilled,
  IconBrandFacebookFilled,
  IconBrandGoogleFilled,
  IconMailFilled
} from "@tabler/icons-react";

/**
 * Renders the login page
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function Login() {

  const [isLoading, setIsLoading] = useState(false)
  const [success, setSuccess] = useState<'success' | 'failed' | 'undefined'>('undefined')
  const [state, setState] = useState<'login' | 'register' | 'forgot'>('register')
  const [showRegisterForm, setShowRegisterForm] = useState(false)

  const loginFormSchema = LoginSchema()
  const forgotFormSchema = ForgotPasswordSchema()
  const registerFormSchema = CRUDUserSchema(false)

  const loginForm = useForm<z.infer<typeof loginFormSchema>>({
    resolver: zodResolver(loginFormSchema),
    defaultValues: {
      username: '',
      password: ''
    }
  })

  const forgotForm = useForm<z.infer<typeof forgotFormSchema>>({
    resolver: zodResolver(forgotFormSchema),
    defaultValues: {
      email: '',
    }
  })

  const registerForm = useForm<z.infer<typeof registerFormSchema>>({
    resolver: zodResolver(registerFormSchema),
    defaultValues: {
      firstName: '',
      lastName: '',
      username: '',
      email: '',
      phoneType: '',
      telephoneNumber: '',
      password: '',
      confirmPassword: '',
    }
  })


  //  GENERAL FUNCTIONS

  /**
   * Submits the login form
   *
   * @param values form values
   */
  async function onLoginSubmit(values: z.infer<typeof loginFormSchema>) {
    // Do something with the form values.
    // ✅ This will be type-safe and validated.
    setIsLoading(true)
    await delay(4000);
    console.log(values)

    setSuccess("success")
    setIsLoading(false)
  }

  /**
   * Submits the forgot password form
   *
   * @param values form values
   */
  async function onForgotPasswordSubmit(values: z.infer<typeof forgotFormSchema>) {
    // Do something with the form values.
    // ✅ This will be type-safe and validated.
    setIsLoading(true)
    await delay(4000);
    console.log(values)

    setSuccess("success")
    setIsLoading(false)
  }

  /**
   * Submits the user registration form
   *
   *
   * @param values form values
   */
  async function onRegisterSubmit(values: z.infer<typeof registerFormSchema>) {
    // Do something with the form values.
    // ✅ This will be type-safe and validated.
    setIsLoading(true)
    await delay(4000);
    console.log(values)

    setSuccess("success")
    setIsLoading(false)
  }

  /**
   * Displays the appropriate text based on the form state
   */
  function computeDisplayText() {
    switch (state) {
      case 'login':
        return 'Sign In'
      case 'register':
        return 'Create Account'
      default:
        return 'Forgot Username/Password'
    }
  }

  /**
   * Computes the width of the form depending on its state
   */
  function computeMaxWidth() {
    switch (state) {
      case 'login':
        return ' max-w-[800px] '
      case 'register':
        return ' max-w-[425px] '
      default:
        return ' max-w-[375px] '
    }
  }


  //  RENDER

  return (
    <div className={'h-[100vh] flex items-center justify-center w-full'}>
      <div className={'grid grid-cols-1 justify-items-center w-full'}>
        <div className={'mb-6'}>
          <div className={'flex items-center justify-center'}>
            <MainLogo size={100}/>
          </div>
        </div>
        <div className={'bg-white rounded-2xl shadow-lg p-8 w-4/5 lg:w-3/5 ' + computeMaxWidth()}>
          <div className={'grid grid-cols-1 items-center mb-6 text-center'}>
            <div className={'font-bold tracking-tighter text-md text-tertiary p-2'}>{computeDisplayText()}</div>
          </div>
          {
            state === 'login' ?
              <div className={'grid grid-cols-2 items-center'}>
                <div
                  className={'max-lg:col-span-2 lg:pr-12 max-lg:pb-12 max-lg:border-b-1 lg:border-r-1 border-gray-100'}>
                  <Form {...loginForm}>
                    <form onSubmit={loginForm.handleSubmit(onLoginSubmit, (e) => console.log(e))}>
                      <div className={'grid grid-cols-1 gap-4 items-center'}>
                        <div>
                          <FormField
                            control={loginForm.control}
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
                            control={loginForm.control}
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
                      <Button variant={'ghost'} className={'text-current'} onClick={() => setState('forgot')}>Trouble
                        logging in?</Button>
                    </div>
                    <div className={'text-right'}>
                      <Button variant={'ghost'} className={'text-current'} onClick={() => setState("register")}>Create
                        Account (TODO)</Button>
                    </div>
                  </div>
                </div>
              </div> : null
          }
          {
            state === 'forgot' ?
              <div className={'grid grid-cols-1 items-center mb-6'}>
                <Form {...forgotForm}>
                  <form onSubmit={forgotForm.handleSubmit(onForgotPasswordSubmit, (e) => console.log(e))}>
                    <div className={'grid grid-cols-1 gap-4 items-center'}>
                      <div className={'text-sm text-center'}>
                        Enter your account’s email and we’ll send you an email to reset the password.
                      </div>
                      <div>
                        <FormField
                          control={forgotForm.control}
                          name="email"
                          render={({field}) => (
                            <FormItem>
                              <FormLabel className="!text-current">Email Address</FormLabel>
                              <FormControl>
                                <Input placeholder="john.trader@email.com" {...field} type={'text'}/>
                              </FormControl>
                              <FormMessage className={'text-primaryRed font-semibold'}/>
                            </FormItem>
                          )}
                        />
                      </div>
                      <div className={'flex w-full items-center mt-2'}>
                        <Button type="submit" className={'w-full bg-primary text-white'} disabled={isLoading}>
                          {isLoading ? <Loader2 className="mr-2 h-4 w-4 animate-spin"/> : null}
                          Send Email
                        </Button>
                      </div>
                    </div>
                  </form>
                </Form>
              </div> : null
          }
          {
            state === 'register' ?
              <div className={'grid grid-cols-1 items-center mb-6'}>
                <Form {...registerForm}>
                  <form onSubmit={registerForm.handleSubmit(onRegisterSubmit, (e) => console.log(e))}>
                    <div className={'grid grid-cols-1 gap-8 items-center'}>
                      <div className={'text-sm text-center'}>
                        By creating an account, you agree to our Terms of Service and have read and understood the Privacy Policy.
                      </div>
                      <div className={'grid grid-cols-1 gap-4 items-center'}>
                        <div>
                          <Button className={'w-full'} variant={'outline'} onClick={() => setShowRegisterForm(true)}>
                            <IconMailFilled size={18} className={'mr-2 text-primary'}/>Continue with Email
                          </Button>
                        </div>
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
                  </form>
                </Form>
              </div> : null
          }
          {
            state !== 'login' ?
              <div className={'grid grid-cols-2 items-center mt-8 text-center'}>
                <div>
                <div className={'text-left'}>
                    {
                      state === 'forgot' || state === 'register' ?
                        <Button variant={'ghost'} className={'text-current'} onClick={() => setState('login')}>
                          <IconArrowLeft size={18}/>&nbsp;&nbsp;Login
                        </Button>
                        : null
                    }
                  </div>
                </div>
                <div/>
              </div> : null
          }
        </div>
      </div>
    </div>
  )
}