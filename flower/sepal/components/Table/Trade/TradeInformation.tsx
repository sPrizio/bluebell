import { Trade } from "@/types/apiTypes";
import BaseTableContainer from "@/components/Table/BaseTableContainer";
import { Table, TableBody, TableCell, TableRow } from "@/components/ui/table";
import moment from "moment/moment";
import { DateTime } from "@/lib/constants";
import {
  formatNegativePoints,
  formatNumberForDisplay,
} from "@/lib/functions/util-functions";

/**
 * Renders basic trade information
 *
 * @param trade trade info
 * @author Stephen Prizio
 * @version 0.2.4
 */
export default function TradeInformation({
  trade,
}: Readonly<{ trade: Trade | undefined | null }>) {
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

  function getProfitColors() {
    const base = "text-slate-500 font-bold";

    if ((trade?.netProfit ?? 0) < 0) {
      return `${base} text-primaryRed`;
    } else if ((trade?.netProfit ?? 0) > 0) {
      return `${base} text-primaryGreen`;
    } else {
      return base;
    }
  }

  //  RENDER

  return (
    <div className={"py-4"}>
      <BaseTableContainer
        table={
          <Table>
            <TableBody>
              <TableRow className={"hover:bg-transparent"}>
                <TableCell className={getHeaderColumnStyles()}>
                  Trade Id
                </TableCell>
                <TableCell>{trade?.tradeId}</TableCell>
              </TableRow>
              <TableRow className={"hover:bg-transparent"}>
                <TableCell className={getHeaderColumnStyles()}>
                  Product
                </TableCell>
                <TableCell>{trade?.product}</TableCell>
              </TableRow>
              <TableRow className={"hover:bg-transparent"}>
                <TableCell className={getHeaderColumnStyles()}>
                  Platform
                </TableCell>
                <TableCell>{trade?.tradePlatform.label}</TableCell>
              </TableRow>
              <TableRow className={"hover:bg-transparent"}>
                <TableCell className={getHeaderColumnStyles()}>Type</TableCell>
                <TableCell>{trade?.tradeType.label}</TableCell>
              </TableRow>
              <TableRow className={"hover:bg-transparent"}>
                <TableCell className={getHeaderColumnStyles()}>
                  Open Time
                </TableCell>
                <TableCell>
                  {moment(trade?.tradeOpenTime).format(
                    DateTime.ISOLongMonthDayYearWithTimeFormat,
                  )}
                </TableCell>
              </TableRow>
              <TableRow className={"hover:bg-transparent"}>
                <TableCell className={getHeaderColumnStyles()}>
                  Close Time
                </TableCell>
                <TableCell>
                  {trade?.tradeCloseTime ? (
                    moment(trade?.tradeCloseTime).format(
                      DateTime.ISOLongMonthDayYearWithTimeFormat,
                    )
                  ) : (
                    <span className="text-primary font-bold">Live Trade</span>
                  )}
                </TableCell>
              </TableRow>
              <TableRow className={"hover:bg-transparent"}>
                <TableCell className={getHeaderColumnStyles()}>
                  Lot Size
                </TableCell>
                <TableCell>{trade?.lotSize}</TableCell>
              </TableRow>
              <TableRow className={"hover:bg-transparent"}>
                <TableCell className={getHeaderColumnStyles()}>
                  Open Price
                </TableCell>
                <TableCell>
                  {formatNumberForDisplay(trade?.openPrice ?? 0)}
                </TableCell>
              </TableRow>
              <TableRow className={"hover:bg-transparent"}>
                <TableCell className={getHeaderColumnStyles()}>
                  Close Price
                </TableCell>
                <TableCell>
                  {(trade?.closePrice ?? 0) === 0 && "-"}
                  {(trade?.closePrice ?? 0) !== 0 &&
                    formatNumberForDisplay(trade?.closePrice ?? 0)}
                </TableCell>
              </TableRow>
              <TableRow className={"hover:bg-transparent"}>
                <TableCell className={getHeaderColumnStyles()}>
                  Net Profit
                </TableCell>
                <TableCell className={getProfitColors()}>
                  {(trade?.closePrice ?? 0) === 0 && "-"}
                  {(trade?.closePrice ?? 0) !== 0 && (
                    <>$&nbsp;{formatNumberForDisplay(trade?.netProfit ?? 0)}</>
                  )}
                </TableCell>
              </TableRow>
              <TableRow className={"hover:bg-transparent"}>
                <TableCell className={getHeaderColumnStyles()}>
                  Points
                </TableCell>
                <TableCell>
                  {(trade?.closePrice ?? 0) === 0 && "-"}
                  {(trade?.closePrice ?? 0) !== 0 &&
                    formatNegativePoints(trade?.points ?? 0)}
                </TableCell>
              </TableRow>
              <TableRow className={"hover:bg-transparent"}>
                <TableCell className={getHeaderColumnStyles()}>
                  Stop Loss
                </TableCell>
                <TableCell>
                  {(trade?.closePrice ?? 0) === 0 && "-"}
                  {(trade?.closePrice ?? 0) !== 0 &&
                    formatNumberForDisplay(trade?.stopLoss ?? 0)}
                </TableCell>
              </TableRow>
              <TableRow className={"hover:bg-transparent"}>
                <TableCell className={getHeaderColumnStyles(true)}>
                  Take Profit
                </TableCell>
                <TableCell>
                  {(trade?.closePrice ?? 0) === 0 && "-"}
                  {(trade?.closePrice ?? 0) !== 0 &&
                    formatNumberForDisplay(trade?.takeProfit ?? 0)}
                </TableCell>
              </TableRow>
            </TableBody>
          </Table>
        }
      />
    </div>
  );
}
