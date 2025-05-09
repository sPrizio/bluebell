/**
 * Renders a simple message banner
 *
 * @param text Content
 * @param alignment positioning
 * @param variant color variant
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function SimpleBanner({
  text,
  alignment = "left",
  variant = "info",
}: Readonly<{
  text: string;
  alignment?: "left" | "center" | "right";
  variant?: "info" | "warning" | "danger" | "success";
}>) {
  //  GENERAL FUNCTIONS

  /**
   * Computes the color variant
   */
  function computeVariant() {
    switch (variant) {
      case "success":
        return " bg-primaryGreenLight text-primaryGreen ";
      case "warning":
        return " bg-primaryYellowLight text-primaryYellow ";
      case "danger":
        return " bg-primaryRedLight text-primaryRed ";
      default:
        return " bg-primaryLight text-primary ";
    }
  }

  /**
   * Computes the alignment of the Content
   */
  function computeAlignment() {
    switch (alignment) {
      case "left":
        return " justify-start ";
      case "center":
        return " justify-center ";
      default:
        return " justify-end ";
    }
  }

  //  RENDER

  return (
    <div
      className={
        " p-4 flex items-center rounded-lg " +
        computeVariant() +
        computeAlignment()
      }
    >
      {text}
    </div>
  );
}
