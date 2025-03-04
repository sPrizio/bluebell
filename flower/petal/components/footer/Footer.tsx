/**
 * Universal footer
 *
 * @param variant color variant
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function Footer(
  {
    variant = 'primary',
  }
    : Readonly<{
    variant?: 'primary' | 'secondary' | 'tertiary' | 'white' | 'black',
  }>
) {


  //  FUNCTIONS

  /**
   * Computes the css classes based on the given props
   */
  function computeVariant() {
    switch (variant) {
      case 'secondary':
        return ' bg-secondary text-tertiary ';
      case 'tertiary':
        return ' bg-tertiary text-white ';
      case 'white':
        return ' bg-white text-tertiary ';
      case 'black':
        return ' bg-black text-white ';
      default:
        return ' bg-primary text-white ';
    }
  }


  //  RENDER

  return (
    <div className={computeVariant() + "flex items-center py-12 text-sm"}>
      <div className="container">
        <div className="flex items-center justify-between">
          <div>
            <strong>bluebell</strong> &copy; helps individuals across the country grow and flourish throughout their financial journey
          </div>
          <div>
            &copy;&nbsp;2024 <strong>bluebell</strong>, All rights reserved
          </div>
        </div>
        <div className="mt-6 text-xs">
          <strong>bluebell</strong> is a Registered Investment Advisor. Content presented on
          this website is for informational and educational purposes only and does not intend to make an offer or
          solicitation for the sale or purchase of any specific securities product, service, or investment strategy.
          Information on this website is not intended to be comprehensive tax advice or financial planning advice for
          any particular client investment needs. Be sure to consult with a qualified financial or investment adviser
          , tax professional, or attorney before implementing any strategy or recommendation discussed
          herein. Investing involves risk, including the loss of principal.
        </div>
      </div>
    </div>
  )
}