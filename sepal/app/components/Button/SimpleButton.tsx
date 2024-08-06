import styles from './SimpleButton.module.scss'

/**
 * Base button component for a generic button
 *
 * @param text - text label
 * @param variant - determines color & shape. Accepted values are : 'primary', 'secondary', 'tertiary'.
 *                 if the value is not one of the above or is missing, the button will not render
 * @param inverted - flag to determine if the colors should be inverted
 * @param plain - flag to determine whether to render plain button with no styling other than theming
 * @param active - flag used with the plain flag, if true will always show the hover state
 * @param disabled - flag to determine whether this button should render as disabled
 * @param loading - flag to show the button as loading
 * @param handler - handler function for button
 * @param icon - icon component
 * @param iconPosition - icon position on left or right of text
 *
 * @author Stephen Prizio
 * @version 0.0.1
 */
function SimpleButton(
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
    }
        : Readonly<{
      text?: string,
      variant?: 'primary' | 'secondary' | 'tertiary',
      inverted?: boolean,
      plain?: boolean,
      active?: boolean,
      disabled?: boolean,
      loading?: boolean,
      handler?: any,
      icon?: any,
      iconPosition?: 'left' | 'right' | 'center'
    }>
) {

    const baseClass = 'simple-button'


    //  FUNCTIONS

    /**
     * Computes the css class based on the given props
     *
     * @param variant - determines color & shape. Accepted values are : 'primary', 'secondary', 'tertiary'.
     *                 if the value is not one of the above or is missing, the button will not render
     * @param inverted - flag to determine if the colors should be inverted
     * @param plain - flag to determine whether to render plain button with no styling other than theming
     * @param active - flag used with the plain flag, if true will always show the hover state
     */
    function computeClass(variant: string, inverted: boolean, plain: boolean, active: boolean) {
        const v = variant ? styles[`${baseClass}--${variant}`] : ""
        const i = inverted ?  styles[`${baseClass}--inverted`] : ""
        const p = plain ? styles[`${baseClass}--plain`] : ""
        const a = active ? styles[`${baseClass}--active`] : ""
        const l = loading ? styles[`${baseClass}--loading`] : ""

        return `${styles[baseClass]} ${v} ${i} ${p} ${a} ${l}`.trim()
    }

    if (!variant || variant.length === 0) {
        return null
    }


    //  RENDER

    return (
        <div className={computeClass(variant, inverted, plain, active)} onClick={handler}>
            {!loading && icon && iconPosition === 'left' ? icon : null}
            {!loading && icon && iconPosition === 'center' ? icon : null}
            {
                text && text.length > 0 ?
                    <button className={styles[`${baseClass}__inner`]} disabled={disabled}>
                        {!loading ? text : <div className={styles[`${baseClass}--loader`]}>L</div>}
                    </button>
                    :
                    null
            }
            {!loading && icon && iconPosition === 'right' ? icon : null}
        </div>
    );
}

export default SimpleButton;