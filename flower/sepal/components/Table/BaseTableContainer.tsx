import React from "react";
import { Table, TableBody, TableHeader } from "@/components/ui/table";

/**
 * Base container for tables to give them managed heights and localize pagination
 *
 * @param height table height, if not set table will auto grow
 * @param table table content
 * @param pagination pagination content
 * @author Stephen Prizio
 * @version 1.0.0
 */
export default function BaseTableContainer({
  height,
  headerContent,
  bodyContent,
  footerContent,
  caption,
  pagination,
}: Readonly<{
  height?: number;
  headerContent?: React.ReactNode;
  bodyContent: React.ReactNode;
  footerContent?: React.ReactNode;
  caption?: React.ReactNode;
  pagination?: React.ReactNode;
}>) {
  //  RENDER
  return (
    <div className="flex flex-col gap-2">
      <div className="relative">
        <div
          className="overflow-y-auto"
          style={height ? { maxHeight: `${height}px` } : undefined}
        >
          <Table className="table-fixed w-full">
            {headerContent && (
              <TableHeader className="sticky top-0 bg-white z-10">
                {headerContent}
              </TableHeader>
            )}
            <TableBody>{bodyContent}</TableBody>
          </Table>
        </div>
      </div>

      {footerContent && (
        <div>
          <Table className="table-fixed w-full">
            <TableBody>{footerContent}</TableBody>
          </Table>
        </div>
      )}

      {caption && <div>{caption}</div>}
      {pagination && <div className="mt-2">{pagination}</div>}
    </div>
  );
}
