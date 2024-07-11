import React from "react";
import styles from "./MainLogo.module.scss";
import Image from "next/image";
import brandPrimary from '@/app/assets/images/brand/bluebell/bluebell_primary.png';
import brandSecondary from '@/app/assets/images/brand/bluebell/bluebell_secondary.png';
import brandTertiary from '@/app/assets/images/brand/bluebell/bluebell_tertiary.png';
import brandWhite from '@/app/assets/images/brand/bluebell/bluebell_white.png';

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

  const baseClass: string = "main-logo"


  //  GENERAL FUNCTIONS

  /**
   * Computes the image depending on the variant
   */
  function determineImage() : any {
    switch (variant) {
      case 'primary':
        return brandPrimary;
      case 'secondary':
        return brandSecondary;
      case 'tertiary':
        return brandTertiary;
      default:
        return brandWhite;
    }
  }


  //  RENDER

  return (
    <div className={styles[baseClass]}>
      <div className={styles[`${baseClass}__container`]}>
        <Image src={determineImage()} height={75} alt={'Brand Logo'}/>
      </div>
    </div>
  )
}