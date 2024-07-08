import styles from './ClientCard.module.scss'
import {ClientCardSectionElement} from "@/app/types/appTypes";
import Image from "next/image";

export default function ClientCard(
  {
    element = null
  }
    : Readonly<{
    element: ClientCardSectionElement
  }>
) {

  const baseClass = "client-card"


  //  RENDER FUNCTION

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