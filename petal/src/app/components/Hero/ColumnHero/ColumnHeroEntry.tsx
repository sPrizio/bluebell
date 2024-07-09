import {ColumnHeroElement} from "@/app/types/appTypes";
import styles from './ColumnHeroEntry.module.scss';

/**
 * Individual column hero entry
 *
 * @param element element to render
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function ColumnHeroEntry(
  {
    element = null
  }
    : Readonly<{
    element: ColumnHeroElement
  }>
) {

  const baseClass : string = "column-hero-entry"


  //  RENDER

  return (
    <div className={styles[baseClass]}>
      <div className={styles[`${baseClass}__element`]}>
        <div className={styles[`${baseClass}__title`]}>
          {element.title}
        </div>
      </div>
      <div className={styles[`${baseClass}__element`] + ' ' + styles[`${baseClass}__text`]}>
        <div className={styles[`${baseClass}__text`]}>
          {element.text}
        </div>
      </div>
    </div>
  )

}