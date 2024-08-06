import type {Metadata} from 'next'
import './assets/css/normalize.css'
import 'react-datepicker/dist/react-datepicker.min.css'
import './bluebell-datepicker.css'
import styles from './layout.module.scss'
import Footer from "@/app/components/Footer/Footer";
import NavBar from "@/app/components/Navigation/Navbar/NavBar";

export const metadata: Metadata = {
  title: 'Flowerpot',
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

  const baseClass = 'flex-page'

  //  RENDER

  return (
    <html lang="en" className={styles['html']}>
    <body className={styles['body']}>
    <NavBar/>
    <div className={styles[baseClass]}>
      <div className={styles[`${baseClass}__content`]}>
        {children}
      </div>
    </div>
    <Footer/>
    </body>
    </html>
  )
}
