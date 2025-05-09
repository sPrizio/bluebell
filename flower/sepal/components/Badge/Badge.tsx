/**
 * Renders a standard badge/chip
 *
 * @param variant color variant
 * @param text badge text
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function Badge({
  variant = "primary",
  text = "",
}: Readonly<{
  variant?:
    | "primary"
    | "secondary"
    | "tertiary"
    | "success"
    | "warning"
    | "info"
    | "danger";
  text: string;
}>) {
  //  GENERAL FUNCTIONS

  /**
   * Computes the color variants for each variant
   */
  function computeVariant() {
    let bg = "";
    let color = "";

    switch (variant) {
      case "primary":
        bg = "bg-primary";
        color = "text-white";
        break;
      case "secondary":
        bg = "bg-secondary";
        color = "text-primary";
        break;
      case "tertiary":
        bg = "bg-tertiary";
        color = "text-white";
        break;
      case "success":
        bg = "bg-primaryGreenLight";
        color = "text-primaryGreen";
        break;
      case "warning":
        bg = "bg-primaryYellowLight";
        color = "text-primaryYellow";
        break;
      case "info":
        bg = "bg-primaryLight";
        color = "text-primary";
        break;
      case "danger":
        bg = "bg-primaryRedLight";
        color = "text-primaryRed";
        break;
      default:
        break;
    }

    return ` ${bg} ${color} `;
  }

  //  RENDER

  return (
    <div
      className={
        "rounded-2xl text-center inline-block px-4 py-1 font-bold " +
        computeVariant()
      }
    >
      {text}
    </div>
  );
}
