/**
 * Renders a simple message
 *
 * @param text Content
 * @param alignment positioning
 * @param variant color variant
 * @author Stephen Prizio
 * @version 0.0.1
 */
export default function SimpleMessage({
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
        return " bg-primaryGreenLight text-primaryGreen border-primaryGreen ";
      case "warning":
        return " bg-primaryYellowLight text-primaryYellow border-primaryYellow ";
      case "danger":
        return " bg-primaryRedLight text-primaryRed border-primaryRed ";
      default:
        return " bg-primaryLight text-primary border-primary ";
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
        "w-full flex items-center py-4 px-4 rounded-lg border-l-8 text-[14px] " +
        computeAlignment() +
        computeVariant()
      }
    >
      <div className={"w-full text-left"}>{text}</div>
    </div>
  );
}
