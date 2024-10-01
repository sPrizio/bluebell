import React from "react";
import styles from './BaseMessage.module.scss'
import {LuInfo} from "react-icons/lu";
import {VscError} from "react-icons/vsc";
import {AiOutlineWarning} from "react-icons/ai";
import {GoCheckCircle} from "react-icons/go";

function BaseMessage(
  {
    title = '',
    text = '',
    type = 'info'
  }: Readonly<{
    title?: string,
    text?: string,
    type?: "info" | "success" | "danger" | "warning"
  }>) {

  const baseClass = "base-message"


  //  RENDER

  return (
    <div className={styles[baseClass] + ' ' + styles[`${baseClass}--${type}`]}>
      <div className={styles[`${baseClass}__icon`] + ' ' + styles[`${baseClass}__item`]}>
        {type === 'info' ? <LuInfo /> : null}
        {type === 'success' ? <GoCheckCircle /> : null}
        {type === 'danger' ? <VscError /> : null}
        {type === 'warning' ? <AiOutlineWarning /> : null}
      </div>
      <div className={styles[`${baseClass}__content`] + ' ' + styles[`${baseClass}__item`]}>
        {title && title.length > 0 ? <div className={styles[`${baseClass}__title`]}>{title}</div> : null }
        {text && text.length > 0 ? <div className={styles[`${baseClass}__text`]}>{text}</div> : null }
      </div>
    </div>
  )
}

export default BaseMessage;