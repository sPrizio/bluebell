import styles from './Footer.module.scss'

/**
 * The footer component TODO: increase this to show more links and socials
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function Footer() {

  const baseClass : string = "footer";


  //  RENDER

  return (
    <div className={styles[baseClass]}>
      <div className={styles[`${baseClass}__container`]}>
        <div className={styles[`${baseClass}__content`]}>
          <div className={styles[`${baseClass}__item`]}>
            bluebell &copy; helps individuals across the country grow and flourish throughout their financial journey
          </div>
          <div className={styles[`${baseClass}__item`]}>
            &copy;&nbsp;2024 bluebell, All rights reserved
          </div>
        </div>
        <div className={styles[`${baseClass}__content`]}>
          <div className={styles[`${baseClass}__item`]}>
            bluebell is a Registered Investment Advisor. Content presented on
            this website is for informational and educational purposes only and does not intend to make an offer or
            solicitation for the sale or purchase of any specific securities product, service, or investment strategy.
            Information on this website is not intended to be comprehensive tax advice or financial planning advice for
            any particular client investment needs. Be sure to consult with a qualified financial or investment adviser
            , tax professional, or attorney before implementing any strategy or recommendation discussed
            herein. Investing involves risk, including the loss of principal.
          </div>
        </div>
      </div>
    </div>
  )
}