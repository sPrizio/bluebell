import React from "react";
import styles from './BaseModal.module.scss'
import SimpleButton from "@/app/components/Button/SimpleButton";
import {CgClose} from "react-icons/cg";

/**
 * Base modal to be used as skeleton for custom modals
 *
 * @param active flag to show the modal
 * @param title modal title
 * @param content modal custom content
 * @param hasControls if true, show section for buttons
 * @param submitHandler when buttons are shown, a submit handler is needed
 * @param closeHandler handle closing the modal
 * @param cssClasses custom css classes
 * @param isLoading flag to mark the modal as loading
 * @param icon optional icon
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
function BaseModal(
  {
    active,
    title = '',
    content = [],
    hasControls = true,
    submitHandler,
    closeHandler,
    cssClasses = '',
    isLoading = false,
    icon = null,
  }
    : Readonly<{
    active: boolean,
    title?: string,
    content?: Array<any>,
    hasControls?: boolean,
    submitHandler?: Function,
    closeHandler: Function,
    cssClasses?: string
    isLoading?: boolean,
    icon?: any
  }>) {

  const baseClass = "base-modal"

  /**
   * Wrapper for close handler to prevent typescript errors
   */
  const cHandler = () => {
    closeHandler()
  }


  //  RENDER

  return (
    <div className={styles[baseClass] + ' ' + (active ? styles[`${baseClass}--active`] : '')}>
      <div className={styles[`${baseClass}__content`]}>
        <div className={styles[`${baseClass}__header`] + ' ' + styles[`${baseClass}__content-item`]}>
          {
            icon ?
              <div className={styles[`${baseClass}__header-item`] + ' ' + styles[`${baseClass}__header-icon`]}>
                {icon}
              </div>
              : null
          }
          <div className={styles[`${baseClass}__header-item`]}>
            {title}
          </div>
          <div className={styles[`${baseClass}__header-item`] + ' ' + styles[`${baseClass}__header-close`]}>
            <div className={styles[`${baseClass}__close-wrapper`]} onClick={cHandler}>
              <CgClose />
            </div>
          </div>
        </div>
        <div className={styles[`${baseClass}__body`] + ' ' + styles[`${baseClass}__content-item`]}>
          {
            content.map(item => {
              return (
                <div className={styles[`${baseClass}__body-item`]} key={item}>{item}</div>
              )
            })
          }
        </div>
        <div className={styles[`${baseClass}__footer`] + ' ' + styles[`${baseClass}__content-item`]}>
          {
            hasControls ?
              <div className={styles[`${baseClass}__footer-controls`]}>
                <div className={styles[`${baseClass}__footer-control`]}>
                  <SimpleButton text={'Cancel'} variant={"primary"} handler={closeHandler} plain={true} />
                </div>
                <div className={styles[`${baseClass}__footer-control`]}>
                  <SimpleButton text={'Submit'} variant={"primary"} handler={submitHandler} loading={isLoading} />
                </div>
              </div>
              : null
          }
        </div>
      </div>
    </div>
  )
}

export default BaseModal;