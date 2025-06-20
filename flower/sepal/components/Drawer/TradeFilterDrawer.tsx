import {
  Drawer,
  DrawerClose,
  DrawerContent,
  DrawerDescription,
  DrawerFooter,
  DrawerHeader,
  DrawerTitle,
  DrawerTrigger,
} from "@/components/ui/drawer";
import { Button } from "@/components/ui/button";
import React from "react";
import { UserTradeControlSelection } from "@/types/uiTypes";
import { Label } from "@/components/ui/label";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import ControlDatePicker from "@/components/DateTime/ControlDatePicker";

type Props = {
  userSelection: UserTradeControlSelection;
  onChange: (newSelection: UserTradeControlSelection) => void;
  onSubmit: () => void;
  onCancel: () => void;
  symbols?: Array<string>;
};

/**
 * Renders the trade filters popup drawer
 *
 * @param userSelection user's selected filters
 * @param onChange on change of a filter
 * @param onSubmit on apply of filters
 * @param onCancel on cancel of filters
 * @param symbols symbol filters
 * @author Stephen Prizio
 * @version 0.2.5
 */
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
              <ControlDatePicker
                value={userSelection.start}
                modal={true}
                onChange={(val) =>
                  onChange({ ...userSelection, start: val ?? new Date() })
                }
              />
            </div>
            <div>
              <Label>End</Label>
              <ControlDatePicker
                value={userSelection.end}
                modal={true}
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
            <DrawerClose asChild>
              <Button variant={"primary"} onClick={onSubmit}>
                Apply
              </Button>
            </DrawerClose>
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
