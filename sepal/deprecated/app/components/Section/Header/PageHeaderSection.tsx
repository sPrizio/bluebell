import React, {ReactNode} from "react";
import styles from "./PageHeaderSection.module.scss";

/**
 * Defines the page Header and top section
 *
 * @param icon page icon
 * @param title page title
 * @param subtitle page subtitle
 * @param controls optional buttons to include
 * @author Stephen Prizio
 * @version 0.0.1
 */
function PageHeaderSection({ icon = null, title = '', subtitle = '', controls = [] }: Readonly<{ icon?: any, title: string, subtitle?: string, controls: Array<ReactNode> }>) {

  const baseClass = "page-header-section"


  //  RENDER

  let controlItems = null
  if (controls && controls.length > 0) {
    controlItems =
      controls.map((item, key) => {
        return (
          <div key={key} className={styles[`${baseClass}__control`]}>
            {item}
          </div>
        )
      })
  }

  return (
    <div className={styles[baseClass]}>
      {
        icon ?
          <div className={styles[`${baseClass}__item`] + ' ' + styles[`${baseClass}__icon`]}>
            {icon}
          </div>
          : null
      }
      <div className={styles[`${baseClass}__item`] + ' ' + styles[`${baseClass}__title`]}>
        <div className={styles[`${baseClass}__title-wrapper`]}>
          {title}
          {
            subtitle && subtitle.length > 0 ?
              <div className={styles[`${baseClass}__subtitle`]}>
                {subtitle}
              </div>
              : null
          }
        </div>
      </div>
      <div className={styles[`${baseClass}__item`] + ' ' + styles[`${baseClass}__controls`]}>
        {controlItems}
      </div>
    </div>
  )
}

export default PageHeaderSection;