import React from "react";
import styles from "./MainLogo.module.scss";
import Image from "next/image";
import brand from '@/app/assets/images/brand/bluebell/brand_main.png';
import brandWhite from '@/app/assets/images/brand/bluebell/brand_main_white-removebg-preview.png';

/**
 * Renders the main logo of the app
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function MainLogo(
  {
    variant = 'primary',
  }
    : Readonly<{
    variant?: 'primary' | 'secondary' | 'tertiary' | 'transparent' | 'white',
  }>
) {

  const baseClass : string = "main-logo"

  //  TODO: logos in various colors

  //  RENDER

  return (
    <div className={styles[baseClass]}>
      <div className={styles[`${baseClass}__container`]}>
        <Image src={variant === 'transparent' ? brandWhite : brand} height={50} alt={'Brand Logo'} />
      </div>
    </div>
  )
}