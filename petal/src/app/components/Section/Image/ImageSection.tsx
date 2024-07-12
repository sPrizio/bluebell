import styles from './ImageSection.module.scss'
import {SimpleImage} from "@/app/types/appTypes";
import Image from "next/image";
import React from "react";

export default function ImageSection(
  {
    title = '',
    text = '',
    variant = 'primary',
    alignment = 'left',
    size = 'medium',
    image = null,
    cta = null
  }
    : Readonly<{
    title: string,
    text: any,
    variant?: 'primary' | 'secondary' | 'tertiary' | 'white',
    alignment?: 'left' | 'center' | 'right'
    size?: 'small' | 'medium' | 'large',
    image?: SimpleImage,
    cta?: React.ReactNode
  }>
) {

  const baseClass = "image-section"


  //  GENERAL FUNCTIONS

  /**
   * Computes the css class based on the given props
   *
   * @param variant - determines color & shape. Accepted values are : 'primary', 'secondary', 'tertiary'.
   *                 if the value is not one of the above or is missing, the button will not render
   * @param alignment - text alignment
   * @param size - font size
   */
  function computeClass(variant, alignment, size) {
    const v = variant ? styles[`${baseClass}--${variant}`] : ""
    const a = alignment ? styles[`${baseClass}--${alignment}`] : ""
    const s = size ? styles[`${baseClass}--${size}`] : ""

    return `${styles[baseClass]} ${v} ${a} ${s}`.trim()
  }

  if (!variant || variant.length === 0) {
    return null
  }


  //  RENDER

  return (
    <div className={computeClass(variant, alignment, size)}>
      <div className={styles[`${baseClass}__title`]}>
        {title}
      </div>
      <div className={styles[`${baseClass}__container`]}>
        <div className={styles[`${baseClass}__item`]}>
          <div className={styles[`${baseClass}__text`]}>
            {text}
          </div>
          <div className={styles[`${baseClass}__text`]}>
            {cta}
          </div>
        </div>
        <div className={styles[`${baseClass}__item`] + ' ' + styles[`${baseClass}__image`]}>
          <Image src={image?.src} alt={image?.alt ?? ''} />
        </div>
      </div>
    </div>
  )
}