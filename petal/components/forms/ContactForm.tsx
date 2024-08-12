'use client'

import { z } from "zod"
import { zodResolver } from "@hookform/resolvers/zod"
import { useForm } from "react-hook-form"
import {Form, FormControl, FormField, FormItem, FormLabel, FormMessage} from "@/components/ui/form";
import { Input } from "@/components/ui/input"
import {Button} from "@/components/ui/button";
import {zodPhone} from "@/lib/utils";
import {Textarea} from "@/components/ui/textarea";

const formSchema = z.object({
  firstName: z.string().min(2, { message: 'Must contain at least 2 characters' }).max(50, { message: 'Max length exceeded.' }),
  lastName: z.string().min(2, { message: 'Must contain at least 2 characters' }).max(50, { message: 'Max length exceeded.' }),
  email: z.string().min(2, { message: 'Email cannot be empty' }).max(50).email(),
  phone: zodPhone,
  message: z.string().max(240, { message: 'Message size exceeded' }).min(4, { message: 'Message must contain at least 4 characters' })
})

/**
 * Renders the contact form
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function ContactForm() {

  // 1. Define the form.
  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      firstName: '',
      lastName: '',
      email: '',
      phone: '',
      message: '',
    },
  })

  // 2. Define a submit handler.
  function onSubmit(values: z.infer<typeof formSchema>) {
    // Do something with the form values.
    // ✅ This will be type-safe and validated.
    console.log(values)
  }


  //  RENDER

  return (
    <div className="">
      <Form {...form}>
        <form onSubmit={form.handleSubmit(onSubmit)} className={'space-y-8'}>
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-4 lg:gap-8">
            <FormField
              control={form.control}
              name="firstName"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>First Name</FormLabel>
                  <FormControl>
                    <Input placeholder="John" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="lastName"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Last Name</FormLabel>
                  <FormControl>
                    <Input placeholder="Doe" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="email"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Email</FormLabel>
                  <FormControl>
                    <Input placeholder="john.doe@email.com" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="phone"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Phone&nbsp;<small>(Optional)</small></FormLabel>
                  <FormControl>
                    {/*@ts-ignore*/}
                    <Input placeholder="555-555-5555" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
          </div>
          <div className="grid grid-cols-1 gap-8">
            <FormField
              control={form.control}
              name="message"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Message <small>(max 240 characters)</small></FormLabel>
                  <FormControl>
                    <Textarea
                      placeholder="How can we help you?"
                      className="resize-none"
                      {...field}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
          </div>
          <Button type="submit" className="bg-primary text-white">Submit</Button>
        </form>
      </Form>
    </div>
  )
}