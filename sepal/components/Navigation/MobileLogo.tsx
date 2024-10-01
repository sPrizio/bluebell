'use client'
import Image from "next/image";
import brandTertiary from '../../app/assets/brand/bluebell/bluebell_logo_tertiary.png';
import brandWhite from '../../app/assets/brand/bluebell/bluebell_logo_white.png';
import brandPrimary from '../../app/assets/brand/bluebell/bluebell_logo_primary.png';
import brandSecondary from '../../app/assets/brand/bluebell/bluebell_logo_secondary.png';
import {useEffect, useState} from "react";
import Link from "next/link";

/**
 * Renders the main logo of the app
 *
 * @param variant color variant
 * @param hasBackground should have a background
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function MobileLogo(
  {
    variant = 'primary',
    hasBackground = false
  }
    : Readonly<{
    variant?: 'primary' | 'secondary' | 'tertiary' | 'white' | 'transparent',
    hasBackground?: boolean
  }>
) {

  const [windowSize, setWindowSize] = useState([0, 0])

  useEffect(() => {
    function updateSize() {
      setWindowSize([window.innerWidth, window.innerHeight]);
    }

    window.addEventListener('resize', updateSize);
    updateSize();
    return () => window.removeEventListener('resize', updateSize);
  }, [])


  //  FUNCTIONS

  /**
   * Updates the window size
   */
  function updateSize() {
    setWindowSize([window.innerWidth, window.innerHeight]);
  }

  function computeBackground() {
    if (hasBackground) {
      switch (variant) {
        case 'primary':
          return 'bg-primary'
        case 'secondary':
          return 'bg-secondary'
        case 'tertiary':
          return 'bg-tertiary'
        case 'transparent':
          return 'bg-transparent'
        default:
          return 'bg-white'
      }
    }

    return 'bg-transparent';
  }

  /**
   * Computes the image depending on the variant
   */
  function determineImage(): any {
    switch (variant) {
      case 'primary':
        return hasBackground ? brandWhite : brandPrimary;
      case 'secondary':
        return hasBackground ? brandTertiary : brandSecondary;
      case 'tertiary':
        return hasBackground ? brandSecondary : brandTertiary;
      case 'transparent':
        return brandPrimary;
      default:
        return hasBackground ? brandPrimary : brandWhite;
    }
  }


  //  RENDER

  return (
    <Link href={'/home'} className={computeBackground()}>
      <Image src={determineImage()} height={windowSize[0] < 992 ? 50 : 65} alt={'Brand Logo'}/>
    </Link>
  )
}