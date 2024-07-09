'use client';

import styles from './ContactSection.module.scss'
import SimpleButton from "@/app/components/Button/SimpleButton";
import SimpleInput from "@/app/components/Input/SimpleInput";
import {useState} from "react";
import SimpleTextArea from "@/app/components/Input/SimpleTextArea";

/**
 * Contact us section component
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function ContactSection() {

  const baseClass : string = "contact-section"

  const [firstName, setFirstName] = useState<string>('')
  const [lastName, setlastName] = useState<string>('')
  const [email, setEmail] = useState<string>('')
  const [phone, setPhone] = useState<string>('')
  const [message, setMessage] = useState<string>('')


  //  RENDER

  return (
    <div className={styles[baseClass]}>
      <div className={styles[`${baseClass}__container`]}>
        <div className={styles[`${baseClass}__column`]}>
          <div className={styles[`${baseClass}__title`]}>
            Let&apos;s Chat!
          </div>
          <div className={styles[`${baseClass}__text`]}>
            In 15 minutes we can get to know you – your situation, goals and needs – then connect you with an advisor
            committed to helping you pursue true wealth.
          </div>
        </div>
        <div className={styles[`${baseClass}__column`]}>
          <div className={styles[`${baseClass}__form-group`]}>
            <div className={styles[`${baseClass}__input`]}>
              <SimpleInput hasButton={false} isRounded={false} placeholder={'First name'}
                           isLoading={false} inputType={"text"}
                           val={firstName}
              />
            </div>
            <div className={styles[`${baseClass}__input`]}>
              <SimpleInput hasButton={false} isRounded={false} placeholder={'Last name'}
                           isLoading={false} inputType={"text"}
                           val={lastName}
              />
            </div>
          </div>
          <div className={styles[`${baseClass}__form-group`]}>
            <div className={styles[`${baseClass}__input`]}>
              <SimpleInput hasButton={false} isRounded={false} placeholder={'Email'}
                           isLoading={false} inputType={"email"}
                           val={email}
              />
            </div>
            <div className={styles[`${baseClass}__input`]}>
              <SimpleInput hasButton={false} isRounded={false} placeholder={'Phone'}
                           isLoading={false} inputType={"tel"}
                           val={phone}
              />
            </div>
          </div>
          <div className={styles[`${baseClass}__form-group`]}>
            <div className={styles[`${baseClass}__input`] + ' ' + styles[`${baseClass}__message`]}>
              <SimpleTextArea
                placeholder={'Message'}
                val={message}
              />
            </div>
          </div>
          <div className={styles[`${baseClass}__form-group`]}>
            <div className={styles[`${baseClass}__input`]}>
              <SimpleButton text={'Submit'} variant={"tertiary"} />
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}