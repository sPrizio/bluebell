import styles from './ColumnHero.module.scss'
import ColumnHeroEntry from "@/app/components/Hero/ColumnHero/ColumnHeroEntry";
import {ColumnHeroElement} from "@/app/types/appTypes";

/**
 * A special hero that can support variable columns
 *
 * @param title hero title
 * @param elements array of elements to render as columns
 * @param disclaimer optional disclaimer text
 * @param variant colored variant
 * @param size size of hero
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function ColumnHero(
  {
    title = '',
    elements = [],
    disclaimer = '',
    variant = 'primary',
    size = 'medium'
  }
    : Readonly<{
    title: string,
    elements: Array<ColumnHeroElement>,
    disclaimer?: string,
    variant?: 'primary' | 'secondary' | 'tertiary',
    size?: 'small' | 'medium' | 'large',
  }>
) {

  const baseClass : string = "column-hero"


  //  FUNCTIONS

  /**
   * Computes the css class based on the given props
   *
   * @param variant - determines color & shape. Accepted values are : 'primary', 'secondary', 'tertiary'.
   *                 if the value is not one of the above or is missing, the button will not render
   * @param size - font size
   */
  function computeClass(
    variant: 'primary' | 'secondary' | 'tertiary' | undefined,
    size: 'small' | 'medium' | 'large' | undefined
  ) {
    const v = variant ? styles[`${baseClass}--${variant}`] : ""
    const s = size ? styles[`${baseClass}--${size}`] : ""

    return `${styles[baseClass]} ${v} ${s}`.trim()
  }


  //  RENDER

  return (
    <div className={computeClass(variant, size)}>
      <div className={styles[`${baseClass}__container`]}>
        <div className={styles[`${baseClass}__title`]}>
          {title}
        </div>
        <div className={styles[`${baseClass}__columns`]}>
          {
            elements && elements.length > 0 && elements.map((element, index) => {
              return (
                <div className={styles[`${baseClass}__column`]} key={index}>
                  <ColumnHeroEntry element={element}/>
                </div>
              )
            })
          }
        </div>
        {
          disclaimer && disclaimer.length > 0 ?
            <div className={styles[`${baseClass}__disclaimer`]}>
              {disclaimer}
            </div> : null
        }
      </div>
    </div>
  )
}