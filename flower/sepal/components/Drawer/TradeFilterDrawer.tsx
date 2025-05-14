import {
  Drawer,
  DrawerContent,
  DrawerTrigger,
  DrawerHeader,
  DrawerTitle,
  DrawerDescription,
  DrawerClose,
  DrawerFooter,
} from "@/components/ui/drawer";
import { Button } from "@/components/ui/button";
import React from "react";
import { UserTradeControlSelection } from "@/types/uiTypes";
import { Label } from "@/components/ui/label";
import {
  Select,
  SelectTrigger,
  SelectContent,
  SelectItem,
  SelectValue,
} from "@/components/ui/select";
import TradeControlDatePicker from "@/components/DateTime/TradeControlDatePicker";

type Props = {
  userSelection: UserTradeControlSelection;
  onChange: (newSelection: UserTradeControlSelection) => void;
  onSubmit: () => void;
  onCancel: () => void;
  symbols?: Array<string>;
};

export default function TradeFilterDrawer({
  userSelection,
  onChange,
  onSubmit,
  onCancel,
  symbols = [],
}: Readonly<Props>) {
  //  RENDER
  return (
    <Drawer>
      <DrawerTrigger asChild>
        <Button variant="primary">Filters</Button>
      </DrawerTrigger>
      <DrawerContent>
        <div className="mx-auto w-full max-w-xl">
          <DrawerHeader>
            <DrawerTitle>Filter Trades</DrawerTitle>
            <DrawerDescription>
              Look at your trades at specific points in time.
            </DrawerDescription>
          </DrawerHeader>
          <div className="grid grid-cols-3 items-center w-full gap-4 p-4">
            <div>
              <Label>Start</Label>
              <TradeControlDatePicker
                value={userSelection.start}
                onChange={(val) =>
                  onChange({ ...userSelection, start: val ?? new Date() })
                }
              />
            </div>
            <div>
              <Label>End</Label>
              <TradeControlDatePicker
                value={userSelection.end}
                onChange={(val) =>
                  onChange({ ...userSelection, end: val ?? new Date() })
                }
              />
            </div>
            <div>
              <Label>Sort</Label>
              <Select
                value={userSelection.sort}
                onValueChange={(val: "asc" | "desc") =>
                  onChange({
                    ...userSelection,
                    sort: val,
                  })
                }
              >
                <SelectTrigger className="bg-white">
                  <SelectValue placeholder="Sort" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value={"asc"}>Oldest First</SelectItem>
                  <SelectItem value={"desc"}>Recent First</SelectItem>
                </SelectContent>
              </Select>
            </div>
            <div>
              <Label>Trade Type</Label>
              <Select
                value={userSelection.type}
                onValueChange={(val: string) =>
                  onChange({
                    ...userSelection,
                    type: val,
                  })
                }
              >
                <SelectTrigger className="bg-white">
                  <SelectValue placeholder="Sort" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value={"ALL"}>All</SelectItem>
                  <SelectItem value={"BUY"}>Buy Trades</SelectItem>
                  <SelectItem value={"SELL"}>Sell Trades</SelectItem>
                </SelectContent>
              </Select>
            </div>
            <div>
              <Label>Symbol/Equity</Label>
              <Select
                value={userSelection.symbol}
                onValueChange={(val: string) =>
                  onChange({
                    ...userSelection,
                    symbol: val,
                  })
                }
              >
                <SelectTrigger className="bg-white">
                  <SelectValue placeholder="Sort" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value={"ALL"}>All</SelectItem>
                  {symbols?.map((symbol) => (
                    <SelectItem key={symbol} value={symbol}>
                      {symbol}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>
          </div>
          <DrawerFooter className={""}>
            <Button variant={"primary"} onClick={onSubmit}>
              Apply
            </Button>
            <DrawerClose asChild>
              <Button variant="outline" onClick={onCancel}>
                Cancel
              </Button>
            </DrawerClose>
          </DrawerFooter>
        </div>
      </DrawerContent>
    </Drawer>
  );
}
