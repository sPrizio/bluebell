import styles from './TradeListPagination.module.scss'
import React from "react";

/**
 * Pagination elements for a trade list
 *
 * @param page current page
 * @param pageSize size of page
 * @param totalElements total items
 * @param totalPages total pages of items
 * @param maxVisible total number of max visible pages
 * @param pageHandler page switch handler
 * @author Stephen prizio
 * @version 0.0.1
 */
function TradeListPagination(
  {
    page = -1,
    pageSize = -1,
    totalElements = -1,
    totalPages = -1,
    maxPagesVisible = 3,
    pageHandler
  }: Readonly<{
    page: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    maxPagesVisible?: number,
    pageHandler: Function,
  }>) {

  const baseClass = "trade-list-pagination";


  //  GENERAL FUNCTIONS

  /**
   * Determines the display text depending on the number of items
   */
  function computePageDisplayText() {
    const startBound = (page * pageSize) + 1
    const endBound = (page + 1) * pageSize

    if (endBound > totalElements) {
      return (
        <span>
          Viewing {startBound} - {totalElements} of {totalElements} trades
        </span>
      )
    }

    return (
      <span>
        Viewing {startBound} - {endBound} of {totalElements} trades
      </span>
    )
  }


  //  RENDER

  let leftBound
  let rightBound
  let maxVisible = (maxPagesVisible < totalPages) ? maxPagesVisible : totalPages

  if (page === 0) {
    leftBound = 0
    rightBound = maxVisible
  } else if (page === (totalPages - 1)) {
    leftBound = (totalPages) - maxVisible
    rightBound = (totalPages)
  } else {
    const lPage = page - Math.floor(maxVisible / 2)
    const rPage = page + Math.ceil(maxVisible / 2)
    leftBound =  lPage < 0 ? 0 : lPage
    rightBound = rPage > (totalPages) ? (totalPages) : rPage
  }


  let pages = []
  if (maxVisible < totalPages && leftBound !== 0) {
    pages.push(
      <div className={styles[`${baseClass}__page`] + ' ' + (page === 0 ? styles[`${baseClass}__page--current`] : '')}
           aria-label="Goto page 1"
           onClick={() => pageHandler(0)}
      >
        1
      </div>
    )

    pages.push(<div className={styles[`${baseClass}__page`] + ' ' + styles[`${baseClass}__page--divider`]} aria-label="Page Divider">...</div>)
  }

  for (let i = leftBound; i < rightBound; i++) {
    pages.push(
      <div className={styles[`${baseClass}__page`] + ' ' + (page === i ? styles[`${baseClass}__page--current`] : '')}
           aria-label="Goto page 1"
           onClick={() => pageHandler(i)}
      >
        {i + 1}
      </div>
    )
  }

  if (maxVisible < totalPages && rightBound !== totalPages) {
    pages.push(<div className={styles[`${baseClass}__page`] + ' ' + styles[`${baseClass}__page--divider`]} aria-label="Page Divider">...</div>)

    pages.push(
      <div className={styles[`${baseClass}__page`] + ' ' + (page === (totalPages - 1) ? styles[`${baseClass}__page--current`] : '')}
           aria-label="Goto page 1"
           onClick={() => pageHandler((totalPages - 1))}
      >
        {totalPages}
      </div>
    )
  }

  return (
    <div className={styles[baseClass]}>
      <div className={styles[`${baseClass}__item`] + ' ' + styles[`${baseClass}__label`]}>
        {computePageDisplayText()}
      </div>
      <div className={styles[`${baseClass}__item`] + ' ' + styles[`${baseClass}__pages`]}>
        <div className={styles[`${baseClass}__pages-list`]} aria-label={'navigation'}>
          {
            pages.map((item, key) => {
              return (
                <div className={styles[`${baseClass}__page-wrapper`]} key={key}>
                  {item}
                </div>
              )
            })
          }
        </div>
      </div>
    </div>
  )
}

export default TradeListPagination;