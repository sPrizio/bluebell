import { Job } from "@/types/apiTypes";
import { Table, TableBody, TableCell, TableRow } from "@/components/ui/table";
import moment from "moment/moment";
import { DateTime } from "@/lib/constants";
import Badge from "@/components/Badge/Badge";
import BaseTableContainer from "@/components/Table/BaseTableContainer";

/**
 * Renders a job information component
 *
 * @author Stephen Prizio
 * @version 0.2.4
 */
export default function JobInformation({
  job,
}: Readonly<{ job: Job | undefined }>) {
  //  GENERAL FUNCTIONS

  /**
   * Renders the base styles for the header column cells
   *
   * @param last if last, remove the border
   */
  function getHeaderColumnStyles(last = false) {
    const base = "text-right bg-primary bg-opacity-5 py-3";
    if (last) {
      return base;
    }

    return `${base} border-b border-gray-200`;
  }

  //  RENDER

  return (
    <div className={"py-4"}>
      <BaseTableContainer
        table={
          <Table>
            <TableBody>
              <TableRow className={"hover:bg-transparent"}>
                <TableCell className={getHeaderColumnStyles()}>Name</TableCell>
                <TableCell>{job?.name}</TableCell>
              </TableRow>
              <TableRow className={"hover:bg-transparent"}>
                <TableCell className={getHeaderColumnStyles()}>Type</TableCell>
                <TableCell>{job?.type?.label ?? ""}</TableCell>
              </TableRow>
              <TableRow className={"hover:bg-transparent"}>
                <TableCell className={getHeaderColumnStyles()}>
                  Execution Time
                </TableCell>
                <TableCell>
                  {moment(job?.executionTime).format(
                    DateTime.ISOShortMonthShortDayYearWithTimeFormat,
                  )}
                </TableCell>
              </TableRow>
              <TableRow className={"hover:bg-transparent"}>
                <TableCell className={getHeaderColumnStyles()}>
                  Completion Time
                </TableCell>
                <TableCell>
                  {job?.executionTime ? null : "Ongoing"}
                  {job?.completionTime &&
                    moment(job?.completionTime).format(
                      DateTime.ISOShortMonthShortDayYearWithTimeFormat,
                    )}
                </TableCell>
              </TableRow>
              <TableRow className={"hover:bg-transparent"}>
                <TableCell className={getHeaderColumnStyles(true)}>
                  Status
                </TableCell>
                <TableCell>
                  {job?.status?.code === "COMPLETED" && (
                    <Badge
                      text={job?.status?.label ?? ""}
                      variant={"success"}
                    />
                  )}
                  {job?.status?.code === "IN_PROGRESS" && (
                    <Badge
                      text={job?.status?.label ?? ""}
                      variant={"warning"}
                    />
                  )}
                  {job?.status?.code === "FAILED" && (
                    <Badge text={job?.status?.label ?? ""} variant={"danger"} />
                  )}
                </TableCell>
              </TableRow>
            </TableBody>
          </Table>
        }
      />
    </div>
  );
}
