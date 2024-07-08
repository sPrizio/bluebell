import styles from './StepSectionEntry.module.scss'
import {StepSectionElement} from "@/app/types/appTypes";
import Image from "next/image";

/**
 * Individual step section element
 *
 * @param element element
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function StepSectionEntry(
  {
    element = null
  }
    : Readonly<{
    element: StepSectionElement
  }>
) {

  const baseClass = "step-section-entry"


  //  RENDER FUNCTION

  return (
    <div className={styles[baseClass]}>
      {
        element.image && element.image.length > 0 ?
          <div className={styles[`${baseClass}__element`]}>
            <div className={styles[`${baseClass}__image`]}>
              <Image src={element.image} alt={'hello'} height={144} width={275} />
            </div>
          </div> : null
      }
      <div className={styles[`${baseClass}__element`]}>
        <div className={styles[`${baseClass}__title`]}>
          {element.title}
        </div>
      </div>
      <div className={styles[`${baseClass}__element`]}>
        <div className={styles[`${baseClass}__text`]}>
          {element.text}
        </div>
      </div>
    </div>
  )
}