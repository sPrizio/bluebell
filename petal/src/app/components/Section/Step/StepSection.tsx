import {StepSectionElement} from "@/app/types/appTypes";
import styles from './StepSection.module.scss';
import StepSectionEntry from "@/app/components/Section/Step/StepSectionEntry";
import React from "react";

/**
 * Section for containing steps which are essentially columns
 *
 * @param title section title
 * @param elements array of elements
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function StepSection(
  {
    title = '',
    elements = [],
    cta = null
  }
    : Readonly<{
    title: string,
    elements: Array<StepSectionElement>,
    cta?: React.ReactNode
  }>
) {

  const baseClass : string = "step-section"


  //  RENDER

  return (
    <div className={styles[baseClass]}>
      <div className={styles[`${baseClass}__container`]}>
        <div className={styles[`${baseClass}__title`]}>
          {title}
        </div>
        <div className={styles[`${baseClass}__columns`]}>
          {
            elements && elements.length > 0 && elements.map((element, index) => {
              return (
                <div className={styles[`${baseClass}__column`]} key={index}>
                  <StepSectionEntry element={element}/>
                </div>
              )
            })
          }
        </div>
        <div className={styles[`${baseClass}__cta`]}>
          {cta}
        </div>
      </div>
    </div>
  )
}