import Link from "next/link";

/**
 * Component representing individual links on the navbar
 *
 * @param text text
 * @param url url
 * @param variant color variant
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function NavBarLink(
  {
    text = '',
    url = '',
    variant = 'primary'
  }
    : Readonly<{
    text: string,
    url: string
    variant?: 'primary' | 'secondary' | 'tertiary' | 'white' | 'transparent'
  }>
) {

  //  FUNCTIONS

  /**
   * Computes the hovering animations
   *
   * @param variant color variant
   */
  function computeAfter(variant: string) {
    switch (variant) {
      case 'secondary':
        return ` relative py-2 after:content-[''] after:absolute after:h-[2px] after:left-0 after:bottom-0 after:w-0 after:bg-tertiary after:transition-[width] after:duration-300 after:ease-in-out hover:after:w-full `
      case 'tertiary':
        return ` relative py-2 after:content-[''] after:absolute after:h-[2px] after:left-0 after:bottom-0 after:w-0 after:bg-secondary after:transition-[width] after:duration-300 after:ease-in-out hover:after:w-full `
      case 'white':
        return ` relative py-2 after:content-[''] after:absolute after:h-[2px] after:left-0 after:bottom-0 after:w-0 after:bg-primary after:transition-[width] after:duration-300 after:ease-in-out hover:after:w-full `
      case 'transparent':
        return ` relative py-2 after:content-[''] after:absolute after:h-[2px] after:left-0 after:bottom-0 after:w-0 after:bg-white after:transition-[width] after:duration-300 after:ease-in-out hover:after:w-full `
      default:
        return ` relative py-2 after:content-[''] after:absolute after:h-[2px] after:left-0 after:bottom-0 after:w-0 after:bg-white after:transition-[width] after:duration-300 after:ease-in-out hover:after:w-full `
    }
  }


  //  RENDER

  return (
    <Link className={computeAfter(variant)} href={url}>
      {text}
    </Link>
  )
}