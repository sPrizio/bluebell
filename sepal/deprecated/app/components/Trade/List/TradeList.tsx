import styles from './TradeList.module.scss'
import React, {useState} from "react";
import moment from 'moment-timezone';
import TradeListPagination from "@/app/components/Trade/List/Pagination/TradeListPagination";
import SimpleButton from "@/app/components/Button/SimpleButton";
import {MdDelete} from "react-icons/md";
import BaseModal from "@/app/components/Modal/BaseModal";
import {CoreConstants} from "@/app/constants";
import {formatNegativePoints, formatNumberForDisplay} from "@/app/services/data/dataIntegrityService";
import {PagedResponse, Trade} from "@/app/types/apiTypes";
import {TradeType} from "@/app/types/appTypes";
import {IoArrowDownCircle, IoArrowUpCircle} from "react-icons/io5";

/**
 * Lists trades as a table, useful for reporting
 *
 * @param hasAdmin flag to show admin controls for trades
 * @param trades paginated trades
 * @param paginationHandler handler for changing pages
 * @author Stephen Prizio
 * @version 0.0.1
 */
function TradeList({hasAdmin = false, trades, paginationHandler}: Readonly<{
  hasAdmin?: boolean,
  trades: PagedResponse<Trade> | undefined,
  paginationHandler: Function
}>) {

  const baseClass = 'trade-list'
  const [adminDeleteActive, setAdminDeleteActive] = useState(false)


  //  GENERAL FUNCTIONS

  /**
   * Toggles the admin delete modal
   */
  function toggleAdminDeleteModal() {
    setAdminDeleteActive(!adminDeleteActive)
  }

  /**
   * Formats the given number of seconds into a pretty display string
   */
  function formatTimeElapsed(seconds: number) {
    if (seconds < 60) {
      return seconds + 's'
    } else if (seconds < 3600) {
      return Math.floor(seconds / 60) + 'm ' + (seconds % 60) + 's'
    } else {
      return Math.floor(seconds / 3600) + 'h ' + Math.floor((seconds % 3600) / 60) + 'm ' + ((seconds % 3600) % 60) + 's'
    }
  }

  /**
   * Computes the display class for P&L
   */
  function computeClass(val: number) {

    if (val > 0) {
      return styles[`${baseClass}__positive`]
    } else if (val < 0) {
      return styles[`${baseClass}__negative`]
    }

    return styles[`${baseClass}__neutral`]
  }

  /**
   * Computes the time elapsed between a {@link Trade}'s open and close (in seconds)
   */
  function computeTimeElapsed(trade: Trade) {
    const start = moment(trade.tradeOpenTime, CoreConstants.DateTime.ISOEasyDateTimeFormat)
    const end = moment(trade.tradeCloseTime, CoreConstants.DateTime.ISOEasyDateTimeFormat)

    return moment.duration(end.diff(start, 'seconds')).milliseconds()
  }

  /**
   * Formats the date for display
   */
  function getTime(val: string) {
    return moment(val, CoreConstants.DateTime.ISOEasyDateTimeFormat)
      .tz('Europe/Helsinki', true)
      .tz('America/Toronto')
      .format(CoreConstants.DateTime.ISOShortHourFormat);
  }

  /**
   * Handles page change
   */
  function pageHandler(val: number) {
    window.scrollTo({top: val, behavior: "smooth"});
    paginationHandler(val)
  }

  function formatTradeType(val: string) {
    if (val.toUpperCase() === TradeType.BUY.toString()) {
      return <span className={styles[`${baseClass}--buy`]}><IoArrowUpCircle /></span>
    } else {
      return <span className={styles[`${baseClass}--sell`]}><IoArrowDownCircle /></span>
    }
  }


  //  RENDER

  return (
    <>
      {
        (trades?.content?.length ?? 0) > 0 ?
          <div className={styles[baseClass]}>
            <table className={styles[`${baseClass}__table`]}>
              <thead>
              <tr>
                <th>Date</th>
                <th>Trade Id</th>
                <th>Open</th>
                <th>Price</th>
                <th>Type</th>
                <th>Size</th>
                <th>Symbol</th>
                <th>Close</th>
                <th>Price</th>
                <th>P/L ($)</th>
                <th>Points</th>
                <th>Duration</th>
                {hasAdmin ? <th></th> : null}
              </tr>
              </thead>
              <tbody>
              {
                trades?.content.map((item, i) => {
                  return (
                    <tr key={i}>
                      <td>{moment(item.tradeOpenTime, CoreConstants.DateTime.ISOEasyDateTimeFormat).format(CoreConstants.DateTime.ISOShortMonthFullDayFormat)}</td>
                      <td>{item.tradeId}</td>
                      <td>{getTime(item.tradeOpenTime)}</td>
                      <td>{formatNumberForDisplay(item.openPrice)}</td>
                      <td className={styles[`${baseClass}__trade-type`]}>{formatTradeType(item.tradeType)}</td>
                      <td>{formatNumberForDisplay(item.lotSize)}</td>
                      <td>{item.product}</td>
                      <td>{getTime(item.tradeCloseTime)}</td>
                      <td>{formatNumberForDisplay(item.closePrice)}</td>
                      <td className={computeClass(item.netProfit)}>{formatNumberForDisplay(item.netProfit)}</td>
                      <td>{formatNegativePoints(item.points)}</td>
                      <td>{formatTimeElapsed(computeTimeElapsed(item))}</td>
                      {
                        hasAdmin ?
                          <td>
                            <div className={styles[`${baseClass}__table-admin`]}>
                              <SimpleButton icon={<MdDelete/>} iconPosition={"center"} variant={"primary"} plain={true}
                                            handler={toggleAdminDeleteModal}/>
                            </div>
                          </td>
                          : null
                      }
                    </tr>
                  )
                }) ?? []
              }
              </tbody>
            </table>

            <div>
              <TradeListPagination
                page={trades?.pageable?.pageNumber ?? -1}
                pageSize={trades?.pageable?.pageSize ?? 0}
                totalElements={trades?.totalElements ?? 0}
                totalPages={trades?.totalPages ?? 0}
                pageHandler={pageHandler}
              />
            </div>
          </div>
          :
          <>
            <div className={styles[baseClass]}>
              No data available. Try selecting another time period.
            </div>
          </>
      }

      {/*TODO: delete handler on submission*/}
      <BaseModal
        title={'Delete Trade'}
        active={adminDeleteActive}
        content={[
          <div key={0}>
            Are you sure you want to delete the selected trade? This action cannot be undone.
          </div>
        ]}
        closeHandler={() => setAdminDeleteActive(false)}
      />
    </>
  )
}

export default TradeList;