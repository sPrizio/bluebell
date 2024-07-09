'use client'

import styles from './StepImageSection.module.scss'
import {StepImageSectionElement} from "@/app/types/appTypes";
import Image from "next/image";
import {useState} from "react";

/**
 * Step image section, shows images with cards
 *
 * @param title title of section
 * @param elements array of step image section elements
 * @author Stephen Prizio
 * @version 0.0.1
 */
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

  const baseClass : string = "step-image-section"

  const [currentSelection, setCurrentSelection] = useState<StepImageSectionElement>(elements[0]);


  //  GENERAL FUNCTIONS

  /**
   * Selects a new step image to display
   *
   * @param val index
   */
  function selectStep(val: number) {
    setCurrentSelection(elements[val])
  }

  /**
   * Returns true if the given index matches the index of the elements
   *
   * @param val index
   */
  function isActive(val: number) {
    return elements[val].title === currentSelection.title;
  }


  //  RENDER

  return (
    <div className={styles[baseClass]}>
      <div className={styles[`${baseClass}__container`]}>
        <div className={styles[`${baseClass}__title`]}>
          {title}
        </div>
        <div className={styles[`${baseClass}__items`]}>
          <div className={styles[`${baseClass}__item`] + ' ' + styles[`${baseClass}__image-container`]}>
            <div className={styles[`${baseClass}__image-overlay`]}>
              <div className={styles[`${baseClass}__image-overlay-container`]}>
                <div className={styles[`${baseClass}__image-overlay-title`]}>{currentSelection.title}</div>
                <div className={styles[`${baseClass}__image-overlay-text`]}>{currentSelection.text}</div>
              </div>
            </div>
            <Image src={currentSelection.image.src} alt={currentSelection.image.alt} fill={true} />
          </div>
          <div className={styles[`${baseClass}__item`] + ' ' + styles[`${baseClass}__step-container`]}>
            {
              elements && elements.length > 0 && elements.map((item: any, index: number) => {
                return (
                  <div key={index} className={styles[`${baseClass}__step`] + ' ' + (isActive(index) ? styles[`${baseClass}__step--active`] : '')} onClick={() => selectStep(index)}>
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