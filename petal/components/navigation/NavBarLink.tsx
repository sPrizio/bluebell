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
        return 'hover:text-white';
      case 'tertiary':
        return 'hover:text-secondary';
      case 'white':
        return 'hover:text-primary';
      default:
        return 'hover:text-secondary';
    }
  }


  //  RENDER

  return (
    <a className={computeVariant() + " transition delay-50"} href={url}>
      {text}
    </a>
  )
}