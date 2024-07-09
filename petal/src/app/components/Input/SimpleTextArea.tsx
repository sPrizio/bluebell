import styles from './SimpleTextArea.module.scss'
import React, {ChangeEvent, useState} from "react";

/**
 * Custom text area
 *
 * @param isLoading loading flag
 * @param variant color
 * @param placeholder placeholder text
 * @param val state
 * @param cols width
 * @param rows height
 * @param handler state handler
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function SimpleTextArea(
  {
    isLoading = false,
    variant = "primary",
    placeholder = "",
    val = '',
    cols = 25,
    rows = 1,
    handler
  }: Readonly<{
    isLoading?: boolean,
    variant?: "primary" | "secondary" | "tertiary",
    placeholder?: string
    val?: string
    handler?: Function,
    cols? : number,
    rows? : number,
  }>
) {

  const baseClass = "simple-text-area";

  const [input, setInput] = useState('')


  //  GENERAL FUNCTIONS

  /**
   * Captures input changes on non-file types
   *
   * @param e change event
   */
  function handleInputChange(e: ChangeEvent) {
    const target = e.target as HTMLInputElement

    setInput(target.value)
    if (handler) {
      handler(target.value)
    }
  }


  //  RENDER FUNCTION

  return (
    <div className={styles[baseClass] + ' ' + (isLoading ? styles[`${baseClass}--loading`] : '')}>
      <div className={styles[`${baseClass}__input`] + ' ' + styles[`${baseClass}__item`]}>
        <div className={styles[`${baseClass}__write-input-container`]}>
          {
            isLoading ?
              <div className={styles[`${baseClass}__write-input-item`] + ' ' + styles[`${baseClass}__write-input-icon`] + ' ' + styles[`${baseClass}__write-input-icon--left`]}>
                <div className={styles[`${baseClass}--loader`]}>L</div>
              </div>
              : null
          }
          <div className={styles[`${baseClass}__write-input-item`] + ' ' + styles[`${baseClass}__write-input-input`]}>
              <textarea
                placeholder={placeholder}
                className={styles[`${baseClass}__write-input-raw-input`]}
                value={input}
                onChange={handleInputChange}
                disabled={false}
                cols={cols}
                rows={rows}
              />
          </div>
        </div>
      </div>
    </div>
  )
}