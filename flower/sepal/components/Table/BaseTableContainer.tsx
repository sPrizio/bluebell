import React from "react";

/**
 * Base container for tables to give them managed heights and localize pagination
 *
 * @param height table height, if not set table will auto grow
 * @param table table content
 * @param pagination pagination content
 * @author Stephen Prizio
 * @version 0.2.4
 */
export default function BaseTableContainer({
  height,
  table,
  pagination,
}: Readonly<{
  height?: number;
  table: React.ReactNode;
  pagination?: React.ReactNode;
}>) {
  //  RENDER
  return (
    <div>
      <div
        style={{
          minHeight: height ? `${height}px` : "100%",
          maxHeight: height ? `${height}px` : "100%",
        }}
        className={height ? "overflow-y-auto " : " h-full "}
      >
        {table}
      </div>
      {pagination && <div className={"mt-4"}>{pagination}</div>}
    </div>
  );
}
