"use client";

import * as React from "react";
import { format } from "date-fns";

import { cn } from "@/lib/utils";
import { Button } from "@/components/ui/button";
import { Calendar } from "@/components/ui/calendar";
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "@/components/ui/popover";
import { ControllerRenderProps, FieldValues, Path } from "react-hook-form";
import { resolveIcon } from "@/lib/functions/util-component-functions";
import { Icons } from "@/lib/enums";

interface Props<T extends FieldValues, K extends Path<T>> {
  label: string;
  hasIcon?: boolean;
  field: ControllerRenderProps<T, K>;
}

/**
 * Renders a datepicker component
 *
 * @param label display label
 * @param hasIcon render icon
 * @param field form field handler
 * @author Stephen Prizio
 * @version 0.2.2
 */
export default function ReusableDatePicker<
  T extends FieldValues,
  K extends Path<T>,
>({ label, hasIcon = true, field }: Readonly<Props<T, K>>) {
  //  RENDER

  return (
    <Popover>
      <PopoverTrigger asChild>
        <Button
          variant={"outline"}
          className={cn(
            "w-full justify-start text-left font-normal",
            !field.value && "text-muted-foreground",
          )}
        >
          {hasIcon && resolveIcon(Icons.CalendarMonth, "", 18)}
          &nbsp;&nbsp;
          {field.value ? format(field.value, "PPP") : <span>{label}</span>}
        </Button>
      </PopoverTrigger>
      <PopoverContent className="w-auto p-0">
        <Calendar
          mode="single"
          selected={field.value}
          onSelect={field.onChange}
          disabled={(date) =>
            date > new Date() || date < new Date("1900-01-01")
          }
          initialFocus
        />
      </PopoverContent>
    </Popover>
  );
}
