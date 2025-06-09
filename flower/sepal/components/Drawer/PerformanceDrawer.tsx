import React, { useEffect, useState } from "react";
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
import { UserTradeRecordControlSelection } from "@/types/uiTypes";
import { TradeRecordControlsYearEntry } from "@/types/apiTypes";

type Props = {
  userSelection: UserTradeRecordControlSelection;
  onSubmit: (newSelection: UserTradeRecordControlSelection) => void;
  onCancel: () => void;
  tradeRecordControls:
    | { yearEntries: TradeRecordControlsYearEntry[] }
    | undefined;
};

/**
 * Component that allows a user to filter trade record performances by various filters
 *
 * @param userSelection user's selection
 * @param onSubmit submit handler
 * @param onCancel cancel handler
 * @param tradeRecordControls control filter
 * @author Stephen Prizio
 * @version 0.2.4
 */
export default function PerformanceDrawer({
  userSelection,
  onSubmit,
  onCancel,
  tradeRecordControls,
}: Readonly<Props>) {
  const [localSelection, setLocalSelection] = useState(userSelection);

  useEffect(() => {
    setLocalSelection(userSelection);
  }, [userSelection]);

  const matchingYear =
    tradeRecordControls?.yearEntries.find(
      (ye) => ye.year === localSelection.year,
    ) ?? null;

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
                value={localSelection.aggInterval.code}
                onValueChange={(val) =>
                  setLocalSelection((prev) => ({
                    ...prev,
                    aggInterval: AggregateInterval.get(val),
                  }))
                }
              >
                <SelectTrigger className="bg-white">
                  <SelectValue placeholder="Interval" />
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
                value={localSelection.month}
                disabled={
                  localSelection.aggInterval.code !==
                  AggregateInterval.DAILY.code
                }
                onValueChange={(val) =>
                  setLocalSelection((prev) => ({
                    ...prev,
                    month: val,
                  }))
                }
              >
                <SelectTrigger className="bg-white">
                  <SelectValue placeholder="Month" />
                </SelectTrigger>
                <SelectContent>
                  {matchingYear?.monthEntries.map((item, idx) => (
                    <SelectItem
                      key={item.uid + "my" + (idx + 1)}
                      value={item.month}
                      disabled={item.value === 0}
                    >
                      {capitalize(item.month)}
                    </SelectItem>
                  )) ?? <SelectItem value={"NA"}>N/A</SelectItem>}
                </SelectContent>
              </Select>
            </div>
            <div>
              <Label>Year</Label>
              <Select
                value={localSelection.year}
                onValueChange={(val) =>
                  setLocalSelection((prev) => ({
                    ...prev,
                    year: val,
                  }))
                }
              >
                <SelectTrigger className="bg-white">
                  <SelectValue placeholder="Year" />
                </SelectTrigger>
                <SelectContent>
                  {tradeRecordControls?.yearEntries?.map((item, idx) => (
                    <SelectItem
                      key={item.uid + "ye" + (idx + 1)}
                      value={item.year}
                      disabled={item.monthEntries.length === 0}
                    >
                      {item.year}
                    </SelectItem>
                  )) ?? <SelectItem value={"NA"}>N/A</SelectItem>}
                </SelectContent>
              </Select>
            </div>
          </div>
          <DrawerFooter>
            <DrawerClose asChild>
              <Button
                variant="primary"
                onClick={() => onSubmit(localSelection)}
              >
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
