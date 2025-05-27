import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Action, JobResultEntry } from "@/types/apiTypes";
import React from "react";
import { Loader2 } from "lucide-react";
import { Button } from "@/components/ui/button";
import {
  Sheet,
  SheetContent,
  SheetDescription,
  SheetHeader,
  SheetTitle,
  SheetTrigger,
} from "@/components/ui/sheet";
import { resolveIcon } from "@/lib/functions/util-component-functions";
import { Icons } from "@/lib/enums";

/**
 * Renders the actions table
 *
 * @param entries job results entries
 * @param actions job actions
 * @author Stephen Prizio
 * @version 0.2.1
 */
export default function ActionsTable({
  entries,
  actions,
}: Readonly<{
  entries: Array<JobResultEntry>;
  actions: Array<Action>;
}>) {
  //  GENERAL FUNCTIONS

  /**
   * Returns the job result entry for the action priority
   *
   * @param idx index
   */
  function getJobResult(idx: number) {
    return entries?.[idx] ?? null;
  }

  /**
   * Computes the icon to display based on the status
   *
   * @param val status
   */
  function computeIcon(val: string) {
    const size = 25;
    switch (val) {
      case "SUCCESS":
        return resolveIcon(Icons.CircleCheck, "text-primaryGreen", size);
      case "FAILURE":
        return resolveIcon(Icons.XboxX, "text-primaryRed", size);
      case "IN_PROGRESS":
        return <Loader2 className="animate-spin text-primary" size={size} />;
      default:
        return resolveIcon(Icons.CircleMinus, "text-slate-400", size);
    }
  }

  //  RENDER

  console.log(entries);

  return (
    <div>
      <Table>
        <TableHeader>
          <TableRow className={"hover:bg-transparent"}>
            <TableHead className={"text-left text-primary font-bold"}>
              Name
            </TableHead>
            <TableHead className={"text-center text-primary font-bold"}>
              Priority
            </TableHead>
            <TableHead className={"text-center text-primary font-bold"}>
              Status
            </TableHead>
            <TableHead className={"text-center text-primary font-bold"}>
              Data
            </TableHead>
            <TableHead className={"text-center text-primary font-bold"}>
              Logs
            </TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {actions?.map((action) => {
            return (
              <TableRow key={action.uid} className={"hover:bg-transparent"}>
                <TableCell>{action?.name ?? ""}</TableCell>
                <TableCell className={"text-center"}>
                  {action?.priority ?? -1}
                </TableCell>
                <TableCell className={"text-right"}>
                  <div className={"flex items-center justify-end"}>
                    {action?.status?.label}&nbsp;&nbsp;&nbsp;
                    <span className={"inline-block text-primary"}>
                      {computeIcon(action?.status?.code ?? "")}
                    </span>
                  </div>
                </TableCell>
                <TableCell className={"text-center"}>
                  <div className={"flex items-center justify-center"}>
                    <Sheet>
                      <SheetTrigger>
                        <Button variant={"outline"}>
                          {resolveIcon(Icons.Database, "text-slate-500", 25)}
                        </Button>
                      </SheetTrigger>
                      <SheetContent
                        className={
                          "sm:w-1/2 md:w-1/2 lg:w-[600px] xl:w-[800px]"
                        }
                      >
                        <SheetHeader>
                          <SheetTitle>Action Data</SheetTitle>
                          <SheetDescription>
                            {/*<pre className={"w-full whitespace-pre-wrap"}>*/}
                            <pre className={"w-full overflow-x-auto"}>
                              {getJobResult(action.priority - 1)?.data ??
                                "No data to display."}
                            </pre>
                          </SheetDescription>
                        </SheetHeader>
                      </SheetContent>
                    </Sheet>
                  </div>
                </TableCell>
                <TableCell className={"text-center"}>
                  <div className={"flex items-center justify-center"}>
                    <Sheet>
                      <SheetTrigger>
                        <Button variant={"outline"}>
                          {resolveIcon(Icons.Logs, "text-slate-500", 25)}
                        </Button>
                      </SheetTrigger>
                      <SheetContent
                        className={
                          "sm:w-1/2 md:w-1/2 lg:w-[600px] xl:w-[800px]"
                        }
                      >
                        <SheetHeader>
                          <SheetTitle>Action Logs</SheetTitle>
                          <SheetDescription>
                            {/*<pre className={"w-full whitespace-pre-wrap"}>*/}
                            <pre className={"w-full overflow-x-auto"}>
                              {getJobResult(action.priority - 1)?.logs ??
                                "No logs to display."}
                            </pre>
                          </SheetDescription>
                        </SheetHeader>
                      </SheetContent>
                    </Sheet>
                  </div>
                </TableCell>
              </TableRow>
            );
          }) ?? null}
        </TableBody>
      </Table>
    </div>
  );
}
