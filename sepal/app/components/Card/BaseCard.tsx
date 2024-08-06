'use client'

import styles from './BaseCard.module.scss'
import React, {useEffect, useRef, useState} from "react";
import empty from '../../assets/images/status/out-of-stock.png'
import Image from "next/image";

/**
 * Base card component representing a generic card that can be used throughout the app
 *
 * @param title card title
 * @param subtitle card subtitle (if applicable)
 * @param hasBorder show a border underneath the title & subtitle section
 * @param content card content, can accept raw html or Components
 * @param controls a list of buttons to show in the footer of the card
 * @param loading loading flag, if true will show a spinner over the card content
 * @param hasError error flag, if true will show a default image and text
 * @param hasOverflow if true, will make the content of the card scrollable horizontally
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
function BaseCard(
  {
    title = '',
    subtitle = '',
    hasBorder = true,
    content = [],
    controls = [],
    loading = false,
    hasError = false,
    hasOverflow = true,
  }
    : {
    title?: any,
    subtitle?: any,
    hasBorder?: boolean,
    content?: Array<any>,
    controls?: Array<any>,
    loading?: boolean,
    hasError?: boolean,
    hasOverflow?: boolean,
  }
) {

  const baseClass = 'base-card'
  const [isOverflowing, setIsOverflowing] = useState(false)
  const contentDiv = useRef(null)

  useEffect(() => {
    let timeoutId: string | number | NodeJS.Timeout | null | undefined = null;
    const resizeListener = () => {
      // @ts-ignore
      clearTimeout(timeoutId);
      timeoutId = setTimeout(() => {
        setIsOverflowing(isOverflown())
      }, 150)
    }

    window.addEventListener('resize', resizeListener)

    return () => {
      window.removeEventListener('resize', resizeListener)
    }
  }, [])


  //  GENERAL FUNCTIONS

  /**
   * Returns true if the content is overflowing
   */
  function isOverflown() {
    if (hasOverflow) {
      // @ts-ignore
      return (contentDiv?.current?.scrollHeight ?? -1) > (contentDiv?.current?.clientHeight ?? -1) || (contentDiv?.current?.scrollWidth ?? -1) > (contentDiv?.current?.clientWidth ?? -1);
    }

    return false
  }

  /**
   * Returns true if the card doesn't contain data or if the error flag is marked as true
   */
  function isEmptyState() {
    return !content || content.length === 0 || hasError;
  }


  //  RENDER

  let mainContent = null
  if (content && content.length > 0) {
    mainContent =
      content.map((item, key) => {
        return <div className={styles[`${baseClass}__content-item`]} key={key + '_' + item}>{item}</div>
      })
  }

  let mainControls = null
  if (controls && controls.length > 0) {
    mainControls =
      controls.map((item, key) => {
        return (
          <div key={key} className={styles[`${baseClass}__control`]}>
            {item}
          </div>
        )
      })
  }

  let data;
  if (isEmptyState()) {
    data =
      <div className={styles[baseClass] + ' ' + (styles[`${baseClass}--has-error-state`])}>
        <div className={styles[`${baseClass}__error-container`]}>
          <figure className={styles[`${baseClass}__error-image`]}>
            <Image src={empty} alt={'No data available'}/>
          </figure>
          <p>No data available. Please try again later</p>
        </div>
      </div>
  } else {
    data =
      <div className={styles[baseClass] + ' ' + (loading ? styles[`${baseClass}--is-loading`] : '')}>
        <div className={styles[`${baseClass}__loader-overlay`]}/>
        {
          title || subtitle ?
            <div
              className={styles[`${baseClass}__header-container`] + ' ' + (hasBorder ? styles[`${baseClass}__header-container--has-border`] : '')}>
              <div className={styles[`${baseClass}__header-item`] + ' ' + styles[`${baseClass}__header`]}>
                {title}
                {
                  subtitle && subtitle.length > 0 ?
                    <>
                      <br/>
                      <span className={styles[`${baseClass}__sub-header`]}>{subtitle}</span>
                    </>
                    : null
                }
              </div>
              <div className={styles[`${baseClass}__header-item`] + ' ' + styles[`${baseClass}__controls`]}>
                {mainControls}
              </div>
            </div>
            : null
        }
        <div
          className={styles[`${baseClass}__content`] + ' ' + (isOverflowing ? styles[`${baseClass}__content--has-overflow`] : '')}
          ref={contentDiv}>
          {mainContent}
        </div>
      </div>
  }

  return (data)
}

export default BaseCard;