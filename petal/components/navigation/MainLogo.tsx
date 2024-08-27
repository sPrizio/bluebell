'use client'
import Image from "next/image";
import brandTertiary from '@/assets/brand/bluebell/bluebell_tertiary.png';
import brandWhite from '@/assets/brand/bluebell/bluebell_white.png';
import brandPrimary from '@/assets/brand/bluebell/bluebell_primary.png';
import {useEffect, useState} from "react";
import Link from "next/link";

/**
 * Renders the main logo of the app
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function MainLogo(
  {
    variant = 'primary',
  }
    : Readonly<{
    variant?: 'primary' | 'secondary' | 'tertiary' | 'white' | 'transparent',
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

  /**
   * Computes the image depending on the variant
   */
  function determineImage(): any {
    switch (variant) {
      case 'white':
        return brandPrimary;
      case 'secondary':
        return brandTertiary;
      default:
        return brandWhite;
    }
  }


  //  RENDER

  return (
    <Link href={'/home'}>
      <Image src={determineImage()} height={windowSize[0] < 992 ? 50 : 65} alt={'Brand Logo'}/>
    </Link>
  )
}