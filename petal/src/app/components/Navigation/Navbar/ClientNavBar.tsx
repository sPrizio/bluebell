import styles from './ClientNavBar.module.scss'
import {FaMessage, FaUser} from "react-icons/fa6";

/**
 * The client nav bar that sits above the regular nav bar
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function ClientNavBar() {

  const baseClass = "client-nav-bar"


  //  RENDER FUNCTION

  return (
    <div className={styles[baseClass]}>
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