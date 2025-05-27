"use client";

import React, { useState } from "react";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { z } from "zod";
import {
  Form,
  FormControl,
  FormDescription,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import {
  CRUDUserSchema,
  ForgotPasswordSchema,
  LoginSchema,
} from "@/lib/constants";
import { delay } from "@/lib/functions/util-functions";
import MainLogo from "@/components/Navigation/MainLogo";
import { Loader2 } from "lucide-react";
import { IconArrowLeft } from "@tabler/icons-react";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import parsePhoneNumberFromString from "libphonenumber-js";
import SimpleMessage from "@/components/Message/SimpleMessage";
import { useToast } from "@/lib/hooks/ui/use-toast";
import { Toaster } from "@/components/ui/toaster";
import { resolveIcon } from "@/lib/functions/util-component-functions";
import { Icons } from "@/lib/enums";

/**
 * Renders the login page
 *
 * @author Stephen Prizio
 * @version 0.2.0
 */
export default function Login() {
  const { toast } = useToast();
  const [isLoading, setIsLoading] = useState(false);
  const [success, setSuccess] = useState<"success" | "failed" | "undefined">(
    "undefined",
  );
  const [state, setState] = useState<"login" | "register" | "forgot">("login");
  const [showRegisterForm, setShowRegisterForm] = useState(false);

  const loginFormSchema = LoginSchema();
  const forgotFormSchema = ForgotPasswordSchema();
  const registerFormSchema = CRUDUserSchema(false);

  const loginForm = useForm<z.infer<typeof loginFormSchema>>({
    resolver: zodResolver(loginFormSchema),
    defaultValues: {
      username: "",
      password: "",
    },
  });

  const forgotForm = useForm<z.infer<typeof forgotFormSchema>>({
    resolver: zodResolver(forgotFormSchema),
    defaultValues: {
      email: "",
    },
  });

  const registerForm = useForm<z.infer<typeof registerFormSchema>>({
    resolver: zodResolver(registerFormSchema),
    defaultValues: {
      firstName: "",
      lastName: "",
      username: "",
      email: "",
      phoneType: "MOBILE",
      telephoneNumber: "",
      password: "",
      confirmPassword: "",
    },
  });

  //  GENERAL FUNCTIONS

  /**
   * Submits the login form
   *
   * @param values form values
   */
  async function onLoginSubmit(values: z.infer<typeof loginFormSchema>) {
    // Do something with the form values.
    // ✅ This will be type-safe and validated.
    setIsLoading(true);
    await delay(4000);
    console.log(values);

    setSuccess("success");
    setIsLoading(false);
  }

  /**
   * Submits the forgot password form
   *
   * @param values form values
   */
  async function onForgotPasswordSubmit(
    values: z.infer<typeof forgotFormSchema>,
  ) {
    // Do something with the form values.
    // ✅ This will be type-safe and validated.
    setIsLoading(true);
    await delay(4000);
    console.log(values);

    setSuccess("success");
    setIsLoading(false);
  }

  /**
   * Submits the user registration form
   *
   *
   * @param values form values
   */
  async function onRegisterSubmit(values: z.infer<typeof registerFormSchema>) {
    setIsLoading(true);
    await delay(2000);

    //const user = await registerUser(values)
    const user = null;
    if (!user) {
      toast({
        title: "Account Creation Error",
        description:
          "An error occurred during account creation. Please check your inputs and try again.",
        variant: "danger",
      });
      setSuccess("failed");
    } else {
      console.log(user);
      //if success, perform login(user)
      //once successful login, redirect to dashboard

      //TODO: hook into user context to fetch the user info
      //TODO: redirect to login page with success toast

      toast({
        title: "Account Created",
        description:
          "Your account was successfully created. Welcome to bluebell!",
        variant: "success",
      });
      setSuccess("success");
    }

    setIsLoading(false);
    setIsLoading(false);
  }

  /**
   * Displays the appropriate text based on the form state
   */
  function computeDisplayText() {
    switch (state) {
      case "login":
        return "Sign In";
      case "register":
        return "Create Account";
      default:
        return "Forgot Username/Password";
    }
  }

  /**
   * Computes the width of the form depending on its state
   */
  function computeMaxWidth() {
    switch (state) {
      case "login":
        return " max-w-[800px] ";
      case "register":
        return " max-w-[450px] ";
      default:
        return " max-w-[400px] ";
    }
  }

  /**
   * Cleans the phone number for display purposes
   * @param val
   */
  function sanitizeTelephoneNumber(val: string) {
    const returnVal = val.replace(/[^0-9]/gi, "");
    if (returnVal.length < 11) {
      const phone = parsePhoneNumberFromString(returnVal, {
        // set this to use a default country when the phone number omits country code
        defaultCountry: "US",

        // set to false to require that the whole string is exactly a phone number,
        // otherwise, it will search for a phone number anywhere within the string
        extract: false,
      });

      if (phone) {
        return phone.formatNational();
      }
    }

    return returnVal.substring(0, 10);
  }

  //  RENDER

  return (
    <div className={"h-[100vh] flex items-center justify-center w-full"}>
      <div className={"grid grid-cols-1 justify-items-center w-full"}>
        <div className={"mb-6"}>
          <div className={"flex items-center justify-center"}>
            <MainLogo size={100} />
          </div>
        </div>
        <div
          className={
            "bg-white rounded-2xl shadow-lg p-8 w-4/5 lg:w-3/5 " +
            computeMaxWidth()
          }
        >
          <div className={"grid grid-cols-1 items-center mb-6 text-center"}>
            <div
              className={"font-bold tracking-tighter text-md text-tertiary p-2"}
            >
              {computeDisplayText()}
            </div>
            {state === "login" && success === "failed" ? (
              <SimpleMessage
                text={
                  "An error occurred during login. Please check your inputs and try again."
                }
                variant={"danger"}
                alignment={"left"}
              />
            ) : null}
            {state === "forgot" && success === "success" ? (
              <SimpleMessage
                text={
                  "If an account associated with the given email exists, a password email will have been sent."
                }
                variant={"info"}
              />
            ) : null}
            {state === "register" && success === "failed" ? (
              <SimpleMessage
                text={
                  "Your account could not be created. Please check your inputs and try again."
                }
                variant={"danger"}
              />
            ) : null}
          </div>
          {state === "login" ? (
            <div className={"grid grid-cols-2 items-center"}>
              <div
                className={
                  "max-lg:col-span-2 lg:pr-12 max-lg:pb-12 max-lg:border-b-1 lg:border-r-1 border-gray-100"
                }
              >
                <Form {...loginForm}>
                  <form
                    onSubmit={loginForm.handleSubmit(onLoginSubmit, (e) =>
                      console.log(e),
                    )}
                  >
                    <div className={"grid grid-cols-1 gap-4 items-center"}>
                      <div>
                        <FormField
                          control={loginForm.control}
                          name="username"
                          render={({ field }) => (
                            <FormItem>
                              <FormLabel className="!text-current">
                                Username
                              </FormLabel>
                              <FormControl>
                                <Input
                                  placeholder="john.trader"
                                  {...field}
                                  type={"text"}
                                />
                              </FormControl>
                              <FormMessage
                                className={"text-primaryRed font-semibold"}
                              />
                            </FormItem>
                          )}
                        />
                      </div>
                      <div>
                        <FormField
                          control={loginForm.control}
                          name="password"
                          render={({ field }) => (
                            <FormItem>
                              <FormLabel className="!text-current">
                                Password
                              </FormLabel>
                              <FormControl>
                                <Input
                                  placeholder="Password"
                                  {...field}
                                  type={"password"}
                                />
                              </FormControl>
                              <FormMessage
                                className={"text-primaryRed font-semibold"}
                              />
                            </FormItem>
                          )}
                        />
                      </div>
                      <div className={"flex w-full items-center mt-4"}>
                        <Button
                          type="submit"
                          className={"w-full bg-primary text-white"}
                          disabled={isLoading}
                        >
                          {isLoading ? (
                            <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                          ) : null}
                          Log In
                        </Button>
                      </div>
                    </div>
                  </form>
                </Form>
              </div>
              <div
                className={
                  "max-lg:col-span-2 lg:pl-12 max-lg:pt-12 max-lg:border-t-1 lg:border-l-1 border-gray-100 flex items-center w-full"
                }
              >
                <div className={"w-full"}>
                  <div className={"grid grid-cols-1 gap-4 items-center"}>
                    <div>
                      <Button className={"w-full"} variant={"outline"}>
                        {resolveIcon(
                          Icons.BrandGoogleFilled,
                          "mr-2 text-primary",
                          18,
                        )}
                        Continue with Google
                      </Button>
                    </div>
                    <div>
                      <Button className={"w-full"} variant={"outline"}>
                        {resolveIcon(
                          Icons.BrandAppleFilled,
                          "mr-2 text-primary",
                          18,
                        )}
                        Continue with Apple
                      </Button>
                    </div>
                    <div>
                      <Button className={"w-full"} variant={"outline"}>
                        {resolveIcon(
                          Icons.BrandFacebookFilled,
                          "mr-2 text-primary",
                          18,
                        )}
                        Continue with Facebook
                      </Button>
                    </div>
                  </div>
                </div>
              </div>
              <div className={"col-span-2 mt-8 text-center"}>
                <div className={"grid grid-cols-2 gap-2 items-center"}>
                  <div className={"text-left"}>
                    <Button
                      variant={"ghost"}
                      className={"text-current"}
                      onClick={() => {
                        setState("forgot");
                        setSuccess("undefined");
                      }}
                    >
                      Trouble logging in?
                    </Button>
                  </div>
                  <div className={"text-right"}>
                    <Button
                      variant={"ghost"}
                      className={"text-current"}
                      onClick={() => {
                        setState("register");
                        setSuccess("undefined");
                      }}
                    >
                      Create Account
                    </Button>
                  </div>
                </div>
              </div>
            </div>
          ) : null}
          {state === "forgot" ? (
            <div className={"grid grid-cols-1 items-center mb-6"}>
              <Form {...forgotForm}>
                <form
                  onSubmit={forgotForm.handleSubmit(
                    onForgotPasswordSubmit,
                    (e) => console.log(e),
                  )}
                >
                  <div className={"grid grid-cols-1 gap-4 items-center"}>
                    <div className={"text-sm text-center"}>
                      Enter your account’s email and we’ll send you an email to
                      reset the password.
                    </div>
                    <div>
                      <FormField
                        control={forgotForm.control}
                        name="email"
                        render={({ field }) => (
                          <FormItem>
                            <FormLabel className="!text-current">
                              Email Address
                            </FormLabel>
                            <FormControl>
                              <Input
                                placeholder="john.trader@email.com"
                                {...field}
                                type={"text"}
                              />
                            </FormControl>
                            <FormMessage
                              className={"text-primaryRed font-semibold"}
                            />
                          </FormItem>
                        )}
                      />
                    </div>
                    <div className={"flex w-full items-center mt-2"}>
                      <Button
                        type="submit"
                        className={"w-full bg-primary text-white"}
                        disabled={isLoading}
                      >
                        {isLoading ? (
                          <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                        ) : null}
                        Send Email
                      </Button>
                    </div>
                  </div>
                </form>
              </Form>
            </div>
          ) : null}
          {state === "register" ? (
            <div className={"grid grid-cols-1 items-center mb-6"}>
              <Form {...registerForm}>
                <form
                  onSubmit={registerForm.handleSubmit(onRegisterSubmit, (e) =>
                    console.log(e),
                  )}
                >
                  <div className={"grid grid-cols-1 gap-8 items-center"}>
                    {!showRegisterForm ? (
                      <div className={"text-sm text-center"}>
                        By creating an account, you agree to our Terms of
                        Service and have read and understood the Privacy Policy.
                      </div>
                    ) : null}
                    {!showRegisterForm ? (
                      <div className={"grid grid-cols-1 gap-4 items-center"}>
                        <div>
                          <Button
                            className={"w-full"}
                            variant={"outline"}
                            onClick={() => setShowRegisterForm(true)}
                          >
                            {resolveIcon(
                              Icons.MailFilled,
                              "mr-2 text-primary",
                              18,
                            )}
                            Continue with Email
                          </Button>
                        </div>
                        <div>
                          <Button className={"w-full"} variant={"outline"}>
                            {resolveIcon(
                              Icons.BrandGoogleFilled,
                              "mr-2 text-primary",
                              18,
                            )}
                            Continue with Google
                          </Button>
                        </div>
                        <div>
                          <Button className={"w-full"} variant={"outline"}>
                            {resolveIcon(
                              Icons.BrandAppleFilled,
                              "mr-2 text-primary",
                              18,
                            )}
                            Continue with Apple
                          </Button>
                        </div>
                        <div>
                          <Button className={"w-full"} variant={"outline"}>
                            {resolveIcon(
                              Icons.BrandFacebookFilled,
                              "mr-2 text-primary",
                              18,
                            )}
                            Continue with Facebook
                          </Button>
                        </div>
                      </div>
                    ) : null}
                    {showRegisterForm ? (
                      <div>
                        <div className={"grid grid-cols-1 gap-3"}>
                          <div
                            className={"grid grid-cols-2 gap-3 items-center"}
                          >
                            <div className={""}>
                              <FormField
                                control={registerForm.control}
                                name="firstName"
                                render={({ field }) => (
                                  <FormItem>
                                    <FormLabel className="!text-current">
                                      First Name
                                    </FormLabel>
                                    <FormControl>
                                      <Input
                                        placeholder="John"
                                        {...field}
                                        type={"text"}
                                      />
                                    </FormControl>
                                    <FormMessage
                                      className={
                                        "text-primaryRed font-semibold"
                                      }
                                    />
                                  </FormItem>
                                )}
                              />
                            </div>
                            <div className={""}>
                              <FormField
                                control={registerForm.control}
                                name="lastName"
                                render={({ field }) => (
                                  <FormItem>
                                    <FormLabel className="!text-current">
                                      Last Name
                                    </FormLabel>
                                    <FormControl>
                                      <Input
                                        placeholder="Trader"
                                        {...field}
                                        type={"text"}
                                      />
                                    </FormControl>
                                    <FormMessage
                                      className={
                                        "text-primaryRed font-semibold"
                                      }
                                    />
                                  </FormItem>
                                )}
                              />
                            </div>
                          </div>
                          <div className={""}>
                            <FormField
                              control={registerForm.control}
                              name="username"
                              render={({ field }) => (
                                <FormItem>
                                  <FormLabel className="!text-current">
                                    Username
                                  </FormLabel>
                                  <FormControl>
                                    <Input
                                      placeholder="john.trader"
                                      {...field}
                                      type={"text"}
                                    />
                                  </FormControl>
                                  <FormMessage
                                    className={"text-primaryRed font-semibold"}
                                  />
                                </FormItem>
                              )}
                            />
                          </div>
                          <div className={""}>
                            <FormField
                              control={registerForm.control}
                              name="email"
                              render={({ field }) => (
                                <FormItem>
                                  <FormLabel className="!text-current">
                                    Email
                                  </FormLabel>
                                  <FormControl>
                                    <Input
                                      placeholder="j.trader@example.com"
                                      {...field}
                                      type={"text"}
                                    />
                                  </FormControl>
                                  <FormMessage
                                    className={"text-primaryRed font-semibold"}
                                  />
                                </FormItem>
                              )}
                            />
                          </div>
                          <div className={"grid grid-cols-3 gap-2"}>
                            <div>
                              <FormField
                                control={registerForm.control}
                                name="phoneType"
                                render={({ field }) => (
                                  <FormItem>
                                    <FormLabel className="!text-current">
                                      Phone Type
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
                                        <SelectItem value={"MOBILE"}>
                                          Mobile
                                        </SelectItem>
                                        <SelectItem value={"HOME"}>
                                          Home
                                        </SelectItem>
                                        <SelectItem value={"WORK"}>
                                          Work
                                        </SelectItem>
                                        <SelectItem value={"OTHER"}>
                                          Other
                                        </SelectItem>
                                      </SelectContent>
                                    </Select>
                                    <FormMessage
                                      className={
                                        "text-primaryRed font-semibold"
                                      }
                                    />
                                  </FormItem>
                                )}
                              />
                            </div>
                            <div className={"col-span-2"}>
                              <FormField
                                control={registerForm.control}
                                name="telephoneNumber"
                                render={({ field }) => (
                                  <FormItem>
                                    <FormLabel className="!text-current">
                                      Phone Number
                                    </FormLabel>
                                    <FormControl>
                                      <Input
                                        placeholder="(123) 456-7890"
                                        {...field}
                                        type={"tel"}
                                        max={10}
                                        value={sanitizeTelephoneNumber(
                                          field.value,
                                        )}
                                      />
                                    </FormControl>
                                    <FormMessage
                                      className={
                                        "text-primaryRed font-semibold"
                                      }
                                    />
                                  </FormItem>
                                )}
                              />
                            </div>
                          </div>
                          <div className={""}>
                            <FormField
                              control={registerForm.control}
                              name="password"
                              render={({ field }) => (
                                <FormItem>
                                  <FormLabel className="!text-current">
                                    Password
                                  </FormLabel>
                                  <FormControl>
                                    <Input
                                      placeholder="Password"
                                      {...field}
                                      type={"password"}
                                    />
                                  </FormControl>
                                  <FormDescription>
                                    Password must contain at least 8 characters
                                    and be a mix of alphanumeric characters &
                                    symbols.
                                  </FormDescription>
                                  <FormMessage
                                    className={"text-primaryRed font-semibold"}
                                  />
                                </FormItem>
                              )}
                            />
                          </div>
                          <div className={""}>
                            <FormField
                              control={registerForm.control}
                              name="confirmPassword"
                              render={({ field }) => (
                                <FormItem>
                                  <FormLabel className="!text-current">
                                    Confirm Password
                                  </FormLabel>
                                  <FormControl>
                                    <Input
                                      placeholder="Confirm Password"
                                      {...field}
                                      type={"password"}
                                    />
                                  </FormControl>
                                  <FormMessage
                                    className={"text-primaryRed font-semibold"}
                                  />
                                </FormItem>
                              )}
                            />
                          </div>
                          <div>
                            <div className={"text-sm text-center my-2"}>
                              By creating an account, you agree to our Terms of
                              Service and have read and understood the Privacy
                              Policy.
                            </div>
                          </div>
                          <div className={""}>
                            <div>
                              <Button
                                type="submit"
                                variant={"primary"}
                                className={"w-full"}
                                disabled={isLoading}
                              >
                                {isLoading ? (
                                  <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                                ) : null}
                                Submit
                              </Button>
                            </div>
                            <div className={"mt-2"}>
                              <Button
                                type="button"
                                className={"w-full"}
                                variant={"ghost"}
                                onClick={() => {
                                  setShowRegisterForm(false);
                                  setSuccess("undefined");
                                  registerForm.reset();
                                }}
                              >
                                Cancel
                              </Button>
                            </div>
                          </div>
                        </div>
                      </div>
                    ) : null}
                  </div>
                </form>
              </Form>
            </div>
          ) : null}
          {state !== "login" ? (
            <div className={"grid grid-cols-2 items-center mt-8 text-center"}>
              <div>
                <div className={"text-left"}>
                  {state === "forgot" || state === "register" ? (
                    <Button
                      variant={"ghost"}
                      className={"text-current"}
                      onClick={() => {
                        setState("login");
                        setShowRegisterForm(false);
                        registerForm.reset();
                      }}
                    >
                      {resolveIcon(Icons.ArrowLeft, "", 18)}
                      &nbsp;&nbsp;Login
                    </Button>
                  ) : null}
                </div>
              </div>
              <div />
            </div>
          ) : null}
        </div>
      </div>
      <Toaster />
    </div>
  );
}
