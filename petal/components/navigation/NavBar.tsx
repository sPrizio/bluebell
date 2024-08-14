import NavBarLink from "@/components/navigation/NavBarLink";
import SimpleButton from "@/components/buttons/SimpleButton";
import MainLogo from "@/components/navigation/MainLogo";

/**
 * The nav bar component
 *
 * @param variant color variant
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function NavBar(
  {
    variant = 'primary',
  }
    : Readonly<{
    variant?: 'primary' | 'secondary' | 'tertiary' | 'white' | 'transparent',
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
      case 'transparent':
        return 'bg-transparent text-white ';
      default:
        return 'bg-primary text-white';
    }
  }

  /**
   * Computes the type of button to show based on the given variant
   */
  function computeButtonVariant() {
    switch (variant) {
      case 'secondary':
        return 'tertiary';
      case 'tertiary':
        return 'secondary';
      case 'white':
        return 'primary';
      case 'transparent':
        return 'primary';
      default:
        return 'white';
    }
  }

  /**
   * Computes the border color based on the given variant
   */
  function computeBorder() {
    switch (variant) {
      case 'primary':
      case 'transparent':
        return 'border-white';
      case 'secondary':
        return 'border-tertiary';
      case 'tertiary':
        return 'border-secondary';
      default:
        return 'border-tertiary';
    }
  }


  //  RENDER

  if (!variant || variant.length === 0) {
    return null
  }

  return (
    <div className={(computeVariant()) + " relative py-4 lg:py-6 font-semibold text-sm leading-6"}>
      <div className={" container flex items-center justify-between"}>
        <MainLogo variant={variant}/>
        <div className="flex items-center">
          <div className="-my-1 ml-2 -mr-1 md:hidden">
            <button type="button" className="w-8 h-8 flex items-center justify-center">
              <span className="sr-only">Navigation</span>
              <svg width="24" height="24" fill="none" aria-hidden="true">
                <path
                  d="M12 6v.01M12 12v.01M12 18v.01M12 7a1 1 0 1 1 0-2 1 1 0 0 1 0 2Zm0 6a1 1 0 1 1 0-2 1 1 0 0 1 0 2Zm0 6a1 1 0 1 1 0-2 1 1 0 0 1 0 2Z"
                  stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round"/>
              </svg>
            </button>
          </div>
          <div className="hidden md:flex items-center">
            <nav>
              <ul className="flex items-center gap-x-8">
                <li><NavBarLink variant={variant} text={'About Us'} url={''}/></li>
                <li><NavBarLink variant={variant} text={'Our Services'} url={''}/></li>
                <li><NavBarLink variant={variant} text={'Contact Us'} url={''}/></li>
                <li><NavBarLink variant={variant} text={'FAQ'} url={''}/></li>
              </ul>
            </nav>
            <div className={computeBorder() + " flex items-center border-l ml-6 pl-6"}>
              <SimpleButton color={computeButtonVariant()} text={'Get Started'}/>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}