import type {Metadata} from "next";
import './assets/css/normalize.css'
import styles from './layout.module.scss'
import React from "react";

export const metadata: Metadata = {
  title: 'bluebell',
  description: 'Helping you grow your financial freedom!',
}

/**
 * Represents the base layout for each page in the app
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function RootLayout(
  {
    children,
  }: Readonly<{
    children: React.ReactNode
  }>) {

  const baseClass : string = 'flex-page'

  //  RENDER

  return (
    <html lang="en" className={styles['html']}>
    <body className={styles['body']}>
    <div className={styles[baseClass]}>
      <div className={styles[`${baseClass}__content`]}>
        {children}
      </div>
    </div>
    </body>
    </html>
  )
}
