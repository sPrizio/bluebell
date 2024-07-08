import React from "react";
import styles from "./MainLogo.module.scss";
import Image from "next/image";
import brand from '@/app/assets/images/brand/bluebell/brand_main.png';

/**
 * Renders the main logo of the app
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
function MainLogo() {

  const baseClass = "main-logo"


  //  RENDER

  return (
    <div className={styles[baseClass]}>
      <div className={styles[`${baseClass}__container`]}>
        <Image src={brand} height={35} alt={'Brand Logo'} />
      </div>
    </div>
  )
}

export default MainLogo;