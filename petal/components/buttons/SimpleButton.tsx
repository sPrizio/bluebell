export default function SimpleButton(
  {
    text = '',
    variant = 'primary',
    inverted = false,
    plain = false,
    active = false,
    disabled = false,
    loading = false,
    handler = null,
    icon = null,
    iconPosition = 'left',
    highlightText = false,
  }
    : Readonly<{
    text?: string,
    variant?: 'primary' | 'secondary' | 'tertiary' | 'white',
    inverted?: boolean,
    plain?: boolean,
    active?: boolean,
    disabled?: boolean,
    loading?: boolean,
    handler?: any,
    icon?: any,
    iconPosition?: 'left' | 'right' | 'center',
    highlightText?: boolean,
  }>
) {


  //  FUNCTIONS

  /**
   * Computes the css classes based on the given props
   */
  function computeVariant() {
    switch (variant) {
      case 'secondary':
        return 'bg-secondary text-tertiary';
      case 'tertiary':
        return 'bg-tertiary text-white';
      case 'white':
        return 'bg-white text-tertiary';
      default:
        return 'bg-primary';
    }
  }


  //  RENDER

  return (
    <div className={computeVariant() + " px-4 py-4 text-sm font-bold rounded-md hover:cursor-pointer transition delay-50 hover:scale-110 transform-gpu"}>
      {text}
    </div>
  )
}