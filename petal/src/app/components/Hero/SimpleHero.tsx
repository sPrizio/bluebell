import styles from './SimpleHero.module.scss'

export default function SimpleHero(
  {
    title = '',
    text = '',
    variant = 'primary',
    alignment = 'left',
    size = 'medium'
  }
    : Readonly<{
    title: string,
    text: string,
    variant?: 'primary' | 'secondary' | 'tertiary',
    alignment?: 'left' | 'center' | 'right'
    size?: 'small' | 'medium' | 'large',
  }>
) {

  const baseClass = "simple-hero";


  //  FUNCTIONS

  /**
   * Computes the css class based on the given props
   *
   * @param variant - determines color & shape. Accepted values are : 'primary', 'secondary', 'tertiary'.
   *                 if the value is not one of the above or is missing, the button will not render
   * @param alignment - text alignment
   * @param size - font size
   */
  function computeClass(
    variant: string,
    alignment: "left" | "center" | "right" | undefined,
    size: 'small' | 'medium' | 'large' | undefined
  ) {
    const v = variant ? styles[`${baseClass}--${variant}`] : ""
    const a = alignment ? styles[`${baseClass}--${alignment}`] : ""
    const s = size ? styles[`${baseClass}--${size}`] : ""

    return `${styles[baseClass]} ${v} ${a} ${s}`.trim()
  }

  if (!variant || variant.length === 0) {
    return null
  }


  //  RENDER FUNCTION

  return (
    <div className={computeClass(variant, alignment, size)}>
      <div className={styles[`${baseClass}__container`]}>
        <div className={styles[`${baseClass}__title`]}>
          {title}
        </div>
        <div className={styles[`${baseClass}__text`]}>
          {text}
        </div>
      </div>
    </div>
  )
}