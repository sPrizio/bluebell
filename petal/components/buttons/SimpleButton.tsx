/**
 * Base button component for a generic button
 *
 * @param text - text label
 * @param variant - determines color & shape. Accepted values are : 'primary', 'secondary', 'tertiary'.
 *                 if the value is not one of the above or is missing, the button will not render
 * @param inverted - flag to determine if the colors should be inverted
 * @param disabled - flag to determine whether this button should render as disabled
 * @param loading - flag to show the button as loading
 * @param handler - handler function for button
 * @param icon - icon component
 * @param iconPosition - icon position on left or right of text
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function SimpleButton(
  {
    text = '',
    variant = 'primary',
    disabled = false,
    loading = false,
    handler = null,
    icon = null,
    iconPosition = 'left',
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