import styles from './ClientCardSection.module.scss'
import ClientCard from "@/app/components/Card/ClientCard";
import {ClientCardSectionElement} from "@/app/types/appTypes";

/**
 * Client card section renders multiple client cards
 *
 * @param title title
 * @param subtitle subtitle
 * @param elements list of client cards
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function ClientCardSection(
  {
    title = '',
    subtitle = '',
    elements = []
  }
    : Readonly<{
    title: string,
    subtitle: string,
    elements: Array<ClientCardSectionElement>
  }>
) {

  const baseClass : string = "client-card-section"


  //  RENDER

  return (
    <div className={styles[baseClass]}>
      <div className={styles[`${baseClass}__container`]}>
        <div className={styles[`${baseClass}__title`]}>
          {title}
        </div>
        <div className={styles[`${baseClass}__subtitle`]}>
          {subtitle}
        </div>
        <div className={styles[`${baseClass}__cards`]}>
          {
            elements && elements.length > 0 && elements.map((item: any, index: number) => {
              return (
                <div key={index} className={styles[`${baseClass}__column`]}>
                  <ClientCard element={item}/>
                </div>
              )
            })
          }
        </div>
      </div>
    </div>
  )
}