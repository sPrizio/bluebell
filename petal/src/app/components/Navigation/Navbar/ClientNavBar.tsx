import styles from './ClientNavBar.module.scss'
import {FaMessage, FaUser} from "react-icons/fa6";

/**
 * The client nav bar that sits above the regular nav bar
 *
 * @param variant color variant
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function ClientNavBar(
  {
    variant = 'primary',
  }
    : Readonly<{
    variant?: 'primary' | 'secondary' | 'tertiary' | 'white' | 'black',
  }>
) {

  const baseClass : string = "client-nav-bar"


  //  GENERAL FUNCTIONS

  /**
   * Computes the css class based on the given props
   *
   * @param variant - determines color & shape. Accepted values are : 'primary', 'secondary', 'tertiary'.
   *                 if the value is not one of the above or is missing, the button will not render
   */
  function computeClass(variant) {
    const v = variant ? styles[`${baseClass}--${variant}`] : ""

    return `${styles[baseClass]} ${v}`.trim()
  }

  if (!variant || variant.length === 0) {
    return null
  }


  //  RENDER

  return (
    <div className={computeClass(variant)}>
      <div className={styles[`${baseClass}__container`]}>
        <div className={styles[`${baseClass}__item-container`]}>
          <div className={styles[`${baseClass}__item`]}>
            <div className={styles[`${baseClass}__item--icon`]}>
              <FaMessage/>
            </div>
            <div className={styles[`${baseClass}__item--text`]}>
              welcome@bluebell.com
            </div>
          </div>
          <div className={styles[`${baseClass}__item`]}>
            <div className={styles[`${baseClass}__item--icon`]}>
              <FaUser/>
            </div>
            <div className={styles[`${baseClass}__item--text`]}>
              Client Login
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}