import styles from './layout.module.scss';

/**
 * The services page
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function ServicesPage() {

  const baseClass : string = "services-page"


  //  RENDER

  return (
    <div className={styles[baseClass]}>
      The services page.
    </div>
  )
}