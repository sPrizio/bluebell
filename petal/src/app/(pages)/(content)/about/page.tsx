import styles from './layout.module.scss';

/**
 * The about page
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function AboutPage() {

  const baseClass : string = "about-page"


  //  RENDER

  return (
    <div className={styles[baseClass]}>
      The about page.
    </div>
  )
}