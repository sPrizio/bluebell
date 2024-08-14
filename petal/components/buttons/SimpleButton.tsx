import {Button} from "@/components/ui/button";
import React from "react";

/**
 * Base button component for a generic button
 *
 * @param text text label
 * @param color color type
 * @param variant type of button
 * @param icon icon component
 * @param isLoading flag to show the button as loading
 * @param isDisabled flag to determine whether this button should render as disabled
 * @param button button type
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function SimpleButton(
  {
    text = '',
    color = 'primary',
    variant = 'fill',
    icon = null,
    isLoading = false,
    isDisabled = false,
    type = 'button',
  }
    : Readonly<{
    text?: string,
    color?: 'primary' | 'secondary' | 'tertiary' | 'white' | 'black',
    variant?: 'fill' | 'outline' | 'ghost' | 'link',
    icon?: React.ReactNode,
    isLoading?: boolean,
    isDisabled?: boolean,
    type?: 'button' | 'submit' | 'reset',
  }>
) {


  //  FUNCTIONS

  /**
   * Computes the button's color
   */
  function computeColor() {
    switch (color) {
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
   * Computes the button's design type
   */
  function computeVariant() {
    switch (variant) {
      case 'fill':
        return undefined;
      case 'outline':
        return 'outline';
      case 'ghost':
        return 'ghost';
      default:
        return 'link';
    }
  }


  //  RENDER

  return (
    <div className={"transform-gpu"}>
      <Button type={type} variant={computeVariant()} className={computeColor()}>{text}</Button>
    </div>
  )
}






















