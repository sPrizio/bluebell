import styles from "./Avatar.module.scss";
import Image from "next/image";
import avatar from '@/app/assets/images/avatars/male.png';

/**
 * Component to display a user's information
 *
 * @param handler toggle handler
 * @author Stephen Prizio
 * @version 0.0.1
 */
function Avatar({handler}: {handler: Function}) {

  const baseClass = 'avatar'


  //  RENDER

  return (
    <div className={styles[baseClass]} onClick={() => handler()}>
      <div className={styles[`${baseClass}__item`] + ' ' + styles[`${baseClass}__icon`]}>
        <div className={styles[`${baseClass}__icon-wrapper`]}>
          <Image src={avatar} width={50} height={50} alt={'Avatar'} />
        </div>
      </div>
      <div className={styles[`${baseClass}__item`] + ' ' + styles[`${baseClass}__content`]}>
        <div className={styles[`${baseClass}__container`]}>
          <div className={styles[`${baseClass}__name`]}>Stephen</div>
        </div>
      </div>
    </div>
  )
}

export default Avatar;