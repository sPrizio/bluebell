import { UserTransactionControlSelection } from "@/types/uiTypes";
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
import ControlDatePicker from "@/components/DateTime/ControlDatePicker";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import React from "react";

type Props = {
  userSelection: UserTransactionControlSelection;
  onChange: (userSelection: UserTransactionControlSelection) => void;
  onSubmit: () => void;
  onCancel: () => void;
};

/**
 * Renders the filter drawer for transactions
 *
 * @param userSelection user selection
 * @param onChange on change of a value handler
 * @param onSubmit on submit handler
 * @param onCancel on cancel handler
 * @author Stephen Prizio
 * @version 0.2.5
 */
export default function TransactionFilterDrawer({
  userSelection,
  onChange,
  onSubmit,
  onCancel,
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
            <DrawerTitle>Filter Transactions</DrawerTitle>
            <DrawerDescription>
              Look at your transactions at specific points in time.
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
              <Label>Transaction Type</Label>
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
                  <SelectItem value={"DEPOSIT"}>Deposit</SelectItem>
                  <SelectItem value={"WITHDRAWAL"}>Withdrawal</SelectItem>
                </SelectContent>
              </Select>
            </div>
            <div>
              <Label>Transaction Status</Label>
              <Select
                value={userSelection.status}
                onValueChange={(val: string) =>
                  onChange({
                    ...userSelection,
                    status: val,
                  })
                }
              >
                <SelectTrigger className="bg-white">
                  <SelectValue placeholder="Sort" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value={"ALL"}>All</SelectItem>
                  <SelectItem value={"FAILED"}>Failed</SelectItem>
                  <SelectItem value={"IN_PROGRESS"}>In Progress</SelectItem>
                  <SelectItem value={"PENDING"}>Pending</SelectItem>
                  <SelectItem value={"COMPLETED"}>Completed</SelectItem>
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
