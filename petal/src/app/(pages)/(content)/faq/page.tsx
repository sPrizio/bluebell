import styles from './layout.module.scss';

/**
 * The faq page
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function FAQPage() {

  const baseClass : string = "faq-page"


  //  RENDER

  return (
    <div className={styles[baseClass]}>
      The faq page.
    </div>
  )
}