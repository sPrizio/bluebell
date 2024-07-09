import styles from './SimpleHero.module.scss'
import {SimpleImage} from "@/app/types/appTypes";
import Image from "next/image";
import NavBar from "@/app/components/Navigation/Navbar/NavBar";

/**
 * A simple hero is a generic, re-usable hero
 *
 * @param title title
 * @param text message
 * @param variant color
 * @param alignment text alignment
 * @param size hero size
 * @param image image
 * @param hasNavBar allow nav bar
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function SimpleHero(
  {
    title = '',
    text = '',
    variant = 'primary',
    alignment = 'left',
    size = 'medium',
    image = null,
    hasNavBar = false
  }
    : Readonly<{
    title: string,
    text: string,
    variant?: 'primary' | 'secondary' | 'tertiary' | 'image' | 'video',
    alignment?: 'left' | 'center' | 'right'
    size?: 'small' | 'medium' | 'large',
    image?: SimpleImage,
    hasNavBar?: boolean
  }>
) {

  const baseClass : string = "simple-hero";


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
      {
        hasNavBar ?
          <div className={styles[`${baseClass}__item`] + ' ' + styles[`${baseClass}__nav`]}>
            <NavBar variant={"transparent"} size={"medium"} />
          </div> : null
      }
      <div className={styles[`${baseClass}__item`] + ' ' + styles[`${baseClass}__content`]}>
        <div className={styles[`${baseClass}__container`]}>
          <div className={styles[`${baseClass}__title`]}>
            {title}
          </div>
          <div className={styles[`${baseClass}__text`]}>
            {text}
          </div>
        </div>
      </div>
      {
        (variant === 'image') && (image !== undefined) && (image?.src) ?
          <>
            <div className={styles[`${baseClass}__color-overlay`]}/>
            <div className={styles[`${baseClass}__image-overlay`]}>
              <Image src={image?.src ?? ''} alt={image?.alt ?? ''}/>
            </div>
          </> : null
      }
    </div>
  )
}