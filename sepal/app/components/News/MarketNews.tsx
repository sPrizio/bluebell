import React from "react";
import styles from './MarketNews.module.scss'
import {News} from "@/app/types/apiTypes";
import moment from "moment";
import {CoreConstants} from "@/app/constants";
import MarketNewsSlot from "@/app/components/News/MarketNewsSlot";

/**
 * Renders market news as a card component
 *
 * @param news market news
 * @param showOnlyImportant only show the important columns
 * @param hideDate hides the date row headers
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function MarketNews({news, showOnlyImportant = false, hideDate = false}: Readonly<{ news: Array<News>, showOnlyImportant?: boolean, hideDate?: boolean }>) {

  const baseClass = "news"


  //  RENDER

  return (
    <div className={styles[baseClass]}>
      <table className={styles[`${baseClass}__table`]}>
        <thead>
        <tr>
          <th className={styles[`${baseClass}__label`]}>Time</th>
          <th className={styles[`${baseClass}__label`]}>Event</th>
          <th className={styles[`${baseClass}__symbol`]}>Country</th>
          <th className={styles[`${baseClass}__value`]}>Severity</th>
          {
            !showOnlyImportant ?
              <>
                <th className={styles[`${baseClass}__value`]}>Forecast</th>
                <th className={styles[`${baseClass}__value`]}>Previous</th>
              </>
              : null
          }
        </tr>
        </thead>
        <tbody>
        {
          news && news.length > 0 && news.map((item, key) => {
            return (
              <>
                {
                  !hideDate ?
                    <tr key={key}>
                      <td className={styles[`${baseClass}__date-row`]} colSpan={6}>
                        {moment(item.date, CoreConstants.DateTime.ISODateFormat).format(CoreConstants.DateTime.ISOShortMonthWeekDayFormat)}
                      </td>
                    </tr>
                    : null
                }
                {
                  item.slots && item.slots.length > 0 && item.slots.map((slotItem, slotKey) => {
                    return (
                      <MarketNewsSlot
                        slot={slotItem}
                        key={slotKey}
                        isPast={item.past}
                        showOnlyImportant={showOnlyImportant}
                      />
                    )
                  })
                }
              </>
            )
          })
        }
        </tbody>
      </table>
    </div>
  )
}