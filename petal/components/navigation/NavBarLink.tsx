export default function NavBarLink(
  {
    text = '',
    url = '',
    variant = 'primary'
  }
    : Readonly<{
    text: string,
    url: string
    variant?: 'primary' | 'secondary' | 'tertiary' | 'white'
  }>
) {

  //  FUNCTIONS

  /**
   * Computes the css classes based on the given props
   */
  function computeVariant() {
    switch (variant) {
      case 'secondary':
        return 'hover:text-white';
      case 'tertiary':
        return 'hover:text-secondary';
      case 'white':
        return 'hover:text-primary hover:text-underline';
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