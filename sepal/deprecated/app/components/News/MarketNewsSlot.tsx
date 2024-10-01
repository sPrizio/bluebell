import {NewsSlot} from "@/app/types/apiTypes";
import React from "react";
import styles from './MarketNewsSlot.module.scss'
import moment from "moment";
import {CoreConstants} from "@/app/constants";
import {getFlagForCode} from "@/app/services/locale/localeService";
import {AiOutlineExclamation} from "react-icons/ai";
import {FaExclamationCircle} from "react-icons/fa";
import {BsShieldExclamation} from "react-icons/bs";

/**
 * Component representing a news slot (a time and entry)
 *
 * @param slot news slot
 * @param isPast if true, this news has already happened
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function MarketNewsSlot({slot, isPast = false, showOnlyImportant = false}: {slot: NewsSlot, isPast?: boolean, showOnlyImportant?: boolean}) {

  const baseClass = "news"


  //  GENERAL FUNCTIONS

  /**
   * Determines what icon to show depending on the given severity level
   *
   * @param val severity level
   */
  function computeSeverity(val: number) {
    switch (val) {
      case 1:
        return <FaExclamationCircle />
        case 2:
          return <BsShieldExclamation />
      case 3:
        return <AiOutlineExclamation />
      default:
        return null
    }
  }


  //  RENDER

  return (
    slot.entries.map((item, key) => {
      return (
        <tr className={styles[`${baseClass}__slot`] + ' ' + (isPast ? styles[`${baseClass}__slot--past`] : '')} key={key}>
          <td>{moment(slot.time, CoreConstants.DateTime.ISOTimeFormat).format(CoreConstants.DateTime.ISOShortTimeFormat)}</td>
          <td>{item.content}</td>
          <td className={styles[`${baseClass}__symbol`] + ' ' + styles[`${baseClass}__flag`]}>
            <div>
              {getFlagForCode(item.country)}
            </div>
          </td>
          <td className={styles[`${baseClass}__value`]}>
            <div className={styles[`${baseClass}__severity`] + ' ' + styles[`${baseClass}__severity--${item.severity.toLowerCase()}`]}>
              {computeSeverity(item.severityLevel)}
            </div>
          </td>
          {
            !showOnlyImportant ?
              <>
                <td className={styles[`${baseClass}__value`]}>
                  {item.forecast}
                </td>
                <td className={styles[`${baseClass}__value`]}>
                  {item.previous}
                </td>
              </>
              : null
          }
        </tr>
      )
    })
  )
}