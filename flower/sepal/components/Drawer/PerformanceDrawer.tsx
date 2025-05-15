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
import { Label } from "@/components/ui/label";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { AggregateInterval } from "@/lib/enums";
import { capitalize } from "@/lib/functions/util-functions";
import React from "react";
import { UserTradeRecordControlSelection } from "@/types/uiTypes";
import { TradeRecordControlsYearEntry } from "@/types/apiTypes";

type Props = {
  userSelection: UserTradeRecordControlSelection;
  onChange: (newSelection: UserTradeRecordControlSelection) => void;
  onSubmit: () => void;
  onCancel: () => void;
  tradeRecordControls:
    | { yearEntries: TradeRecordControlsYearEntry[] }
    | undefined;
};

/**
 * Component that allows a user to filter trade record performances by various filters
 *
 * @param userSelection user's selection
 * @param onChange what to do on changing of a value
 * @param onSubmit submit handler
 * @param onCancel cancel handler
 * @param tradeRecordControls control filter
 * @author Stephen Prizio
 * @version 0.2.0
 */
export default function PerformanceDrawer({
  userSelection,
  onChange,
  onSubmit,
  onCancel,
  tradeRecordControls,
}: Readonly<Props>) {
  const matchingYear =
    tradeRecordControls?.yearEntries.find(
      (ye) => ye.year === userSelection.year,
    ) ?? null;

  //  RENDER

  return (
    <Drawer>
      <DrawerTrigger asChild>
        <Button variant="primary">Filters</Button>
      </DrawerTrigger>
      <DrawerContent>
        <div className="mx-auto w-full max-w-md">
          <DrawerHeader>
            <DrawerTitle>Filter Performance</DrawerTitle>
            <DrawerDescription>
              Look at your performance at specific points in time.
            </DrawerDescription>
          </DrawerHeader>
          <div className="grid grid-cols-3 items-center w-full gap-1.5 p-4">
            <div>
              <Label>Interval</Label>
              <Select
                value={userSelection.aggInterval.code}
                onValueChange={(val) =>
                  onChange({
                    ...userSelection,
                    aggInterval: AggregateInterval.get(val),
                  })
                }
              >
                <SelectTrigger className="bg-white">
                  <SelectValue placeholder="Account" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value={AggregateInterval.DAILY.code}>
                    {AggregateInterval.DAILY.label}
                  </SelectItem>
                  <SelectItem value={AggregateInterval.MONTHLY.code}>
                    {AggregateInterval.MONTHLY.label}
                  </SelectItem>
                  <SelectItem value={AggregateInterval.YEARLY.code}>
                    {AggregateInterval.YEARLY.label}
                  </SelectItem>
                </SelectContent>
              </Select>
            </div>
            <div>
              <Label>Month</Label>
              <Select
                value={userSelection.month}
                disabled={
                  userSelection.aggInterval.code !==
                  AggregateInterval.DAILY.code
                }
                onValueChange={(val) =>
                  onChange({
                    ...userSelection,
                    month: val,
                  })
                }
              >
                <SelectTrigger className="bg-white">
                  <SelectValue placeholder="Account" />
                </SelectTrigger>
                <SelectContent>
                  {matchingYear?.monthEntries.map((item, idx) => {
                    return (
                      <SelectItem
                        key={item.uid + "my" + (idx + 1)}
                        value={item.month}
                        disabled={item.value === 0}
                      >
                        {capitalize(item.month)}
                      </SelectItem>
                    );
                  }) ?? <SelectItem value={"NA"}>N/A</SelectItem>}
                </SelectContent>
              </Select>
            </div>
            <div>
              <Label>Year</Label>
              <Select
                value={userSelection.year}
                onValueChange={(val) =>
                  onChange({
                    ...userSelection,
                    year: val,
                  })
                }
              >
                <SelectTrigger className="bg-white">
                  <SelectValue placeholder="Account" />
                </SelectTrigger>
                <SelectContent>
                  {tradeRecordControls?.yearEntries?.map((item, idx) => {
                    return (
                      <SelectItem
                        key={item.uid + "ye" + (idx + 1)}
                        value={item.year}
                        disabled={item.monthEntries.length === 0}
                      >
                        {item.year}
                      </SelectItem>
                    );
                  }) ?? <SelectItem value={"NA"}>N/A</SelectItem>}
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
