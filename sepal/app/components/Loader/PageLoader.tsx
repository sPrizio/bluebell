import React from "react";
import styles from "./PageLoader.module.scss";

/**
 * A full-size page loader used for navigation
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
function PageLoader() {

  const baseClass = "page-loader"


  //  RENDER

  return (
    <div className={styles[baseClass]}>
      <div className={styles[`${baseClass}__content`]}>
        <div className={styles[`${baseClass}__loader`]} />
      </div>
    </div>
  )
}

export default PageLoader;