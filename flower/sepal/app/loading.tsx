import SepalLoader from "@/components/Svg/SepalLoader";

/**
 * Renders a global loader
 *
 * @author Stephen Prizio
 * @version 0.2.2
 */
export default function LoadingPage() {
  //  RENDER

  return (
    <div className="fixed left-0 top-0 z-50 block h-full w-full bg-white opacity-75">
      <span className="r-4 relative top-1/2 mx-auto my-0 block h-0 w-0 text-green-500 opacity-75">
        <div role="status">
          <SepalLoader />
        </div>
      </span>
    </div>
  );
}
