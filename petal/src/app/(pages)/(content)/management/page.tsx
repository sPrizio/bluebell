import styles from './layout.module.scss';

/**
 * The management page
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function ManagementPage() {

  const baseClass : string = "management-page"


  //  RENDER

  return (
    <div className={styles[baseClass]}>
      The management page.
    </div>
  )
}