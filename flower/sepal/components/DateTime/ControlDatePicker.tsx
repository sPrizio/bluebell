"use client";

import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "@/components/ui/popover";
import { Button } from "@/components/ui/button";
import { cn } from "@/lib/utils";
import { format } from "date-fns";
import { Calendar } from "@/components/ui/calendar";
import * as React from "react";

/**
 * Datepicker for filters
 *
 * @param value value
 * @param onChange onChange handler
 * @param modal is component in a modal
 * @author Stephen Prizio
 * @version 0.2.5
 */
export default function ControlDatePicker({
  value,
  onChange,
  modal = false,
}: Readonly<{
  value: Date | undefined;
  onChange: (date: Date | undefined) => void;
  modal?: boolean;
}>) {
  //  RENDER
  return (
    <Popover modal={modal}>
      <PopoverTrigger asChild>
        <Button
          variant={"outline"}
          className={cn(
            "w-full justify-start text-left font-normal",
            !value && "text-muted-foreground",
          )}
        >
          {value ? format(value, "PPP") : <span>Pick a date</span>}
        </Button>
      </PopoverTrigger>
      <PopoverContent className="z-[9999] w-auto p-0">
        <Calendar
          mode="single"
          selected={value}
          onSelect={onChange}
          disabled={(date) =>
            date > new Date() || date < new Date("1900-01-01")
          }
        />
      </PopoverContent>
    </Popover>
  );
}
