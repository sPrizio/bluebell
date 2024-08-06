import React, {ChangeEvent, useState} from "react";
import styles from "./SimpleInput.module.scss";

/**
 * Basic universal input
 *
 * @param isLoading loading flag
 * @param variant color variant, similar to SimpleButton
 * @param placeholder placeholder text
 * @param isRounded if true, round the edges
 * @param hasButton if true, attach a button to the right of the input
 * @param buttonText button text
 * @param buttonIcon icon on the button if necessary
 * @param hasIcon has icons inside the search
 * @param iconPosition icon position : left or right of the input
 * @param icon the actual icon to render
 * @param inputType type of input supported
 * @param val the react value to track
 * @param handler function to handle state change
 * @author Stephen Prizio
 * @version 0.0.1
 */
function SimpleInput(
  {
    isLoading = false,
    variant = "primary",
    placeholder = "",
    isRounded = false,
    hasButton = false,
    buttonText = "",
    buttonIcon = null,
    hasIcon = false,
    iconPosition = "right",
    icon = null,
    inputType = "text",
    val = '',
    handler
  }: Readonly<{
    isLoading?: boolean,
    variant?: "primary" | "secondary" | "tertiary",
    placeholder?: string
    isRounded?: boolean,
    hasButton?: boolean,
    buttonText?: string,
    buttonIcon?: any,
    hasIcon?: boolean,
    iconPosition?: "left" | "right" | "both",
    icon?: any,
    inputType?: "text" | "tel" | "file",
    val?: string
    handler?: Function,
  }>
) {

  const baseClass = "simple-input"

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


  //  RENDER

  let buttonElement = null
  if (hasButton) {
    buttonElement =
      <div
        className={styles[`${baseClass}__item`] + ' ' + styles[`${baseClass}__button`] + ' ' + styles[`${baseClass}__button--${variant}`]}>
        <div className={styles[`${baseClass}__flex-wrapper`]}>
          <div className={styles[`${baseClass}__flex-item`]}>
            <div
              className={styles[`${baseClass}__flex-content`] + ' ' + (buttonIcon !== null ? styles[`${baseClass}__flex-content--align-left`] : '')}>
              {
                buttonIcon !== null && !isLoading ?
                  <div className={styles[`${baseClass}__button-icon`]}>
                    {buttonIcon}
                  </div>
                  : null
              }
              <div className={styles[`${baseClass}__button-text`]}>
                {!isLoading ? buttonText : <div className={styles[`${baseClass}--loader`]}>L</div>}
              </div>
            </div>
          </div>
        </div>
      </div>
  }

  return (
    <div className={styles[baseClass] + ' ' + (isRounded ? styles[`${baseClass}--rounded`] : '') + ' ' + (isLoading ? styles[`${baseClass}--loading`] : '')}>
      <div className={styles[`${baseClass}__input`] + ' ' + styles[`${baseClass}__item`] + ' ' + (!hasButton ? styles[`${baseClass}__input--full-rounded`] : '')}>
        <div className={styles[`${baseClass}__write-input-container`]}>
          {
            hasIcon && iconPosition === 'left' && isLoading ?
              <div className={styles[`${baseClass}__write-input-item`] + ' ' + styles[`${baseClass}__write-input-icon`] + ' ' + styles[`${baseClass}__write-input-icon--left`]}>
                <div className={styles[`${baseClass}--loader`]}>L</div>
              </div>
            : null
          }
          {
            hasIcon && iconPosition === 'left' && !isLoading ?
              <div className={styles[`${baseClass}__write-input-item`] + ' ' + styles[`${baseClass}__write-input-icon`]  + ' ' + styles[`${baseClass}__write-input-icon--left`]}>
                {icon}
              </div>
              : null
          }
          {
            (hasIcon && iconPosition === 'both') ?
              <div className={styles[`${baseClass}__write-input-item`] + ' ' + styles[`${baseClass}__write-input-icon`]  + ' ' + styles[`${baseClass}__write-input-icon--left`]}>
                {icon}
              </div>
              : null
          }
          <div className={styles[`${baseClass}__write-input-item`] + ' ' + styles[`${baseClass}__write-input-input`]}>
            {
              inputType !== "file" ?
                <input
                  type={inputType}
                  placeholder={placeholder}
                  className={styles[`${baseClass}__write-input-raw-input`]}
                  value={input}
                  onChange={handleInputChange}
                  disabled={false}
                />
                : <input
                  type={"text"}
                  placeholder={placeholder}
                  className={styles[`${baseClass}__write-input-raw-input`]}
                  value={val}
                  disabled={true}
                />
            }
          </div>
          {
            (hasIcon && !isLoading && (iconPosition === 'right' || iconPosition === 'both')) ?
              <div className={styles[`${baseClass}__write-input-item`] + ' ' + styles[`${baseClass}__write-input-icon`] + ' ' + styles[`${baseClass}__write-input-icon--right`]}>
                {icon}
              </div>
              : null
          }
          {
            !hasButton && isLoading && iconPosition !== 'left' ?
              <div
                className={styles[`${baseClass}__write-input-item`] + ' ' + styles[`${baseClass}__write-input-icon`] + ' ' + styles[`${baseClass}__write-input-icon--right`]}>
                <div className={styles[`${baseClass}--loader`]}>L</div>
              </div>
              : null
          }
        </div>
      </div>
      {buttonElement}
    </div>
  )
}

export default SimpleInput;