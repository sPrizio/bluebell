'use client'

import {Select, SelectContent, SelectGroup, SelectItem, SelectTrigger, SelectValue} from "@/components/ui/select";
import React, {useState} from "react";
import {Label} from "../ui/label";

type Option = {
  label: string,
  value: string | number,
  disabled?: boolean,
}

/**
 * A re-usable select component
 *
 * @param title title of the select
 * @param defaultText default display text
 * @param initialValue initially selected value
 * @param options select options
 * @param handler handler function called after a select change
 * @author Stephen Prizio
 * @version 0.2.0
 */
export default function ReusableSelect(
  {
    title,
    defaultText = 'Please select an option',
    options,
    initialValue,
    handler
  }:
  Readonly<{
    title: string,
    defaultText?: string,
    options: Option[],
    initialValue?: string,
    handler: Function
  }>) {

  const [field, setField] = useState(initialValue ?? 'DEFAULT')


  //  RENDER

  return (
    <div>
      <Label>{title}</Label>
      <Select disabled={options.length <= 1} value={field.toString()} onValueChange={val => {
        setField(val)
        handler(val)
      }}>
        <SelectTrigger className="w-[240px] bg-white">
          <SelectValue placeholder={defaultText} />
        </SelectTrigger>
        <SelectContent>
          <SelectGroup>
            {
              !initialValue &&
                <SelectItem disabled={true} value={'DEFAULT'}>Please select an option</SelectItem>
            }
            {
              options?.map((item, idx) => {
                return (
                  <SelectItem key={idx + 1} value={item.value.toString()} disabled={item.disabled}>
                    {item.label}
                  </SelectItem>
                )
              }) ?? null
            }
          </SelectGroup>
        </SelectContent>
      </Select>
    </div>
  )
}