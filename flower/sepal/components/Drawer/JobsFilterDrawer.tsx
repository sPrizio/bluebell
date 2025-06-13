import { UserJobControlSelection } from "@/types/uiTypes";
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
import TradeControlDatePicker from "@/components/DateTime/TradeControlDatePicker";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import React from "react";
import { EnumDisplay } from "@/types/apiTypes";

type Props = {
  userSelection: UserJobControlSelection;
  onChange: (newSelection: UserJobControlSelection) => void;
  onSubmit: () => void;
  onCancel: () => void;
  jobTypes?: Array<EnumDisplay>;
};

/**
 * Renders the job filters popup drawer
 *
 * @param userSelection user's selected filters
 * @param onChange on changed of a filter
 * @param onSubmit on apply of filters
 * @param onCancel on cancel of filters
 * @param jobTypes job type filters
 * @param jobStatuses job status filters
 * @author Stephen Prizio
 * @version 0.2.4
 */
export default function JobsFilterDrawer({
  userSelection,
  onChange,
  onSubmit,
  onCancel,
  jobTypes = [],
}: Readonly<Props>) {
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
                modal={true}
                onChange={(val) =>
                  onChange({ ...userSelection, start: val ?? new Date() })
                }
              />
            </div>
            <div>
              <Label>End</Label>
              <TradeControlDatePicker
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
              <Label>Job Type</Label>
              <Select
                value={userSelection.jobType}
                onValueChange={(val: string) =>
                  onChange({
                    ...userSelection,
                    jobType: val,
                  })
                }
              >
                <SelectTrigger className="bg-white">
                  <SelectValue placeholder="Sort" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value={"ALL"}>All</SelectItem>
                  {jobTypes?.map((jt) => (
                    <SelectItem key={jt.code} value={jt.code}>
                      {jt.label}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>
            <div>
              <Label>Status</Label>
              <Select
                value={userSelection.jobStatus}
                onValueChange={(val: string) =>
                  onChange({
                    ...userSelection,
                    jobStatus: val,
                  })
                }
              >
                <SelectTrigger className="bg-white">
                  <SelectValue placeholder="Sort" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value={"ALL"}>All</SelectItem>
                  <SelectItem value={"NOT_STARTED"}>Not Started</SelectItem>
                  <SelectItem value={"IN_PROGRESS"}>In Progress</SelectItem>
                  <SelectItem value={"COMPLETED"}>Completed</SelectItem>
                  <SelectItem value={"FAILED"}>Failed</SelectItem>
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
