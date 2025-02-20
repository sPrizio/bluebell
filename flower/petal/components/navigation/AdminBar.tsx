import {IconMailFilled, IconPhoneFilled, IconUserFilled} from "@tabler/icons-react";

/**
 * A smaller version of the nav bar that sits at the top of the page for ui controls
 *
 * @param color variant
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function AdminBar(
  {
    variant = 'primary',
  }
    : Readonly<{
    variant?: 'primary' | 'secondary' | 'tertiary' | 'white',
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
        return ' bg-tertiary text-secondary ';
      case 'white':
        return ' bg-white text-primary ';
      default:
        return ' bg-primary text-white ';
    }
  }

  /**
   * Computes the hovering animations
   *
   * @param variant color variant
   */
  function computeAfter(variant: string) {
    let v = '';
    switch (variant) {
      case 'secondary':
        v = 'tertiary';
        break;
      case 'tertiary':
        v = 'secondary';
        break;
      case 'white':
        v = 'primary';
        break;
      default:
        v = 'white';
    }

    return ` relative py-2 after:content-[''] after:absolute after:h-[2px] after:left-0 after:bottom-0 after:w-0 after:bg-${v} after:transition-[width] after:duration-300 after:ease-in-out hover:after:w-full `
  }


  //  RENDER

  return (
    <div className={computeVariant() + " flex items-center py-2 font-medium text-sm "}>
      <div className="container">
        <div className="flex items-center justify-end">
          <div className={"flex items-center hover:cursor-pointer mr-6" + computeAfter(variant)}>
            <div className="mr-2"><IconMailFilled size={18}/></div>
            <div className="">hello@bluebell.com</div>
          </div>
          <div className={"flex items-center hover:cursor-pointer mr-6" + computeAfter(variant)}>
            <div className="mr-2"><IconPhoneFilled size={18}/></div>
            <div className="">514-941-1025</div>
          </div>
          <div className={"flex items-center hover:cursor-pointer" + computeAfter(variant)}>
            <div className="mr-2"><IconUserFilled size={18}/></div>
            <div className="">Client Login</div>
          </div>
        </div>
      </div>
    </div>
  )
}


























