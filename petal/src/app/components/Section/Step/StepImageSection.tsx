import styles from './StepImageSection.module.scss'
import {StepImageSectionElement} from "@/app/types/appTypes";

export default function StepImageSection(
  {
    title = '',
    elements = []
  }
    : Readonly<{
    title: string,
    elements: Array<StepImageSectionElement>
  }>
) {

  const baseClass = "step-image-section"


  //  RENDER FUNCTION

  return (
    <div className={styles[baseClass]}>
      <div className={styles[`${baseClass}__container`]}>
        <div className={styles[`${baseClass}__title`]}>
          {title}
        </div>
        <div className={styles[`${baseClass}__items`]}>
          <div className={styles[`${baseClass}__item`] + ' ' + styles[`${baseClass}__image-container`]}>
            Image goes here
          </div>
          <div className={styles[`${baseClass}__item`] + ' ' + styles[`${baseClass}__step-container`]}>
            {
              elements && elements.length > 0 && elements.map((item: any, index: number) => {
                return (
                  <div key={index} className={styles[`${baseClass}__step`]}>
                    {item.title}
                  </div>
                )
              })
            }
          </div>
        </div>
      </div>
    </div>
  )
}