import styles from './ClientCard.module.scss'
import {ClientCardSectionElement} from "@/app/types/appTypes";
import Image from "next/image";

/**
 * A client card component is an image with scrolling text card
 *
 * @param element client card
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function ClientCard(
  {
    element = null
  }
    : Readonly<{
    element: ClientCardSectionElement
  }>
) {

  const baseClass : string = "client-card"


  //  RENDER

  return (
    <div className={styles[baseClass]}>
      <div className={styles[`${baseClass}__image`]}>
        <Image src={element.image.src} alt={element.image.alt} />
      </div>
      <div className={styles[`${baseClass}__container`]}>
        <div className={styles[`${baseClass}__text-container`]}>
          <div className={styles[`${baseClass}__title`]}>
            {element.title}
          </div>
          <div className={styles[`${baseClass}__text`]}>
            {element.text}
          </div>
        </div>
      </div>
    </div>
  )
}