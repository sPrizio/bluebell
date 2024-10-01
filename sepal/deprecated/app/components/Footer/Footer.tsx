import React from "react";
import styles from "./Footer.module.scss";

/**
 * The global footer component
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
function Footer() {

  const baseClass = "footer"


  //  RENDER

  return (
    <div className={styles[baseClass]}>
      <div className={styles[`${baseClass}__container`]}>
        <div className={styles[`${baseClass}__content`]}>
          <strong>&nbsp;bluebell&nbsp;</strong>&copy;&nbsp;2024
          <br />
          All Rights Reserved
        </div>
      </div>
    </div>
  )
}

export default Footer;
