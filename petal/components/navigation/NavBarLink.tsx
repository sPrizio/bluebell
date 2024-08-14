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
   * Computes the css classes based on the given props
   */
  function computeVariant() {
    switch (variant) {
      case 'secondary':
      case 'transparent':
        return ' hover:text-white ';
      case 'tertiary':
        return ' hover:text-secondary ';
      case 'white':
        return ' hover:text-primary ';
      default:
        return ' hover:text-secondary ';
    }
  }

  /**
   * Computes the hovering animations
   *
   * @param variant color variant
   */
  function computeAfter(variant: string) {
    let v = variant;
    if (variant === 'transparent') {
      v = 'white'
    }

    return ` relative py-2 after:content-[''] after:absolute after:h-[2px] after:left-0 after:bottom-0 after:w-0 after:bg-${v} after:transition-[width] after:duration-300 after:ease-in-out hover:after:w-full `
  }


  //  RENDER

  return (
    <Link className={computeVariant() + computeAfter(variant)} href={url}>
      {text}
    </Link>
  )
}