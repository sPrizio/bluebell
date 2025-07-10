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
  modal?: boolean;
  disabled?: boolean;
}

/**
 * Renders a datepicker component
 *
 * @param label display label
 * @param hasIcon render icon
 * @param field form field handler
 * @param modal is component in a modal
 * @param disabled is disabled
 * @author Stephen Prizio
 * @version 1.0.0
 */
export default function ReusableDatePicker<
  T extends FieldValues,
  K extends Path<T>,
>({
  label,
  hasIcon = true,
  field,
  modal = true,
  disabled = false,
}: Readonly<Props<T, K>>) {
  //  RENDER

  return (
    <Popover modal={modal}>
      <PopoverTrigger asChild>
        <Button
          variant={"outline"}
          className={cn(
            "w-full justify-start text-left font-normal px-[12px]",
            !field.value && "text-muted-foreground",
          )}
          disabled={disabled}
        >
          {hasIcon && (
            <>{resolveIcon(Icons.CalendarMonth, "", 18)}&nbsp;&nbsp;</>
          )}
          {field.value ? (
            <span>{format(field.value, "PPP")}</span>
          ) : (
            <span>{label}</span>
          )}
        </Button>
      </PopoverTrigger>
      <PopoverContent className="z-[9999] w-auto p-0">
        <Calendar
          mode="single"
          selected={field.value}
          onSelect={field.onChange}
          disabled={(date) =>
            date > new Date() || date < new Date("1900-01-01") || disabled
          }
        />
      </PopoverContent>
    </Popover>
  );
}
