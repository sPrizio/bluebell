import styles from './SimpleSelect.module.scss'
import React, {useState} from "react";
import {IoChevronDown} from "react-icons/io5";
import {SimpleOption} from "@/app/types/appTypes";

/**
 * Base select component that can be used wherever a select input is required
 *
 * @param val tracked val
 * @param options array of selectable options
 * @param handler handler function
 * @author Stephen Prizio
 * @version 0.0.1
 */
function SimpleSelect({val = '', options = [], handler}: Readonly<{ val: string, options: Array<SimpleOption>, handler: Function }>) {

  const baseClass = "simple-select"

  const [active, setActive] = useState(false)


  //  GENERAL FUNCTIONS

  /**
   * Computes the initial state of the select based on the given properties
   */
  function getInitialSelection() {
    if (options && options.length > 0) {
      const returnVal = options.find(opt => opt.value === val)
      return returnVal ?? options[0]
    }

    return { label: '', value: '' }
  }

  function getDisplayValue() {
    if (options && options.length > 0) {
      const returnVal = options.find(opt => opt.value === val)?.label
      return returnVal ?? options[0].label
    }

    return ''
  }

  /**
   * Toggles the menu active
   */
  function toggleActive() {
    setActive(!active)
  }

  /**
   * Returns true if the given value is equal to the current selection
   *
   * @param selection currently selected menu item
   */
  function isCurrentSelection(selection: SimpleOption) {
    return val === selection.value
  }


  //  RENDER

  return (
    <div className={styles[`${baseClass}__wrapper`]} onClick={toggleActive}>
      <div className={styles[baseClass] + ' ' + (active ? styles[`${baseClass}--active`] : '')}>
        <div className={styles[`${baseClass}__item`] + ' ' + styles[`${baseClass}__content`]}>
          {getDisplayValue()}
        </div>
        <div className={styles[`${baseClass}__item`] + ' ' + styles[`${baseClass}__icon`]}>
          <IoChevronDown/>
        </div>
      </div>
      <div className={styles[`${baseClass}__menu`] + ' ' + (active ? styles[`${baseClass}__menu--active`] : '')}>
        { options?.map((item, key) => {
          return (
            <div
              key={key}
              className={styles[`${baseClass}__menu-item`] + ' ' + (isCurrentSelection(item) ? styles[`${baseClass}__menu-item--active`] : '')}
              onClick={() => handler(item.value)}
            >
              {item.label}
            </div>
          ) ?? null
        }) }
      </div>
    </div>
  )
}

export default SimpleSelect;