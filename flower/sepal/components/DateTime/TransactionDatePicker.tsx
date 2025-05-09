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
import { IconCalendarMonth } from "@tabler/icons-react";
import { ControllerRenderProps } from "react-hook-form";

/**
 * Renders a datepicker component
 *
 * @param field form field handler
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function TransactionDatePicker({
  field,
}: Readonly<{
  field: ControllerRenderProps<
    {
      date: Date;
      type: string;
      amount: number;
      account: number;
    },
    "date"
  >;
}>) {
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
          <IconCalendarMonth size={18} />
          &nbsp;&nbsp;
          {field.value ? (
            format(field.value, "PPP")
          ) : (
            <span>Transaction Date</span>
          )}
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
